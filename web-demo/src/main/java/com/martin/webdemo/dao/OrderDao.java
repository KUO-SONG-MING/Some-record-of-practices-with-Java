package com.martin.webdemo.dao;

import com.martin.webdemo.dto.OrderQueryParams;
import com.martin.webdemo.viewModel.OrderItemVM;
import com.martin.webdemo.model.Order;
import com.martin.webdemo.model.OrderItem;
import com.martin.webdemo.viewModel.OrderVM;

import java.util.List;

public interface OrderDao {
    Order  getOrderById(Integer orderId);
    List<OrderItemVM> getOrderItemsById(Integer orderId);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer createOrder(Integer userId,Integer totalAmount);
    void  createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
