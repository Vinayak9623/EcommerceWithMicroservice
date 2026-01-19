package com.micro.order.globalException.customException;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message){

        super(message);
    }
}
