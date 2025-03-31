package com.micro.user.customException;

public class EmailAlredyExistException extends RuntimeException{

    public EmailAlredyExistException(String message){

        super(message);
    }
}
