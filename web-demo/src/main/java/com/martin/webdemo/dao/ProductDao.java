package com.martin.webdemo.dao;

import com.martin.webdemo.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
