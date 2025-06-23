CREATE TABLE `Refund` (
                          `refund_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `return_id` BIGINT NOT NULL,
                          `payment_id` BIGINT NOT NULL,
                          `refund_type` VARCHAR(20) NOT NULL DEFAULT 'full' COMMENT 'full, partial',
                          `refund_amount` DECIMAL(12,2) NULL,
                          `refund_method_code` INT NULL,
                          `refund_account` VARCHAR(100) NULL,
                          `admin_memo` VARCHAR(500) NULL,
                          `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          `requested_at` TIMESTAMP NULL,
                          `processed_at` TIMESTAMP NULL,
                          `completed_at` TIMESTAMP NULL,
                          PRIMARY KEY (`refund_id`),
                          INDEX `idx_refund_return` (`return_id`),
                          INDEX `idx_refund_payment` (`payment_id`)
) ENGINE=InnoDB COMMENT='환불';

CREATE TABLE `ORDER_ITEM` (
                              `order_item_id` BIGINT NOT NULL AUTO_INCREMENT,
                              `order_id` BIGINT NOT NULL,
                              `product_snapshot_id` BIGINT NOT NULL,
                              `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`order_item_id`),
                              INDEX `idx_order_item_order` (`order_id`),
                              INDEX `idx_order_item_snapshot` (`product_snapshot_id`)
) ENGINE=InnoDB COMMENT='주문 아이템';

CREATE TABLE `Inquiry` (
                           `inquiry_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `user_id` BIGINT NOT NULL,
                           `product_id` BIGINT NULL,
                           `title` VARCHAR(200) NULL,
                           `content` TEXT NULL,
                           `inquiry_category` VARCHAR(20) NULL DEFAULT 'product' COMMENT 'product, delivery, payment, refund, etc',
                           `status` VARCHAR(20) NULL DEFAULT 'pending' COMMENT 'pending, answered, closed',
                           `is_secret` TINYINT(1) NULL DEFAULT 0,
                           `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `deleted_at` DATETIME NULL,
                           PRIMARY KEY (`inquiry_id`),
                           INDEX `idx_inquiry_user` (`user_id`),
                           INDEX `idx_inquiry_product` (`product_id`),
                           INDEX `idx_inquiry_status` (`status`, `created_at`)
) ENGINE=InnoDB COMMENT='문의';

CREATE TABLE `product_varient_detail` (
                                          `product_detail_id` BIGINT NOT NULL AUTO_INCREMENT,
                                          `product_variant_id` BIGINT NOT NULL,
                                          `model_id` BIGINT NOT NULL,
                                          `delivery_option` VARCHAR(100) NULL,
                                          `model_specific` VARCHAR(100) NULL,
                                          `fabric_info` TEXT NULL,
                                          `fabric_mangement_info` VARCHAR(1000) NULL,
                                          PRIMARY KEY (`product_detail_id`),
                                          INDEX `idx_variant_detail_variant` (`product_variant_id`),
                                          INDEX `idx_variant_detail_model` (`model_id`)
) ENGINE=InnoDB COMMENT='상품 변형 상세';

CREATE TABLE `model` (
                         `model_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(100) NULL,
                         `height` INT NULL,
                         `weight` INT NULL,
                         `gender` CHAR(1) NULL COMMENT 'M, F',
                         `age` INT NULL,
                         `is_active` TINYINT(1) NULL DEFAULT 1,
                         PRIMARY KEY (`model_id`),
                         INDEX `idx_model_active` (`is_active`)
) ENGINE=InnoDB COMMENT='모델';

CREATE TABLE `USER_DETAIL` (
                               `user_id` BIGINT NOT NULL,
                               `email` VARCHAR(255) NULL,
                               `name` VARCHAR(255) NULL,
                               `phone_number` VARCHAR(20) NULL,
                               `birth_date` DATE NULL,
                               `gender` CHAR(1) NULL COMMENT 'M, F',
                               `job_code` INT NOT NULL,
                               PRIMARY KEY (`user_id`),
                               UNIQUE KEY `uk_user_email` (`email`),
                               INDEX `idx_user_detail_job` (`job_code`)
) ENGINE=InnoDB COMMENT='사용자 상세';

CREATE TABLE `pay_status_code` (
                                   `pay_status_id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `pay_status_name` VARCHAR(50) NULL,
                                   PRIMARY KEY (`pay_status_id`)
) ENGINE=InnoDB COMMENT='결제 상태 코드';

CREATE TABLE `USER_COUPON` (
                               `user_coupon_id` BIGINT NOT NULL AUTO_INCREMENT,
                               `user_id` BIGINT NOT NULL,
                               `coupon_id` BIGINT NOT NULL,
                               `coupon_code` VARCHAR(50) NULL,
                               `issued_at` DATETIME NULL,
                               `is_used` TINYINT(1) NULL DEFAULT 0,
                               `used_at` DATETIME NULL,
                               `coupon_num` BIGINT NULL,
                               `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                               `expire_at` DATETIME NULL,
                               `deleted_at` DATETIME NULL,
                               PRIMARY KEY (`user_coupon_id`),
                               INDEX `idx_user_coupon_user` (`user_id`),
                               INDEX `idx_user_coupon_coupon` (`coupon_id`),
                               INDEX `idx_user_coupon_code` (`coupon_code`),
                               INDEX `idx_user_coupon_expire` (`expire_at`, `is_used`)
) ENGINE=InnoDB COMMENT='사용자 쿠폰';

CREATE TABLE `inquiry_answer` (
                                  `inquiry_answer_id` BIGINT NOT NULL AUTO_INCREMENT,
                                  `inquiry_id` BIGINT NOT NULL,
                                  `admin_id` BIGINT NOT NULL,
                                  `content` TEXT NULL,
                                  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`inquiry_answer_id`),
                                  INDEX `idx_inquiry_answer_inquiry` (`inquiry_id`),
                                  INDEX `idx_inquiry_answer_admin` (`admin_id`)
) ENGINE=InnoDB COMMENT='문의 답변';

