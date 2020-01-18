package com.richard.earthquake.app.presantation;

public class ErrorMessage {
    public static final String USER_USER_EXIST = "User exist with this email";
    public static final String USER_USER_NOT_EXIST_OR_AUTH_ERROR = "Cant find user. User do not exist or authentication error. Please try again";
    public static final String USER_ERROR_PARSING_USER = "Unable to parse user. Please try again or contact administrator";
    public static final String USER_TOKEN_EXIST = "This token exist for this user";
    public static final String EARTHQUAKE_NO_EARTHQUAKES_FOR_USER = "There are no earthquakes for this filters in our database.";
    public static final String USER_INCORRECT_AUTH_DATA = "Wrong username or password";
    public static final String FORGOT_PASSWORD_EMAIL_SEND = "We have send ypu new password on your email. Please check your email and log in again";
    public static final String USER_USER_NOT_EXIST = "Cant find any user with this email";
    public static final String USER_PASSWORD_NOT_MACH = "This password do not mach with old password";
    public static String ERROR = "error";
    public static String FORGOT_PASSWORD_SUBJECT = "Earthquake new password";


    public static String FORGOT_PASSWORD_SMS(String email, String password) {
        return " Your new password for account: " + email + " is \n Password: " + password;
    }
}

