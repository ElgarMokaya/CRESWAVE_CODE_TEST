package com.creswave.blog.helper.hash;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface HashHelper {

    String getRandomHash();


    String getPasswordHashWithBcrypt( String password );


    String getMD5Spring( String text );


    String getRandomAlphanumeric();


    String getSha256Hash( String text );
}
