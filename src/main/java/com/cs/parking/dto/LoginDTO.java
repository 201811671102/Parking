package com.cs.parking.dto;

import com.cs.parking.pojo.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName LoginDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 20:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 6945890905894008271L;

    private Integer uid;

    private String name;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date viptime;

    private Boolean vip;

    private String jwt;

    public LoginDTO(User user, String jwt) {
        this.uid = user.getId();
        this.name = user.getName();
        this.viptime =user.getVipTime();
        this.jwt = jwt;
        if (user.getVipTime() == null || user.getVipTime().before(new Date())){
            vip = false;
        }else{
            vip = true;
        }
    }
}
