package com.java.se;

import com.java.se.utils.Cmd;
import com.java.se.utils.MD5;

import java.io.File;

public class Main {
    public static void main(String[] args) {
//        System.out.println(MD5.getPMD5("com.pdk.demo"));
        String path = "/Users/qi.yan/Desktop/debug/xiaohongshu/lib/arm64-v8a/";
        File f = new File(path);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File ff = files[i];
            String cmd = "readelf -s " + path + ff.getName() + "| grep  getFingerPrint";
            String result = Cmd.exec(cmd);
            System.out.println(cmd);
            if (result != null && result.length() > 0) {
                System.out.println(result);
            }

        }

    }
}
