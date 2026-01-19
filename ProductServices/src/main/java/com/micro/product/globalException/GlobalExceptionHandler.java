package com.micro.product.globalException;

import com.micro.product.common.ApiResponse;
import com.micro.product.globalException.Costomexception.InsufficientStockException;
import com.micro.product.globalException.Costomexception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(
            ProductNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null,
                        "PRODUCT_NOT_FOUND",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientStockException(
            InsufficientStockException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null,
                        "Insufficient Stock found",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        500,
                        "Something went wrong",
                        null,
                        "INTERNAL_SERVER_ERROR",
                        LocalDateTime.now()
                ));
    }
}
