package com.sparta.icy.error;

public class ResignupWithSignedoutUsernameException extends RuntimeException{
    public ResignupWithSignedoutUsernameException(String message){
        super(message);
    }
}
