package com.example.shop.exception;

import lombok.Getter;

@Getter
public enum ErrorResponse {
    CATEGORY_EXISTED(1001,"category exist"),
    CATEGORY_NOT_EXISTED(1002,"category not exist"),
    PRODUCT_EXISTED(1003,"product exist"),
    PRODUCT_NOT_EXISTED(1004,"product not exist"),
    OUT_OF_STOCK(1005,"out of stock"),
    USER_EXISTED(1006,"user exist"),
    USER_NOT_EXISTED(1007,"user not exist"),
    ADDRESS_EXISTED(1008,"address exist"),
    ADDRESS_NOT_EXISTED(1009,"address not exist"),
    ROLE_EXISTED(1010,"role exist"),
    ROLE_NOT_EXISTED(1011,"role not exist"),
    TRANSACTION_NOT_EXISTED(1012,"transaction not exist"),
    CART_NOT_EXISTED(1013,"cart not exist"),
    CART_EMPTY(1014,"cart empty"),
    ORDER_NOT_EXISTED(1015,"order not exist"),
    INVALID_REQUEST_PARAMETERS(1016, "Invalid request parameters"),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),

    ;
    ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;

}
