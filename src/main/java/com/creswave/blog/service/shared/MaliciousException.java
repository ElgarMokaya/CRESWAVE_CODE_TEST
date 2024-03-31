package com.creswave.blog.service.shared;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class MaliciousException extends RuntimeException {

    private static final long serialVersionUID = -8376685694861111620L;

    public MaliciousException() {

    }


    public MaliciousException( String message ) {

        super( message );
    }


    public MaliciousException( Throwable cause ) {

        super( cause );
    }


    public MaliciousException( String message, Throwable cause ) {

        super( message, cause );
    }


    public MaliciousException( String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace ) {

        super( message, cause, enableSuppression, writableStackTrace );
    }

}
