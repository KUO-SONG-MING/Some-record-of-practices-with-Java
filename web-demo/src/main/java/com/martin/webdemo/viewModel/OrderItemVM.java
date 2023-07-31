package com.martin.webdemo.viewModel;

import com.martin.webdemo.model.OrderItem;

public class OrderItemVM extends OrderItem {

    private String productName;
    private String imageUrl;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
