package com.micro.user.service.serviceImpl;
import com.micro.user.globalException.customException.EmailAlredyExistException;
import com.micro.user.globalException.customException.InvalidCredentialsException;
import com.micro.user.globalException.customException.MobileNumberAlredyExistException;
import com.micro.user.globalException.customException.UserNotFoundException;
import com.micro.user.dto.LoginRequest;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.dto.UserResponse;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.model.Role;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse registerUser(UserDto userDto) {

        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new EmailAlredyExistException("Email Already Exist");
        }
        if(userRepository.findByMobileNumber(userDto.getMobileNumber()).isPresent()){
            throw new MobileNumberAlredyExistException("Mobile number Already Exist");
        }
        User user = userMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userDto.getRole() != null ? userDto.getRole() : Role.USER);
        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser,UserResponse.class);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(), loginRequest.getPassword()));
        }
        catch (BadCredentialsException ex){
            throw new InvalidCredentialsException("Invalid email or password");
        }
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new EmailAlredyExistException("Email not found"));
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
            return LoginResponse.builder().token(token).success(true).build();

    }


    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> userMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateUser(long id, UserDto userDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updateUser = userRepository.save(user);
        return userMapper.map(updateUser, UserResponse.class);
    }

    @Override
    public String deleteUser(long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow
                        (() -> new UsernameNotFoundException
                                ("User not found"));
        userRepository.deleteById(id);
        return "User with given ID: " + id + "deleted Sucessfully";
    }

}




