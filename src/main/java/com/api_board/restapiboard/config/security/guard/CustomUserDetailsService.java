package com.api_board.restapiboard.config.security.guard;

import com.api_board.restapiboard.config.security.guard.CustomUserDetails;
import com.api_board.restapiboard.config.token.TokenHelper;
import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.Role;
import com.api_board.restapiboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final TokenHelper accessTokenHelper;

    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        return accessTokenHelper.parse(token)
                .map(this::convert)
                .orElse(null);
    }

    private CustomUserDetails convert(TokenHelper.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.getMemberId(),
                privateClaims.getRoleTypes().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
