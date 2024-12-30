package com.blazemhan.usersmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final SecretKey Key;

    private static final long expirationTime = 1000 * 60 * 60 * 24;

    public JWTUtils() {
        String secretString = "ed5c5ff39bf25219b301904c2f43e8e6b99686916d0e" +
        "1bd067645ee60be5724d";
        byte[] keyBytes = Base64.getDecoder().
                decode(secretString.getBytes(StandardCharsets.UTF_8));

        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }


        public String generateToken(UserDetails userDetails) {
            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(Key)
                    .compact();
        }

    public String generateRefereshToken(HashMap<String, Object> claims,UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Key)
                .compact();
    }


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){

        return claimsTFunction.apply(Jwts.parser().verifyWith(Key)
                .build().parseSignedClaims(token).getPayload());

    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(Key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }






}
