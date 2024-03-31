package com.creswave.blog.helper.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Component
public class HashHelperImpl implements HashHelper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SecureRandom secureRandom = new SecureRandom();

    public HashHelperImpl() {

    }


    public HashHelperImpl( PasswordEncoder passwordEncoder ) {

        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String getPasswordHashWithBcrypt( String password ) {

        String hashedPassword = passwordEncoder.encode( password );
        return hashedPassword;
    }


    @Override
    public String getRandomHash() {

        String hash = secureRandom.nextLong() + "";
        hash = hash.replaceFirst( "-", "" );
        return hash;
    }


    @Override
    public String getRandomAlphanumeric() {

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints( leftLimit, rightLimit + 1 )
                .filter( i -> ( i <= 57 || i >= 65 ) && ( i <= 90 || i >= 97 ) )
                .limit( targetStringLength )
                .collect( StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append )
                .toString();

        return generatedString + getRandomHash();
    }


    @Override
    public String getMD5Spring( String text ) {

        return DigestUtils.md5DigestAsHex( text.getBytes() );
    }


    private static String bytesToHex( byte[] hash ) {

        StringBuilder hexString = new StringBuilder( 2 * hash.length );

        for ( int i = 0; i < hash.length; i++ ) {

            String hex = Integer.toHexString( 0xff & hash[i] );

            if ( hex.length() == 1 ) {

                hexString.append( '0' );
            }

            hexString.append( hex );
        }

        return hexString.toString();
    }


    public String getSha256Hash( String text ) {

        try {
            final MessageDigest digest = MessageDigest.getInstance( "SHA3-256" );
            final byte[] hashbytes = digest.digest( text.getBytes( StandardCharsets.UTF_8 ) );
            String sha3Hex = bytesToHex( hashbytes );
            return sha3Hex;
        }
        catch ( NoSuchAlgorithmException e ) {

            throw new RuntimeException( e );
        }
    }


    public static void main( String[] args ) throws Exception {

        HashHelper hashHelper = new HashHelperImpl( new BCryptPasswordEncoder() );
        String password = hashHelper.getPasswordHashWithBcrypt( "blog" );
        System.out.println( password );
        System.out.println( hashHelper.getMD5Spring( "blah" ) );
        System.out.println( hashHelper.getSha256Hash( "blah" ) );
    }
}
