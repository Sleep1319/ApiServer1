package com.api_board.restapiboard.config.security.guard;

import com.api_board.restapiboard.domain.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {

    private final AuthHelper authHelper;

    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthHelper.extractMemberId());
    }
}
