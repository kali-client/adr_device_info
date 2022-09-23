package com.android.device.ext;
import com.android.device.ext.utils.SimUtils;
class IdsGenerator {

    private static final String FIX_PRE = "89";

    public static String generateICCID(Country country) {
        StringBuilder iccId = new StringBuilder();
        iccId.append(FIX_PRE);
        iccId.append(country.getCountryCode());
        iccId.append(country.getMnc());
        iccId.append(SimUtils.generateRandomNum(20 - iccId.length()));
        return iccId.toString();
    }


    public static String generateIMSI(Country country) {
        StringBuilder imsi = new StringBuilder();
        imsi.append(country.getSimOperator());
        imsi.append(SimUtils.generateRandomNum(15 - imsi.length()));
        return imsi.toString();
    }



    public static String genPhoneNumber(Country country) {
        StringBuilder phoneNo = new StringBuilder();
        phoneNo.append(country.getPhonePre());
        phoneNo.append(SimUtils.generateRandomNum(country.getPhoneNoCount() - phoneNo.length()));
        return phoneNo.toString();
    }


//    public static void main(String[] args) {
//        ChinaOperator chinaMobile = OperatorFactory.genChinaOperator();
//        System.out.println(chinaMobile.getPhoneNo());
//        System.out.println(chinaMobile.getIMSI());
//        System.out.println(chinaMobile.getICCID());
//        System.out.println(chinaMobile.getSimOperator());
//    }
}
