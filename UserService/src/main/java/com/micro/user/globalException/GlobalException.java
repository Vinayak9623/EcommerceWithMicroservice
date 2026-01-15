package com.micro.user.globalException;
import com.micro.user.common.ApiResponse;
import com.micro.user.globalException.customException.EmailAlredyExistException;
import com.micro.user.globalException.customException.InvalidCredentialsException;
import com.micro.user.globalException.customException.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(EmailAlredyExistException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailAlreadyExistException(EmailAlredyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>
                        (400,ex.getMessage()
                                ,null,"Email Alredy Exist"
                                ,LocalDateTime.now()));

    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>
                        (400,ex.getMessage()
                                ,null,"User not found Exception"
                                ,LocalDateTime.now()));

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidCredentialsException(InvalidCredentialsException ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(400,ex.getMessage(),null,"Invalid Credentials",LocalDateTime.now()));
    }
}
