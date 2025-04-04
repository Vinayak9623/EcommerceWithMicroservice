package com.micro.user.controller;
import com.micro.user.FeignClientServices.dto.OrderWithProductResponseDto;
import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.model.Role;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


//    @GetMapping("/{userId}/orders")
//    public ResponseEntity<List<OrderWithProductResponseDto>> getOrderByUserId(@PathVariable Long userId) {
//
//        System.out.println("Controller call");
//        List<OrderWithProductResponseDto> orders = userService.getOrderByUserId(userId);
//
//        System.out.println(orders);
//        return ResponseEntity.ok(orders);
//    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderWithProductResponseDto>> getOrderByUserId(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        List<OrderWithProductResponseDto> orders = userService.getOrderByUserId(userId,token);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/userDetails")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            String role = jwtUtil.extractRole(jwt);
            Optional<User> user = userRepository.findByEmail(username);

            if (user.isPresent()) {
               // UserDto userDto = new UserDto(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getPassword(), Role.valueOf(role));
                UserDto userDto = new UserDto(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getPassword(),user.get().getRole());
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



}




