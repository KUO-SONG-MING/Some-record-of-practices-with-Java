package com.martin.webdemo.viewModel;

import com.martin.webdemo.model.Order;

import java.util.List;

public class OrderVM extends Order {
    private List<OrderItemVM> orderItemVMList;

    public List<OrderItemVM> getOrderItemVMList() {
        return orderItemVMList;
    }

    public void setOrderItemVMList(List<OrderItemVM> orderItemVMList) {
        this.orderItemVMList = orderItemVMList;
    }
}
