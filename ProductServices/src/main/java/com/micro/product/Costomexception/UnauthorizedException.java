package com.micro.product.Costomexception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message){

        super(message);
    }
}