CREATE TABLE `PRODUCT_SNAPSHOT` (
                                    `product_snapshot_id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `product_id` BIGINT NULL DEFAULT NULL,
                                    `title` VARCHAR(200) NOT NULL,
                                    `price` DECIMAL(10,2) NOT NULL,
                                    `discount_price` DECIMAL(10,2) NULL,
                                    `sku` VARCHAR(50) NULL,
                                    `summary` VARCHAR(500) NULL,
                                    `description` TEXT NULL,
                                    `thumbnail` VARCHAR(255) NULL,
                                    `weight` DECIMAL(5,2) NULL,
                                    `dimensions` VARCHAR(50) NULL,
                                    `special` VARCHAR(100) NULL,
                                    `tags` VARCHAR(255) NULL,
                                    `seasons` VARCHAR(10) NOT NULL DEFAULT '0000' COMMENT 'spring,summer,fall,winter bits',
                                    `is_featured` TINYINT(1) NOT NULL DEFAULT 0,
                                    `average_rating` DECIMAL(3,1) NULL,
                                    `sales_count` INT NULL DEFAULT 0,
                                    `view_count` INT NULL DEFAULT 0,
                                    `stock_quantity` INT NULL,
                                    `availability_date` DATETIME NULL,
                                    `enabled` TINYINT(1) NOT NULL DEFAULT 1,
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    `deleted_at` DATETIME NULL,
                                    `is_deleted` DATETIME NULL,
                                    `hex_code` VARCHAR(7) NULL,
                                    `color_name` VARCHAR(50) NULL,
                                    `size` VARCHAR(50) NULL,
                                    `fabric_info` TEXT NULL,
                                    `care_instructions` TEXT NULL,
                                    `original_price` DECIMAL(10,2) NULL,
                                    `extra_charge` DECIMAL(10,2) NULL,
                                    `sale_price` DECIMAL(10,2) NULL,
                                    `discount_rate` DECIMAL(5,2) NULL,
                                    PRIMARY KEY (`product_snapshot_id`),
                                    INDEX `idx_product_snapshot_product` (`product_id`),
                                    INDEX `idx_product_snapshot_enabled` (`enabled`, `created_at`),
                                    INDEX `idx_product_snapshot_featured` (`is_featured`, `enabled`)
) ENGINE=InnoDB COMMENT='상품 스냅샷';

CREATE TABLE `USERS` (
                         `user_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `password` VARCHAR(255) NULL,
                         `is_activate` TINYINT(1) NULL DEFAULT 1,
                         `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `deleted_at` DATETIME NULL,
                         `role` VARCHAR(20) NULL DEFAULT 'USER' COMMENT 'ADMIN, USER',
                         `is_deleted` TINYINT(1) NULL DEFAULT 0,
                         PRIMARY KEY (`user_id`),
                         INDEX `idx_users_role` (`role`),
                         INDEX `idx_users_active` (`is_activate`, `is_deleted`)
) ENGINE=InnoDB COMMENT='사용자';

CREATE TABLE `Order_state_code` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `name` VARCHAR(50) NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `pg_code` (
                           `pg_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `pg_code` VARCHAR(36) NULL COMMENT 'UUID format',
                           `pg_name` VARCHAR(20) NULL,
                           PRIMARY KEY (`pg_id`),
                           UNIQUE KEY `uk_pg_code` (`pg_code`)
) ENGINE=InnoDB COMMENT='PG 코드';

CREATE TABLE `Colors` (
                          `color_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `name` VARCHAR(50) NOT NULL,
                          `hex_code` VARCHAR(7) NOT NULL,
                          `is_active` TINYINT(1) NULL DEFAULT 1,
                          PRIMARY KEY (`color_id`),
                          UNIQUE KEY `uk_colors_hex` (`hex_code`),
                          INDEX `idx_colors_active` (`is_active`)
) ENGINE=InnoDB COMMENT='색상';

CREATE TABLE `Cart` (
                        `cart_id` INT NOT NULL AUTO_INCREMENT,
                        `user_id` BIGINT NOT NULL,
                        `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `deleted_at` DATETIME NULL,
                        PRIMARY KEY (`cart_id`),
                        INDEX `idx_cart_user` (`user_id`)
) ENGINE=InnoDB COMMENT='장바구니';

CREATE TABLE `Return` (
                          `return_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `order_id` BIGINT NOT NULL COMMENT '주문에 고객정보 있음',
                          `approve_admin_id` BIGINT NOT NULL,
                          `delivery_exception_id` BIGINT NOT NULL,
                          `return_judgement` VARCHAR(500) NULL,
                          `judgment_reason` VARCHAR(500) NULL,
                          `refund_option_code` VARCHAR(20) NULL DEFAULT 'refund',
                          `return_reason_code` INT NULL,
                          `reason_detail` VARCHAR(500) NULL,
                          `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          `requested_at` TIMESTAMP NULL,
                          `processed_at` TIMESTAMP NULL,
                          `completed_at` TIMESTAMP NULL,
                          PRIMARY KEY (`return_id`),
                          INDEX `idx_return_order` (`order_id`),
                          INDEX `idx_return_admin` (`approve_admin_id`),
                          INDEX `idx_return_exception` (`delivery_exception_id`)
) ENGINE=InnoDB COMMENT='반품';

CREATE TABLE `ORDER_HISTORY` (
                                 `history_id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `order_id` BIGINT NOT NULL,
                                 `status` VARCHAR(50) NULL,
                                 `comment` TEXT NULL,
                                 `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                 `created_by` VARCHAR(255) NULL,
                                 `id2` BIGINT NOT NULL,
                                 PRIMARY KEY (`history_id`),
                                 INDEX `idx_order_history_order` (`order_id`, `created_at`),
                                 INDEX `idx_order_history_id2` (`id2`)
) ENGINE=InnoDB COMMENT='주문 이력';

CREATE TABLE `terms` (
                         `terms_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `title` VARCHAR(200) NULL,
                         `code` VARCHAR(50) NULL,
                         `content` TEXT NULL,
                         `version` VARCHAR(20) NULL,
                         `is_required` TINYINT(1) NULL DEFAULT 0,
                         `is_active` TINYINT(1) NULL DEFAULT 1,
                         `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                         `effective_from` DATETIME NULL,
                         `effective_to` DATETIME NULL DEFAULT NULL,
                         PRIMARY KEY (`terms_id`),
                         UNIQUE KEY `uk_terms_code` (`code`),
                         INDEX `idx_terms_active` (`is_active`, `effective_from`, `effective_to`)
) ENGINE=InnoDB COMMENT='약관';

