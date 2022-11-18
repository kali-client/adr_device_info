package com.java.se;

import com.java.se.utils.Cmd;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(Locale.getDefault().getCountry().toLowerCase());
    }
}
