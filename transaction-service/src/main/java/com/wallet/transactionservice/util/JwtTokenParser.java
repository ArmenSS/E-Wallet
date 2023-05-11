package com.wallet.transactionservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenParser {

  @Value("${jwt.secret}")
  private String secret;

  public Map<String, Object> parseToken(String token) {
    try {
      Claims body = Jwts.parser()
          .setSigningKey(secret)
          .parseClaimsJws(token.split(" ")[1])
          .getBody();
      Map<String, Object> credentials = new HashMap<>();
      credentials.put("user", body.get("user"));
      return credentials;
    } catch (JwtException | ClassCastException e) {
      return null;
    }
  }
}
