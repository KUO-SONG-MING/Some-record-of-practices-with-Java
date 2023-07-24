package com.martin.webdemo.dao.impl;

import com.martin.webdemo.dao.ProductDao;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProductById(Integer productId)
    {
        String sql = "SELECT product_id,product_name, category, image_url, price, stock, description, created_date, last_modified_date " +
                     "FROM product WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);

        List<Product> querys = namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());

        if(querys.size() > 0) {
            return querys.get(0);
        }else {
            return null;
        }
    }
}
