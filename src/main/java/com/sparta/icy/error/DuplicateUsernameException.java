package com.sparta.icy.error;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message){
        super(message);
    }
}
