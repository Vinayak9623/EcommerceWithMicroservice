package com.micro.user.service.serviceImpl;

import com.micro.user.FeignClientServices.OrderClient;
import com.micro.user.FeignClientServices.ProductClient;
import com.micro.user.FeignClientServices.dto.OrderResonseDto;
import com.micro.user.FeignClientServices.dto.OrderWithProductResponseDto;
import com.micro.user.FeignClientServices.dto.ProductResponseDto;
import com.micro.user.customException.EmailAlredyExistException;
import com.micro.user.customException.UserNotFoundException;
import com.micro.user.dto.LoginResponse;
import com.micro.user.dto.UserDto;
import com.micro.user.jwtSecurity.JwtUtil;
import com.micro.user.model.Role;
import com.micro.user.model.User;
import com.micro.user.repository.UserRepository;
import com.micro.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ModelMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;


    @Override
    public LoginResponse registerUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlredyExistException("Email already exists");
        }

        User user = userMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userDto.getRole() != null ? userDto.getRole() : Role.USER);

        User registeredUser = userRepository.save(user);
//        String token = jwtUtil.generateToken(registeredUser.getEmail(), registeredUser.getRole().name());

        return new LoginResponse("", true);
    }

    @Override
    public LoginResponse loginUser(String email, String password) {

        try {

            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

            return new LoginResponse(token, true);
        } catch (Exception e) {
            return new LoginResponse("Login failed: " + e.getMessage(), false);
        }
    }


    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> userMapper.map(user, UserDto.class))
                .collect(Collectors.toList());


    }

    @Override
    public UserDto getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not found "));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updateUser = userRepository.save(user);

        return userMapper.map(updateUser, UserDto.class);
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


    @Override
    public List<OrderWithProductResponseDto> getOrderByUserId(Long userId) {

        List<OrderResonseDto> orders = orderClient.getOrderByUserId(userId);

        if (orders == null || orders.isEmpty()) {

            throw new RuntimeException("no order found for this userId" + userId);
        }

        List<OrderWithProductResponseDto> orderWithProduct = orders.stream().map(order -> {
            ProductResponseDto product =
                    productClient.getProductById(order.getProductId());

            OrderWithProductResponseDto responseDto = new OrderWithProductResponseDto();

            responseDto.setOrderId(order.getId());
            responseDto.setProduct(product);
            responseDto.setQuantity(order.getQuantity());
            responseDto.setOrderDate(order.getOrderDate());

            return responseDto;
        }).collect(Collectors.toList());

        return orderWithProduct;
    }

}





