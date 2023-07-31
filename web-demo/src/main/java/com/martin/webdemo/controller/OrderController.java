package com.martin.webdemo.controller;

import com.martin.webdemo.dto.CreateOrderRequest;
import com.martin.webdemo.dto.OrderQueryParams;
import com.martin.webdemo.model.Order;
import com.martin.webdemo.service.OrderService;
import com.martin.webdemo.util.Page;
import com.martin.webdemo.viewModel.OrderVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<OrderVM> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest)
    {
        Integer orderId = orderService.createOrder(userId,createOrderRequest);
        OrderVM orderVM = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderVM);
    }

    @GetMapping("/users/{userId}/orders")
    public  ResponseEntity<Page<OrderVM>> getOrders(@PathVariable Integer userId,
                                                    @RequestParam(defaultValue = "10") @Min(1) @Max(1000) Integer limit,
                                                    @RequestParam(defaultValue = "0") @Min(0) Integer offset)
    {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        List<OrderVM> orderVMList = orderService.getOrders(orderQueryParams);
        Integer count = orderService.countOrder(orderQueryParams);
        Page<OrderVM> result = new Page<>();
        result.setTotal(count);
        result.setLimit(limit);
        result.setOffset(offset);
        result.setResults(orderVMList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
