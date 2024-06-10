package com.sparta.icy.error;

public class PasswordDoesNotMatchException extends RuntimeException{
    public PasswordDoesNotMatchException (String message){
        super(message);
    }
}
