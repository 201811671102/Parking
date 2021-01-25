package com.cs.parking.code;

/**
 * @ClassName QuartzJobCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/23 20:51
 **/
public enum  QuartzJobCode {
    Un_Do_Job(200),
    Order_Coming_Remind_First(201),
    Order_Coming_Remind_Last(202),
    Order_OnGoing_Remind(203),
    Order_Complete_Remind(204),
    Vip_Over_Remind(206),
    Coupons_Over(207),
    ;

    private Integer code;

    QuartzJobCode(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
