package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.Colors;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ColorsMapper {
    List<Colors> findAll();
    void insert(Colors color);
    void update(Colors color);
    void delete(Long colorId);
}

