package com.cs.parking.pojo.DIYpojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * @ClassName Appointment
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/21 1:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Appointment {
    private String appointmentTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private LocalDate appointmentDate;
}
