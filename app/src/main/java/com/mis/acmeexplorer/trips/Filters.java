package com.mis.acmeexplorer.trips;

import android.text.TextUtils;

public class Filters {

    private String title = null;
    private int price = -1;

    public Filters() {}

    public boolean hasTitle() {
        return !(TextUtils.isEmpty(title));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasPrice() {
        return (price > 0);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}