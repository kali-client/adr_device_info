package com.android.device.ext;

import com.android.device.ext.operator.OperatorFactory;
import com.android.device.ext.utils.FileUtils;

import java.util.Random;
import java.util.TimeZone;

class CountryFactory {

    public final static String[] COUNTRY_CODE = {"86", "01", "44", "63", "63", "84", "66"};
    public final static String[] MCC = {"460", "310", "234", "515", "510", "452", "520"};

    public final static String[][] MNC = {{"00", "01", "03"}, {"410"}, {"10"}, {"02"}, {"11"}, {"05"}, {"00"}};
    public final static String[][] PHONE_PRE = {{"134/135/136/137/138/139/150/151/152/188", "131/132/155/156/185/186", "133/149/153/180/181/189/199/193"}, {"206", "360", "802", "213", "609"}, {"07410", "07529", "07536"}, {"91", "92", "93", "94"}, {"08110", "08118", "08169"}, {"09"}, {"08"}};
    public final static int[][] SIM_CARRIER_ID = {{1435,1436,1437}, {1187}, {1492}, {1653}, {788}, {1994}, {1096}};

    public final static int[] PHONE_NO_COUNT = {11, 10, 11, 10, 10, 10, 10};
    public final static String[] CHINESE_NAME = {"中国", "美国", "英国", "菲律宾", "印度尼西亚", "越南", "泰国"};
    public final static String[] ENGLISH_NAME = {"ChiaNa", "America", "Britain", "Philippines", "Indonesia", "Viet Nam", "Thailand"};
    public final static String[] COUNTRY_ABBR = {"CN", "US", "GB", "PH", "ID", "VN", "TH"};

    public final static String[] TIME_ZONE = {"Asia/Shanghai", "America/Los_Angeles", "Europe/London", "Asia/Manila", "Asia/Jakarta", "Asia/Ho_Chi_Minh", "Asia/Bangkok"};

    private static Country country = null;

    private static final String FILE_PATH = "/data/data/com.android.phone/files/u_country.bin";


    private static Country generateCountry() {
        int len = TIME_ZONE.length;
        for (int i = 0; i < len; i++) {
            if (TIME_ZONE[i].equalsIgnoreCase(TimeZone.getDefault().getID())) {
                Country country = new Country();
                country.setMcc(MCC[i]);
                country.setMnc(MNC[i]);
                country.setCountryCode(COUNTRY_CODE[i]);
                country.setChineseName(CHINESE_NAME[i]);
                country.setEnglishName(ENGLISH_NAME[i]);
                country.setCountryAbbr(COUNTRY_ABBR[i]);
                country.setPhoneNoCount(PHONE_NO_COUNT[i]);
                country.setCountryAbbr(COUNTRY_ABBR[i]);
                country.setTimeZone(TIME_ZONE[i]);
                country.setSimCarrierId(SIM_CARRIER_ID[i]);
                country.setPhonePre(PHONE_PRE[i]);
                if("Asia/Shanghai".equalsIgnoreCase(TimeZone.getDefault().getID()) || "Asia/Urumqi".equalsIgnoreCase(TimeZone.getDefault().getID())){//china
                    int index = new Random().nextInt(PHONE_PRE[i].length);
                    country.setMncRandomIndex(index);
                    country.setPrePhoneRandomIndex(index);
                    country.setSimCarrierIdRandomIndex(index);
                    country.setChinaOperator(OperatorFactory.genChinaOperator());
                }else{//otherwise
                    country.setPrePhoneRandomIndex(new Random().nextInt(PHONE_PRE[i].length));
                    country.setMncRandomIndex(new Random().nextInt(MNC[i].length));
                    country.setSimCarrierIdRandomIndex(new Random().nextInt(SIM_CARRIER_ID[i].length));
                    country.setIccid(IdsGenerator.generateICCID(country));
                    country.setImsi(IdsGenerator.generateIMSI(country));
                    country.setPhoneNumber(IdsGenerator.genPhoneNumber(country));
                }
                return country;
            }
        }

        return null;
    }

    public final static Country getCountry() {
        if (country == null) {
            synchronized (CountryFactory.class) {
                if (country == null) {
                    try {
                        Object o = FileUtils.readObject(FILE_PATH);
                        if (o == null) {
                            country = generateCountry();
                            FileUtils.writeObject(country, FILE_PATH);
                        } else {
                            country = (Country) o;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return country;
    }
}
