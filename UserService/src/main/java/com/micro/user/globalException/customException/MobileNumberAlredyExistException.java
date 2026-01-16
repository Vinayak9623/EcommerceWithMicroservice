package com.micro.user.globalException.customException;

public class MobileNumberAlredyExistException extends RuntimeException{

    public MobileNumberAlredyExistException(String message){
        super(message);
    }
}
