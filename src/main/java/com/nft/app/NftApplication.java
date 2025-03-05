package com.nft.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class NftApplication {

  public static void main(String[] args) {
    SpringApplication.run(NftApplication.class, args);
  }

}
