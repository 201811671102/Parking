package com.cs.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName NoticeMessageDTO
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/25 14:54
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMessageDTO {
    private int id;
    private int value;
    private Object data;
}
