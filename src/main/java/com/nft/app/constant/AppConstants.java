package com.nft.app.constant;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AppConstants {

  public static final String EMAIL = "EMAIL";
  public static final String MOBILE = "MOBILE";
  public static final String MIN_DEPOSIT_AMT = "55";

  public static final List<String> NFT_UPLOAD_TABLE_COLUMN = List.of(
      "Name", "Owner Name", "Allowed Level", "Investment Type",
      "Buy Price", "Profit", "Block Period", "Category", "Status", "Image"
  );

  public static final List<String> INVESTMENT_TYPE_TABLE_COLUMN = List.of(
      "Name", "Level Allowed"
  );

}
