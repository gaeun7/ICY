package com.sparta.icy.error;

public class AlreadySignedOutUserCannotBeSignoutAgainException extends RuntimeException{
    public AlreadySignedOutUserCannotBeSignoutAgainException(String message){
        super(message);
    }
}
