package com.micro.user.service;

import com.micro.user.FeignClientServices.dto.OrderResonseDto;
import com.micro.user.FeignClientServices.dto.OrderWithProductResponseDto;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;

import java.util.List;

public interface UserService {

    LoginResponse registerUser(UserDto userDto);
    LoginResponse loginUser(String email,String password);
    List<UserDto> getAllUser();
    UserDto getUserById(Long id);
    UserDto updateUser(long id,UserDto userDto);
    String deleteUser(long id);
    List<OrderWithProductResponseDto> getOrderByUserId(Long userId);
}
