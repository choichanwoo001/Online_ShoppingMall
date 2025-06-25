CREATE TABLE `USERS` (
                         `USER_ID` BIGINT NOT NULL AUTO_INCREMENT,
                         `LOGIN_ID` VARCHAR(30) NOT NULL UNIQUE,
                         `PASSWORD` VARCHAR(255) NOT NULL,
                         `IS_ACTIVATE` TINYINT(1) NULL DEFAULT 1,
                         `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                         `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `DELETED_AT` DATETIME NULL,
                         `ROLE` VARCHAR(20) NULL DEFAULT 'USER' COMMENT 'ADMIN, USER',
                         `IS_DELETED` TINYINT(1) NULL DEFAULT 0,
                         PRIMARY KEY (`USER_ID`),
                         UNIQUE KEY `UK_USERS_LOGIN_ID` (`LOGIN_ID`),
                         INDEX `IDX_USERS_ROLE` (`ROLE`),
                         INDEX `IDX_USERS_ACTIVE` (`IS_ACTIVATE`, `IS_DELETED`)
) ENGINE=INNODB COMMENT='사용자';

CREATE TABLE `USER_DETAIL` (
                               `USER_ID` BIGINT NOT NULL,
                               `EMAIL` VARCHAR(255) NOT NULL,
                               `NAME` VARCHAR(255) NOT NULL,
                               `PHONE_NUMBER` VARCHAR(20) NOT NULL,
                               `BIRTH_DATE` DATE NULL,
                               `GENDER` CHAR(1) NULL COMMENT 'M, F',
                               `JOB_CODE` INT NOT NULL,
                               PRIMARY KEY (`USER_ID`),
                               UNIQUE KEY `UK_USER_EMAIL` (`EMAIL`),
                               INDEX `IDX_USER_DETAIL_JOB` (`JOB_CODE`)
) ENGINE=INNODB COMMENT='사용자 상세';

CREATE TABLE `ADMIN_DETAIL` (
                                `USER_ID` BIGINT NOT NULL,
                                `NICKNAME` VARCHAR(255) NULL,
                                `NAME` VARCHAR(255) NOT NULL,
                                `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `DELETED_AT` DATETIME NULL,
                                PRIMARY KEY (`USER_ID`)
) ENGINE=INNODB COMMENT='관리자 상세';

CREATE TABLE `CATEGORY_LV1` (
                                `CATEGORY_LV1_ID` VARCHAR(8) NOT NULL,
                                `NAME` VARCHAR(100) NULL,
                                `SORT_ORDER` INT NULL DEFAULT 0,
                                `IS_ACTIVE` CHAR(1) NULL DEFAULT 'Y',
                                PRIMARY KEY (`CATEGORY_LV1_ID`),
                                INDEX `IDX_LV1_ACTIVE` (`IS_ACTIVE`, `SORT_ORDER`)
) ENGINE=INNODB COMMENT='카테고리 대';

CREATE TABLE `CATEGORY_LV2` (
                                `CATEGORY_LV2_ID` VARCHAR(8) NOT NULL,
                                `CATEGORY_LV1_ID` VARCHAR(8) NOT NULL,
                                `NAME` VARCHAR(100) NULL,
                                `SORT_ORDER` INT NULL DEFAULT 0,
                                `IS_ACTIVE` CHAR(1) NULL DEFAULT 'Y',
                                PRIMARY KEY (`CATEGORY_LV2_ID`),
                                INDEX `IDX_LV2_LV1` (`CATEGORY_LV1_ID`),
                                INDEX `IDX_LV2_ACTIVE` (`IS_ACTIVE`, `SORT_ORDER`)
) ENGINE=INNODB COMMENT='카테고리 중';

CREATE TABLE `CATEGORY_LV3` (
                                `CATEGORY_LV3_ID` VARCHAR(8) NOT NULL,
                                `CATEGORY_LV2_ID` VARCHAR(8) NOT NULL,
                                `NAME` VARCHAR(100) NULL,
                                `SORT_ORDER` INT NULL DEFAULT 0,
                                `IS_ACTIVE` CHAR(1) NULL DEFAULT 'Y',
                                PRIMARY KEY (`CATEGORY_LV3_ID`),
                                INDEX `IDX_LV3_LV2` (`CATEGORY_LV2_ID`),
                                INDEX `IDX_LV3_ACTIVE` (`IS_ACTIVE`, `SORT_ORDER`)
) ENGINE=INNODB COMMENT='카테고리 대';


CREATE TABLE `PRODUCT` (
                           `PRODUCT_ID` BIGINT NOT NULL AUTO_INCREMENT,
                           `CATEGORY_LV3_ID` VARCHAR(8) NOT NULL,
                           `PRODUCT_TITLE` VARCHAR(200) NOT NULL,
                           `PRODUCT_PRICE` DECIMAL(10,2) NOT NULL,
                           `PRODUCT_SUMMARY` VARCHAR(500) NULL,
                           `PRODUCT_THUMBNAIL` VARCHAR(255) NULL,
                           `PRODUCT_TAG` VARCHAR(255) NULL,
                           `PRODUCT_SEASON` VARCHAR(10) NOT NULL DEFAULT '0000' COMMENT 'SPRING,SUMMER,FALL,WINTER BITS',
                           `PRODUCT_AVERAGE_RATING` DECIMAL(3,1) NULL DEFAULT 0,
                           `PRODUCT_SALE_COUNT` INT NULL DEFAULT 0,
                           `PRODUCT_VIEW_COUNT` INT NULL DEFAULT 0,
                           `PRODUCT_STOCK` INT NULL DEFAULT 0,
                           `PRODUCT_AVAILABLE_DATE` DATETIME NULL,
                           `IS_ENABLED` TINYINT(1) NOT NULL DEFAULT 1,
                           `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `DELETED_AT` DATETIME NULL,
                           `IS_DELETED` DATETIME NULL,
                           PRIMARY KEY (`PRODUCT_ID`),
                           INDEX `IDX_PRODUCT_CATEGORY` (`CATEGORY_LV3_ID`),
                           INDEX `IDX_PRODUCT_ENABLED` (`IS_ENABLED`, `CREATED_AT`),
                           INDEX `IDX_PRODUCT_PRICE` (`PRODUCT_PRICE`),
                           FULLTEXT KEY `FT_PRODUCT_SEARCH` (`PRODUCT_TITLE`, `PRODUCT_SUMMARY`, `PRODUCT_TAG`)
) ENGINE=INNODB COMMENT='상품';

