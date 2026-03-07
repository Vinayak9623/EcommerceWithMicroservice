package com.micro.cart.service.serviceImpl;

import com.micro.cart.client.ProductClient;
import com.micro.cart.dto.CartDto;
import com.micro.cart.dto.ProductDto;
import com.micro.cart.model.Cart;
import com.micro.cart.model.CartItem;
import com.micro.cart.repository.CartItemRepository;
import com.micro.cart.repository.CartRepository;
import com.micro.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final ModelMapper modelMapper;

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));
        return modelMapper.map(cart, CartDto.class);
    }

    private Cart createCart(Long userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .totalAmount(0)
                .build();
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long userId, Long productId, int quantity, String token) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        // Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // Verify product exists and fetch price
            ProductDto product = productClient.getProductById(productId, token);
            
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(productId)
                    .quantity(quantity)
                    .price(product.getPrice()) // Store the price at the time of adding
                    .build();
            
            cart.getItems().add(newItem);
        }

        recalculateTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));

        if (quantity <= 0) {
            removeCartItem(cartItemId);
            Cart cart = cartRepository.findById(item.getCart().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            return modelMapper.map(cart, CartDto.class);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
            
            Cart cart = item.getCart();
            recalculateTotal(cart);
            Cart savedCart = cartRepository.save(cart);
            return modelMapper.map(savedCart, CartDto.class);
        }
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + cartItemId));
        
        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        
        recalculateTotal(cart);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
                
        cart.getItems().clear();
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }

    private void recalculateTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }
}
