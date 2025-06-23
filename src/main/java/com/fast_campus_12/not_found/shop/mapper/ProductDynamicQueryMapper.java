package com.fast_campus_12.not_found.shop.mapper;

import com.fast_campus_12.not_found.shop.product.model.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDynamicQueryMapper {
    List<Product> findProductListByCategory(@Param("lv2Name") String lv2Name, @Param("lv3Name") String lv3Name);
}
