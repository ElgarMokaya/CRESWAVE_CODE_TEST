# BLOG BOOT README

## Abstract

See coding assignment at:

    docs/Creswave_Code_Test.pdf


## Instructions

### Create Postgres DB

Start by creating the blog user:

    % createuser --interactive --pwprompt
    Enter name of role to add: blog
    Enter password for new role: blog
    Enter it again: blog
    Shall the new role be a superuser? (y/n) y

Now login to the DB as the postgres user and create the blog db and assign it it to the blog user:

    psql postgres
    postgres=# create database blog owner blog;
    

### Install java 20

The pom.xml file has:

    <properties>
        <java.version>20</java.version>
    </properties>
    
Make sure you have java 20 installed or downgrade the version in the pom.xml file


### Run Unit Tests

Change to the directory where the pom.xml file is and type:

    mvn -e -Dskip.ut=false clean test


### Run Integration Tests

Change to the directory where the pom.xml file is and type:

    mvn -e -Dskip.ut=true clean verify


## Testing Strategy

I have integration tests and unit tests.  Validation code has been re-factored out of the Services
into Validation classes so that they can be unit tested.

The responses objects return both the record details and also validation information:

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class CreatePostResponse {
    
        private Long id;
        private String title;
        private String text;
    
        private Boolean hasValdationErrors;
        private Boolean titleToShort;
        private Boolean titleToLong;
        private Boolean textToShort;
        private Boolean textToLong;
        private Integer postTitleMinimumLength;
        private Integer postTitleMaximumLength;
        private Integer postTextMinimumLength;
        private Integer postTextMaximumLength;
    }

In the above example the response object's first three variables are from the Post entity record.

The rest of the fields are for validation purposes.  The key validation field is called:

    hasBalidationErrors
    
If true, the other fields specify where validation failed.  Also included in the response objects
are the criteria which need to be matched.  For example it specifies what the minimum and 
maximum lengths of each field are.

A Validator interface looks like:

    HttpStatus validate( CreatePostResponseBuilder builder, String title, String text );

Of note is that the validator returns the HttpStatus code. Typically validation errors result in:

    status code 422 Unprocessable Entity
    
The Validator also populates the lombok builder used to build the response object.  The great thing
about this architecture is that all execution paths of the Validator can be tested in a unit test.

As a lot of the validation criteria are read as follows:

    @Value( "${post.title.validation.minimum-length}" )
    private Integer postTitleMinimumLength;
    
and we do not want to bootstrap the application context in unit tests, we instead use reflection 
to set these variables:

    ReflectionTestUtils.setField( createPostValidator, "postTitleMinimumLength", 3 );

So while the integration tests test the major execution paths, by encapsulating the building of
response objects, status code and validation in Validator classes I am easily able to test all 
execution paths in my unit tests.  This greatly reduces the number of execution paths I need to 
test in my integration tests.  This simplifies the testing strategy.


## Security

I  am using Spring Security including role based security.  However this role based security is 
not sufficient. For example I need to rest mindful that a logged in user who has the correct role 
to delete comments may maliciously try and delete someone else's comment.  This is a pretty common
practice used by malicious actors.  To guard against malicious actions, checks are made in the 
services.

If the service detects that someone is trying to maliciously delete a comment of someone else a
Malicious Exception is thrown.  The Malicious Exception can with refinement include the IP address 
of the malicious actor. The Malicious Exception and IP address will find their way into the log 
files.  Reverse Proxy configuration of a web server like nginx can be configured to make this ip 
address available to Java Spring Boot.


## Scalability, Complexity and Performance

Breaking out functionality into micro web services adds increased complexity for deployment and
architecture.  Different nodes specialising in specialised functionality often use common code.
This necessitates building common artifacts which means more projects, and more builds configured in
continuous integration. From a deployment perspective it means more docker images, increased 
complexity in kubernetes, more cloud resources and more expense.

Therefore it is best to not prematurely increase complexity and cloud resources.  Typically the move
to a micro web service architecture with multiple nodes starts when it is seen to be cheaper to run 
the software on multiple small nodes instead of increasing the cores and RAM of one big instance.

The move to micro web service architecture occurs when bottle necks are encountered.  Big VMs are 
also less predictable as stop the world garbage collection pauses can grow to several seconds when 
the Java JVM size is large.  Dependency injection design patterns used by Spring Boot do however 
hold long lasting instances for the life time of the VM which reduces stop the world pauses.

