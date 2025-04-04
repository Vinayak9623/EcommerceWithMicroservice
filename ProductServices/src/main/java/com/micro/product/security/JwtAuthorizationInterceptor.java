package com.micro.product.security;

import com.micro.product.feignClientServices.Role;
import com.micro.product.feignClientServices.UserClient;
import com.micro.product.feignClientServices.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class JwtAuthorizationInterceptor implements HandlerInterceptor {

    @Lazy
    @Autowired
    private UserClient userClient;


    private static final List<String> ADMIN_ONLY_ENDPOINTS = List.of(
            "POST",
            "PUT",
            "DELETE"
    );


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("Intercepter call");
        // Bypass interceptor for internal service-to-service calls
        String internalAuth = request.getHeader("Internal-Auth");
        if ("SECRET_INTERNAL_KEY".equals(internalAuth)) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("in if");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return false;
        }

        try {
            System.out.println("In try");
            UserDto user = userClient.getUserDetails(authHeader).getBody();
            System.out.println(user);
            request.setAttribute("user", user);

            String requestPath = request.getRequestURI();
            String method = request.getMethod();
            String endpoint = method + " " + requestPath;

            System.out.println(endpoint);

            if (ADMIN_ONLY_ENDPOINTS.stream().anyMatch(endpoint::startsWith) && user.getRole() != Role.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admins only");
                return false;
            }



            System.out.println("User Role: " + user.getRole());

            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }

    }
}
