package com.android.device.ext.operator;

import com.android.device.ext.utils.SimUtils;

import java.util.Random;

/**
 * 中国移动
 * 898600MFSSYYGXXXXXXP
 * 89： 国际编号
 * 86： 国家编号，86：中国
 * 00： 运营商编号，00：中国移动
 * M： 号段，对应用户号码前3位
 * 0：159 1：158 2：150
 * 3：151 4-9：134-139 A：157
 * B：188 C：152 D：147 E：187
 * F： 用户号码第4位
 * SS： 省编号
 * 北京01 天津02 河北03 山西04 内蒙古05 辽宁06 吉林07
 * 黑龙江08 上海09 江苏10 浙江11 安徽12 福建13 江西14
 * 山东15 河南16 湖北17 湖南18 广东19 广西20 海南21
 * 四川22 贵州23 云南24 西藏25 陕西26 甘肃27 青海28
 * 宁夏29 新疆30 重庆31
 * YY： 编制ICCID时年号的后两位
 * G： SIM卡供应商代码
 * 0：雅斯拓 1：GEMPLUS 2：武汉天喻 3：江西捷德 4：珠海东信和平
 * 5：大唐微电子通 6：航天九州通 7：北京握奇 8：东方英卡
 * 9：北京华虹 A ：上海柯斯
 * X…X： 用户识别码
 * P： 校验位
 */
public class ChinaMobile extends  ChinaOperator{

    private static final String MNC = "00";
    private static final String[] NO_SEGMENT = {"159", "158", "150", "151", "134", "135", "136", "137", "138", "139", "157", "188", "152", "147", "187"};
//    private static String[] F = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[] PROVINCE_CODE = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31"};
    private static String[] YY = {"17", "18", "19", "20", "21"};
    private static String[] G = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A"};


    public ChinaMobile(){
        this.randomIndex = new Random().nextInt(10);
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
         iccid.append(randomIndex);
         iccid.append(getPhoneNo().charAt(3));
         iccid.append(PROVINCE_CODE[new Random().nextInt(PROVINCE_CODE.length)]);
         iccid.append(YY[new Random().nextInt(YY.length)]);
         iccid.append(G[new Random().nextInt(G.length)]);
         iccid.append(SimUtils.generateRandomNum(20 - 1 - iccid.length()));
         iccid.append(SimUtils.getICCIDLastBit(iccid.toString()));
        return iccid.toString();
    }

    @Override
    public String getMNC() {
        return MNC;
    }
}
