package com.nft.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class JwtUtil {

  public static String generateToken(String email) {
    Algorithm algorithm = getJwtAlgo();
    long EXPIRATION_TIME = 1000 * 60 * 60 * 10;
    return JWT.create()
        .withSubject(email)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(algorithm);
  }

  public static boolean validateToken(String token, String email) {
    Algorithm algorithm = getJwtAlgo();
    JWTVerifier verifier = JWT.require(algorithm)
        .withSubject(email)
        .build();
    verifier.verify(token);
    return true;
  }

  public static String extractEmail(String token) {
    Algorithm algorithm = getJwtAlgo();
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getSubject();
  }

  private static Algorithm getJwtAlgo() {
    String SECRET_KEY = "mySuperDuperSuperSeBhiUparSecretKey";
    return Algorithm.HMAC256(SECRET_KEY);
  }
}
