package com.martin.webdemo.dto;

import com.martin.webdemo.constant.ProductCategory;

public class ProductQueryParams {
    private ProductCategory productCategory;
    private String search;

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
