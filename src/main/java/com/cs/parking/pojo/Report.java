package com.cs.parking.pojo;

import java.util.Date;

public class Report {
    private Integer id;

    private String carNumber;

    private Integer reportType;

    private String province;

    private String city;

    private String county;

    private String town;

    private String address;

    private Date reportTime;

    private Double longitude;

    private Double latitude;

    private String reportPhoto;

    private Boolean deal;

    private Boolean cancel;

    public Report(String carNumber, Integer reportType, String province, String city, String county, String town, String address, Date reportTime, Double longitude, Double latitude, String reportPhoto, Boolean deal, Boolean cancel) {
        this.carNumber = carNumber;
        this.reportType = reportType;
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
        this.address = address;
        this.reportTime = reportTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reportPhoto = reportPhoto;
        this.deal = deal;
        this.cancel = cancel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
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

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
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

    public String getReportPhoto() {
        return reportPhoto;
    }

    public void setReportPhoto(String reportPhoto) {
        this.reportPhoto = reportPhoto == null ? null : reportPhoto.trim();
    }

    public Boolean getDeal() {
        return deal;
    }

    public void setDeal(Boolean deal) {
        this.deal = deal;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }
}