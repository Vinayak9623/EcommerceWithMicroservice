package com.micro.order.globalException;
import com.micro.order.customException.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<OrderErrorResponseStatus> handleOrderNotFoundException(OrderNotFoundException ex) {

        OrderErrorResponseStatus orderErrorResponseStatus = new OrderErrorResponseStatus();

        orderErrorResponseStatus.setStatus(HttpStatus.NOT_FOUND.value());
        orderErrorResponseStatus.setMessage(ex.getMessage());
        orderErrorResponseStatus.setTimeStamp(LocalDateTime.now());


        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(orderErrorResponseStatus);

    }

}
