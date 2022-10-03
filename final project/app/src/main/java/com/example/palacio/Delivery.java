package com.example.palacio;

class Delivery {
    private long deliveryCode;
    private myDate DeliveryDate;
    private String orderUser; //user email
    private String status;
    private long orderCode;
    private String userOrderAddress;

    public Delivery(long deliveryCode, myDate DeliveryDate, String orderUser, String status, long orderCode, String userOrderAddress) {
        this.deliveryCode = deliveryCode;
        this.DeliveryDate = DeliveryDate;
        this.orderUser = orderUser;
        this.status = status;
        this.orderCode = orderCode;
        this.userOrderAddress = userOrderAddress;
    }

    public Delivery()
    {

    }

    public long getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(long deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public myDate getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(myDate deliveryDate) {
        this.DeliveryDate = deliveryDate;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(long orderCode) {
        this.orderCode = orderCode;
    }

    public String getUserOrderAddress() {
        return userOrderAddress;
    }

    public void setUserOrderAddress(String userOrderAddress) {
        this.userOrderAddress = userOrderAddress;
    }
}
