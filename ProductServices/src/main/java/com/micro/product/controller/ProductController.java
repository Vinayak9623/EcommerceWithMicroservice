package com.micro.product.controller;

import com.micro.product.dto.ProductDto;
import com.micro.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/save")
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto) {
        System.out.println("Controler call");

        ProductDto saveProduct = productService.create(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProduct);
    }


    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts(){
        System.out.println("Controller call");
        List<ProductDto> productDtos = productService.getAllProduct();

        return ResponseEntity.ok(productDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        ProductDto product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,@RequestBody ProductDto productDto){
        ProductDto update = productService.update(id, productDto);

        return ResponseEntity.ok(update);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        String response = productService.deleteProduct(id);

        return ResponseEntity.ok(response);
    }


}
