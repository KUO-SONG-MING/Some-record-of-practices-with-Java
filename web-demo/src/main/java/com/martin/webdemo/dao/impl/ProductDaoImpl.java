package com.martin.webdemo.dao.impl;

import com.martin.webdemo.dao.ProductDao;
import com.martin.webdemo.dto.ProductRequest;
import com.martin.webdemo.model.Product;
import com.martin.webdemo.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                     "VALUES (:product_name, :category, :image_url, :price, :stock, :description, :created_date, :last_modified_date) ";
        Map<String,Object> map = new HashMap<>();
        map.put("product_name",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());
        map.put("image_url",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now = new Date();
        map.put("created_date",now);
        map.put("last_modified_date",now);

        KeyHolder key = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),key);

        Integer productId = key.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :product_name, category = :category, image_url = :image_url, price = :price, stock =:stock, description =:description, last_modified_date =:last_modified_date "+
                     "WHERE product_id = :product_id";

        Map<String,Object> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("product_name",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());
        map.put("image_url",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("last_modified_date", new Date());

        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id =:product_id";
        Map<String,Object> map = new HashMap<>();
        map.put("product_id",productId);
        namedParameterJdbcTemplate.update(sql,map);
    }
}
