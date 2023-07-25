package com.martin.webdemo.service;

import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void  deleteProduct(Integer productId);
}
