package com.cs.parking.pojo;

import java.util.Date;

public class Coupons {
    private Integer id;

    private String name;

    private Double price;

    private Integer number;

    private Date startTime;

    private Date endTime;

    private Integer parkingSpaceId;

    private Boolean cancel;

    public Coupons() {
    }

    public Coupons(String name, Double price, Integer number, Date startTime, Date endTime, Integer parkingSpaceId, Boolean cancel) {
        this.name = name;
        this.price = price;
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
        this.parkingSpaceId = parkingSpaceId;
        this.cancel = cancel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Integer parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }
}