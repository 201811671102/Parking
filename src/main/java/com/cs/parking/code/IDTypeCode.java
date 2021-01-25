package com.cs.parking.code;

/**
 * @ClassName IDTypeCode
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/20 15:46
 **/
public enum  IDTypeCode {
    IdCard(1,"身份证"),
    Passport(2,"护照"),
    MainlandTravelPermitForHongKongAndMacaoResidents(3,"Mainland Travel Permit for Hong Kong and Macao Residents"),
    MainlandTravelPermitForTaiwanResidents(4,"Mainland_Travel_Permit_"),
    ;
    private int code;
    private String idType;

    IDTypeCode(int code, String idType) {
        this.code = code;
        this.idType = idType;
    }

    public int getCode() {
        return code;
    }

    public String getIdType() {
        return idType;
    }

    public static String getType(int code){
        IDTypeCode[] values = IDTypeCode.values();
        for (IDTypeCode idTypeCode : values){
            if (idTypeCode.getCode() == code){
                return idTypeCode.getIdType();
            }
        }
        return null;
    }
}
