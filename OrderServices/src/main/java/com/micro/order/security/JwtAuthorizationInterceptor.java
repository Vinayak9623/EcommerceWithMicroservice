package com.micro.order.security;
import com.micro.order.externalServices.Role;
import com.micro.order.externalServices.UserClient;
import com.micro.order.externalServices.UserDto;
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

    private static final List<String> ADMIN_ONLY_METHODS = List.of(
            "GET",
            "PUT",
            "DELETE"
    );

    private static final List<String> ALLOWED_USER_ENDPOINTS = List.of(
            "/order/place",
            "/order/history",
            "/order/user/"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return false;
        }

        try {
            // Get user details
            UserDto user = userClient.getUserDetails(authHeader);
            request.setAttribute("user", user);

            String path = request.getRequestURI();
            String method = request.getMethod();

            if ("POST".equals(method) && path.endsWith("/order/place")) {
                return true;
            }
            boolean isAllowedForUser = ALLOWED_USER_ENDPOINTS.stream()
                    .anyMatch(endpoint -> path.contains(endpoint));
            if (ADMIN_ONLY_METHODS.contains(method)){
                if (user.getRole() != Role.ADMIN && !isAllowedForUser) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admins only");
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }
}