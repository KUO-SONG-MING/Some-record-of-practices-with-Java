package com.martin.webdemo.dao;

import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void  deleteProduct(Integer productId);
}
