package com.micro.user.globalException;
import com.micro.user.customException.EmailAlredyExistException;
import com.micro.user.customException.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(EmailAlredyExistException.class)
    public ResponseEntity<ErrorResponseStatus> handleEmailAlreadyExistException(EmailAlredyExistException ex) {


        ErrorResponseStatus errorResponseStatus=ErrorResponseStatus.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseStatus);

    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseStatus> handleUserNotFoundException(UserNotFoundException ex) {


        ErrorResponseStatus errorResponseStatus=ErrorResponseStatus.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseStatus);

    }
}
