package com.micro.product.globalException.Costomexception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message){

        super(message);
    }
}
