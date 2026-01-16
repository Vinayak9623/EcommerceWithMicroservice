package com.micro.user.testCases;

import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.dto.UserResponse;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.model.Role;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  ModelMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private UserServiceImpl userService;

    //Arrange → Mock → Act → Assert
    @Test
    void registerUserSuccess(){
        UserDto userDto=new UserDto();

        userDto.setEmail("vinayakd@gmail.com");
        userDto.setMobileNumber("9308097387");
        userDto.setPassword("HashPassword");

        User user=new User();
        user.setEmail("vinayakd@gmail.com");

        User savedUser=new User();
        savedUser.setId(1L);
        savedUser.setEmail("vinayakd@gmail.com");

        UserResponse userResponse=new UserResponse();
        userResponse.setEmail("vinayakd@gmail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.map(userDto,User.class)).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("HashedPassword");
        when(userRepository.save(any())).thenReturn(savedUser);
        when(userMapper.map(savedUser,UserResponse.class)).thenReturn(userResponse);

        //Act
        UserResponse result =userService.registerUser(userDto);

        //Assert
        assertNotNull(result);
        assertEquals("vinayakd@gmail.com",result.getEmail());
    }

    @Test
    void loginUser(){

        LoginRequest request=new LoginRequest();

        request.setEmail("vinayakd@gmail.com");
        request.setPassword("HashedPassword");

        User user=new User();
        user.setEmail("vinayakd@gmail.com");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(), any())).thenReturn("Fake-Jwt-token");

        LoginResponse loginResponse =userService.loginUser(request);

        assertNotNull(loginResponse);
        assertTrue(loginResponse.isSuccess());
        assertEquals("Fake-Jwt-token",loginResponse.getToken());

    }

    @Test
    void shouldUpdateUserSuccessfully() {

        UserDto userDto = new UserDto();
        userDto.setName("Updated Name");
        userDto.setEmail("updated@gmail.com");
        userDto.setRole(Role.ADMIN);
        userDto.setPassword("newpassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@gmail.com");
        existingUser.setRole(Role.USER);
        existingUser.setPassword("old-hashed-password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@gmail.com");
        savedUser.setRole(Role.ADMIN);
        savedUser.setPassword("encoded-new-password");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Updated Name");
        response.setEmail("updated@gmail.com");
        response.setRole(Role.ADMIN);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newpassword"))
                .thenReturn("encoded-new-password");

        when(userRepository.save(any()))
                .thenReturn(savedUser);

        when(userMapper.map(savedUser, UserResponse.class))
                .thenReturn(response);

        UserResponse result = userService.updateUser(1L, userDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@gmail.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        String result = userService.deleteUser(1L);
        assertNotNull(result);
        assertTrue(result.contains("deleted"));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.deleteUser(1L)
        );
    }

}
