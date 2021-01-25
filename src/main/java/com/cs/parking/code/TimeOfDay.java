package com.cs.parking.code;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TimeOfDay {
    Zero(0,"00:00-01:00"),
    One(1,"01:00-02:00"),
    Two(2,"02:00-03:00"),
    Three(3,"03:00-04:00"),
    Four(4,"04:00-05:00"),
    Five(5,"05:00-06:00"),
    Six(6,"06:00-07:00"),
    Seven(7,"07:00-08:00"),
    Eight(8,"08:00-09:00"),
    Nine(9,"09:00-010:00"),
    Ten(10,"10:00-11:00"),
    Eleven(11,"11:00-12:00"),
    Twelve(12,"12:00-13:00"),
    Thirteen(13,"13:00-14:00"),
    Fourteen(14,"14:00-15:00"),
    Fifteen(15,"15:00-16:00"),
    Sixteen(16,"16:00-17:00"),
    Seventeen(17,"17:00-18:00"),
    Eighteen(18,"18:00-19:00"),
    Nineteen(19,"19:00-20:00"),
    Twenty(20,"20:00-21:00"),
    TwentyOne(21,"21:00-22:00"),
    TwentyTwo(22,"22:00-23:00"),
    TwentyThree(23,"23:00-24:00")
    ;
    private int code;
    private String time;

    TimeOfDay(int code, String time) {
        this.code = code;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }

    public static String getTime(int code){
        TimeOfDay[] values = TimeOfDay.values();
        for (TimeOfDay timeOfDay : values){
            if (timeOfDay.getCode() == code){
                return timeOfDay.getTime();
            }
        }
        return null;
    }


    public static Map<Integer,String> getAppointment(Integer startTimeDay,Integer endTimeDay){
        Map<Integer, String> collect = Arrays.stream(TimeOfDay.values())
                .filter((x) -> {
                    if (x.getCode() < startTimeDay || x.getCode() > endTimeDay) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toMap(TimeOfDay::getCode, TimeOfDay::getTime));
        return collect;
    }
}
