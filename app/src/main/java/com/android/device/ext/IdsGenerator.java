package com.android.device.ext;

import java.util.Random;
import java.util.UUID;

public class IdsGenerator {

    private static final String FIX_PRE = "89";

    /**
     * 20位
     *
     * @param countryCode
     * @return
     */
    public static String generateICCID(String countryCode) {
        Country country = Countries.getCountryByCode(countryCode);
        StringBuilder iccId = new StringBuilder();
        iccId.append(FIX_PRE);
        iccId.append(country.getCountryCode());
        iccId.append(country.getMnc());
        iccId.append(getRandomNum(20 - iccId.length()));
        return iccId.toString();
    }


    public static String generateIMSI(String countryCode) {
        Country country = Countries.getCountryByCode(countryCode);
        StringBuilder imsi = new StringBuilder();
        imsi.append(country.getSimOperator());
        imsi.append(getRandomNum(15 - imsi.length()));
        return imsi.toString();
    }

    private static String getRandomNum(int count) {
        UUID uuid = UUID.randomUUID();
        String uid = String.valueOf(Math.abs(uuid.getMostSignificantBits()));
        if (uid.length() >= count) {
            return uid.substring(0, count);
        }
        String result = "";
        for (int i = 0; i < count; i++) {
            result += (new Random().nextInt(10));
        }
        return result;
    }


    public static String genPhoneNumber(String countryCode) {
        Country country = Countries.getCountryByCode(countryCode);
        StringBuilder phoneNo = new StringBuilder();
        phoneNo.append(country.getPhonePre());
        phoneNo.append(getRandomNum(country.getPhoneNoCount() - phoneNo.length()));
        return phoneNo.toString();
    }


//    public static void main(String[] args) {
//        String iccid = generateICCID("44");
//        String imsi = generateIMSI("44");
//        String phoneNo = genPhoneNumber("44");
//        System.out.println("iccid:" + iccid + " len:" + iccid.length());
//        System.out.println("imsi:" + imsi + " len:" + imsi.length());
//        System.out.println("phoneNo:" + phoneNo + " len:" + phoneNo.length());
//    }
}