CREATE TABLE `PRODUCT_VARIANT` (
                                   `PRODUCT_VARIANT_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                   `PRODUCT_VARIANT_SIZE_ID` BIGINT,
                                   `PRODUCT_ID` BIGINT NOT NULL,
                                   `PRODUCT_VARIANT_COLOR_ID` INT NOT NULL,
                                   `PRODUCT_VARIANT_STOCK_ID` BIGINT NOT NULL,
                                   `PRODUCT_VARIANT_WEIGHT` VARCHAR(255) NULL,
                                   `PRODUCT_VARIANT_MEASUREMENT` VARCHAR(255) NULL,
                                   `PRODUCT_VARIANT_EXTRA_CHARGE` DECIMAL(10, 2) NULL DEFAULT 0,
                                   `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (`PRODUCT_VARIANT_ID`),
                                   INDEX `IDX_PRODUCT_VARIANT_PRODUCT` (`PRODUCT_ID`),
                                   INDEX `IDX_PRODUCT_VARIANT_COLOR` (`PRODUCT_VARIANT_COLOR_ID`)
) ENGINE=INNODB COMMENT='상품 변형';

CREATE TABLE `PRODUCT_VARIANT_COLORS` (
                                          `PRODUCT_VARIANT_COLOR_ID` INT NOT NULL AUTO_INCREMENT,
                                          `PRODUCT_VARIANT_COLOR_NAME` VARCHAR(50) NOT NULL,
                                          `HEX_CODE` VARCHAR(7) NOT NULL,
                                          `IS_ACTIVE` TINYINT(1) NOT NULL DEFAULT 1,
                                          PRIMARY KEY (`PRODUCT_VARIANT_COLOR_ID`),
                                          UNIQUE KEY `UK_COLORS_HEX` (`HEX_CODE`),
                                          INDEX `IDX_COLORS_ACTIVE` (`IS_ACTIVE`)
) ENGINE=INNODB COMMENT='색상';

CREATE TABLE `PRODUCT_VARIANT_SIZE` (
                                        `PRODUCT_VARIANT_SIZE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                        `PRODUCT_VARIANT_SIZE_NAME` VARCHAR(20),
                                        `IS_ACTIVE` TINYINT(1) NOT NULL DEFAULT 1,
                                        PRIMARY KEY (`PRODUCT_VARIANT_SIZE_ID`)
) ENGINE=INNODB COMMENT='상품 색상';


CREATE TABLE `PRODUCT_STOCK` (
                                 `PRODUCT_VARIANT_STOCK_ID` BIGINT NOT NULL,
                                 `PRODUCT_STOCK` BIGINT NOT NULL DEFAULT 0,
                                 `PRODUCT_STOCK_STATUS` VARCHAR(20) NOT NULL DEFAULT 'OUT_OF_STOCK',
                                 PRIMARY KEY (`PRODUCT_VARIANT_STOCK_ID`)
) ENGINE=INNODB COMMENT='상품 재고';


CREATE TABLE `PRODUCT_VARIANT_DESCRIPTION_IMAGE` (
                                                     `PRODUCT_VARIANT_DESCRIPTION_IMAGE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                                     `PRODUCT_VARIANT_ID` BIGINT NOT NULL,
                                                     `PRODUCT_VARIANT_DESCRIPTION_IMAGE_URL` VARCHAR(255) NOT NULL,
                                                     `SHOW_DEVICE` VARCHAR(100) NOT NULL DEFAULT 'PC' COMMENT 'PC, TABLET, MOBILE',
                                                     `FORMAT` VARCHAR(10) NOT NULL DEFAULT 'JPG' COMMENT 'JPG, PNG, WEBP',
                                                     `RENDER_PRIORITY` INT NULL DEFAULT 0,
                                                     `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     PRIMARY KEY (`PRODUCT_VARIANT_DESCRIPTION_IMAGE_ID`),
                                                     INDEX `IDX_VARIANT_IMAGE_VARIANT` (`PRODUCT_VARIANT_ID`)
) ENGINE=INNODB COMMENT='상품 변형 이미지';

CREATE TABLE `PRODUCT_VARIANT_IMAGE` (
                                         `PRODUCT_VARIANT_IMAGE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                         `PRODUCT_VARIANT_ID` BIGINT NOT NULL,
                                         `SHOW_DEVICE` VARCHAR(100) NULL,
                                         `PRODUCT_VARIANT_IMAGE_URL` VARCHAR(255) NOT NULL,
                                         `FORMAT` VARCHAR(10) NOT NULL DEFAULT 'JPG' COMMENT 'JPG, PNG, WEBP',
                                         `RENDER_PRIORITY` INT NULL DEFAULT 0,
                                         `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`PRODUCT_VARIANT_IMAGE_ID`),
                                         INDEX `IDX_PRODUCT_DESC_IMAGE_PRODUCT` (`PRODUCT_VARIANT_ID`),
                                         INDEX `IDX_PRODUCT_DESC_IMAGE_PRIORITY` (`RENDER_PRIORITY`)
) ENGINE=INNODB COMMENT='상품 설명 이미지';

CREATE TABLE `PRODUCT_OPTION_PRICE` (
                                        `PRODUCT_OPTION_PRICE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                        `PRODUCT_VARIANT_ID` BIGINT NOT NULL,
                                        `ORIGINAL_PRICE` DECIMAL(10,2) NULL,
                                        `SALE_PRICE` DECIMAL(10,2) NULL,
                                        `DISCOUNT_RATE` DECIMAL(5,2) NULL,
                                        `DISCOUNT_START_DATE` DATETIME NOT NULL,
                                        `DISCOUNT_END_DATE` DATETIME NULL,
                                        `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                        `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`PRODUCT_OPTION_PRICE_ID`)
) ENGINE=INNODB COMMENT='상품 옵션 가격';



CREATE TABLE `INQUIRY` (
                           `INQUIRY_ID` BIGINT NOT NULL AUTO_INCREMENT,
                           `USER_ID` BIGINT NOT NULL,
                           `PRODUCT_ID` BIGINT NULL,
                           `TITLE` VARCHAR(200) NULL,
                           `CONTENT` TEXT NULL,
                           `INQUIRY_CATEGORY` VARCHAR(20) NULL DEFAULT 'PRODUCT' COMMENT 'PRODUCT, DELIVERY, PAYMENT, REFUND, ETC',
                           `STATUS` VARCHAR(20) NULL DEFAULT 'PENDING' COMMENT 'PENDING, ANSWERED, CLOSED',
                           `IS_SECRET` TINYINT(1) NULL DEFAULT 0,
                           `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                           `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `DELETED_AT` DATETIME NULL,
                           PRIMARY KEY (`INQUIRY_ID`),
                           INDEX `IDX_INQUIRY_USER` (`USER_ID`),
                           INDEX `IDX_INQUIRY_PRODUCT` (`PRODUCT_ID`),
                           INDEX `IDX_INQUIRY_STATUS` (`STATUS`, `CREATED_AT`)
) ENGINE=INNODB COMMENT='문의';


