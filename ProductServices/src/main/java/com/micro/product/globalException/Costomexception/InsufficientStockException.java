package com.micro.product.globalException.Costomexception;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(String message){
        super(message);
    }
}
