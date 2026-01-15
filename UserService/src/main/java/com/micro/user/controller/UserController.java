package com.micro.user.controller;

import com.micro.user.common.ApiResponse;
import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.dto.UserResponse;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody UserDto userDto) {
        UserResponse response = userService.registerUser(userDto);
        return ResponseEntity.ok(new ApiResponse<>(200,"User register successfully",response,null, LocalDateTime.now()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.loginUser(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(201,"Successfully Logged in",loginResponse,null,LocalDateTime.now()));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        List<UserResponse> allUser = userService.getAllUser();
        return ResponseEntity.ok(new ApiResponse<>(200,"User fetch Successfully",allUser,null,LocalDateTime.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userById = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"User fetch Successfully",userById,null,LocalDateTime.now()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        UserResponse updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(new ApiResponse<>(201,"user updated successfully",updatedUser,null,LocalDateTime.now()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable long id) {
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"User deleted successfully",null,null,LocalDateTime.now()));
    }

}




