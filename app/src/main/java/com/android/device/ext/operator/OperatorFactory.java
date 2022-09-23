package com.android.device.ext.operator;

import java.util.Random;

public class OperatorFactory {

    private OperatorFactory(){}

    public static ChinaOperator genChinaOperator(){

        final int randomIndex = new Random().nextInt(3);
        switch(randomIndex) {
            case 1:
                return new ChinaMobile();
            case 2:
                return new ChinaUnicom();
            default:
                return new ChinaTelecom();
        }


    }
}