For productivity purposes Spring wrapped hibernate massively improves coding productivity.  However
looping through entity collections and calling child relations on each entity can result in 
excessive database calls.

If it is found to be slow one can instead drop down to native QL and create more joins in the select
statements.  This can result in duplicate data.  However a process called flattening can take place
where maps are used to get rid of duplicate data.  It is possible to reduce 200 hits to the database
down to a single hit using this approach.  Hibernate queries which take 20 seconds can be reduced 
down to milliseconds using this approach.  Generally this refactoring takes place when performance
bottle necks are identified.  application.yaml can be tweaked to enable logging of sql so it can
become easily apparent where hibernate is being used incorrectly.

This performance tuning in the usage of Hibernate can often eliminate the need to break the code
into more nodes which as mentioned above increases complexity and costs.

However once the database interactions have been tuned and we are still experiencing bottle necks
the next step is to refactor out our common modules. At this time it becomes important to start 
using artifact repositories like Nexus to manage these common modules. It becomes time to think 
about a delivery pipe line using Continuous Integration servers.  Once the common modules have 
been refactored out we now create multiple Java Spring Boot projects that consume these common 
modules and we configure load balancers and / or reverse proxy configurations to route traffic to 
different Java Spring Boot node instances.

We now start caring about the availability of all the nodes and start worrying about what happens
if part of the system goes down.  To ensure that data is not lost we refactor nodes to start 
consuming data off queues.  Queues ensure that when a service comes back online that it can 
continue consuming unprocessed records off the queue without suffering inconsistent data issues.

We also start worrying about error propagation across nodes.  We do not want continuous exception
throwing to bring down systems.  We then need to increase complexity and integrate libraries that
ensure that when systems go down we pause hitting the third party systems for a while.  We now
enter the territory of Spring Cloud and start using technologies like circuit breakers. Circuit 
breaker libraries have been ported from companies like Netflix and have been made open source.

With the increased complexity of running multiple nodes we now also have the problem of managing 
end points across different environments.

The idea with the delivery pipeline is that as an artifact passes through different testing steps 
and that it is only built once.  This means that either all the end points need to live within each 
artifact and a  switch used to specify the environment or there needs to be a mechanism to pass 
these end points in to the artifact.  As the system grows it becomes useful for the different nodes 
to load their end points at start up.  As these configuration services may themselves go down even 
those configuration services need to be replicated and use strategies like round robin availability 
strategies to ensure failover.  We are now entering the world of Spring Cloud in earnest and will 
begin considering using tools like Zookeeper for end point management.  This adds significant 
complexity and is best avoided if the system does not have very high usage.

So key to reducing the need for migration to complex architecture is to encourage re factoring of 
the code base.  If performance issues can be addressed by reducing complexity instead of increasing 
it this is truly a blessing. 

In the Java World creating a lot of small methods with little dependencies might seem like there will
be a massive overhead of CPU cycles caused by code calling other code.  However the opposite is the
case - when code is broken into lots of small methods the Hot Spot Just in Time Compiler of the JVM 
can more easily optimise code.  For example it can decide to repeat code instead of adding overhead 
by using a loop.  These optimisations only work with short easily readable code.  So readable code 
actually runs faster.  Another thing is that small methods mean variables are referenced for a 
shorter time which makes them more available for garbage collection - this reduces stop the world 
pauses and improves performance.

Generally it is best to have very short lived objects or hold onto long lived objects for the 
duration of the VM. As mentioned above, Dependency injection used by Spring Boot ensures 
the latter. It can increase the amount of memory required but memory is pretty cheap nowadays. Of 
more concern is the increased expense of increasing the number of cores.

On a final note - we all become obsessed with CPU cycles - however there is a different kind of 
cycle called a Brain Cycle and that is often overlooked.  The more we need to decode complexity the 
more brain cycles we require.  The more brain cycles we need the harder it becomes to re factor 
code.  We end up with developers adding shit to shit for it is too difficult for their brains to
tackle the refactor.  Therefore a balance is needed.  Sometimes code is easier to understand if
there is a little bit of duplication or a little more verbosity. We are human and we should not
over engineer code so that others find it hard to decode.  Here we see the balance of library code
being able to support all use cases versus it supporting a sub section of all use cases.  If the code 
needs to support all possible future use cases, it will become very complex to understand and hard to 
maintain.  Complex code which is not properly understood by many, ends up being used incorrectly which
results in bugs.  For library code, it is best to choose a sub section of supported use cases 
under the understanding that it can be refactored if a new use case comes along - this reduces
complexity and leads to more maintainable and more bug free code.  Complexity is our enemy and leads
to complete rewrites of software.


