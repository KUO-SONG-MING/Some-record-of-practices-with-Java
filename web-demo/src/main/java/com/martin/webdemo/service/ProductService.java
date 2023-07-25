package com.martin.webdemo.service;

import com.martin.webdemo.dto.ProductQueryParams;
import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductService {
    Integer countProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void  deleteProduct(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
}
