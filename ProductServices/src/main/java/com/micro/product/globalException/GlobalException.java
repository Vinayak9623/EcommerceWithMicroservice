package com.micro.product.globalException;

import com.micro.product.Costomexception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseStatus> handleProductNotFoundException(ProductNotFoundException ex){

        ErrorResponseStatus errorResponseStatus=new ErrorResponseStatus();

        errorResponseStatus.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponseStatus.setMessage(ex.getMessage());
        errorResponseStatus.setTimeStamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponseStatus);
    }



}
