package com.api_board.restapiboard.controller;

import com.api_board.restapiboard.dto.rsponse.Response;
import com.api_board.restapiboard.dto.sign.SignInRequest;
import com.api_board.restapiboard.dto.sign.SignUpRequest;
import com.api_board.restapiboard.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Sign Controller", tags = "Sign")
@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.")
    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Response signUp(@Valid @RequestBody SignUpRequest req) {
        signService.signUp(req);
        return Response.success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다")
    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest req) {
        return Response.success(signService.signIn(req));
    }

    /**
     * 파라미터에 설정된 @RequestHeadr는 required 디폴트 설정 값이 true
     * 그러기에 헤더 값이 전달되지 않았을 때 MissingRequestHeaderException 이다
     */

    @ApiOperation(value = "토큰 재발급", notes = "리프레시 토큰으로 새로운 엑세스 토큰을 발급 받는다.")
    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return Response.success(signService.refreshToken(refreshToken));
    }
}
/**
 * SignService는 회원가입과 로그인에 대한 로직
 *
 * TokenService는 우리의 서비스에서 사용할 토큰 발급과 검증
 *
 * JwtHandler는 jwt 발급과 검증
 */