CREATE TABLE `PAY_STATUS_CODE` (
                                   `PAY_STATUS_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                   `PAY_STATUS_NAME` VARCHAR(50) NULL,
                                   PRIMARY KEY (`PAY_STATUS_ID`)
) ENGINE=INNODB COMMENT='결제 상태 코드';

CREATE TABLE `USER_COUPON` (
                               `USER_COUPON_ID` BIGINT NOT NULL AUTO_INCREMENT,
                               `USER_ID` BIGINT NOT NULL,
                               `COUPON_ID` BIGINT NOT NULL,
                               `COUPON_CODE` VARCHAR(50) NULL,
                               `ISSUED_AT` DATETIME NULL,
                               `IS_USED` TINYINT(1) NULL DEFAULT 0,
                               `USED_AT` DATETIME NULL,
                               `COUPON_NUM` BIGINT NULL,
                               `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                               `EXPIRE_AT` DATETIME NULL,
                               `DELETED_AT` DATETIME NULL,
                               PRIMARY KEY (`USER_COUPON_ID`),
                               INDEX `IDX_USER_COUPON_USER` (`USER_ID`),
                               INDEX `IDX_USER_COUPON_COUPON` (`COUPON_ID`),
                               INDEX `IDX_USER_COUPON_CODE` (`COUPON_CODE`),
                               INDEX `IDX_USER_COUPON_EXPIRE` (`EXPIRE_AT`, `IS_USED`)
) ENGINE=INNODB COMMENT='사용자 쿠폰';

CREATE TABLE `INQUIRY_ANSWER` (
                                  `INQUIRY_ANSWER_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                  `INQUIRY_ID` BIGINT NOT NULL,
                                  `ADMIN_ID` VARCHAR(50) NOT NULL,
                                  `CONTENT` TEXT NULL,
                                  `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                  `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`INQUIRY_ANSWER_ID`),
                                  INDEX `IDX_INQUIRY_ANSWER_INQUIRY` (`INQUIRY_ID`),
                                  INDEX `IDX_INQUIRY_ANSWER_ADMIN` (`ADMIN_ID`)
) ENGINE=INNODB COMMENT='문의 답변';



CREATE TABLE `ORDER_STATE_CODE` (
                                    `ID` BIGINT NOT NULL AUTO_INCREMENT,
                                    `NAME` VARCHAR(50) NULL,
                                    PRIMARY KEY (`ID`)
) ENGINE=INNODB;

CREATE TABLE `PG_CODE` (
                           `PG_ID` BIGINT NOT NULL AUTO_INCREMENT,
                           `PG_CODE` VARCHAR(36) NULL COMMENT 'UUID FORMAT',
                           `PG_NAME` VARCHAR(20) NULL,
                           PRIMARY KEY (`PG_ID`),
                           UNIQUE KEY `UK_PG_CODE` (`PG_CODE`)
) ENGINE=INNODB COMMENT='PG 코드';



CREATE TABLE `CART` (
                        `CART_ID` INT NOT NULL AUTO_INCREMENT,
                        `USER_ID` BIGINT NOT NULL,
                        `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                        `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `DELETED_AT` DATETIME NULL,
                        PRIMARY KEY (`CART_ID`),
                        INDEX `IDX_CART_USER` (`USER_ID`)
) ENGINE=INNODB COMMENT='장바구니';

CREATE TABLE `ORDER_HISTORY` (
                                 `HISTORY_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                 `ORDER_ID` BIGINT NOT NULL,
                                 `STATUS` VARCHAR(50) NULL,
                                 `COMMENT` TEXT NULL,
                                 `CREATED_AT` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                 `CREATED_BY` VARCHAR(255) NULL,
                                 `ID2` BIGINT NOT NULL,
                                 PRIMARY KEY (`HISTORY_ID`),
                                 INDEX `IDX_ORDER_HISTORY_ORDER` (`ORDER_ID`, `CREATED_AT`),
                                 INDEX `IDX_ORDER_HISTORY_ID2` (`ID2`)
) ENGINE=INNODB COMMENT='주문 이력';

CREATE TABLE `TERMS` (
                         `TERMS_ID` BIGINT NOT NULL AUTO_INCREMENT,
                         `TITLE` VARCHAR(200) NULL,
                         `CODE` VARCHAR(50) NULL,
                         `CONTENT` TEXT NULL,
                         `VERSION` VARCHAR(20) NULL,
                         `IS_REQUIRED` TINYINT(1) NULL DEFAULT 0,
                         `IS_ACTIVE` TINYINT(1) NULL DEFAULT 1,
                         `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                         `EFFECTIVE_FROM` DATETIME NULL,
                         `EFFECTIVE_TO` DATETIME NULL DEFAULT NULL,
                         PRIMARY KEY (`TERMS_ID`),
                         UNIQUE KEY `UK_TERMS_CODE` (`CODE`),
                         INDEX `IDX_TERMS_ACTIVE` (`IS_ACTIVE`, `EFFECTIVE_FROM`, `EFFECTIVE_TO`)
) ENGINE=INNODB COMMENT='약관';

CREATE TABLE `PAY_TYPE_CODE` (
                                 `PAY_TYPE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                 `PAY_TYPE` VARCHAR(50) NULL,
                                 `CASH_CODE` VARCHAR(50) NULL,
                                 PRIMARY KEY (`PAY_TYPE_ID`)
) ENGINE=INNODB COMMENT='결제 타입 코드';

CREATE TABLE `GLOBAL_CODE` (
                               `CODE` INT NOT NULL AUTO_INCREMENT,
                               `CODE_GROUP_ID` VARCHAR(50) NULL,
                               `CODE_ID` INT NULL COMMENT 'J123 (JOB), C121(옷 카테고리)',
                               `CODE_NAME` VARCHAR(100) NULL,
                               `DESCRIPTION` VARCHAR(255) NULL COMMENT '설명',
                               `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                               `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (`CODE`),
                               INDEX `IDX_GLOBAL_CODE_GROUP` (`CODE_GROUP_ID`)
) ENGINE=INNODB COMMENT='전역 코드';




