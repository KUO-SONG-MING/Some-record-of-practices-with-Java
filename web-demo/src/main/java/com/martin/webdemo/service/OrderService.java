package com.martin.webdemo.service;

import com.martin.webdemo.dto.CreateOrderRequest;
import com.martin.webdemo.dto.OrderQueryParams;
import com.martin.webdemo.model.Order;
import com.martin.webdemo.viewModel.OrderVM;

import java.util.List;

public interface OrderService {
    OrderVM getOrderById(Integer orderId);
    List<OrderVM> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer createOrder(Integer orderId, CreateOrderRequest createOrderRequest);
}
