package com.api_board.restapiboard.service;

import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.dto.MemberDTO;
import com.api_board.restapiboard.exception.MemberNotFoundException;
import com.api_board.restapiboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDTO read(Long id) {
        return MemberDTO.memberDTO(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

//    @Transactional
//    public void delete(Long id) {
//        if(notExistsMember(id)) throw new MemberNotFoundException();
//        memberRepository.deleteById(id);
//    }

    @Transactional
    @PreAuthorize("@memberGuard.check(#id)")
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }
}