CREATE TABLE `DEFAULT_USER_ADDRESS` (
                                        `USER_ID` BIGINT NOT NULL,
                                        `ROAD_ADDRESS_1` VARCHAR(255) NULL,
                                        `ROAD_ADDRESS_2` VARCHAR(255) NULL,
                                        `JIBUN_ADDRESS` VARCHAR(255) NULL,
                                        `DETAIL_ADDRESS` VARCHAR(255) NULL,
                                        `ENGLISH_ADDRESS` VARCHAR(255) NULL,
                                        `ZIP_CODE` INT NULL,
                                        `ADDRESS_NAME` VARCHAR(100) NULL,
                                        PRIMARY KEY (`USER_ID`)
) ENGINE=INNODB COMMENT='기본 사용자 주소';

CREATE TABLE `ORDERS` (
                          `ORDER_ID` BIGINT NOT NULL AUTO_INCREMENT,
                          `USER_ID` BIGINT NOT NULL,
                          `USER_COUPON_ID` BIGINT NOT NULL,
                          `ORDER_NUM` VARCHAR(50) NULL,
                          `TOTAL_COUNT` INT NULL DEFAULT 0,
                          `TOTAL_PRICE` DECIMAL(12,2) NULL,
                          `ORDER_STATUS` VARCHAR(20) NULL DEFAULT 'PENDING' COMMENT 'PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED',
                          `USE_MILE` DECIMAL(10,2) NULL DEFAULT 0,
                          `ORDER_AMOUNT` DECIMAL(12,2) NULL,
                          `SHIPPING_FEE` DECIMAL(8,2) NULL DEFAULT 0,
                          `DISCOUNT_AMOUNT` DECIMAL(10,2) NULL DEFAULT 0,
                          `FINAL_AMOUNT` DECIMAL(12,2) NULL,
                          `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `ID2` BIGINT NOT NULL,
                          PRIMARY KEY (`ORDER_ID`),
                          UNIQUE KEY `UK_ORDERS_NUM` (`ORDER_NUM`),
                          INDEX `IDX_ORDERS_USER` (`USER_ID`),
                          INDEX `IDX_ORDERS_STATUS` (`ORDER_STATUS`, `CREATED_AT`),
                          INDEX `IDX_ORDERS_COUPON` (`USER_COUPON_ID`),
                          INDEX `IDX_ORDERS_ID2` (`ID2`)
) ENGINE=INNODB COMMENT='주문';

CREATE TABLE `REVIEW_COMMENT` (
                                  `COMMENT_ID` INT NOT NULL AUTO_INCREMENT,
                                  `REVIEW_ID` BIGINT NOT NULL,
                                  `USER_ID` BIGINT NULL,
                                  `CONTENT` TEXT NOT NULL,
                                  `IS_DELETED` TINYINT(1) NOT NULL DEFAULT 0,
                                  `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `DELETED_AT` DATETIME NULL,
                                  PRIMARY KEY (`COMMENT_ID`),
                                  INDEX `IDX_REVIEW_COMMENT_REVIEW` (`REVIEW_ID`),
                                  INDEX `IDX_REVIEW_COMMENT_USER` (`USER_ID`)
) ENGINE=INNODB COMMENT='리뷰 댓글';

CREATE TABLE `REVIEW` (
                          `REVIEW_ID` BIGINT NOT NULL AUTO_INCREMENT,
                          `USER_ID` BIGINT NOT NULL,
                          `PRODUCT_ID` BIGINT NOT NULL,
                          `PURCHASE_ID` VARCHAR(255) NOT NULL,
                          `RATING` TINYINT NOT NULL CHECK (`RATING` >= 1 AND `RATING` <= 5),
                          `TITLE` VARCHAR(200) NULL,
                          `CONTENT` TEXT NULL,
                          `IMG_URL` TEXT NULL,
                          `LIKE_COUNT` INT NOT NULL DEFAULT 0,
                          `STATE` VARCHAR(20) NOT NULL DEFAULT 'AUTO' COMMENT 'AUTO, PENDING, APPROVED, REJECTED',
                          `APPROVED_AT` DATETIME NULL,
                          `IS_DELETED` TINYINT(1) NULL DEFAULT 0,
                          `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `DELETED_AT` DATETIME NULL,
                          PRIMARY KEY (`REVIEW_ID`),
                          INDEX `IDX_REVIEW_USER` (`USER_ID`),
                          INDEX `IDX_REVIEW_PRODUCT` (`PRODUCT_ID`),
                          INDEX `IDX_REVIEW_STATE` (`STATE`, `CREATED_AT`),
                          INDEX `IDX_REVIEW_RATING` (`RATING`)
) ENGINE=INNODB COMMENT='리뷰';



CREATE TABLE `SPECIAL_SECTION` (
                                   `SPECIAL_ID` VARCHAR(2) NOT NULL,
                                   `NAME` VARCHAR(100) NULL,
                                   `SORT_ORDER` INT NULL DEFAULT 0,
                                   `IS_ACTIVE` TINYINT(1) NULL DEFAULT 1,
                                   PRIMARY KEY (`SPECIAL_ID`),
                                   INDEX `IDX_SPECIAL_SECTION_ACTIVE` (`IS_ACTIVE`, `SORT_ORDER`)
) ENGINE=INNODB COMMENT='특별 섹션';

CREATE TABLE `MILEAGE_CODE` (
                                `REASON_CODE` VARCHAR(255) NOT NULL,
                                `REASON_NAME` VARCHAR(100) NULL,
                                `AMOUNT` DECIMAL(10,2) NULL,
                                `DESCRIPTION` TEXT NULL,
                                `MODIFY_TYPE` VARCHAR(20) NULL COMMENT 'EARN, USE, EXPIRE, CANCEL',
                                `IS_ACTIVE` TINYINT(1) NULL DEFAULT 1,
                                `EXP_DAY` INT NULL COMMENT '만료일수',
                                PRIMARY KEY (`REASON_CODE`),
                                INDEX `IDX_MILEAGE_CODE_TYPE` (`MODIFY_TYPE`, `IS_ACTIVE`)
) ENGINE=INNODB COMMENT='마일리지 코드';

CREATE TABLE `COUPON` (
                          `COUPON_ID` BIGINT NOT NULL AUTO_INCREMENT,
                          `COUPON_NAME` VARCHAR(100) NULL,
                          `COUPON_TYPE` VARCHAR(20) NULL DEFAULT 'FIXED' COMMENT 'FIXED, PERCENTAGE',
                          `DISCOUNT_VALUE` DECIMAL(10,2) NULL,
                          `MIN_ORDER_AMOUNT` DECIMAL(10,2) NULL,
                          `MAX_DISCOUNT_AMOUNT` DECIMAL(10,2) NULL,
                          `AVAILABLE_PERIOD` INT NULL,
                          `TOTAL_CNT` INT NULL,
                          `DUPLICATE_USE` CHAR(1) NULL DEFAULT 'N',
                          `COUPON_STATUS` VARCHAR(20) NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, EXPIRED',
                          `DESCRIPTION` TEXT NULL,
                          `START_DATE` DATETIME NULL,
                          `END_DATE` DATETIME NULL,
                          `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `CREATED_BY` VARCHAR(255) NULL,
                          PRIMARY KEY (`COUPON_ID`),
                          INDEX `IDX_COUPON_STATUS` (`COUPON_STATUS`),
                          INDEX `IDX_COUPON_DATE` (`START_DATE`, `END_DATE`)
) ENGINE=INNODB COMMENT='쿠폰';



