package com.nft.app.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class OtpGenerator {
  private static final SecureRandom random = new SecureRandom();

  public static String generateSixDigitOtp() {
    int number = 100000 + random.nextInt(900000);
    return Integer.toString(number);
  }

}
