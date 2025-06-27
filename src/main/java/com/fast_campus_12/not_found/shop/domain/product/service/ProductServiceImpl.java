package com.fast_campus_12.not_found.shop.domain.product.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public abstract class ProductServiceImpl implements ProductService {
}
