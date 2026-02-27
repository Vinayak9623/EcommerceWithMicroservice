package com.micro.product.service;

import com.micro.product.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {


    ProductDto create(ProductDto productDto, MultipartFile image);
    List<ProductDto> getAllProduct();
    ProductDto getProductById(Long id);
    ProductDto update(Long id,ProductDto productDto);
    String deleteProduct(Long id);
    void reduceStock(Long productId, int quantity);
    void restoreStock(Long productId, int quantity);



}
