package com.api_board.restapiboard.dto;

import com.api_board.restapiboard.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String email;
    private String username;
    private String nickname;

    public static MemberDTO memberDTO(Member member) {
        return new MemberDTO(member.getId(), member.getEmail(), member.getUsername(), member.getNickname());
    }

    public static MemberDTO empty() {
        return new MemberDTO(null, "", "", "");
}
