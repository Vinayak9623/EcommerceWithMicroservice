package com.micro.product.service.ServiceImpl;

import com.micro.product.Costomexception.ProductNotFoundException;
import com.micro.product.dto.ProductDto;
import com.micro.product.jwtServices.JwtValidationService;
import com.micro.product.model.Product;
import com.micro.product.repository.ProductRepository;
import com.micro.product.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper productMapper;

    @Autowired
    private JwtValidationService jwtValidationService;



    @Override
    public ProductDto create(ProductDto productDto) {
        Product product=productMapper.map(productDto, Product.class);
        productRepository.save(product);

        return productMapper.map(product,ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> productMapper
                        .map(product,ProductDto.class)).collect(Collectors.toList());
    }


    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return productMapper.map(product,ProductDto.class);
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found"));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(productDto.getCategory());

        Product updateProduct = productRepository.save(product);

        return productMapper.map(updateProduct,ProductDto.class);
    }

    @Override
    public String deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("product not found"));

        productRepository.deleteById(id);
        return "Product Deleted sucessfully";
    }

}
