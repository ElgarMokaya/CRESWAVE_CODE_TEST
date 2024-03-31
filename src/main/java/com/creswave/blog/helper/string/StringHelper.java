package com.creswave.blog.helper.string;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface StringHelper {

    String trim( String string );


    String trimAndLowerCase( String string );


    String trimAndCapitaliseFirstLetter( String string );


    Boolean empty( String string );


    String getSubstring( String string, int maxSize );
}
