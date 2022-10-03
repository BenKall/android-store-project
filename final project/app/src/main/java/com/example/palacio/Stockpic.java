package com.example.palacio;

class Stockpic {
    private int Productcode;
    private String Picname;

    public Stockpic(int productcode, String picname) {
        Productcode = productcode;
        Picname = picname;
    }

    public Stockpic()
    {

    }

    public int getProductcode() {
        return Productcode;
    }

    public void setProductcode(int productcode) {
        Productcode = productcode;
    }

    public String getPicname() {
        return Picname;
    }

    public void setPicname(String picname) {
        Picname = picname;
    }
}
