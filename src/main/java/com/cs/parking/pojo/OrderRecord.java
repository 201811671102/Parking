package com.cs.parking.pojo;

import java.util.Date;

public class OrderRecord {
    private Integer id;

    private Integer parkingSpaceId;

    private Integer userId;

    private String couponsId;

    private String carNumber;

    private String carOwnerPhoneNumber;

    private Date payTime;

    private Date effectTime;

    private Date failureTime;

    private String appointmentTime;

    private Double paymentAmount;

    private Integer orderState;

    public OrderRecord() {
    }

    public OrderRecord(Integer parkingSpaceId, Integer userId, String carNumber, String carOwnerPhoneNumber, Date payTime, Date effectTime, Date failureTime, String appointmentTime, Double paymentAmount, Integer orderState) {
        this.parkingSpaceId = parkingSpaceId;
        this.userId = userId;
        this.carNumber = carNumber;
        this.carOwnerPhoneNumber = carOwnerPhoneNumber;
        this.payTime = payTime;
        this.effectTime = effectTime;
        this.failureTime = failureTime;
        this.appointmentTime = appointmentTime;
        this.paymentAmount = paymentAmount;
        this.orderState = orderState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Integer parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId == null ? null : couponsId.trim();
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public String getCarOwnerPhoneNumber() {
        return carOwnerPhoneNumber;
    }

    public void setCarOwnerPhoneNumber(String carOwnerPhoneNumber) {
        this.carOwnerPhoneNumber = carOwnerPhoneNumber == null ? null : carOwnerPhoneNumber.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }

    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime == null ? null : appointmentTime.trim();
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }
}