package com.micro.product.service.ServiceImpl;
import com.micro.product.globalException.Costomexception.InsufficientStockException;
import com.micro.product.globalException.Costomexception.ProductNotFoundException;
import com.micro.product.dto.ProductDto;
import com.micro.product.model.Product;
import com.micro.product.repository.ProductRepository;
import com.micro.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper productMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public ProductDto create(ProductDto productDto, MultipartFile image) {

        // ✅ 1. Validate image
        if (image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        if (!image.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        try {

            // ✅ 2. Generate unique filename
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            // ✅ 3. Define upload directory
            Path uploadPath = Paths.get("uploads");
            // Create folder if not exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // ✅ 4. Save file to folder
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // ✅ 5. Set image URL in DTO
            productDto.setImageUrl(baseUrl + "/uploads/" + fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image");
        }
        // ✅ 6. Save product in DB
        Product product = productMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);

        return productMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProduct() {

        return productRepository.findAll()
                .stream()
                .map(product -> productMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        return productMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setCategory(productDto.getCategory());

        Product updatedProduct = productRepository.save(product);
        return productMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public String deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public void reduceStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for product id: " + productId);
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity);

        productRepository.save(product);
    }

    @Override
    public void restoreStock(Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId));
        product.setStockQuantity(
                product.getStockQuantity() + quantity);
        productRepository.save(product);
    }
}

