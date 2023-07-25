package com.martin.webdemo.controller;

import com.martin.webdemo.constant.ProductCategory;
import com.martin.webdemo.dto.ProductQueryParams;
import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.module.ModuleDescriptor;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory productCategory,
            @RequestParam(required = false) String search)
    {
        ProductQueryParams model = new ProductQueryParams();
        model.setProductCategory(productCategory);
        model.setSearch(search);

        List<Product> products = productService.getProducts(model);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId)
    {
        Product product = productService.getProductById(productId);

        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest)
    {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody ProductRequest productRequest)
    {
        // check whether old product is or not
        Product oldProduct = productService.getProductById(productId);
        if(oldProduct == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // execute update
        productService.updateProduct(productId,productRequest);
        Product updatedProduct = productService.getProductById(productId);
        return  ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public  ResponseEntity<?> deleteProduct(@PathVariable Integer productId)
    {
        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
