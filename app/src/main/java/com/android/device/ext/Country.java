package com.android.device.ext;

import com.android.device.ext.operator.ChinaOperator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

class Country implements Serializable {

    private static final long serialVersionUID = 5195820088855252284L;

    private int prePhoneRandomIndex = -1;
    private int mncRandomIndex = -1;
    private int simCarrierIdRandomIndex = -1;

    private ChinaOperator chinaOperator;
    private String chineseName;//中文名
    private String englishName;//英文名

    private String countryAbbr;//英文缩写

    private String countryCode;//国际区号

    private String mcc;//移动国家码

    private String[] mnc;//移动网络码
    private int[] simCarrierId;

    private String timeZone;
    private String[] phonePre;

    private int phoneNoCount;


    private String imsi;//International Mobile Subscriber Identity
    private String iccid;//Integrate circuit card identity 集成电路卡识别码

    private String phoneNumber;

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
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

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }



    public void setSimCarrierId(int[] simCarrierId) {
        this.simCarrierId = simCarrierId;
    }

    public int getPrePhoneRandomIndex() {
        return prePhoneRandomIndex;
    }

    public void setPrePhoneRandomIndex(int prePhoneRandomIndex) {
        this.prePhoneRandomIndex = prePhoneRandomIndex;
    }

    public int getMncRandomIndex() {
        return mncRandomIndex;
    }

    public void setMncRandomIndex(int mncRandomIndex) {
        this.mncRandomIndex = mncRandomIndex;
    }

    public int getSimCarrierIdRandomIndex() {
        return simCarrierIdRandomIndex;
    }

    public void setSimCarrierIdRandomIndex(int simCarrierIdRandomIndex) {
        this.simCarrierIdRandomIndex = simCarrierIdRandomIndex;
    }

    public String getPhonePre() {
        String[] ss = phonePre[prePhoneRandomIndex].split("/");
        return ss[new Random().nextInt(ss.length)];
    }


    public int getSimCarrierId() {
        return simCarrierId[simCarrierIdRandomIndex];
    }

    public  String getMnc() {
        return mnc[mncRandomIndex];
    }

    public ChinaOperator getChinaOperator() {
        return chinaOperator;
    }

    public void setChinaOperator(ChinaOperator chinaOperator) {
        this.chinaOperator = chinaOperator;
    }



    public String getSimOperator() {
        if(chinaOperator != null){
            return chinaOperator.getSimOperator();
        }
        return mcc + getMnc();
    }

    public String getPhoneNumber() {
        if(chinaOperator != null){
            return chinaOperator.getPhoneNo();
        }
        return phoneNumber;
    }

    public String getImsi() {
        if(chinaOperator != null){
            return chinaOperator.getIMSI();
        }
        return imsi;
    }


    public String getIccid() {
        if(chinaOperator != null){
            return chinaOperator.getICCID();
        }
        return iccid;
    }



    @Override
    public String toString() {
        return "Country{" +
                "prePhoneRandomIndex=" + prePhoneRandomIndex +
                ", mncRandomIndex=" + mncRandomIndex +
                ", simCarrierIdRandomIndex=" + simCarrierIdRandomIndex +
                ", chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", countryAbbr='" + countryAbbr + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", mcc='" + mcc + '\'' +
                ", mnc=" + Arrays.toString(mnc) +
                ", simCarrierId=" + Arrays.toString(simCarrierId) +
                ", timeZone='" + timeZone + '\'' +
                ", phonePre=" + Arrays.toString(phonePre) +
                ", phoneNoCount=" + phoneNoCount +
                ", imsi='" + imsi + '\'' +
                ", iccid='" + iccid + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

