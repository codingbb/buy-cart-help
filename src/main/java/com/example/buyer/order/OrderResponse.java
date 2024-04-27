package com.example.buyer.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class OrderResponse {

    @Data
    public static class BuyFormDTO {
        private Integer id;     //order id
        private Integer buyQty;     //주문한 수량
        private Integer productId;     //상품 id
        private Integer sum;        //총합
        private String payment;
        private Integer userId;     //user id
        private String status;

        private String uName;    //성함
        private String address;
        private String phone;

        private String pName;   //상품명
        private Integer price;  //상품 가격

        //라디오 버튼용
        private Boolean isCredit = false;
        private Boolean isAccount = false;

        @Builder
        public BuyFormDTO(Integer id, Integer buyQty, Integer productId, Integer sum, String payment, Integer userId, String status, String uName, String address, String phone, String pName, Integer price) {
            this.id = id;
            this.buyQty = buyQty;
            this.productId = productId;
            this.sum = sum;
            this.payment = payment;
            this.userId = userId;
            this.status = status;
            this.uName = uName;
            this.address = address;
            this.phone = phone;
            this.pName = pName;
            this.price = price;
            payCheck();

        }

        public void payCheck() {
            if ("계좌이체".equals(payment)) {
                this.isAccount = true;
            } if ("신용카드".equals(payment)) {
                this.isCredit = true;
            }
        }

    }


    //내 구매목록 DTO
    @Data
    public static class ListDTO {
        private Integer id;
        private Integer userId;
        private Integer buyQty;
        private String payment;
        private Integer sum;
        private String status;
        private LocalDate createdAt;
        private String name;
        private Integer indexNum;

        //버튼 구분이 안가서.. 색 변경하려고 넣어줌
        private String buttonColor;

        @Builder
        public ListDTO(Integer id, Integer userId, Integer buyQty, String payment, Integer sum, String status, LocalDate createdAt, String name, Integer indexNum) {
            this.id = id;
            this.userId = userId;
            this.buyQty = buyQty;
            this.payment = payment;
            this.sum = sum;
            this.status = status;
            this.createdAt = createdAt;
            this.name = name;
            this.indexNum = indexNum;
            buttonColor();
        }

        //버튼 변경 클래스 용~
        public void buttonColor() {
             this.buttonColor = "btn btn-primary";
            if ("주문취소".equals(status)) {
                buttonColor = "btn btn-danger";
            }

        }

    }

}
