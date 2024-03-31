package com.creswave.blog.helper.password;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class PasswordHelperImpl implements PasswordHelper {

    @Value( "${password.validation.minimum-length}" )
    private Integer passwordMinimumLength;

    @Value( "${password.validation.maximum-length}" )
    private Integer passwordMaximumLength;

    @Value( "${password.validation.needs-mixed-case}" )
    private Boolean passwordNeedsMixedCase;

    @Value( "${password.validation.needs-special-characters}" )
    private Boolean passwordNeedsSpecialCharacters;

    @Value( "${password.validation.needs-numbers}" )
    private Boolean passwordNeedsNumbers;

    @Value( "${password.validation.needs-letters}" )
    private Boolean passwordNeedsLetters;

    @Override
    public Boolean passwordValid( String password ) {

        if ( password == null || password.length() < passwordMinimumLength ) {

            return Boolean.FALSE;
        }

        if ( password != null && password.length() > passwordMaximumLength ) {

            return Boolean.FALSE;
        }

        Boolean hasLowerCase = Boolean.FALSE;
        Boolean hasUpperCase = Boolean.FALSE;
        Boolean hasNumbers = Boolean.FALSE;
        Boolean hasLetters = Boolean.FALSE;
        Boolean hasSpecialLetters = Boolean.FALSE;

        for ( char letter : password.toCharArray() ) {

            if ( Character.isLowerCase( letter ) ) {

                hasLowerCase = Boolean.TRUE;
            }

            if ( Character.isUpperCase( letter ) ) {

                hasUpperCase = Boolean.TRUE;
            }

            if ( Character.isDigit( letter ) ) {

                hasNumbers = Boolean.TRUE;
            }

            if ( Character.isLetter( letter ) ) {

                hasLetters = Boolean.TRUE;
            }

            if ( !Character.isDigit( letter ) & !Character.isLetter( letter )
                    && !Character.isWhitespace( letter ) ) {

                hasSpecialLetters = Boolean.TRUE;
            }
        }

        if ( passwordNeedsMixedCase && !hasLowerCase && !hasUpperCase ) {

            return Boolean.FALSE;
        }

        if ( passwordNeedsSpecialCharacters && !hasSpecialLetters ) {

            return Boolean.FALSE;
        }

        if ( passwordNeedsNumbers && !hasNumbers ) {

            return Boolean.FALSE;
        }

        if ( passwordNeedsLetters && !hasLetters ) {

            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
