package com.prismforge.boosters.util;

public class TimeUtil {
    public static String formatSeconds(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return mins + "m " + secs + "s";
    }
}
