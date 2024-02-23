package com.api_board.restapiboard.config.token;

import com.api_board.restapiboard.handler.JwtHandler;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final Long maxAgeSeconds;

    private static final String SEP = ",";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String MEMBER_ID = "MEMBER_ID";

    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.getMemberId(), ROLE_TYPES, privateClaims.getRoleTypes().stream().collect(joining(SEP))).toString(),
                maxAgeSeconds
        );

    }

    public Optional<PrivateClaims> parse(String token) { // 2
        return jwtHandler.parse(key, token).map(this::convert);
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims { // 3
        private String memberId;
        private List<String> roleTypes;
    }
}
