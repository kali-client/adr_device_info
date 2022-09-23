package com.android.device.ext.operator;

import com.android.device.ext.utils.SimUtils;

import java.util.Random;

/**
 * 中国联通
 * <p>
 * 898601YY8SSXXXXXXXXP
 * 89： 国际编号
 * 86： 国家编号，86：中国
 * 01： 运营商编号，01：中国联通
 * YY： 编制ICCID时年号的后两位
 * 8： 中国联通ICCID默认此为为8
 * SS：2位省份编码
 * 10内蒙古 11北京 13天津 17山东 18河北 19山西 30安徽 31上海 34江苏
 * 36浙江 38福建 50海南 51广东 59广西 70青海 71湖北 74湖南 75江西
 * 76河南 79西藏 81四川 83重庆 84陕西 85贵州 86云南 87甘肃 88宁夏
 * 89新疆 90吉林 91辽宁 97黑龙江
 * X…X： 卡商生产的顺序编码
 * P： 校验位
 */
public class ChinaUnicom extends ChinaOperator{

    protected  String MNC = "01";
    private static final String[] NO_SEGMENT = {"130", "131", "132", "145","155", "156", "166", "171", "175", "176", "185", "186", "196"};
    private static String[] YY = {"17", "18", "19", "20", "21"};

    private static String DEFAULT_BIT = "8";
    private static String[] PROVINCE_CODE = {
            "10", "11", "13", "17", "18", "19", "30", "31", "34", "36",
            "38", "50", "51", "59", "70", "16", "71", "74", "75", "76",
            "79", "81", "83", "84", "85", "86", "87", "88", "89", "90",
            "91","97"};



    public ChinaUnicom(){
        this.randomIndex = new Random().nextInt(NO_SEGMENT.length);
    }

    @Override
    public String getPhoneNo() {
        if(phoneNo == null){
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
        StringBuilder iccid  = new StringBuilder();
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
