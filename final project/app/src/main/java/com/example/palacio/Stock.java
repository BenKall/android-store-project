package com.example.palacio;

public class Stock {
    private int Productcode;
    private String Productname;
    private String Producttype;
    private String Productdescription;
    private String Price;
    private String Amount;
    private String aType;
    private String hType;
    private String wType;

    public Stock(int productcode, String productname, String producttype, String productdescription, String price, String amount, String aType, String hType, String wType) {
        Productcode = productcode;
        Productname = productname;
        Producttype = producttype;
        Productdescription = productdescription;
        Price = price;
        Amount = amount;
        this.aType = aType;
        this.hType = hType;
        this.wType = wType;
    }

    public int getProductcode() {
        return Productcode;
    }

    public void setProductcode(int productcode) {
        Productcode = productcode;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getProducttype() {
        return Producttype;
    }

    public void setProducttype(String producttype) {
        Producttype = producttype;
    }

    public String getProductdescription() {
        return Productdescription;
    }

    public void setProductdescription(String productdescription) {
        Productdescription = productdescription;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String gethType() {
        return hType;
    }

    public void sethType(String hType) {
        this.hType = hType;
    }

    public String getwType() {
        return wType;
    }

    public void setwType(String wType) {
        this.wType = wType;
    }

    public Stock()
    {

    }

}
