package com.nft.app.util;

import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
@Slf4j
public class ChecksumUtil {
 
  /**
   * Method to get file checksum
   * @param bytes    input byte array
   * @return checksum string
   */
 
  public static String getCheckSum(byte[] bytes) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(bytes);
      byte[] digest = md.digest();
      StringBuilder checksum = new StringBuilder();
      for (byte b : digest) {
        checksum.append(String.format("%02x", b & 0xff));
      }
      return checksum.toString();
    } catch (NoSuchAlgorithmException e) {
      log.error("Exception while getCheckSum ",e);
      throw new NftException(ErrorCode.GENERIC_EXCEPTION);
    }
  }
 
}
