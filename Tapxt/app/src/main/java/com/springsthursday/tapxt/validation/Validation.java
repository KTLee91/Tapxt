package com.springsthursday.tapxt.validation;

import com.springsthursday.tapxt.repository.UserInfo;

import java.util.regex.Pattern;

public class Validation {

    public static boolean CheckPhoneNumber(String phoneNumber)
    {
        if(phoneNumber == ""
            || !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phoneNumber))
        {
            return false;
        }

        return true;
    }

    public static boolean CheckVerificationNumber(String verificationNumber)
    {
        if(verificationNumber.equals("")) return false;

        return true;
    }

    public static boolean isEmptyNickName(String nickName)
    {
        if(nickName.isEmpty() == true)
            return true;
        return false;
    }
}
