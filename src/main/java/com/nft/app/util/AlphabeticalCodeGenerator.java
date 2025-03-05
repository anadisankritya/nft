package com.nft.app.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class AlphabeticalCodeGenerator {
  private static final SecureRandom random = new SecureRandom();
  private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static String generateSixLetterCode() {
    StringBuilder code = new StringBuilder(6);
    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(LETTERS.length());
      code.append(LETTERS.charAt(index));
    }
    return code.toString();
  }
}
