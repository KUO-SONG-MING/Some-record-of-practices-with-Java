package com.martin.webdemo.service.impl;

import com.martin.webdemo.dao.ProductDao;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDaoImpl;
    @Override
    public Product getProductById(Integer productId) {
        return productDaoImpl.getProductById(productId);
    }
}
