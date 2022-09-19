package com.android.device.ext;

import java.util.Random;

public class Country {

    private String chineseName;//中文名
    private String englishName;//英文名

    private String countryAbbr;//英文缩写

    private String countryCode;//国际区号

    private String mcc;//移动国家码
    private String[] mnc;//移动网络码

    private String[] phonePre;

    private int phoneNoCount;

//    private String imsi;//International Mobile Subscriber Identity
//    private String iccid;//Integrate circuit card identity 集成电路卡识别码


    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc[0];
    }

    public void setMnc(String[] mnc) {
        this.mnc = mnc;
    }


    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryAbbr() {
        return countryAbbr;
    }

    public void setCountryAbbr(String countryAbbr) {
        this.countryAbbr = countryAbbr;
    }

    public String getPhonePre() {
        return phonePre[new Random().nextInt(phonePre.length)];
    }

    public void setPhonePre(String[] phonePre) {
        this.phonePre = phonePre;
    }

    public int getPhoneNoCount() {
        return phoneNoCount;
    }

    public void setPhoneNoCount(int phoneNoCount) {
        this.phoneNoCount = phoneNoCount;
    }

    public String getSimOperator() {
        return mcc + mnc[new Random().nextInt(mnc.length)];
    }

}

