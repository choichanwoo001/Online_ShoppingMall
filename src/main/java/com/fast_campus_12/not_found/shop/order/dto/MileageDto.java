// MileageDTO.java
package com.fast_campus_12.not_found.shop.order.dto; // 패키지 경로는 실제 프로젝트에 맞게 변경하세요

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MileageDto {
    private BigDecimal availableMileage; // 사용 가능한 마일리지 금액
}
