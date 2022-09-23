package com.android.device.ext.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtils {

    public static void writeObject(Object o, String filePath) {
        ObjectOutputStream outStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                 file.createNewFile();
            }
            outStream = new ObjectOutputStream(new FileOutputStream(file));
            outStream.writeObject(o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static Object readObject(String filePath) {
        ObjectInputStream inStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
               return null;
            }
            inStream = new ObjectInputStream(new FileInputStream(filePath));
            return inStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
