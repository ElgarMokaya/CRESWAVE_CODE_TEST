package com.creswave.blog.helper.string;

import org.springframework.stereotype.Component;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class StringHelperImpl implements StringHelper {

    // returns null if zero length
    @Override
    public String trim( String string ) {

        String toReturn;

        if ( string == null ) {

            toReturn = null;
        }
        else {
            toReturn = string.trim();

            if ( toReturn.length() == 0 ) {

                toReturn = null;
            }
        }

        return toReturn;
    }


    @Override
    public String trimAndLowerCase( String string ) {

        String trimmed = trim( string );

        if ( trimmed == null ) {

            return null;
        }

        return trimmed.toLowerCase();
    }


    @Override
    public String trimAndCapitaliseFirstLetter( String string ) {

        String trimmedAndLowerCase = trimAndLowerCase( string );

        if ( trimmedAndLowerCase == null || trimmedAndLowerCase.length() == 0 ) {

            return null;
        }

        String firstLetter = string.substring( 0, 1 );
        String restOfLetters = "";

        if ( string.length() > 1 ) {

            restOfLetters = string.substring( 1 );
        }

        return firstLetter + restOfLetters;
    }


    @Override
    public Boolean empty( String string ) {

        if ( trim( string ) == null ) {

            return true;
        }

        return false;
    }


    @Override
    public String getSubstring( String string, int maxSize ) {

        if ( string == null ) {

            return null;
        }

        int size = string.length();
        String dots = "";

        if ( size > maxSize ) {

            size = maxSize;
            dots = " ...";
        }

        return string.substring( 0, size ) + dots;
    }
}
