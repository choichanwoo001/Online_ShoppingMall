// ProductOrderInfoDTO.java
package com.fast_campus_12.not_found.shop.domain.order.dto; // 패키지 경로는 실제 프로젝트에 맞게 변경하세요

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderInfoDto{
    private String productName; // 상품이름 (상품의 제목)
    private int quantity;       // 갯수 (장바구니에 담긴 상품의 수량)
    private double Price;  // 총금액 (갯수 * 상품 가격)
}