CREATE TABLE `Pay_type_code` (
                                 `pay_type_id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `pay_type` VARCHAR(50) NULL,
                                 `cash_code` VARCHAR(50) NULL,
                                 PRIMARY KEY (`pay_type_id`)
) ENGINE=InnoDB COMMENT='결제 타입 코드';

CREATE TABLE `global_code` (
                               `code` INT NOT NULL AUTO_INCREMENT,
                               `code_group_id` VARCHAR(50) NULL,
                               `code_id` INT NULL COMMENT 'J123 (Job), C121(옷 카테고리)',
                               `code_name` VARCHAR(100) NULL,
                               `description` VARCHAR(255) NULL COMMENT '설명',
                               `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                               `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (`code`),
                               INDEX `idx_global_code_group` (`code_group_id`)
) ENGINE=InnoDB COMMENT='전역 코드';

CREATE TABLE `product_variant_image` (
                                         `image_id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `variant_id` BIGINT NOT NULL,
                                         `url` VARCHAR(255) NOT NULL,
                                         `format` VARCHAR(10) NOT NULL DEFAULT 'jpg' COMMENT 'jpg, png, webp',
                                         `image_type` VARCHAR(20) NULL DEFAULT 'main' COMMENT 'main, front, back, side, zoom',
                                         `min_width` INT NULL,
                                         `max_width` INT NULL,
                                         `render_priority` INT NULL DEFAULT 0,
                                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`image_id`),
                                         INDEX `idx_variant_image_variant` (`variant_id`),
                                         INDEX `idx_variant_image_type` (`image_type`, `render_priority`)
) ENGINE=InnoDB COMMENT='상품 변형 이미지';

CREATE TABLE `PRODUCT_OPTION_PRICE` (
                                        `product_variant_id` BIGINT NOT NULL,
                                        `original_price` DECIMAL(10,2) NULL,
                                        `sale_price` DECIMAL(10,2) NULL,
                                        `discount_rate` DECIMAL(5,2) NULL,
                                        `Field2` DATETIME NULL,
                                        `Field3` DATETIME NULL,
                                        `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                        `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`product_variant_id`)
) ENGINE=InnoDB COMMENT='상품 옵션 가격';

