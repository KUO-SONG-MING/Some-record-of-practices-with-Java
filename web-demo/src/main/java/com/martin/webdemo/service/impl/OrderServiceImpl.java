package com.martin.webdemo.service.impl;

import com.martin.webdemo.dao.OrderDao;
import com.martin.webdemo.dao.ProductDao;
import com.martin.webdemo.dao.UserDao;
import com.martin.webdemo.dto.BuyItem;
import com.martin.webdemo.dto.CreateOrderRequest;
import com.martin.webdemo.dto.OrderQueryParams;
import com.martin.webdemo.viewModel.OrderItemVM;
import com.martin.webdemo.model.Order;
import com.martin.webdemo.model.OrderItem;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.model.User;
import com.martin.webdemo.service.OrderService;
import com.martin.webdemo.viewModel.OrderVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        User user = userDao.getUserById(userId);
        if(user == null)
        {
            log.warn("ID為{}的使用者帳號不存在",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Integer totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem item : createOrderRequest.getBuyItemList())
        {
            Product product = productDao.getProductById(item.getProductId());

            if(product == null){
                log.warn("商品{}不存在",item.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < item.getQuantity()){
                log.warn("商品{}庫存不足，目前庫存為{}個，您的購買數量為{}個。",
                        item.getProductId(),
                        product.getStock(),
                        item.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProductId(),product.getStock()-item.getQuantity());

            Integer amount = product.getPrice() * item.getQuantity();
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(amount);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductId(product.getProductId());
            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId,totalAmount);

        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }

    @Override
    public OrderVM getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItemVM> orderItemVMLists = orderDao.getOrderItemsById(orderId);

        OrderVM orderVM = new OrderVM();
        orderVM.setOrderItemVMList(orderItemVMLists);
        orderVM.setOrderId(order.getOrderId());
        orderVM.setUserId(order.getUserId());
        orderVM.setTotalAmount(order.getTotalAmount());
        orderVM.setCreatedDate(order.getCreatedDate());
        orderVM.setLastModifiedDate(order.getLastModifiedDate());

        return orderVM;
    }

    @Override
    public List<OrderVM> getOrders(OrderQueryParams orderQueryParams) {
        List<OrderVM> OrderVmList = new ArrayList<>();
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

       for(Order order : orderList)
       {
           OrderVM orderVM = new OrderVM();
           orderVM.setUserId(order.getUserId());
           orderVM.setOrderId(order.getOrderId());
           orderVM.setTotalAmount(order.getTotalAmount());
           orderVM.setCreatedDate(order.getCreatedDate());
           orderVM.setLastModifiedDate(order.getLastModifiedDate());

           List<OrderItemVM> orderItemList = orderDao.getOrderItemsById(order.getOrderId());
           orderVM.setOrderItemVMList(orderItemList);

           OrderVmList.add(orderVM);
       }

       return OrderVmList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
       return orderDao.countOrder(orderQueryParams);
    }
}
