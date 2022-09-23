package com.android.device.ext.operator;

public abstract class ChinaOperator {
    public static final String FIX_PRE = "89";
    public static final String COUNTRY_CODE = "86";
    public final static String MCC = "460";

    protected int randomIndex;
    protected String phoneNo;


    public  String getSimOperator(){
        return MCC + getMNC();
    }
    public abstract String getPhoneNo();
    public abstract String getIMSI();
    public abstract String getICCID();

    public abstract String getMNC();
}
