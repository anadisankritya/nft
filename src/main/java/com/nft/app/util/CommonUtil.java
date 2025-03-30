package com.nft.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtil {

    public static double calculateProfit(double amount, double profitPercentage) {
        return amount * (profitPercentage / 100);
    }
}
