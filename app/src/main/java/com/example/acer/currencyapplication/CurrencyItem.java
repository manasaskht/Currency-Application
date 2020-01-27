package com.example.acer.currencyapplication;

public class CurrencyItem {
    private  String base;
    private double value;

    CurrencyItem(String base, double value){
        this.base = base;
        this.value = value;
    }

    public String getBase(){
        return this.base;
    }

    public double getValue(){
        return this.value;
    }

}
