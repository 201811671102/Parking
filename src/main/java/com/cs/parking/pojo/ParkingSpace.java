package com.cs.parking.pojo;

import java.util.Date;

public class ParkingSpace {
    private Integer id;

    private Integer userId;

    private String shortName;

    private String province;

    private String city;

    private String county;

    private String town;

    private String address;

    private String rentPhoneNumber;

    private Double price;

    private Date startTimeYear;

    private Date endTimeYear;

    private Integer startTimeDay;

    private Integer endTimeDay;

    private Double longitude;

    private Double latitude;

    private Boolean share;

    private Boolean cancel;

    public ParkingSpace() {
    }

    public ParkingSpace(Integer userId, String shortName, String province, String city, String county, String town, String address, String rentPhoneNumber, Double price, Date startTimeYear, Date endTimeYear, Integer startTimeDay, Integer endTimeDay, Double longitude, Double latitude, Boolean share, Boolean cancel) {
        this.userId = userId;
        this.shortName = shortName;
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
        this.address = address;
        this.rentPhoneNumber = rentPhoneNumber;
        this.price = price;
        this.startTimeYear = startTimeYear;
        this.endTimeYear = endTimeYear;
        this.startTimeDay = startTimeDay;
        this.endTimeDay = endTimeDay;
        this.longitude = longitude;
        this.latitude = latitude;
        this.share = share;
        this.cancel = cancel;
    }

    public ParkingSpace(Integer id, Integer userId, String shortName, String province, String city, String county, String town, String address, String rentPhoneNumber, Double price, Date startTimeYear, Date endTimeYear, Integer startTimeDay, Integer endTimeDay, Double longitude, Double latitude) {
        this.id = id;
        this.userId = userId;
        this.shortName = shortName;
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
        this.address = address;
        this.rentPhoneNumber = rentPhoneNumber;
        this.price = price;
        this.startTimeYear = startTimeYear;
        this.endTimeYear = endTimeYear;
        this.startTimeDay = startTimeDay;
        this.endTimeDay = endTimeDay;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getRentPhoneNumber() {
        return rentPhoneNumber;
    }

    public void setRentPhoneNumber(String rentPhoneNumber) {
        this.rentPhoneNumber = rentPhoneNumber == null ? null : rentPhoneNumber.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getStartTimeYear() {
        return startTimeYear;
    }

    public void setStartTimeYear(Date startTimeYear) {
        this.startTimeYear = startTimeYear;
    }

    public Date getEndTimeYear() {
        return endTimeYear;
    }

    public void setEndTimeYear(Date endTimeYear) {
        this.endTimeYear = endTimeYear;
    }

    public Integer getStartTimeDay() {
        return startTimeDay;
    }

    public void setStartTimeDay(Integer startTimeDay) {
        this.startTimeDay = startTimeDay;
    }

    public Integer getEndTimeDay() {
        return endTimeDay;
    }

    public void setEndTimeDay(Integer endTimeDay) {
        this.endTimeDay = endTimeDay;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }
}