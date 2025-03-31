package com.micro.product.service;

import com.micro.product.dto.ProductDto;

import java.util.List;

public interface ProductService {


    ProductDto create(ProductDto productDto);
    List<ProductDto> getAllProduct();
    ProductDto getProductById(Long id);
    ProductDto update(Long id,ProductDto productDto);
    String deleteProduct(Long id);

}
