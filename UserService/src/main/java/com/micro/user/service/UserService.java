package com.micro.user.service;
import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserDto userDto);
    LoginResponse loginUser(LoginRequest loginRequest);
    List<UserResponse> getAllUser();
    UserResponse getUserById(Long id);
    UserResponse updateUser(long id,UserDto userDto);
    String deleteUser(long id);
    Page<UserResponse> getUsersPage(String keyword, Pageable pageable);

}
