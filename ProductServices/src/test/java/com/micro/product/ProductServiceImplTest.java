package com.micro.product;

import com.micro.product.dto.ProductDto;
import com.micro.product.model.Product;
import com.micro.product.repository.ProductRepository;
import com.micro.product.service.ServiceImpl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void shouldCreateProductSuccessfully() {

        // Prepare input DTO
        ProductDto productDto = new ProductDto();
        productDto.setName("iPhone");
        productDto.setPrice(100000);
        productDto.setStockQuantity(10);
        productDto.setCategory("Electronics");

        // Prepare entity
        Product product = new Product();
        product.setName("iPhone");

        // Prepare saved entity (DB result)
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("iPhone");

        // Prepare response DTO
        ProductDto responseDto = new ProductDto();
        responseDto.setName("iPhone");

        // Mock mapper and repository
        when(productMapper.map(productDto, Product.class))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(savedProduct);

        when(productMapper.map(savedProduct, ProductDto.class))
                .thenReturn(responseDto);

        // Call service
        ProductDto result = productService.create(productDto);

        // Assertions
        assertNotNull(result);
        assertEquals("iPhone", result.getName());
    }

    @Test
    void shouldReturnAllProducts() {

        Product product1 = new Product();
        Product product2 = new Product();

        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        when(productMapper.map(Mockito.any(Product.class), Mockito.eq(ProductDto.class)))
                .thenReturn(new ProductDto());

        List<ProductDto> result = productService.getAllProduct();

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Phone");
        updateDto.setPrice(120000);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Phone");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Phone");

        ProductDto responseDto = new ProductDto();
        responseDto.setName("Updated Phone");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(updatedProduct);

        when(productMapper.map(updatedProduct, ProductDto.class))
                .thenReturn(responseDto);

        ProductDto result = productService.update(1L, updateDto);

        assertEquals("Updated Phone", result.getName());
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        verify(productRepository).delete(product);
        assertTrue(result.contains("deleted"));
    }



}
