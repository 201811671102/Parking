package com.cs.parking.pojo;

import java.util.Date;

public class ScheduleJob {
    private Integer id;

    private String jobKey;

    private String jobGroup;

    private String jobContent;

    private Date jobTime;

    private Boolean state;

    public ScheduleJob() {
    }

    public ScheduleJob(String jobKey, String jobGroup, String jobContent, Date jobTime, Boolean state) {
        this.jobKey = jobKey;
        this.jobGroup = jobGroup;
        this.jobContent = jobContent;
        this.jobTime = jobTime;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey == null ? null : jobKey.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent == null ? null : jobContent.trim();
    }

    public Date getJobTime() {
        return jobTime;
    }

    public void setJobTime(Date jobTime) {
        this.jobTime = jobTime;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}