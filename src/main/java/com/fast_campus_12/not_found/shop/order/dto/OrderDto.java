package com.fast_campus_12.not_found.shop.order.dto;

import lombok.Getter;

import java.util.Date;
import java.util.Objects;

@Getter
public class OrderDto {
        private Long orderId;
        private String loginId;
        private Long userCouponId;
        private String orderNum;
        private Integer totalCount;
        private double totalPrice;
        private String orderStatus;    // pending, confirmed, processing, shipped, delivered, cancelled, refunded
        private double useMile;
        private double orderAmount;
        private double shippingFee;
        private double discountAmount;
        private double finalAmount;
        private Date createdAt;
        private Date updatedAt;
        private Long id2;              // 추가 컬럼

        public void getOrderId() {}
        public OrderDto(Long orderId, String loginId, Long userCouponId, String orderNum, Integer totalCount, double totalPrice, String orderStatus, double useMile, double orderAmount, double shippingFee, double discountAmount, double finalAmount, Date createdAt, Date updatedAt, Long id2) {
                this.orderId = orderId;
                this.loginId = loginId;
                this.userCouponId = userCouponId;
                this.orderNum = orderNum;
                this.totalCount = totalCount;
                this.totalPrice = totalPrice;
                this.orderStatus = orderStatus;
                this.useMile = useMile;
                this.orderAmount = orderAmount;
                this.shippingFee = shippingFee;
                this.discountAmount = discountAmount;
                this.finalAmount = finalAmount;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.id2 = id2;
        }

        public void setOrderId(Long orderId) {
                this.orderId = orderId;
        }

        public String getLoginId() {
                return loginId;
        }

        public void setLoginId(String loginId) {
                this.loginId = loginId;
        }

        public Long getUserCouponId() {
                return userCouponId;
        }

        public void setUserCouponId(Long userCouponId) {
                this.userCouponId = userCouponId;
        }

        public String getOrderNum() {
                return orderNum;
        }

        public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
        }

        public Integer getTotalCount() {
                return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
                this.totalCount = totalCount;
        }

        public double getTotalPrice() {
                return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
        }

        public String getOrderStatus() {
                return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
        }

        public double getUseMile() {
                return useMile;
        }

        public void setUseMile(double useMile) {
                this.useMile = useMile;
        }

        public double getOrderAmount() {
                return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
                this.orderAmount = orderAmount;
        }

        public double getShippingFee() {
                return shippingFee;
        }

        public void setShippingFee(double shippingFee) {
                this.shippingFee = shippingFee;
        }

        public double getDiscountAmount() {
                return discountAmount;
        }

        public void setDiscountAmount(double discountAmount) {
                this.discountAmount = discountAmount;
        }

        public double getFinalAmount() {
                return finalAmount;
        }

        public void setFinalAmount(double finalAmount) {
                this.finalAmount = finalAmount;
        }

        public Date getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
                this.updatedAt = updatedAt;
        }

        public Long getId2() {
                return id2;
        }

        public void setId2(Long id2) {
                this.id2 = id2;
        }

        @Override
        public boolean equals(Object o) {
                if (!(o instanceof OrderDto orderDto)) return false;
            return Objects.equals(orderId, orderDto.orderId) && Objects.equals(loginId, orderDto.loginId) && Objects.equals(orderNum, orderDto.orderNum);
        }

        @Override
        public int hashCode() {
                return Objects.hash(orderId, loginId, orderNum);
        }

        @Override
        public String toString() {
                return "OrderDto{" +
                        "orderId=" + orderId +
                        ", loginId='" + loginId + '\'' +
                        ", userCouponId=" + userCouponId +
                        ", orderNum='" + orderNum + '\'' +
                        ", totalCount=" + totalCount +
                        ", totalPrice=" + totalPrice +
                        ", orderStatus='" + orderStatus + '\'' +
                        ", useMile=" + useMile +
                        ", orderAmount=" + orderAmount +
                        ", shippingFee=" + shippingFee +
                        ", discountAmount=" + discountAmount +
                        ", finalAmount=" + finalAmount +
                        ", createdAt=" + createdAt +
                        ", updatedAt=" + updatedAt +
                        ", id2=" + id2 +
                        '}';
        }
}
