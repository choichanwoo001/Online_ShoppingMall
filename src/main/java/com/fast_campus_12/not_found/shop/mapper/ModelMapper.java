package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

@Mapper
public interface ModelMapper {
    void insert(Model model);
    void update(Model model);
    void delete(@Param("modelId") BigInteger modelId);
}