CREATE TABLE `PAYMENT` (
                           `PAYMENT_ID` BIGINT NOT NULL AUTO_INCREMENT,
                           `ORDER_ID` BIGINT NOT NULL,
                           `USER_ID` BIGINT NULL,
                           `ORDER_NUM` VARCHAR(50) NULL,
                           `PAID_AMOUNT` DECIMAL(12,2) NULL,
                           `PAY_TYPE` VARCHAR(20) NULL COMMENT 'CARD, BANK_TRANSFER, VIRTUAL_ACCOUNT, MOBILE, KAKAO_PAY, NAVER_PAY, PAYCO',
                           `CASH_RECEIPT` CHAR(1) NULL DEFAULT 'N' COMMENT 'Y, N',
                           `PG_TRANSACTION_ID` VARCHAR(100) NULL,
                           `PAYMENT_KEY` VARCHAR(100) NULL,
                           `RECEIPT_URL` VARCHAR(500) NULL,
                           `CREATED_AT` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                           `UPDATED_AT` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `APPROVED_AT` TIMESTAMP NULL,
                           `CANCELLED_AT` TIMESTAMP NULL,
                           `PG_ID` BIGINT NOT NULL,
                           `PAY_STATUS_ID` BIGINT NOT NULL,
                           `PAY_TYPE_ID` BIGINT NOT NULL,
                           PRIMARY KEY (`PAYMENT_ID`),
                           INDEX `IDX_PAYMENT_ORDER` (`ORDER_ID`),
                           INDEX `IDX_PAYMENT_USER` (`USER_ID`),
                           INDEX `IDX_PAYMENT_STATUS` (`PAY_STATUS_ID`),
                           INDEX `IDX_PAYMENT_PG` (`PG_ID`),
                           INDEX `IDX_PAYMENT_TYPE` (`PAY_TYPE_ID`)
) ENGINE=INNODB COMMENT='결제';

CREATE TABLE `CART_ITEM` (
                             `CART_ITEM_ID` INT NOT NULL AUTO_INCREMENT,
                             `CART_ID` INT NOT NULL,
                             `PRODUCT_ID` BIGINT NOT NULL,
                             `PRODUCT_VARIANT_ID` BIGINT NULL,
                             `QUANTITY` INT NOT NULL DEFAULT 1,
                             `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`CART_ITEM_ID`),
                             INDEX `IDX_CART_ITEM_CART` (`CART_ID`),
                             INDEX `IDX_CART_ITEM_PRODUCT` (`PRODUCT_ID`),
                             INDEX `IDX_CART_ITEM_VARIANT` (`PRODUCT_VARIANT_ID`)
) ENGINE=INNODB COMMENT='장바구니 아이템';

CREATE TABLE `USER_TERMS_AGREEMENT` (
                                        `AGREEMENT_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                        `USER_ID` BIGINT NOT NULL,
                                        `TERMS_ID` BIGINT NOT NULL,
                                        `AGREED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                        `IP_ADDRESS` VARCHAR(45) NULL,
                                        PRIMARY KEY (`AGREEMENT_ID`),
                                        UNIQUE KEY `UK_USER_TERMS` (`USER_ID`, `TERMS_ID`),
                                        INDEX `IDX_USER_TERMS_USER` (`USER_ID`),
                                        INDEX `IDX_USER_TERMS_TERMS` (`TERMS_ID`)
) ENGINE=INNODB COMMENT='사용자 약관 동의';

CREATE TABLE `REPORT` (
                          `REPORT_ID` BIGINT NOT NULL AUTO_INCREMENT,
                          `USER_ID` BIGINT NOT NULL,
                          `TARGET_TYPE` VARCHAR(20) NOT NULL DEFAULT 'REVIEW' COMMENT 'REVIEW, COMMENT',
                          `TARGET_ID` BIGINT NOT NULL,
                          `REASON` TEXT NOT NULL,
                          `STATE` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED',
                          `APPROVED_AT` DATETIME NULL,
                          `APPROVED_BY` BIGINT NULL,
                          `IS_DELETED` TINYINT(1) NULL DEFAULT 0,
                          `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`REPORT_ID`),
                          INDEX `IDX_REPORT_USER` (`USER_ID`),
                          INDEX `IDX_REPORT_TARGET` (`TARGET_TYPE`, `TARGET_ID`),
                          INDEX `IDX_REPORT_STATE` (`STATE`, `CREATED_AT`)
) ENGINE=INNODB COMMENT='신고';



CREATE TABLE `REVIEW_LIKE` (
                               `LIKE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                               `REVIEW_ID` BIGINT NOT NULL,
                               `USER_ID` BIGINT NOT NULL,
                               `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`LIKE_ID`),
                               UNIQUE KEY `UK_REVIEW_LIKE` (`REVIEW_ID`, `USER_ID`),
                               INDEX `IDX_REVIEW_LIKE_REVIEW` (`REVIEW_ID`),
                               INDEX `IDX_REVIEW_LIKE_USER` (`USER_ID`)
) ENGINE=INNODB COMMENT='리뷰 좋아요';



CREATE TABLE `MILEAGE_HISTORY` (
                                   `HISTORY_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                   `USER_ID` BIGINT NOT NULL,
                                   `MILEAGE_ID` BIGINT NOT NULL,
                                   `REASON_CODE` VARCHAR(255) NOT NULL,
                                   `RELATED_ORDER_ID` BIGINT NULL,
                                   `REVIEW_ID` BIGINT NULL,
                                   `BALANCE_AFTER` DECIMAL(10,2) NULL,
                                   `EXPIRE_DATE` DATETIME NULL,
                                   `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `FIELD` VARCHAR(100) NULL,
                                   PRIMARY KEY (`HISTORY_ID`),
                                   INDEX `IDX_MILEAGE_HISTORY_USER` (`USER_ID`, `CREATED_AT`),
                                   INDEX `IDX_MILEAGE_HISTORY_MILEAGE` (`MILEAGE_ID`),
                                   INDEX `IDX_MILEAGE_HISTORY_REASON` (`REASON_CODE`),
                                   INDEX `IDX_MILEAGE_HISTORY_ORDER` (`RELATED_ORDER_ID`),
                                   INDEX `IDX_MILEAGE_HISTORY_REVIEW` (`REVIEW_ID`)
) ENGINE=INNODB COMMENT='마일리지 이력';

