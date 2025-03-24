package com.nft.app.util;

import java.util.Random;

public class InvestmentIdGenerator {
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ$%&*@#";
    private static final Random random = new Random();
    
    public static String generateInvestmentId(String investmentType) {
        // Generate random number between 100 and 10000
        int randomNumber = 100 + random.nextInt(9901);
        
        // Generate random characters
        String randomChars = generateRandomChars(4); // 4 random characters
        
        // Create different combination patterns
        String[] patterns = {
            "%s_%s_%d",    // ABC_STOCKS_1234
            "%d-%s-%s",     // 5678-CRYPTO-XYZ
            "%s%d%s",       // REALESTATE4567AB
            "%s-%s%d",      // BONDS-TRADE7890
            "%d%s%s"        // 3210GOLD_INVEST
        };
        
        // Select random pattern
        String pattern = patterns[random.nextInt(patterns.length)];
        
        return String.format(pattern, 
            randomChars, 
            investmentType.toUpperCase(), 
            randomNumber);
    }
    
    private static String generateRandomChars(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }
}