## Libraries used and their justification

* Lombok - used to reduce boiler plate code.  Also has some nice design patterns like builders.
* log4j / sl4j - great for logging.  Is a transative dependency coming from spring libraries.
* junit - used for integration and unit tests.
* jsonwebtoken - Great for JWT token security.

All other libraries are the standard Spring Boot and Spring Security ones.


## Entity diagram - hybrid db / uml diagram.

See Star UML diagram under:

     docs/staruml/blog.mdj
     
There is a README.txt with instructions for downloading staruml:

    docs/staruml/README.txt
    
In case you do not want to install StarUML the diagram has been exported to a pdf:

    docs/staruml/blog.pdf


## Room for improvement

Given more time and for production ready code, I would have also implemented Email account 
verification.

You will see in:

    com.creswave.blog.config.ApplicationConfig
    
Code:

    private UserDetails createUserDetails( User user ) {

        UserDetails userDetails =
                new UserDetails() {

                    private static final long serialVersionUID = 1L;
                
                   //  code removed for demonstration purposes

                    @Override
                    public boolean isEnabled() {

                        return user.getActive();
                    }
                };

        return userDetails;
    }

The isEnabled() method checks whether the user is active or not. If the user is not active you 
will get a forbidden 403 status code.

Ordinarily the registering user should have been sent an email with a link in it with a one time 
usage token in it. On clicking the link the idea is that the user "active" attribute should have 
been set to true.

However I instead set the user as active during registration.  This was because I felt email 
functionality was out of scope for this coding assignment.

Similarly I did not implement email functionality for forgotten password.

Another aspect is the Code Coverage in the unit tests - I have provided examples of unit tests but
have not exhaustively created the code coverage expected of production ready code.  I have also not 
included Code Coverage plugins in the pom.xml file to provide the code coverage metrics.

However given more time I could have have added code coverage metrics and improved code coverage 
in my tests.

I could also have used a mocking library for unit tests but have not.  Given more time I could have
done so.

On the code coverage front balance is in order.  Straight-Jacketing the code down with 100% code 
coverage of all execution paths can dissuade other team members from re-factoring code. Encouraging 
continuous refactoring results in readabe and maintanable code. Sometimes over zealous code 
coverage stops the team refactoring code.  A simple refactoring exercise ceases to be fun when it 
breaks lots of unit tests.  Personally I vouch for a bit of common sense.  Code that is a bit 
tricky and likely to suffer mistakes should be given more code coverage attention than easy code.


## Conclusion

I am a passionate young software dev and I have done my best effort to show case my Java Spring Boot 
skills to Creswave.

Electricity has been a bit of an issue.  I changed house but even the new one has issues.  I will 
buy an inverter if there is home working at Creswave.

I am a contributor in the SpaceYaTech software collective and am also involved with some innovative 
software projects with the SpotADev collective which combine both web2 and web3 technology.

SpotADev is a satellite organisation which codes under the SpaceYaTech umbrella.  Currently we have
thousands of developers in SpaceYaTech and I am assisting in shaping the future of technology in 
Kenya.

One such project I am involved in is Symbiont.

Symbiont has a chrome extension which injects a comment thread into any website on the planet.

Symbiont is built with self censorship in mind and utilises crypto wallet Decentralised 
ID technology to bring trust to the platform.  The backend is written in Java Spring Boot.

In a nut shell we get cryptographic proof that a wallet holder owns their social media handles using
next.id DID technology.  I have helped author wallet examples for the next.id community.

We also use cryptographic proof to prove what information the user wishes to associate with their 
decentralised DID.  This provides a more ethical alternative to World Coin which was accused of 
using evasive eye scanning technology.  By giving the end user this decentralised DID technology 
we eliminate spam and malicious actors on the symbiont platform.  It allows users to subscribe 
not to see content which has been marked by others as being offensive.  The Trust technology 
provides a form  of proof of person hood which in turn stops malicious actors distorting the 
operation of the platform.

I am super happy to be considered for this job at Creswave and I look forward to the next steps of 
the interview process.

Thank you

Elgar :-)


