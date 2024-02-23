package com.api_board.restapiboard.service;

import com.api_board.restapiboard.config.token.TokenHelper;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.RoleType;
import com.api_board.restapiboard.dto.sign.RefreshTokenResponse;
import com.api_board.restapiboard.dto.sign.SignInRequest;
import com.api_board.restapiboard.dto.sign.SignInResponse;
import com.api_board.restapiboard.dto.sign.SignUpRequest;
import com.api_board.restapiboard.exception.*;
import com.api_board.restapiboard.repository.MemberRepository;
import com.api_board.restapiboard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 24~25 동일한 타입의 빈을 두 개 주입 받는다
 * 주입 전략에 의해 타입이 동일한 여러 개의 빈에 대해서는
 * 빈의 이름과 맵핑되는 변수 명에 빈을 주입 받게 된다
 */
@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findWithRolesByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        TokenHelper.PrivateClaims privateClaims = createPrivateClaims(member);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        String refreshToken = refreshTokenHelper.createToken(privateClaims);
        return new SignInResponse(accessToken, refreshToken);
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail())) {
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        }
        if(memberRepository.existsByNickname(req.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
        }
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    public RefreshTokenResponse refreshToken(String refreshToken) {
        TokenHelper.PrivateClaims privateClaims = refreshTokenHelper.parse(refreshToken).orElseThrow(RefreshTokenFailureException::new);
        String accessToken = accessTokenHelper.createToken(privateClaims);
        return new RefreshTokenResponse(accessToken);
    }

    private TokenHelper.PrivateClaims createPrivateClaims(Member member) {
        return new TokenHelper.PrivateClaims(
                String.valueOf(member.getId()),
                member.getRoles().stream()
                        .map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .collect(Collectors.toList()));
    }

    //    private String createSubject(Member member) {
//        return String.valueOf(member.getId());
//    }

//    private void validateRefreshToken(String refreshToken) {
//        if(!refreshTokenHelper.validate(refreshToken)) {
//            throw new AuthenticationEntryPointException();
//        }
//    }
}
