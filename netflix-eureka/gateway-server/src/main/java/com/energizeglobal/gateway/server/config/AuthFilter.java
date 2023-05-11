package com.energizeglobal.gateway.server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  @Value("${token.secret}")
  private String secret;

  public AuthFilter() {
    super(Config.class);
  }

  private String isAuthorized(String authorizationHeader) throws RuntimeException {
    if (authorizationHeader.startsWith("Bearer")) {
      String[] authDetails = authorizationHeader.split(" ");
      return authDetails[1];
    } else {
      log.error(this.getClass().getName(), RuntimeException.class);
      throw new RuntimeException("Authorization error");
    }
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err) {
    ServerHttpResponse response = exchange.getResponse();
    response.getHeaders().add("Message", err);
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return response.setComplete();
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (!request.getHeaders().containsKey("Authorization")) {
        return this.onError(exchange, "No Authorization header");
      }
      String authorizationHeader =
          Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);
      try {
        String token = this.isAuthorized(authorizationHeader);
        isTokenExpired(token);
      } catch (RuntimeException e) {
        return this.onError(exchange, "Token is expired");
      }
      ServerHttpRequest modifiedRequest =
          exchange.getRequest().mutate().header(secret, RandomStringUtils.random(10)).build();
      return chain.filter(exchange.mutate().request(modifiedRequest).build());
    };
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
      throws SignatureException {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) throws SignatureException {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public static class Config {

  }
}
