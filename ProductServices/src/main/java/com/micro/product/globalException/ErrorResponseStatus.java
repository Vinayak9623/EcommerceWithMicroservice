package com.micro.product.globalException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ErrorResponseStatus {

    private int status;
    private String message;
    private LocalDateTime timeStamp;

}
