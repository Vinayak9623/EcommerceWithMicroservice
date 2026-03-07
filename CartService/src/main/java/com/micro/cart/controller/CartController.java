package com.micro.cart.controller;

import com.micro.cart.common.ApiResponse;
import com.micro.cart.dto.CartItemRequest;
import com.micro.cart.dto.CartDto;
import com.micro.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartDto>> getCart(@PathVariable Long userId) {
        CartDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart fetched successfully", cart, null, LocalDateTime.now())
        );
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(
            @PathVariable Long userId,
            @RequestBody CartItemRequest request,
            @RequestHeader("Authorization") String token) {
        
        CartDto cart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity(), token);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Item added to cart successfully", cart, null, LocalDateTime.now())
        );
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDto>> updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        
        CartDto cart = cartService.updateItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart item quantity updated successfully", cart, null, LocalDateTime.now())
        );
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<String>> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart item removed successfully", "SUCCESS", null, LocalDateTime.now())
        );
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart cleared successfully", "SUCCESS", null, LocalDateTime.now())
        );
    }
}
