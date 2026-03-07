package com.micro.cart.service;

import com.micro.cart.dto.CartDto;

public interface CartService {
    
    CartDto getCartByUserId(Long userId);
    
    CartDto addItemToCart(Long userId, Long productId, int quantity, String token);
    
    CartDto updateItemQuantity(Long cartItemId, int quantity);
    
    void removeCartItem(Long cartItemId);
    
    void clearCart(Long userId);
}
