package com.android.device.ext.utils;

import java.util.Random;
import java.util.UUID;

public class SimUtils {


    public static String getICCIDLastBit(String iccid) {
        char[] imeiChar = iccid.toCharArray();
        int resultInt = 0;
        for (int i = 0; i < imeiChar.length; i++) {
            int a = Integer.parseInt(String.valueOf(imeiChar[i])) * 2;
            int count = a / 10 + a % 10;     //将十位数 与个位数 相加
            resultInt = resultInt + count;
            i++;
            if (i < 19) {
                int b = Integer.parseInt(String.valueOf(imeiChar[i]));
                resultInt = resultInt + b;
            }
        }
        resultInt = resultInt * 9;
        return String.valueOf(resultInt % 10);

    }


    public static String generateRandomNum(int count) {
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

}