CREATE TABLE `default_user_address` (
                                        `user_id` BIGINT NOT NULL,
                                        `road_address_1` VARCHAR(255) NULL,
                                        `road_address_2` VARCHAR(255) NULL,
                                        `jibun_address` VARCHAR(255) NULL,
                                        `detail_address` VARCHAR(255) NULL,
                                        `english_address` VARCHAR(255) NULL,
                                        `zip_code` VARCHAR(10) NULL,
                                        `address_name` VARCHAR(100) NULL,
                                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB COMMENT='기본 사용자 주소';

CREATE TABLE `ORDERS` (
                          `order_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `user_coupon_id` BIGINT NOT NULL,
                          `order_num` VARCHAR(50) NULL,
                          `total_count` INT NULL DEFAULT 0,
                          `total_price` DECIMAL(12,2) NULL,
                          `order_status` VARCHAR(20) NULL DEFAULT 'pending' COMMENT 'pending, confirmed, processing, shipped, delivered, cancelled, refunded',
                          `use_mile` DECIMAL(10,2) NULL DEFAULT 0,
                          `order_amount` DECIMAL(12,2) NULL,
                          `shipping_fee` DECIMAL(8,2) NULL DEFAULT 0,
                          `discount_amount` DECIMAL(10,2) NULL DEFAULT 0,
                          `final_amount` DECIMAL(12,2) NULL,
                          `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `id2` BIGINT NOT NULL,
                          PRIMARY KEY (`order_id`),
                          UNIQUE KEY `uk_orders_num` (`order_num`),
                          INDEX `idx_orders_user` (`user_id`),
                          INDEX `idx_orders_status` (`order_status`, `created_at`),
                          INDEX `idx_orders_coupon` (`user_coupon_id`),
                          INDEX `idx_orders_id2` (`id2`)
) ENGINE=InnoDB COMMENT='주문';

CREATE TABLE `review_comment` (
                                  `comment_id` INT NOT NULL AUTO_INCREMENT,
                                  `review_id` BIGINT NOT NULL,
                                  `user_id` BIGINT NULL,
                                  `content` TEXT NOT NULL,
                                  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
                                  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `deleted_at` DATETIME NULL,
                                  PRIMARY KEY (`comment_id`),
                                  INDEX `idx_review_comment_review` (`review_id`),
                                  INDEX `idx_review_comment_user` (`user_id`)
) ENGINE=InnoDB COMMENT='리뷰 댓글';

CREATE TABLE `review` (
                          `review_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `product_id` BIGINT NOT NULL,
                          `purchase_id` VARCHAR(255) NOT NULL,
                          `rating` TINYINT NOT NULL CHECK (`rating` >= 1 AND `rating` <= 5),
                          `title` VARCHAR(200) NULL,
                          `content` TEXT NULL,
                          `img_url` TEXT NULL,
                          `like_count` INT NOT NULL DEFAULT 0,
                          `state` VARCHAR(20) NOT NULL DEFAULT 'auto' COMMENT 'auto, pending, approved, rejected',
                          `approved_at` DATETIME NULL,
                          `is_deleted` TINYINT(1) NULL DEFAULT 0,
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `deleted_at` DATETIME NULL,
                          PRIMARY KEY (`review_id`),
                          INDEX `idx_review_user` (`user_id`),
                          INDEX `idx_review_product` (`product_id`),
                          INDEX `idx_review_state` (`state`, `created_at`),
                          INDEX `idx_review_rating` (`rating`)
) ENGINE=InnoDB COMMENT='리뷰';

CREATE TABLE `product_variant` (
                                   `product_variant_id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `product_id` BIGINT NOT NULL,
                                   `color_id` INT NOT NULL,
                                   `sku` VARCHAR(50) NOT NULL,
                                   `size` VARCHAR(50) NOT NULL,
                                   `remaining_stock` INT NOT NULL DEFAULT 0,
                                   `stock_status` VARCHAR(20) NOT NULL DEFAULT 'in_stock' COMMENT 'in_stock, out_of_stock, low_stock',
                                   `price_modifier` DECIMAL(10,2) NULL DEFAULT 0,
                                   `image_url` VARCHAR(255) NULL,
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   `extra_charge` INT NULL DEFAULT 0,
                                   PRIMARY KEY (`product_variant_id`),
                                   UNIQUE KEY `uk_product_variant_sku` (`sku`),
                                   INDEX `idx_product_variant_product` (`product_id`),
                                   INDEX `idx_product_variant_color` (`color_id`),
                                   INDEX `idx_product_variant_stock` (`stock_status`, `remaining_stock`)
) ENGINE=InnoDB COMMENT='상품 변형';

CREATE TABLE `special_section` (
                                   `special_id` VARCHAR(2) NOT NULL,
                                   `name` VARCHAR(100) NULL,
                                   `sort_order` INT NULL DEFAULT 0,
                                   `is_active` TINYINT(1) NULL DEFAULT 1,
                                   PRIMARY KEY (`special_id`),
                                   INDEX `idx_special_section_active` (`is_active`, `sort_order`)
) ENGINE=InnoDB COMMENT='특별 섹션';

CREATE TABLE `mileage_code` (
                                `reason_code` VARCHAR(255) NOT NULL,
                                `reason_name` VARCHAR(100) NULL,
                                `amount` DECIMAL(10,2) NULL,
                                `description` TEXT NULL,
                                `modify_type` VARCHAR(20) NULL COMMENT 'earn, use, expire, cancel',
                                `is_active` TINYINT(1) NULL DEFAULT 1,
                                `exp_day` INT NULL COMMENT '만료일수',
                                PRIMARY KEY (`reason_code`),
                                INDEX `idx_mileage_code_type` (`modify_type`, `is_active`)
) ENGINE=InnoDB COMMENT='마일리지 코드';

CREATE TABLE `COUPON` (
                          `coupon_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `coupon_name` VARCHAR(100) NULL,
                          `coupon_type` VARCHAR(20) NULL DEFAULT 'fixed' COMMENT 'fixed, percentage',
                          `discount_value` DECIMAL(10,2) NULL,
                          `min_order_amount` DECIMAL(10,2) NULL,
                          `max_discount_amount` DECIMAL(10,2) NULL,
                          `available_period` INT NULL,
                          `total_cnt` INT NULL,
                          `duplicate_use` CHAR(1) NULL DEFAULT 'N',
                          `coupon_status` VARCHAR(20) NULL DEFAULT 'active' COMMENT 'active, inactive, expired',
                          `description` TEXT NULL,
                          `start_date` DATETIME NULL,
                          `end_date` DATETIME NULL,
                          `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `created_by` VARCHAR(255) NULL,
                          PRIMARY KEY (`coupon_id`),
                          INDEX `idx_coupon_status` (`coupon_status`),
                          INDEX `idx_coupon_date` (`start_date`, `end_date`)
) ENGINE=InnoDB COMMENT='쿠폰';

CREATE TABLE `product` (
                           `product_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `lv3_id` VARCHAR(8) NOT NULL,
                           `title` VARCHAR(200) NOT NULL,
                           `price` DECIMAL(10,2) NOT NULL,
                           `discount_price` DECIMAL(10,2) NULL,
                           `summary` VARCHAR(500) NULL,
                           `description` TEXT NULL,
                           `thumbnail` VARCHAR(255) NULL,
                           `weight` DECIMAL(5,2) NULL,
                           `dimensions` VARCHAR(50) NULL,
                           `tags` VARCHAR(255) NULL,
                           `seasons` VARCHAR(10) NOT NULL DEFAULT '0000' COMMENT 'spring,summer,fall,winter bits',
                           `average_rating` DECIMAL(3,1) NULL DEFAULT 0,
                           `sales_count` INT NULL DEFAULT 0,
                           `view_count` INT NULL DEFAULT 0,
                           `stock_quantity` INT NULL DEFAULT 0,
                           `availability_date` DATETIME NULL,
                           `enabled` TINYINT(1) NOT NULL DEFAULT 1,
                           `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `deleted_at` DATETIME NULL,
                           `is_deleted` DATETIME NULL,
                           PRIMARY KEY (`product_id`),
                           INDEX `idx_product_category` (`lv3_id`),
                           INDEX `idx_product_enabled` (`enabled`, `created_at`),
                           INDEX `idx_product_price` (`price`),
                           FULLTEXT KEY `ft_product_search` (`title`, `summary`, `tags`)
) ENGINE=InnoDB COMMENT='상품';

CREATE TABLE `PAYMENT` (
                           `payment_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `order_id` BIGINT NOT NULL,
                           `user_id` BIGINT NULL,
                           `order_num` VARCHAR(50) NULL,
                           `paid_amount` DECIMAL(12,2) NULL,
                           `pay_type` VARCHAR(20) NULL COMMENT 'card, bank_transfer, virtual_account, mobile, kakao_pay, naver_pay, payco',
                           `cash_receipt` CHAR(1) NULL DEFAULT 'N' COMMENT 'Y, N',
                           `pg_transaction_id` VARCHAR(100) NULL,
                           `payment_key` VARCHAR(100) NULL,
                           `receipt_url` VARCHAR(500) NULL,
                           `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `approved_at` TIMESTAMP NULL,
                           `cancelled_at` TIMESTAMP NULL,
                           `pg_id` BIGINT NOT NULL,
                           `pay_status_id` BIGINT NOT NULL,
                           `pay_type_id` BIGINT NOT NULL,
                           PRIMARY KEY (`payment_id`),
                           INDEX `idx_payment_order` (`order_id`),
                           INDEX `idx_payment_user` (`user_id`),
                           INDEX `idx_payment_status` (`pay_status_id`),
                           INDEX `idx_payment_pg` (`pg_id`),
                           INDEX `idx_payment_type` (`pay_type_id`)
) ENGINE=InnoDB COMMENT='결제';

CREATE TABLE `Cart_Item` (
                             `cart_item_id` INT NOT NULL AUTO_INCREMENT,
                             `cart_id` INT NOT NULL,
                             `product_id` BIGINT NOT NULL,
                             `product_variant_id` BIGINT NULL,
                             `quantity` INT NOT NULL DEFAULT 1,
                             `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`cart_item_id`),
                             INDEX `idx_cart_item_cart` (`cart_id`),
                             INDEX `idx_cart_item_product` (`product_id`),
                             INDEX `idx_cart_item_variant` (`product_variant_id`)
) ENGINE=InnoDB COMMENT='장바구니 아이템';

CREATE TABLE `user_terms_agreement` (
                                        `agreement_id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `user_id` BIGINT NOT NULL,
                                        `terms_id` BIGINT NOT NULL,
                                        `agreed_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                        `ip_address` VARCHAR(45) NULL,
                                        PRIMARY KEY (`agreement_id`),
                                        UNIQUE KEY `uk_user_terms` (`user_id`, `terms_id`),
                                        INDEX `idx_user_terms_user` (`user_id`),
                                        INDEX `idx_user_terms_terms` (`terms_id`)
) ENGINE=InnoDB COMMENT='사용자 약관 동의';

CREATE TABLE `report` (
                          `report_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `target_type` VARCHAR(20) NOT NULL DEFAULT 'review' COMMENT 'review, comment',
                          `target_id` BIGINT NOT NULL,
                          `reason` TEXT NOT NULL,
                          `state` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT 'pending, approved, rejected',
                          `approved_at` DATETIME NULL,
                          `approved_by` BIGINT NULL,
                          `is_deleted` TINYINT(1) NULL DEFAULT 0,
                          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`report_id`),
                          INDEX `idx_report_user` (`user_id`),
                          INDEX `idx_report_target` (`target_type`, `target_id`),
                          INDEX `idx_report_state` (`state`, `created_at`)
) ENGINE=InnoDB COMMENT='신고';

