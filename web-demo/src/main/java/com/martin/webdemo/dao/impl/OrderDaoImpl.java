package com.martin.webdemo.dao.impl;

import com.martin.webdemo.dao.OrderDao;
import com.martin.webdemo.dto.OrderQueryParams;
import com.martin.webdemo.dto.ProductQueryParams;
import com.martin.webdemo.viewModel.OrderItemVM;
import com.martin.webdemo.model.Order;
import com.martin.webdemo.model.OrderItem;
import com.martin.webdemo.rowmapper.OrderItemVmRowMapper;
import com.martin.webdemo.rowmapper.OrderRowMapper;
import com.martin.webdemo.viewModel.OrderVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount,created_date,last_modified_date " +
                     "FROM `order` WHERE order_id = :orderId";
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());

        if(orderList.size() > 0){
            return orderList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<OrderItemVM> getOrderItemsById(Integer orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, " +
                     "p.product_name, p.image_url " +
                     "FROM order_item AS oi LEFT JOIN product AS p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = :orderId ";

        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);

        List<OrderItemVM> orderItemVMList = namedParameterJdbcTemplate.query(sql,map,new OrderItemVmRowMapper());

        return orderItemVMList;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount,created_date,last_modified_date " +
                     "FROM `order` WHERE 1 = 1";

        Map<String,Object>map = new HashMap<>();
        sql = addFilteringSql(sql,map,orderQueryParams);
        sql += " ORDER BY created_date DESC";
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit",orderQueryParams.getLimit());
        map.put("offset",orderQueryParams.getOffset());

        return namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT COUNT(*) FROM `order` " +
                     "WHERE 1 = 1";

        Map<String,Object> map = new HashMap<>();
        sql = addFilteringSql(sql,map,orderQueryParams);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql,map, Integer.class);

        return count;
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {

        String sql = "INSERT INTO `order` (user_id, total_amount,created_date,last_modified_date) " +
                     "VALUES(:userId,:totalAmount,:createdDate,:lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("totalAmount",totalAmount);
        Date now = new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder key = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),key);

        Integer orderId = key.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item (order_id,product_id,quantity,amount) " +
                     "VALUES (:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[orderItemList.size()];

        for(int i = 0; i < orderItemList.size(); i++)
        {
            mapSqlParameterSources[i] = new MapSqlParameterSource();
            OrderItem orderItem = orderItemList.get(i);
            mapSqlParameterSources[i].addValue("orderId",orderId);
            mapSqlParameterSources[i].addValue("productId",orderItem.getProductId());
            mapSqlParameterSources[i].addValue("quantity",orderItem.getQuantity());
            mapSqlParameterSources[i].addValue("amount",orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql,mapSqlParameterSources);
    }

    private String addFilteringSql(String sql, Map<String,Object> map, OrderQueryParams orderQueryParams)
    {
        //category filter
        if(orderQueryParams.getUserId() != null){
            sql += " AND user_id = :userId";
            map.put("userId",orderQueryParams.getUserId());
        }

        return sql;
    }
}
