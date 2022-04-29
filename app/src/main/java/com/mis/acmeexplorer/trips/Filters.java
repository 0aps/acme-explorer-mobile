package com.mis.acmeexplorer.trips;

import android.text.TextUtils;

import java.util.Date;

public class Filters {

    private String title = null;
    private Date minDate = null;
    private Date maxDate = null;
    private double minPrice = -1;
    private double maxPrice = -1;

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

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean hasMaxPrice() {
        return (maxPrice > 0);
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
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