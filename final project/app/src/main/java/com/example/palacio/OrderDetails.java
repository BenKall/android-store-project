package com.example.palacio;

class OrderDetails {
    private long orderDetsCode; // = orderCode
    private int orderDetsProductCode;
    private String orderDetsProAmount;
    private String orderDetsProPPU;//PPU = Price Per Unit

    public OrderDetails(long orderDetsCode, int orderDetsProductcode, String orderDetsProAmount, String orderDetsProPPU) {
        this.orderDetsCode = orderDetsCode;
        this.orderDetsProductCode = orderDetsProductcode;
        this.orderDetsProAmount = orderDetsProAmount;
        this.orderDetsProPPU = orderDetsProPPU;
    }

    public OrderDetails()
    {

    }

    public long getOrderDetsCode() {
        return orderDetsCode;
    }

    public void setOrderDetsCode(long orderDetsCode) {
        this.orderDetsCode = orderDetsCode;
    }

    public int getOrderDetsProductCode() {
        return orderDetsProductCode;
    }

    public void setOrderDetsProductCode(int orderDetsProductCode) {
        this.orderDetsProductCode = orderDetsProductCode;
    }

    public String getOrderDetsProAmount() {
        return orderDetsProAmount;
    }

    public void setOrderDetsProAmount(String orderDetsProAmount) {
        this.orderDetsProAmount = orderDetsProAmount;
    }

    public String getOrderDetsProPPU() {
        return orderDetsProPPU;
    }

    public void setOrderDetsProPPU(String orderDetsProPPU) {
        this.orderDetsProPPU = orderDetsProPPU;
    }
}
