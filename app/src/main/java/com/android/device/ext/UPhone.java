package com.android.device.ext;

public final class UPhone {

    public static String getIccSerialNumber() {
        return CountryFactory.getCountry().getIccid();
    }

    public static String getIMSI() {
        return CountryFactory.getCountry().getImsi();
    }

    public static String getLine1Number() {
        return CountryFactory.getCountry().getPhoneNumber();
    }

    public static int getSimCarrierId() {
        return CountryFactory.getCountry().getSimCarrierId();
    }

    public static String getSimOperator() {
        return CountryFactory.getCountry().getSimOperator();
    }
}
