package com.nft.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {

    public static long convertDaysToMilliseconds(Integer noOfDays) {
        return noOfDays * 24L * 60 * 60 * 1000;
    }
}
