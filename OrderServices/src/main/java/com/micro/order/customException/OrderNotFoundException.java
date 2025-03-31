package com.micro.order.customException;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message){

        super(message);
    }
}
