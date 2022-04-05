package com.mis.acmeexplorer.trips;

import android.text.TextUtils;

import java.util.Date;

public class Filters {

    private String title = null;
    private Date minDate = null;
    private Date maxDate = null;
    private int minPrice = -1;
    private int maxPrice = -1;

    public Filters() {
    }

    public boolean hasTitle() {
        return !(TextUtils.isEmpty(title));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasMinPrice() {
        return (minPrice > 0);
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public boolean hasMaxPrice() {
        return (maxPrice > 0);
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean hasMinDate() {
        return minDate != null;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public boolean hasMaxDate() {
        return maxDate != null;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
}