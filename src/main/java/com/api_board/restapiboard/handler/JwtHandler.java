package com.api_board.restapiboard.handler;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtHandler {

    private String type = "Bearer ";

    //JWT토큰 생성 메소드
    public  String createToken(String encodedKey, String subject, Long maxAgeSeconds) {
        Date now = new Date();

        //JWT토큰 개별 설정
        return type + Jwts.builder()
                .setSubject(subject) //토큰 주제 설정 (대상이 누구인지 명시적으로 지정)
                .setIssuedAt(now) //토큰 발급 시간 설정
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))//토큰 만료 시간 설정, 0으로 설정시 발급과 동시에 만료
                .signWith(SignatureAlgorithm.HS256,encodedKey)//토큰 서명 설정
                .compact();//토큰을 문자열로 압축하여 변환
    }

    //토큰 주제를 추출하는 메소드
    public String extractSubject(String encodedKey, String token) {
        //주어진 토큰에서 주제를 추출하여 반환
        return parse(encodedKey, token).getBody().getSubject();
    }

    //토큰 유효성 검사
    public boolean validate(String encodedKey, String token) {
        try {
            //토큰을 파싱하여 유효성 검사하고, 예외가 발생하지 않으면 유효한 토큰으로 간주
            parse(encodedKey, token);
            return true;
        } catch (JwtException e) {
            //유효한 토큰이 아니면 유효하지 않음을 false로 반환
            return false;
        }
    }

    //토큰을 파싱하는 메소드
    public Optional<Claims> parse(String key, String token) { // 2
        try {
            return Optional.of(Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(unType(token)).getBody());
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
    //토큰 타입 제거 메소드
    private String unType(String token) {
        //토큰의 타입 정보를 제거하고 반환, 파싱 및 검증 과정에서 필요하지 않은 정보를 없애 주는 역할(제거를 안해도 실행에 문제는 없다)
        return token.substring(type.length());
    }
}
