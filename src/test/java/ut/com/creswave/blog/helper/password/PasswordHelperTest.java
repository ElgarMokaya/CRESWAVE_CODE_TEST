package ut.com.creswave.blog.helper.password;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.creswave.blog.helper.password.PasswordHelper;
import com.creswave.blog.helper.password.PasswordHelperImpl;


/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class PasswordHelperTest {

    private static PasswordHelper passwordHelper;


    @Test
    public void passwordValid() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 7 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "a123AB$#aaaaa";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertTrue( valid );
    }


    @Test
    public void passwordTooShort() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 60 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "a123AB$#aaaaa";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertFalse( valid );
    }


    @Test
    public void passwordHasNoMixedCase() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 60 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "a123ab$#aaaaa";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertFalse( valid );
    }


    @Test
    public void passwordHasNoSpecialCharacters() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 60 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "a123ab$aaaaa";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertFalse( valid );
    }


    @Test
    public void passwordHasNoNumbers() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 60 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "aBBBab$aaaaa";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertFalse( valid );
    }


    @Test
    public void passwordHasNoLetters() {

        passwordHelper = new PasswordHelperImpl();
        ReflectionTestUtils.setField( passwordHelper, "passwordMinimumLength", 30 );
        ReflectionTestUtils.setField( passwordHelper, "passwordMaximumLength", 60 );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsMixedCase", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsSpecialCharacters", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsNumbers", true );
        ReflectionTestUtils.setField( passwordHelper, "passwordNeedsLetters", true );
        String password = "1234$5678";
        boolean valid = passwordHelper.passwordValid( password );
        Assert.assertFalse( valid );
    }
}
