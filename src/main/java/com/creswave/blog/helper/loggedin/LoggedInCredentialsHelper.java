package com.creswave.blog.helper.loggedin;

import com.creswave.blog.domain.user.User;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface LoggedInCredentialsHelper {

    User getLoggedInUser();


    String getLoggedInUserEmail();
}