CREATE TABLE `WISH_LIST` (
                             `WISH_ID` BIGINT NOT NULL AUTO_INCREMENT,
                             `USER_ID` BIGINT NOT NULL,
                             `PRODUCT_ID` BIGINT NOT NULL,
                             `ADDED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                             PRIMARY KEY (`WISH_ID`),
                             UNIQUE KEY `UK_WISH_LIST` (`USER_ID`, `PRODUCT_ID`),
                             INDEX `IDX_WISH_LIST_USER` (`USER_ID`),
                             INDEX `IDX_WISH_LIST_PRODUCT` (`PRODUCT_ID`)
) ENGINE=INNODB COMMENT='위시리스트';

CREATE TABLE `SHIPMENT_RELATED_CODE` (
                                         `CODE_GROUP_NUM` TINYINT NULL,
                                         `CODE_ID` TINYINT NULL,
                                         `CODE_NAME` VARCHAR(100) NULL COMMENT 'EX. PENDING',
                                         `DESCRIPTION` VARCHAR(500) NULL,
                                         `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                                         `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         `SHIPMENT_ID` VARCHAR(50) NOT NULL,
                                         INDEX `IDX_SHIPMENT_CODE_SHIPMENT` (`SHIPMENT_ID`),
                                         INDEX `IDX_SHIPMENT_CODE_GROUP` (`CODE_GROUP_NUM`, `CODE_ID`)
) ENGINE=INNODB COMMENT='배송 관련 코드';

CREATE TABLE `LOGIN_HISTORY` (
                                 `ID` BIGINT NOT NULL AUTO_INCREMENT,
                                 `USER_ID` BIGINT NOT NULL,
                                 `IP` VARCHAR(45) NULL,
                                 `BROWSER` VARCHAR(255) NULL,
                                 `NATION` VARCHAR(100) NULL,
                                 `REGION` VARCHAR(100) NULL,
                                 `ATTEMPT_RESULT` TINYINT(1) NULL,
                                 `CONSECUTIVE_FAILED_LOGIN_ATTEMPT` INT NULL DEFAULT 0,
                                 `IS_LOCKED` TINYINT(1) NULL DEFAULT 0,
                                 `CREATED_AT` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`ID`),
                                 INDEX `IDX_LOGIN_HISTORY_USER` (`USER_ID`, `CREATED_AT`),
                                 INDEX `IDX_LOGIN_HISTORY_IP` (`IP`),
                                 INDEX `IDX_LOGIN_HISTORY_RESULT` (`ATTEMPT_RESULT`, `CREATED_AT`)
) ENGINE=INNODB COMMENT='로그인 이력';



CREATE TABLE `MILEAGE` (
                           `MILEAGE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                           `USER_ID` BIGINT NOT NULL,
                           `TOTAL_EARNED` DECIMAL(10,2) NULL DEFAULT 0,
                           `TOTAL_USED` DECIMAL(10,2) NULL DEFAULT 0,
                           `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`MILEAGE_ID`),
                           UNIQUE KEY `UK_MILEAGE_USER` (`USER_ID`)
) ENGINE=INNODB COMMENT='마일리지';



CREATE TABLE `NOTICE` (
                          `NOTICE_ID` INT NOT NULL AUTO_INCREMENT,
                          `TITLE` VARCHAR(200) NOT NULL,
                          `CONTENT` TEXT NOT NULL,
                          `CATEGORY` VARCHAR(20) NULL DEFAULT 'GENERAL' COMMENT 'GENERAL, EVENT, SYSTEM, DELIVERY',
                          `IS_PINNED` TINYINT(1) NULL DEFAULT 0,
                          `IS_ACTIVE` TINYINT(1) NULL DEFAULT 1,
                          `VIEW_COUNT` INT NULL DEFAULT 0,
                          `CREATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
                          `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `DELETED_AT` DATETIME NULL,
                          `CREATED_BY` VARCHAR(50) NULL,
                          PRIMARY KEY (`NOTICE_ID`),
                          INDEX `IDX_NOTICE_CATEGORY` (`CATEGORY`, `IS_ACTIVE`),
                          INDEX `IDX_NOTICE_PINNED` (`IS_PINNED`, `CREATED_AT`),
                          INDEX `IDX_NOTICE_CREATED_BY` (`CREATED_BY`)
) ENGINE=INNODB COMMENT='공지사항';

CREATE TABLE `EXCHANGE` (
                            `EXCHANGE_ID` BIGINT NOT NULL AUTO_INCREMENT,
                            `RETURN_ID` BIGINT NOT NULL,
                            `ORIGINAL_ORDER_ID` BIGINT NOT NULL,
                            `EXCHANGE_ORDER_ID` BIGINT NULL,
                            `ADMIN_MEMO` VARCHAR(500) NULL,
                            `CREATED_AT` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                            `UPDATED_AT` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `APPROVED_AT` TIMESTAMP NULL,
                            `COMPLETED_AT` TIMESTAMP NULL,
                            `FIELD` VARCHAR(255) NULL,
                            PRIMARY KEY (`EXCHANGE_ID`),
                            INDEX `IDX_EXCHANGE_RETURN` (`RETURN_ID`),
                            INDEX `IDX_EXCHANGE_ORIGINAL_ORDER` (`ORIGINAL_ORDER_ID`),
                            INDEX `IDX_EXCHANGE_EXCHANGE_ORDER` (`EXCHANGE_ORDER_ID`)
) ENGINE=INNODB COMMENT='교환';

CREATE TABLE `DELIVERY_EXCEPTION` (
                                      `DELIVERY_EXCEPTION_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                      `SHIPMENT_ID` VARCHAR(50) NOT NULL,
                                      `EXCEPTION_TYPE_CODE` INT NOT NULL,
                                      `OCCURRED_AT` DATETIME NULL,
                                      `CUSTOMER_CLAIM_DESC` TEXT NULL,
                                      `EVIDENCE_URL` VARCHAR(500) NULL,
                                      `COURIER_CLAIM_DESC` TEXT NULL,
                                      `COURIER_EVIDENCE_URL` VARCHAR(500) NULL,
                                      `CUSTOMER_COMPENSATION` VARCHAR(200) NULL,
                                      `INTERNAL_MEMO` TEXT NULL,
                                      `UPDATED_AT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `RESOLVED_AT` DATETIME NULL,
                                      `RESOLVED_BY` VARCHAR(255) NULL,
                                      PRIMARY KEY (`DELIVERY_EXCEPTION_ID`),
                                      INDEX `IDX_DELIVERY_EXCEPTION_SHIPMENT` (`SHIPMENT_ID`),
                                      INDEX `IDX_DELIVERY_EXCEPTION_TYPE` (`EXCEPTION_TYPE_CODE`),
                                      INDEX `IDX_DELIVERY_EXCEPTION_OCCURRED` (`OCCURRED_AT`)
) ENGINE=INNODB COMMENT='배송 예외';

CREATE TABLE `REFUND_ACCOUNT` (
                                  `USER_ID` BIGINT NOT NULL,
                                  `ACCOUNT_NAME` VARCHAR(255) NULL,
                                  `BANK_NAME` VARCHAR(255) NULL,
                                  `ACCOUNT_NUM` VARCHAR(255) NULL,
                                  PRIMARY KEY (`USER_ID`)
) ENGINE=INNODB COMMENT='환불 계좌';


-- 사용자 관련 외래키
ALTER TABLE `USER_DETAIL`
    ADD CONSTRAINT `FK_USER_DETAIL_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `ADMIN_DETAIL`
    ADD CONSTRAINT `FK_ADMIN_DETAIL_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `DEFAULT_USER_ADDRESS`
    ADD CONSTRAINT `FK_DEFAULT_ADDRESS_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

-- 카테고리 관련 외래키
ALTER TABLE `CATEGORY_LV2`
    ADD CONSTRAINT `FK_CATEGORY_LV2_LV1`
        FOREIGN KEY (`CATEGORY_LV1_ID`) REFERENCES `CATEGORY_LV1`(`CATEGORY_LV1_ID`) ON DELETE CASCADE;

ALTER TABLE `CATEGORY_LV3`
    ADD CONSTRAINT `FK_CATEGORY_LV3_LV2`
        FOREIGN KEY (`CATEGORY_LV2_ID`) REFERENCES `CATEGORY_LV2`(`CATEGORY_LV2_ID`) ON DELETE CASCADE;

-- 상품 관련 외래키
ALTER TABLE `PRODUCT`
    ADD CONSTRAINT `FK_PRODUCT_CATEGORY`
        FOREIGN KEY (`CATEGORY_LV3_ID`) REFERENCES `CATEGORY_LV3`(`CATEGORY_LV3_ID`) ON DELETE RESTRICT;

ALTER TABLE `PRODUCT_VARIANT`
    ADD CONSTRAINT `FK_PRODUCT_VARIANT_PRODUCT`
        FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`PRODUCT_ID`) ON DELETE CASCADE;

