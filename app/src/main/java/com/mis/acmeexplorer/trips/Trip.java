package com.mis.acmeexplorer.trips;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class Trip implements Serializable {

    @DocumentId
    private String id;

    private String ticker;

    private String description;

    private String title;

    private String state = "active";

    private String picture;

    private double price;

    private Date startDate;

    private Date endDate;

    // Sevilla default location
    private double latitude = 37.377197;

    private double longitude = -5.986893;

    public Trip() {
    }

    public Trip(String ticker, String description, String title, String state, String picture, double price,
                Date startDate, Date endDate, double latitude, double longitude) {
        this.ticker = ticker;
        this.description = description;
        this.title = title;
        this.state = state;
        this.picture = picture;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getPriceString() {
        return String.format(Locale.ENGLISH, "%.2f", this.getPrice());
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isValid() {

        return (title != null && !title.isEmpty()) &&
                (description != null && !description.isEmpty()) &&
                (price != 0) && (startDate != null) && (endDate != null) &&
                (latitude != 0) && (longitude != 0);
    }

}