CREATE TABLE `CopyOfORDER_HISTORY` (
                                       `history_id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `status` VARCHAR(50) NULL,
                                       `comment` TEXT NULL,
                                       `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                       `created_by` VARCHAR(255) NULL,
                                       `payment_id` BIGINT NOT NULL,
                                       `id2` BIGINT NOT NULL,
                                       PRIMARY KEY (`history_id`),
                                       INDEX `idx_copy_order_history_payment` (`payment_id`),
                                       INDEX `idx_copy_order_history_id2` (`id2`)
) ENGINE=InnoDB;

CREATE TABLE `Shipment` (
                            `shipment_id` VARCHAR(50) NOT NULL,
                            `order_id` BIGINT NULL,
                            `handover_id` VARCHAR(50) NOT NULL,
                            `delivery_company_id` VARCHAR(50) NULL,
                            `return_id` BIGINT NULL COMMENT '배송 예외없는 반품',
                            `shipment_type_code` VARCHAR(20) NULL DEFAULT 'delivery',
                            `shipment_option_code` VARCHAR(20) NULL DEFAULT 'standard',
                            `shipment_fee` DECIMAL(8,2) NULL DEFAULT 0,
                            `fee_bearer` VARCHAR(100) NULL,
                            `tracking_number` VARCHAR(100) NULL,
                            `receiver_name` VARCHAR(100) NULL,
                            `receiver_phone` VARCHAR(20) NULL,
                            `receiver_address` TEXT NULL,
                            `road_name_address1` VARCHAR(100) NULL,
                            `road_name_address2` VARCHAR(100) NULL,
                            `receiver_postal_code` VARCHAR(10) NULL,
                            `street_number` VARCHAR(10) NULL,
                            `english_address` VARCHAR(500) NULL,
                            `receive_method` VARCHAR(20) NULL DEFAULT 'direct',
                            `delivery_memo` TEXT NULL,
                            `shipped_at` TIMESTAMP NULL,
                            `delivered_at` TIMESTAMP NULL,
                            `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`shipment_id`),
                            INDEX `idx_shipment_order` (`order_id`),
                            INDEX `idx_shipment_handover` (`handover_id`),
                            INDEX `idx_shipment_company` (`delivery_company_id`),
                            INDEX `idx_shipment_return` (`return_id`),
                            INDEX `idx_shipment_tracking` (`tracking_number`)
) ENGINE=InnoDB COMMENT='배송';

CREATE TABLE `handover` (
                            `handover_id` VARCHAR(50) NOT NULL,
                            `scheduled_at` DATETIME NULL,
                            `completed_at` DATETIME NULL,
                            `notes` TEXT NULL,
                            `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                            `Field` VARCHAR(255) NULL,
                            `Field3` VARCHAR(20) NULL,
                            `Field2` VARCHAR(255) NULL,
                            `Field4` VARCHAR(20) NULL,
                            PRIMARY KEY (`handover_id`)
) ENGINE=InnoDB COMMENT='인수인계';

CREATE TABLE `ADMIN_DETAIL` (
                                `user_id` BIGINT NOT NULL,
                                `nickname` VARCHAR(255) NULL,
                                PRIMARY KEY (`user_id`)
) ENGINE=InnoDB COMMENT='관리자 상세';

CREATE TABLE `review_like` (
                               `like_id` BIGINT NOT NULL AUTO_INCREMENT,
                               `review_id` BIGINT NOT NULL,
                               `user_id` BIGINT NOT NULL,
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`like_id`),
                               UNIQUE KEY `uk_review_like` (`review_id`, `user_id`),
                               INDEX `idx_review_like_review` (`review_id`),
                               INDEX `idx_review_like_user` (`user_id`)
) ENGINE=InnoDB COMMENT='리뷰 좋아요';

CREATE TABLE `Delivery_Company_API` (
                                        `delivery_company_id` VARCHAR(50) NOT NULL,
                                        `company_name` VARCHAR(100) NULL,
                                        `api_url` VARCHAR(255) NULL,
                                        `api_key` VARCHAR(255) NULL,
                                        `company_code` VARCHAR(20) NULL,
                                        `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                        `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`delivery_company_id`),
                                        INDEX `idx_delivery_company_code` (`company_code`)
) ENGINE=InnoDB COMMENT='배송업체 API';

CREATE TABLE `product_description_image` (
                                             `image_id` BIGINT NOT NULL AUTO_INCREMENT,
                                             `product_id` BIGINT NOT NULL,
                                             `show_device` VARCHAR(100) NULL,
                                             `url` VARCHAR(255) NOT NULL,
                                             `format` VARCHAR(10) NOT NULL DEFAULT 'jpg' COMMENT 'jpg, png, webp',
                                             `render_priority` INT NULL DEFAULT 0,
                                             `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             PRIMARY KEY (`image_id`),
                                             INDEX `idx_product_desc_image_product` (`product_id`),
                                             INDEX `idx_product_desc_image_priority` (`render_priority`)
) ENGINE=InnoDB COMMENT='상품 설명 이미지';

CREATE TABLE `mileage_history` (
                                   `history_id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `user_id` BIGINT NOT NULL,
                                   `mileage_id` BIGINT NOT NULL,
                                   `reason_code` VARCHAR(255) NOT NULL,
                                   `related_order_id` BIGINT NULL,
                                   `review_id` BIGINT NULL,
                                   `balance_after` DECIMAL(10,2) NULL,
                                   `expire_date` DATETIME NULL,
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `Field` VARCHAR(100) NULL,
                                   PRIMARY KEY (`history_id`),
                                   INDEX `idx_mileage_history_user` (`user_id`, `created_at`),
                                   INDEX `idx_mileage_history_mileage` (`mileage_id`),
                                   INDEX `idx_mileage_history_reason` (`reason_code`),
                                   INDEX `idx_mileage_history_order` (`related_order_id`),
                                   INDEX `idx_mileage_history_review` (`review_id`)
) ENGINE=InnoDB COMMENT='마일리지 이력';