ALTER TABLE `PRODUCT_VARIANT`
    ADD CONSTRAINT `FK_PRODUCT_VARIANT_COLOR`
        FOREIGN KEY (`PRODUCT_VARIANT_COLOR_ID`) REFERENCES `PRODUCT_VARIANT_COLORS`(`PRODUCT_VARIANT_COLOR_ID`) ON DELETE RESTRICT;

ALTER TABLE `PRODUCT_VARIANT`
    ADD CONSTRAINT `FK_PRODUCT_VARIANT_SIZE`
        FOREIGN KEY (`PRODUCT_VARIANT_SIZE_ID`) REFERENCES `PRODUCT_VARIANT_SIZE`(`PRODUCT_VARIANT_SIZE_ID`) ON DELETE RESTRICT;

ALTER TABLE `PRODUCT_VARIANT`
    ADD CONSTRAINT `FK_PRODUCT_VARIANT_STOCK`
        FOREIGN KEY (`PRODUCT_VARIANT_STOCK_ID`) REFERENCES `PRODUCT_STOCK`(`PRODUCT_VARIANT_STOCK_ID`) ON DELETE CASCADE;

-- 상품 이미지 관련 외래키
ALTER TABLE `PRODUCT_VARIANT_DESCRIPTION_IMAGE`
    ADD CONSTRAINT `FK_VARIANT_DESC_IMAGE_VARIANT`
        FOREIGN KEY (`PRODUCT_VARIANT_ID`) REFERENCES `PRODUCT_VARIANT`(`PRODUCT_VARIANT_ID`) ON DELETE CASCADE;

ALTER TABLE `PRODUCT_VARIANT_IMAGE`
    ADD CONSTRAINT `FK_VARIANT_IMAGE_VARIANT`
        FOREIGN KEY (`PRODUCT_VARIANT_ID`) REFERENCES `PRODUCT_VARIANT`(`PRODUCT_VARIANT_ID`) ON DELETE CASCADE;

-- 상품 옵션 가격 외래키
ALTER TABLE `PRODUCT_OPTION_PRICE`
    ADD CONSTRAINT `FK_OPTION_PRICE_VARIANT`
        FOREIGN KEY (`PRODUCT_VARIANT_ID`) REFERENCES `PRODUCT_VARIANT`(`PRODUCT_VARIANT_ID`) ON DELETE CASCADE;

-- 주문 관련 외래키
ALTER TABLE `ORDERS`
    ADD CONSTRAINT `FK_ORDERS_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE RESTRICT;

ALTER TABLE `ORDERS`
    ADD CONSTRAINT `FK_ORDERS_USER_COUPON`
        FOREIGN KEY (`USER_COUPON_ID`) REFERENCES `USER_COUPON`(`USER_COUPON_ID`) ON DELETE RESTRICT;

ALTER TABLE `ORDER_HISTORY`
    ADD CONSTRAINT `FK_ORDER_HISTORY_ORDER`
        FOREIGN KEY (`ORDER_ID`) REFERENCES `ORDERS`(`ORDER_ID`) ON DELETE CASCADE;

-- 결제 관련 외래키
ALTER TABLE `PAYMENT`
    ADD CONSTRAINT `FK_PAYMENT_ORDER`
        FOREIGN KEY (`ORDER_ID`) REFERENCES `ORDERS`(`ORDER_ID`) ON DELETE RESTRICT;

ALTER TABLE `PAYMENT`
    ADD CONSTRAINT `FK_PAYMENT_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE RESTRICT;

ALTER TABLE `PAYMENT`
    ADD CONSTRAINT `FK_PAYMENT_PG`
        FOREIGN KEY (`PG_ID`) REFERENCES `PG_CODE`(`PG_ID`) ON DELETE RESTRICT;

ALTER TABLE `PAYMENT`
    ADD CONSTRAINT `FK_PAYMENT_STATUS`
        FOREIGN KEY (`PAY_STATUS_ID`) REFERENCES `PAY_STATUS_CODE`(`PAY_STATUS_ID`) ON DELETE RESTRICT;

