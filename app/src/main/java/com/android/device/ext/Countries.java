package com.android.device.ext;

import java.util.HashMap;

public class Countries {
    public final static String[] COUNTRY_CODE = {"86", "01", "44", "63", "63", "84", "66"};
    public final static String[] MCC = {"460", "310", "234", "515", "510", "452", "520"};
    public final static String[][] MNC = {{"00", "01", "03"}, {"410"}, {"10"}, {"02"}, {"11"}, {"05"}, {"00"}};
    public final static String[][] PHONE_PRE = {{"135", "131", "153"}, {"206","360","802","213","609"}, {"07410","07529","07536"}, {"91","92","93","94"}, {"08110","08118","08169"}, {"09"}, {"08"}};
    public final static int[] PHONE_NO_COUNT = {11, 10, 11, 10, 10, 10, 10};
    public final static int[][] SIM_CARRIER_ID = {{1435,1436},{1187},{1492},{1653},{788},{1994},{1096}};
    public final static String[] CHINESE_NAME = {"中国", "美国", "英国", "菲律宾", "印度尼西亚", "越南", "泰国"};
    public final static String[] ENGLISH_NAME = {"ChiaNa", "America", "Britain", "Philippines", "Indonesia", "Viet Nam", "Thailand"};
    public final static String[] COUNTRY_ABBR = {"CN", "US", "GB", "PH", "ID", "VN", "TH"};

    public final static String[] TIME_ZONE = {"Asia/Shanghai", "America/Los_Angeles", "Europe/London", "Asia/Manila", "Asia/Jakarta", "Asia/Ho_Chi_Minh", "Asia/Bangkok"};
    private final static HashMap<String, Country> countries = new HashMap<>();

    static {
        int len = MCC.length;
        for (int i = 0; i < len; i++) {
            Country country = new Country();
            country.setMcc(MCC[i]);
            country.setMnc(MNC[i]);
            country.setCountryCode(COUNTRY_CODE[i]);
            country.setChineseName(CHINESE_NAME[i]);
            country.setEnglishName(ENGLISH_NAME[i]);
            country.setCountryAbbr(COUNTRY_ABBR[i]);
            country.setPhonePre(PHONE_PRE[i]);
            country.setPhoneNoCount(PHONE_NO_COUNT[i]);
            country.setCountryAbbr(COUNTRY_ABBR[i]);
            country.setTimeZone(TIME_ZONE[i]);
            country.setSimCarrierId(SIM_CARRIER_ID[i]);
            countries.put(COUNTRY_CODE[i], country);
            countries.put(TIME_ZONE[i], country);
        }
    }


    public static Country getCountryByCode(String countryCode) {
        return countries.get(countryCode);
    }

    public static Country getCountryByTimeZone(String timezone) {
        return countries.get(timezone);
    }

}
