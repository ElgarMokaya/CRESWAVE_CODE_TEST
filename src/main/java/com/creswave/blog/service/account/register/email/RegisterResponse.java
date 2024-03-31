package com.creswave.blog.service.account.register.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Boolean success;

    private Boolean usernameTooShort;
    private Boolean usernameTooLong;
    private Boolean emailInvalid;

    private Boolean passwordInvalid;
    private Boolean passwordsDoNotMatch;

    private Boolean emailExists;
    private Boolean usernameTaken;

    private Integer usernameMinimumLength;
    private Integer passwordMinimumLength;
    private Boolean passwordNeedsMixedCase;
    private Boolean passwordNeedsSpecialCharacters;
    private Boolean passwordNeedsNumbers;
    private Boolean passwordNeedsLetters;
}
