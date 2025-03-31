package com.micro.user.controller;
import com.micro.user.FeignClientServices.dto.OrderWithProductResponseDto;
import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody UserDto userDto) {

        LoginResponse loginResponse = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(loginResponse);
    }


    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> allUser = userService.getAllUser();

        return ResponseEntity.ok(allUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userById = userService.getUserById(id);

        return ResponseEntity.ok(userById);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);

        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {

        String response = userService.deleteUser(id);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderWithProductResponseDto>> getOrderByUserId(@PathVariable Long userId) {
        List<OrderWithProductResponseDto> orders = userService.getOrderByUserId(userId);

        return ResponseEntity.ok(orders);
    }


    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            String role = jwtUtil.extractRole(jwt);

            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

//    @GetMapping("/role")
//    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String token) {
//        try {
//            String jwt = token.replace("Bearer ", "");
//            String role = jwtUtil.extractRole(jwt);
//            return ResponseEntity.ok(role);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//    }

    @GetMapping("/role")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String jwt = token.substring(7);
            String role = jwtUtil.extractRole(jwt);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }}}

//    @GetMapping("/role")
//    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
//        String jwt = token.substring(7); // Remove "Bearer "
//        String role = "ROLE_" + jwtUtil.extractRole(jwt); // Add ROLE_ prefix
//        return ResponseEntity.ok(role);
//    }
  //  }


