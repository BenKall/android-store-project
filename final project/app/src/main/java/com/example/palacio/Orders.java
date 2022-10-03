package com.example.palacio;

class Orders {
    private long orderCode;
    private myDate orderDate;
    private myDate orderHour;
    private String orderUser;
    private String orderAmount;

    public Orders(long orderCode, myDate orderDate, myDate orderHour, String orderUser, String orderAmount) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.orderHour = orderHour;
        this.orderUser = orderUser;
        this.orderAmount = orderAmount;
    }

    public Orders()
    {

    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public myDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(myDate orderDate) {
        this.orderDate = orderDate;
    }

    public myDate getOrderHour() {
        return orderHour;
    }

    public void setOrderHour(myDate orderHour) {
        this.orderHour = orderHour;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }


}
