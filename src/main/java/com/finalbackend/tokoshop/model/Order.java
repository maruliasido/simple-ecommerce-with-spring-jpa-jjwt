package com.finalbackend.tokoshop.model;


import java.util.List;

public class Order {
    private String userId;
    private String totalPrice;
    private String cartId;
    private List<String> cartItem;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<String> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<String> cartItem) {
        this.cartItem = cartItem;
    }



    

    
}