ALTER TABLE `PAYMENT`
    ADD CONSTRAINT `FK_PAYMENT_TYPE`
        FOREIGN KEY (`PAY_TYPE_ID`) REFERENCES `PAY_TYPE_CODE`(`PAY_TYPE_ID`) ON DELETE RESTRICT;

-- 장바구니 관련 외래키
ALTER TABLE `CART`
    ADD CONSTRAINT `FK_CART_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `CART_ITEM`
    ADD CONSTRAINT `FK_CART_ITEM_CART`
        FOREIGN KEY (`CART_ID`) REFERENCES `CART`(`CART_ID`) ON DELETE CASCADE;

ALTER TABLE `CART_ITEM`
    ADD CONSTRAINT `FK_CART_ITEM_PRODUCT`
        FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`PRODUCT_ID`) ON DELETE CASCADE;

ALTER TABLE `CART_ITEM`
    ADD CONSTRAINT `FK_CART_ITEM_VARIANT`
        FOREIGN KEY (`PRODUCT_VARIANT_ID`) REFERENCES `PRODUCT_VARIANT`(`PRODUCT_VARIANT_ID`) ON DELETE CASCADE;

-- 쿠폰 관련 외래키
ALTER TABLE `USER_COUPON`
    ADD CONSTRAINT `FK_USER_COUPON_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `USER_COUPON`
    ADD CONSTRAINT `FK_USER_COUPON_COUPON`
        FOREIGN KEY (`COUPON_ID`) REFERENCES `COUPON`(`COUPON_ID`) ON DELETE CASCADE;

-- 문의 관련 외래키
ALTER TABLE `INQUIRY`
    ADD CONSTRAINT `FK_INQUIRY_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `INQUIRY`
    ADD CONSTRAINT `FK_INQUIRY_PRODUCT`
        FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`PRODUCT_ID`) ON DELETE SET NULL;

ALTER TABLE `INQUIRY_ANSWER`
    ADD CONSTRAINT `FK_INQUIRY_ANSWER_INQUIRY`
        FOREIGN KEY (`INQUIRY_ID`) REFERENCES `INQUIRY`(`INQUIRY_ID`) ON DELETE CASCADE;

-- 리뷰 관련 외래키
ALTER TABLE `REVIEW`
    ADD CONSTRAINT `FK_REVIEW_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `REVIEW`
    ADD CONSTRAINT `FK_REVIEW_PRODUCT`
        FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`PRODUCT_ID`) ON DELETE CASCADE;

ALTER TABLE `REVIEW_COMMENT`
    ADD CONSTRAINT `FK_REVIEW_COMMENT_REVIEW`
        FOREIGN KEY (`REVIEW_ID`) REFERENCES `REVIEW`(`REVIEW_ID`) ON DELETE CASCADE;

ALTER TABLE `REVIEW_COMMENT`
    ADD CONSTRAINT `FK_REVIEW_COMMENT_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `REVIEW_LIKE`
    ADD CONSTRAINT `FK_REVIEW_LIKE_REVIEW`
        FOREIGN KEY (`REVIEW_ID`) REFERENCES `REVIEW`(`REVIEW_ID`) ON DELETE CASCADE;

ALTER TABLE `REVIEW_LIKE`
    ADD CONSTRAINT `FK_REVIEW_LIKE_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

-- 위시리스트 외래키
ALTER TABLE `WISH_LIST`
    ADD CONSTRAINT `FK_WISH_LIST_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `WISH_LIST`
    ADD CONSTRAINT `FK_WISH_LIST_PRODUCT`
        FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`PRODUCT_ID`) ON DELETE CASCADE;

-- 마일리지 관련 외래키
ALTER TABLE `MILEAGE`
    ADD CONSTRAINT `FK_MILEAGE_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `MILEAGE_HISTORY`
    ADD CONSTRAINT `FK_MILEAGE_HISTORY_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `MILEAGE_HISTORY`
    ADD CONSTRAINT `FK_MILEAGE_HISTORY_MILEAGE`
        FOREIGN KEY (`MILEAGE_ID`) REFERENCES `MILEAGE`(`MILEAGE_ID`) ON DELETE CASCADE;

ALTER TABLE `MILEAGE_HISTORY`
    ADD CONSTRAINT `FK_MILEAGE_HISTORY_REASON`
        FOREIGN KEY (`REASON_CODE`) REFERENCES `MILEAGE_CODE`(`REASON_CODE`) ON DELETE RESTRICT;

ALTER TABLE `MILEAGE_HISTORY`
    ADD CONSTRAINT `FK_MILEAGE_HISTORY_ORDER`
        FOREIGN KEY (`RELATED_ORDER_ID`) REFERENCES `ORDERS`(`ORDER_ID`) ON DELETE SET NULL;

ALTER TABLE `MILEAGE_HISTORY`
    ADD CONSTRAINT `FK_MILEAGE_HISTORY_REVIEW`
        FOREIGN KEY (`REVIEW_ID`) REFERENCES `REVIEW`(`REVIEW_ID`) ON DELETE SET NULL;

-- 신고 관련 외래키
ALTER TABLE `REPORT`
    ADD CONSTRAINT `FK_REPORT_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

-- 약관 동의 외래키
ALTER TABLE `USER_TERMS_AGREEMENT`
    ADD CONSTRAINT `FK_USER_TERMS_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

ALTER TABLE `USER_TERMS_AGREEMENT`
    ADD CONSTRAINT `FK_USER_TERMS_TERMS`
        FOREIGN KEY (`TERMS_ID`) REFERENCES `TERMS`(`TERMS_ID`) ON DELETE CASCADE;

-- 로그인 이력 외래키
ALTER TABLE `LOGIN_HISTORY`
    ADD CONSTRAINT `FK_LOGIN_HISTORY_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;

-- 교환 관련 외래키
ALTER TABLE `EXCHANGE`
    ADD CONSTRAINT `FK_EXCHANGE_ORIGINAL_ORDER`
        FOREIGN KEY (`ORIGINAL_ORDER_ID`) REFERENCES `ORDERS`(`ORDER_ID`) ON DELETE RESTRICT;

ALTER TABLE `EXCHANGE`
    ADD CONSTRAINT `FK_EXCHANGE_EXCHANGE_ORDER`
        FOREIGN KEY (`EXCHANGE_ORDER_ID`) REFERENCES `ORDERS`(`ORDER_ID`) ON DELETE SET NULL;

-- 환불 계좌 외래키
ALTER TABLE `REFUND_ACCOUNT`
    ADD CONSTRAINT `FK_REFUND_ACCOUNT_USER`
        FOREIGN KEY (`USER_ID`) REFERENCES `USERS`(`USER_ID`) ON DELETE CASCADE;