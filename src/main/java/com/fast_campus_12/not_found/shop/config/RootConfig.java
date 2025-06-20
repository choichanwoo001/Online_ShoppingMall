package com.fast_campus_12.not_found.shop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "com.fast_campus_12.not_found.shop.service",
        "com.fast_campus_12.not_found.shop.dao"
})
@Import({DataSourceConfig.class, MailConfig.class})
public class RootConfig {
}