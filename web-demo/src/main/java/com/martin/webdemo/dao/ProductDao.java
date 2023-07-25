package com.martin.webdemo.dao;

import com.martin.webdemo.dto.ProductQueryParams;
import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;

import java.util.List;

public interface ProductDao {

    Integer countProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void  deleteProduct(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
}
