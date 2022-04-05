package com.mis.acmeexplorer.trips;

import java.io.Serializable;
import java.util.Date;

public class Trip implements Serializable {

    private String ticker;

    private String description;

    private String title;

    private String state;

    private double price;

    private Date startDate;

    private Date endDate;

    public Trip() {
    }

    public Trip(String ticker, String description, String title, String state, double price,
                Date startDate, Date endDate) {
        this.ticker = ticker;
        this.description = description;
        this.title = title;
        this.state = state;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
