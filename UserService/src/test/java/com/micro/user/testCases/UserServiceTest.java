package com.micro.user.testCases;

import com.micro.user.customException.EmailAlredyExistException;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        userDto.setPassword("Test@123");

        user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
    }

    @Test
    void emailAlredyExistTest() {
        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(EmailAlredyExistException.class, () -> {
            userService.registerUser(userDto);
        });

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userDto.getEmail());
    }

    @Test
    void registerSuccess() {

        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        Mockito.when(userMapper.map(userDto, User.class)).thenReturn(user);

        LoginResponse loginResponse = userService.registerUser(userDto);

        Assertions.assertNotNull(loginResponse);
        Assertions.assertEquals("Registration sucessfull", loginResponse.getMessage());
        Assertions.assertTrue(loginResponse.isSuccess());


        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userDto.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userDto.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);

    }


    @Test
    void loginSuccess() {

        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(true);

        LoginResponse loginResponse = userService.loginUser(userDto.getEmail(), userDto.getPassword());

        Assertions.assertNotNull(loginResponse);
        Assertions.assertEquals("Login sucessfull",loginResponse.getMessage());
        Assertions.assertTrue(loginResponse.isSuccess());


        Mockito.verify(userRepository,Mockito.times(1)).findByEmail(userDto.getEmail());
        Mockito.verify(passwordEncoder,Mockito.times(1)).matches(userDto.getPassword(),user.getPassword());
    }

    @Test
    void loginFail(){

        Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(userDto.getPassword(),user.getPassword())).thenReturn(false);

        LoginResponse loginResponse = userService.loginUser(userDto.getEmail(), userDto.getPassword());

        Assertions.assertNotNull(loginResponse);
        Assertions.assertEquals(" login fail ,Enter Correct password ",loginResponse.getMessage());
        Assertions.assertFalse(loginResponse.isSuccess());


    }



}


