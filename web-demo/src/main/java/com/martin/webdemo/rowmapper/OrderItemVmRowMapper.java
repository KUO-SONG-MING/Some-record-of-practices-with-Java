package com.martin.webdemo.rowmapper;

import com.martin.webdemo.viewModel.OrderItemVM;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemVmRowMapper implements RowMapper<OrderItemVM> {
    @Override
    public OrderItemVM mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderItemVM orderItemVM = new OrderItemVM();
        orderItemVM.setOrderItemId(resultSet.getInt("order_item_id"));
        orderItemVM.setOrderId(resultSet.getInt("order_id"));
        orderItemVM.setProductId(resultSet.getInt("product_id"));
        orderItemVM.setQuantity(resultSet.getInt("quantity"));
        orderItemVM.setAmount(resultSet.getInt("amount"));
        orderItemVM.setProductName(resultSet.getString("product_name"));
        orderItemVM.setImageUrl(resultSet.getString("image_url"));
        return orderItemVM;
    }
}
