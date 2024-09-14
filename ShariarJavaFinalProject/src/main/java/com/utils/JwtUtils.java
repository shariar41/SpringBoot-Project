//package com.utils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//import java.util.Date;
//
//@Service
//public class JwtUtils {
//
//    private final String jwtSecret = "yourSecretKey"; // Use a strong secret key and keep it secure
//    private final int jwtExpirationMs = 86400000; // 24 hours
//
//    public String generateJwtToken(UserDetails userDetails) {
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public String getUserNameFromJwtToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//}
