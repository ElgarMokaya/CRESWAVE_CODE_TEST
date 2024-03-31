package com.creswave.blog.helper.email;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class EmailHelperImpl implements EmailHelper {

    private String regexEmailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private Pattern emailPattern = Pattern.compile( regexEmailPattern );

    @Override
    public Boolean emailValid( String email ) {

        if ( email == null ) {

            return Boolean.FALSE;
        }

        return emailPattern.matcher( email ).matches();
    }

}