CREATE TABLE `WISH_LIST` (
                             `wish_id` BIGINT NOT NULL AUTO_INCREMENT,
                             `user_id` BIGINT NOT NULL,
                             `product_id` BIGINT NOT NULL,
                             `added_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`wish_id`),
                             UNIQUE KEY `uk_wish_list` (`user_id`, `product_id`),
                             INDEX `idx_wish_list_user` (`user_id`),
                             INDEX `idx_wish_list_product` (`product_id`)
) ENGINE=InnoDB COMMENT='위시리스트';

CREATE TABLE `Shipment_related_code` (
                                         `code_group_num` TINYINT NULL,
                                         `code_id` TINYINT NULL,
                                         `code_name` VARCHAR(100) NULL COMMENT 'ex. pending',
                                         `description` VARCHAR(500) NULL,
                                         `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                         `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         `shipment_id` VARCHAR(50) NOT NULL,
                                         INDEX `idx_shipment_code_shipment` (`shipment_id`),
                                         INDEX `idx_shipment_code_group` (`code_group_num`, `code_id`)
) ENGINE=InnoDB COMMENT='배송 관련 코드';

CREATE TABLE `LOGIN_HISTORY` (
                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `user_id` BIGINT NOT NULL,
                                 `ip` VARCHAR(45) NULL,
                                 `browser` VARCHAR(255) NULL,
                                 `nation` VARCHAR(100) NULL,
                                 `region` VARCHAR(100) NULL,
                                 `attempt_result` TINYINT(1) NULL,
                                 `consecutive_failed_login_attempt` INT NULL DEFAULT 0,
                                 `is_locked` TINYINT(1) NULL DEFAULT 0,
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_login_history_user` (`user_id`, `created_at`),
                                 INDEX `idx_login_history_ip` (`ip`),
                                 INDEX `idx_login_history_result` (`attempt_result`, `created_at`)
) ENGINE=InnoDB COMMENT='로그인 이력';

CREATE TABLE `lv2` (
                       `lv2_id` VARCHAR(8) NOT NULL,
                       `lv1_id` VARCHAR(8) NOT NULL,
                       `name` VARCHAR(100) NULL,
                       `sort_order` INT NULL DEFAULT 0,
                       `is_active` CHAR(1) NULL DEFAULT 'Y',
                       PRIMARY KEY (`lv2_id`),
                       INDEX `idx_lv2_lv1` (`lv1_id`),
                       INDEX `idx_lv2_active` (`is_active`, `sort_order`)
) ENGINE=InnoDB COMMENT='2단계 카테고리';

CREATE TABLE `mileage` (
                           `mileage_id` BIGINT NOT NULL AUTO_INCREMENT,
                           `user_id` BIGINT NOT NULL,
                           `total_earned` DECIMAL(10,2) NULL DEFAULT 0,
                           `total_used` DECIMAL(10,2) NULL DEFAULT 0,
                           `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`mileage_id`),
                           UNIQUE KEY `uk_mileage_user` (`user_id`)
) ENGINE=InnoDB COMMENT='마일리지';

CREATE TABLE `lv3` (
                       `lv3_id` VARCHAR(8) NOT NULL,
                       `lv2_id` VARCHAR(8) NOT NULL,
                       `name` VARCHAR(100) NULL,
                       `sort_order` INT NULL DEFAULT 0,
                       `is_active` CHAR(1) NULL DEFAULT 'Y',
                       PRIMARY KEY (`lv3_id`),
                       INDEX `idx_lv3_lv2` (`lv2_id`),
                       INDEX `idx_lv3_active` (`is_active`, `sort_order`)
) ENGINE=InnoDB COMMENT='3단계 카테고리';

CREATE TABLE `CopyOfspecial_section` (
                                         `special_id` VARCHAR(2) NOT NULL,
                                         `product_id` BIGINT NOT NULL,
                                         `special_id2` VARCHAR(2) NOT NULL,
                                         PRIMARY KEY (`special_id`),
                                         INDEX `idx_copy_special_product` (`product_id`),
                                         INDEX `idx_copy_special_id2` (`special_id2`)
) ENGINE=InnoDB;

CREATE TABLE `lv1` (
                       `lv1_id` VARCHAR(8) NOT NULL,
                       `name` VARCHAR(100) NULL,
                       `sort_order` INT NULL DEFAULT 0,
                       `is_active` CHAR(1) NULL DEFAULT 'Y',
                       PRIMARY KEY (`lv1_id`),
                       INDEX `idx_lv1_active` (`is_active`, `sort_order`)
) ENGINE=InnoDB COMMENT='1단계 카테고리';

CREATE TABLE `notice` (
                          `notice_id` INT NOT NULL AUTO_INCREMENT,
                          `title` VARCHAR(200) NOT NULL,
                          `content` TEXT NOT NULL,
                          `category` VARCHAR(20) NULL DEFAULT 'general' COMMENT 'general, event, system, delivery',
                          `is_pinned` TINYINT(1) NULL DEFAULT 0,
                          `is_active` TINYINT(1) NULL DEFAULT 1,
                          `view_count` INT NULL DEFAULT 0,
                          `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `deleted_at` DATETIME NULL,
                          `created_by` BIGINT NULL,
                          PRIMARY KEY (`notice_id`),
                          INDEX `idx_notice_category` (`category`, `is_active`),
                          INDEX `idx_notice_pinned` (`is_pinned`, `created_at`),
                          INDEX `idx_notice_created_by` (`created_by`)
) ENGINE=InnoDB COMMENT='공지사항';

CREATE TABLE `Exchange` (
                            `exchange_id` BIGINT NOT NULL AUTO_INCREMENT,
                            `return_id` BIGINT NOT NULL,
                            `original_order_id` BIGINT NOT NULL,
                            `exchange_order_id` BIGINT NULL,
                            `admin_memo` VARCHAR(500) NULL,
                            `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `approved_at` TIMESTAMP NULL,
                            `completed_at` TIMESTAMP NULL,
                            `Field` VARCHAR(255) NULL,
                            PRIMARY KEY (`exchange_id`),
                            INDEX `idx_exchange_return` (`return_id`),
                            INDEX `idx_exchange_original_order` (`original_order_id`),
                            INDEX `idx_exchange_exchange_order` (`exchange_order_id`)
) ENGINE=InnoDB COMMENT='교환';

CREATE TABLE `DELIVERY_EXCEPTION` (
                                      `delivery_exception_id` BIGINT NOT NULL AUTO_INCREMENT,
                                      `shipment_id` VARCHAR(50) NOT NULL,
                                      `exception_type_code` INT NOT NULL,
                                      `occurred_at` DATETIME NULL,
                                      `customer_claim_desc` TEXT NULL,
                                      `evidence_url` VARCHAR(500) NULL,
                                      `courier_claim_desc` TEXT NULL,
                                      `courier_evidence_url` VARCHAR(500) NULL,
                                      `customer_compensation` VARCHAR(200) NULL,
                                      `internal_memo` TEXT NULL,
                                      `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `resolved_at` DATETIME NULL,
                                      `resolved_by` VARCHAR(255) NULL,
                                      PRIMARY KEY (`delivery_exception_id`),
                                      INDEX `idx_delivery_exception_shipment` (`shipment_id`),
                                      INDEX `idx_delivery_exception_type` (`exception_type_code`),
                                      INDEX `idx_delivery_exception_occurred` (`occurred_at`)
) ENGINE=InnoDB COMMENT='배송 예외';

