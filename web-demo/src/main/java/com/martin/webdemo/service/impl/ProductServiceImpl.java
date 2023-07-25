package com.martin.webdemo.service.impl;

import com.martin.webdemo.constant.ProductCategory;
import com.martin.webdemo.dao.ProductDao;
import com.martin.webdemo.dto.ProductQueryParams;
import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDaoImpl;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDaoImpl.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDaoImpl.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDaoImpl.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDaoImpl.updateProduct(productId,productRequest);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDaoImpl.deleteProduct(productId);
    }
}
