package com.android.device.ext.operator;


import com.android.device.ext.utils.SimUtils;

import java.util.Random;

/**
 * 中国电信
 * 898603YYXMHHHXXXXXXP
 * 89：国际编号
 * 86：国家编号，86：中国
 * 03：运营商编号，03：中国电信
 * YY：编制ICCID时的年号（取后两位），如‘09’代表2009年
 * M ：保留位，固定为0
 * HHH：本地网地区代码，位数不够前补零。如上海区号为021，则HHH为'021’；长沙区号为0731，则HHH为‘731’，测试卡代码为001
 * X...X：7位流水号，建议前2位作为批次号
 * P:校验位
 */
public class ChinaTelecom extends ChinaOperator {
    private static final String MNC = "03";
    private static final String[] NO_SEGMENT = {"133", "153", "180", "181", "189", "199"};
    private static String[] YY = {"17", "18", "19", "20", "21"};
    private static String DEFAULT_BIT = "0";
    private static String[] PROVINCE_CODE = {
            "010", "022", "021", "023", "311", "351", "371", "024", "431", "451",
            "471", "531", "551", "571", "591", "731", "791", "771", "851", "871",
            "891", "931", "951", "971", "991", "027", "028", "029", "411", "335",
            "315", "025"};


    public ChinaTelecom() {
        this.randomIndex = new Random().nextInt(NO_SEGMENT.length);
    }

    @Override
    public String getPhoneNo() {
        if (phoneNo == null) {
            this.phoneNo = NO_SEGMENT[randomIndex] + SimUtils.generateRandomNum(11 - NO_SEGMENT[randomIndex].length());
        }
        return phoneNo;
    }

    @Override
    public String getIMSI() {
        return MCC + MNC + SimUtils.generateRandomNum(15 - (MCC + MNC).length());
    }

    @Override
    public String getICCID() {
        StringBuilder iccid = new StringBuilder();
        iccid.append(FIX_PRE);
        iccid.append(COUNTRY_CODE);
        iccid.append(MNC);
        iccid.append(YY[new Random().nextInt(YY.length)]);
        iccid.append(DEFAULT_BIT);
        iccid.append(PROVINCE_CODE[new Random().nextInt(PROVINCE_CODE.length)]);
        iccid.append(SimUtils.generateRandomNum(20 - 1 - iccid.length()));
        iccid.append(SimUtils.getICCIDLastBit(iccid.toString()));
        return iccid.toString();
    }

    @Override
    public String getMNC() {
        return MNC;
    }
}
