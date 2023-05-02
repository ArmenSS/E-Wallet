package com.wallet.userservice.security;

import com.wallet.userservice.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDto user, Long ex) {
        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, user, ex);
    }

    private String doGenerateToken(Map<String, Object> claims, UserDto subject, Long ex) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate, ex);

        claims.put("user", subject);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject.getEmail())
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String refreshToken(String token, Long ex) {
        if (isTokenExpired(token)) {
            return null;
        }
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate, ex);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token, String email) {

        final String username = getUsernameFromToken(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    private Date calculateExpirationDate(Date createdDate, Long ex) {
        return new Date(createdDate.getTime() + ex * 1000);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
