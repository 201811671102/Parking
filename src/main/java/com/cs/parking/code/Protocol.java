package com.cs.parking.code;


public enum Protocol {
    //心跳包
    HeartbeatReq(1000,""),
    Order_Coming_Remind_First_Notice(2000,""),
    Order_Coming_Remind_Last_Notice(3000,""),
    Order_OnGoing_Remind_Notice(4000,""),
    Order_Complete_Remind_Notice(5000,""),
    Vip_Over_Notice(6000,""),
    Coupons_Over_Notice(7000,""),
    Parking_Space_Rent_Notice(8000,""),
    Parking_Number(9000,"")
    ;

    private int value;
    private Object data;

    Protocol(int value, Object data) {
        this.value = value;
        this.data = data;
    }



    public int getValue() {
        return value;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Protocol forNumber(int value){
        switch (value){
            case 1000:return HeartbeatReq;
            case 2000:return Order_Coming_Remind_First_Notice;
            case 3000:return Order_Coming_Remind_Last_Notice;
            case 4000:return Order_OnGoing_Remind_Notice;
            case 5000:return Order_Complete_Remind_Notice;
            case 6000:return Vip_Over_Notice;
            case 7000:return Coupons_Over_Notice;
            case 8000:return Parking_Space_Rent_Notice;
            case 9000:return Parking_Number;
            default:return null;
        }
    }
}
