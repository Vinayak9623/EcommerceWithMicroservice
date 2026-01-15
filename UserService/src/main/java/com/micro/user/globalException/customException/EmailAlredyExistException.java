package com.micro.user.globalException.customException;

public class EmailAlredyExistException extends RuntimeException{

    public EmailAlredyExistException(String message){
        super(message);
    }
}
