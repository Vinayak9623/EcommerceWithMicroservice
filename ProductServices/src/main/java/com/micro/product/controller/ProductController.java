package com.micro.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.product.common.ApiResponse;
import com.micro.product.dto.ProductDto;
import com.micro.product.dto.ReduceStockRequest;
import com.micro.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductDto>> save(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ProductDto productDto = mapper.readValue(productJson, ProductDto.class);

        ProductDto savedProduct = productService.create(productDto, image);

        return ResponseEntity.ok(
                new ApiResponse<>(201,
                        "Product added successfully",
                        savedProduct,
                        null,
                        LocalDateTime.now())
        );
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(){
        List<ProductDto> productDtos = productService.getAllProduct();
        return ResponseEntity.ok(new ApiResponse<>(200,"Data fetch successfully",productDtos,null,LocalDateTime.now()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id){
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"Data fetch successfully",product,null,LocalDateTime.now()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> update(@PathVariable Long id,@RequestBody ProductDto productDto){
        ProductDto update = productService.update(id, productDto);
        return ResponseEntity.ok(new ApiResponse<>(200,"Product Updated successfully",update,null,LocalDateTime.now()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id){
        String response = productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"Product deleted successfully",null,null,LocalDateTime.now()));
    }

    @PutMapping("/reduce-stock")
    public ResponseEntity<ApiResponse<String>> reduceStock(
            @RequestBody ReduceStockRequest request) {
        productService.reduceStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new ApiResponse<>(200, "Stock reduced successfully", "SUCCESS", null, LocalDateTime.now()));
    }

    @PutMapping("/restore-stock")
    public ResponseEntity<ApiResponse<String>> restoreStock(
            @RequestBody ReduceStockRequest request) {
        productService.restoreStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new ApiResponse<>(200, "Stock restored successfully", "SUCCESS", null, LocalDateTime.now()));
    }

    @GetMapping("/getPagableProducts")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProductPage(
            @RequestParam(required = false) String keyword, 
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable){
        Page<ProductDto> page = productService.getproductpage(keyword, category, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(new ApiResponse<>(200,"Product fetch successfully",page,null,LocalDateTime.now()));
    }

}