CREATE TABLE `refund_account` (
                                  `user_id` BIGINT NOT NULL,
                                  `account_name` VARCHAR(255) NULL,
                                  `bank_name` VARCHAR(255) NULL,
                                  `account_num` VARCHAR(255) NULL,
                                  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB COMMENT='환불 계좌';

-- 외래키 제약조건
ALTER TABLE `Refund` ADD CONSTRAINT `FK_Return_TO_Refund_1` FOREIGN KEY (`return_id`) REFERENCES `Return` (`return_id`);
ALTER TABLE `Refund` ADD CONSTRAINT `FK_PAYMENT_TO_Refund_1` FOREIGN KEY (`payment_id`) REFERENCES `PAYMENT` (`payment_id`);
ALTER TABLE `ORDER_ITEM` ADD CONSTRAINT `FK_ORDERS_TO_ORDER_ITEM_1` FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `ORDER_ITEM` ADD CONSTRAINT `FK_PRODUCT_SNAPSHOT_TO_ORDER_ITEM_1` FOREIGN KEY (`product_snapshot_id`) REFERENCES `PRODUCT_SNAPSHOT` (`product_snapshot_id`);
ALTER TABLE `Inquiry` ADD CONSTRAINT `FK_USERS_TO_Inquiry_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `Inquiry` ADD CONSTRAINT `FK_product_TO_Inquiry_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `product_varient_detail` ADD CONSTRAINT `FK_product_variant_TO_product_varient_detail_1` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`product_variant_id`);
ALTER TABLE `product_varient_detail` ADD CONSTRAINT `FK_model_TO_product_varient_detail_1` FOREIGN KEY (`model_id`) REFERENCES `model` (`model_id`);
ALTER TABLE `USER_DETAIL` ADD CONSTRAINT `FK_USERS_TO_USER_DETAIL_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `USER_DETAIL` ADD CONSTRAINT `FK_global_code_TO_USER_DETAIL_1` FOREIGN KEY (`job_code`) REFERENCES `global_code` (`code`);
ALTER TABLE `USER_COUPON` ADD CONSTRAINT `FK_USERS_TO_USER_COUPON_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `USER_COUPON` ADD CONSTRAINT `FK_COUPON_TO_USER_COUPON_1` FOREIGN KEY (`coupon_id`) REFERENCES `COUPON` (`coupon_id`);
ALTER TABLE `inquiry_answer` ADD CONSTRAINT `FK_Inquiry_TO_inquiry_answer_1` FOREIGN KEY (`inquiry_id`) REFERENCES `Inquiry` (`inquiry_id`);
ALTER TABLE `inquiry_answer` ADD CONSTRAINT `FK_ADMIN_DETAIL_TO_inquiry_answer_1` FOREIGN KEY (`admin_id`) REFERENCES `ADMIN_DETAIL` (`user_id`);
ALTER TABLE `PRODUCT_SNAPSHOT` ADD CONSTRAINT `FK_product_TO_PRODUCT_SNAPSHOT_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `Cart` ADD CONSTRAINT `FK_USERS_TO_Cart_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `Return` ADD CONSTRAINT `FK_ORDERS_TO_Return_1` FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `Return` ADD CONSTRAINT `FK_ADMIN_DETAIL_TO_Return_1` FOREIGN KEY (`approve_admin_id`) REFERENCES `ADMIN_DETAIL` (`user_id`);
ALTER TABLE `Return` ADD CONSTRAINT `FK_DELIVERY_EXCEPTION_TO_Return_1` FOREIGN KEY (`delivery_exception_id`) REFERENCES `DELIVERY_EXCEPTION` (`delivery_exception_id`);
ALTER TABLE `ORDER_HISTORY` ADD CONSTRAINT `FK_ORDERS_TO_ORDER_HISTORY_1` FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `ORDER_HISTORY` ADD CONSTRAINT `FK_Order_state_code_TO_ORDER_HISTORY_1` FOREIGN KEY (`id2`) REFERENCES `Order_state_code` (`id`);
ALTER TABLE `product_variant_image` ADD CONSTRAINT `FK_product_variant_TO_product_variant_image_1` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`product_variant_id`);
ALTER TABLE `PRODUCT_OPTION_PRICE` ADD CONSTRAINT `FK_product_variant_TO_PRODUCT_OPTION_PRICE_1` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`product_variant_id`);
ALTER TABLE `default_user_address` ADD CONSTRAINT `FK_USER_DETAIL_TO_default_user_address_1` FOREIGN KEY (`user_id`) REFERENCES `USER_DETAIL` (`user_id`);
ALTER TABLE `ORDERS` ADD CONSTRAINT `FK_USERS_TO_ORDERS_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `ORDERS` ADD CONSTRAINT `FK_USER_COUPON_TO_ORDERS_1` FOREIGN KEY (`user_coupon_id`) REFERENCES `USER_COUPON` (`user_coupon_id`);
ALTER TABLE `ORDERS` ADD CONSTRAINT `FK_Order_state_code_TO_ORDERS_1` FOREIGN KEY (`id2`) REFERENCES `Order_state_code` (`id`);
ALTER TABLE `review_comment` ADD CONSTRAINT `FK_review_TO_review_comment_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`);
ALTER TABLE `review_comment` ADD CONSTRAINT `FK_USERS_TO_review_comment_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `review` ADD CONSTRAINT `FK_USERS_TO_review_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `review` ADD CONSTRAINT `FK_product_TO_review_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `product_variant` ADD CONSTRAINT `FK_product_TO_product_variant_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `product_variant` MODIFY COLUMN `color_id` BIGINT NOT NULL;
ALTER TABLE `product_variant` ADD CONSTRAINT `FK_Colors_TO_product_variant_1` FOREIGN KEY (`color_id`) REFERENCES `Colors` (`color_id`);
ALTER TABLE `product` ADD CONSTRAINT `FK_lv3_TO_product_1` FOREIGN KEY (`lv3_id`) REFERENCES `lv3` (`lv3_id`);
ALTER TABLE `PAYMENT` ADD CONSTRAINT `FK_ORDERS_TO_PAYMENT_1` FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `PAYMENT` ADD CONSTRAINT `FK_USERS_TO_PAYMENT_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `PAYMENT` ADD CONSTRAINT `FK_pg_code_TO_PAYMENT_1` FOREIGN KEY (`pg_id`) REFERENCES `pg_code` (`pg_id`);
ALTER TABLE `PAYMENT` ADD CONSTRAINT `FK_pay_status_code_TO_PAYMENT_1` FOREIGN KEY (`pay_status_id`) REFERENCES `pay_status_code` (`pay_status_id`);
ALTER TABLE `PAYMENT` ADD CONSTRAINT `FK_Pay_type_code_TO_PAYMENT_1` FOREIGN KEY (`pay_type_id`) REFERENCES `Pay_type_code` (`pay_type_id`);
ALTER TABLE `Cart_Item` ADD CONSTRAINT `FK_Cart_TO_Cart_Item_1` FOREIGN KEY (`cart_id`) REFERENCES `Cart` (`cart_id`);
ALTER TABLE `Cart_Item` ADD CONSTRAINT `FK_product_TO_Cart_Item_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `Cart_Item` ADD CONSTRAINT `FK_product_variant_TO_Cart_Item_1` FOREIGN KEY (`product_variant_id`) REFERENCES `product_variant` (`product_variant_id`);
ALTER TABLE `user_terms_agreement` ADD CONSTRAINT `FK_USERS_TO_user_terms_agreement_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `user_terms_agreement` ADD CONSTRAINT `FK_terms_TO_user_terms_agreement_1` FOREIGN KEY (`terms_id`) REFERENCES `terms` (`terms_id`);
ALTER TABLE `report` ADD CONSTRAINT `FK_USERS_TO_report_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `CopyOfORDER_HISTORY` ADD CONSTRAINT `FK_PAYMENT_TO_CopyOfORDER_HISTORY_1` FOREIGN KEY (`payment_id`) REFERENCES `PAYMENT` (`payment_id`);
ALTER TABLE `CopyOfORDER_HISTORY` ADD CONSTRAINT `FK_pay_status_code_TO_CopyOfORDER_HISTORY_1` FOREIGN KEY (`id2`) REFERENCES `pay_status_code` (`pay_status_id`);
ALTER TABLE `Shipment` ADD CONSTRAINT `FK_ORDERS_TO_Shipment_1` FOREIGN KEY (`order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `Shipment` ADD CONSTRAINT `FK_handover_TO_Shipment_1` FOREIGN KEY (`handover_id`) REFERENCES `handover` (`handover_id`);
ALTER TABLE `Shipment` ADD CONSTRAINT `FK_Delivery_Company_API_TO_Shipment_1` FOREIGN KEY (`delivery_company_id`) REFERENCES `Delivery_Company_API` (`delivery_company_id`);
ALTER TABLE `Shipment` ADD CONSTRAINT `FK_Return_TO_Shipment_1` FOREIGN KEY (`return_id`) REFERENCES `Return` (`return_id`);
ALTER TABLE `ADMIN_DETAIL` ADD CONSTRAINT `FK_USERS_TO_ADMIN_DETAIL_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `review_like` ADD CONSTRAINT `FK_review_TO_review_like_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`);
ALTER TABLE `review_like` ADD CONSTRAINT `FK_USERS_TO_review_like_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `product_description_image` ADD CONSTRAINT `FK_product_TO_product_description_image_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `mileage_history` ADD CONSTRAINT `FK_USERS_TO_mileage_history_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `mileage_history` ADD CONSTRAINT `FK_mileage_TO_mileage_history_1` FOREIGN KEY (`mileage_id`) REFERENCES `mileage` (`mileage_id`);
ALTER TABLE `mileage_history` ADD CONSTRAINT `FK_mileage_code_TO_mileage_history_1` FOREIGN KEY (`reason_code`) REFERENCES `mileage_code` (`reason_code`);
ALTER TABLE `mileage_history` ADD CONSTRAINT `FK_ORDERS_TO_mileage_history_1` FOREIGN KEY (`related_order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `mileage_history` ADD CONSTRAINT `FK_review_TO_mileage_history_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`);
ALTER TABLE `WISH_LIST` ADD CONSTRAINT `FK_USERS_TO_WISH_LIST_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `WISH_LIST` ADD CONSTRAINT `FK_product_TO_WISH_LIST_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `Shipment_related_code` ADD CONSTRAINT `FK_Shipment_TO_Shipment_related_code_1` FOREIGN KEY (`shipment_id`) REFERENCES `Shipment` (`shipment_id`);
ALTER TABLE `LOGIN_HISTORY` ADD CONSTRAINT `FK_USERS_TO_LOGIN_HISTORY_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `lv2` ADD CONSTRAINT `FK_lv1_TO_lv2_1` FOREIGN KEY (`lv1_id`) REFERENCES `lv1` (`lv1_id`);
ALTER TABLE `mileage` ADD CONSTRAINT `FK_USERS_TO_mileage_1` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`user_id`);
ALTER TABLE `lv3` ADD CONSTRAINT `FK_lv2_TO_lv3_1` FOREIGN KEY (`lv2_id`) REFERENCES `lv2` (`lv2_id`);
ALTER TABLE `CopyOfspecial_section` ADD CONSTRAINT `FK_product_TO_CopyOfspecial_section_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
ALTER TABLE `CopyOfspecial_section` ADD CONSTRAINT `FK_special_section_TO_CopyOfspecial_section_1` FOREIGN KEY (`special_id2`) REFERENCES `special_section` (`special_id`);
ALTER TABLE `notice` ADD CONSTRAINT `FK_ADMIN_DETAIL_TO_notice_1` FOREIGN KEY (`created_by`) REFERENCES `ADMIN_DETAIL` (`user_id`);
ALTER TABLE `Exchange` ADD CONSTRAINT `FK_Return_TO_Exchange_1` FOREIGN KEY (`return_id`) REFERENCES `Return` (`return_id`);
ALTER TABLE `Exchange` ADD CONSTRAINT `FK_ORDERS_TO_Exchange_1` FOREIGN KEY (`original_order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `Exchange` ADD CONSTRAINT `FK_ORDERS_TO_Exchange_2` FOREIGN KEY (`exchange_order_id`) REFERENCES `ORDERS` (`order_id`);
ALTER TABLE `DELIVERY_EXCEPTION` ADD CONSTRAINT `FK_Shipment_TO_DELIVERY_EXCEPTION_1` FOREIGN KEY (`shipment_id`) REFERENCES `Shipment` (`shipment_id`);
ALTER TABLE `refund_account` ADD CONSTRAINT `FK_USER_DETAIL_TO_refund_account_1` FOREIGN KEY (`user_id`) REFERENCES `USER_DETAIL` (`user_id`);