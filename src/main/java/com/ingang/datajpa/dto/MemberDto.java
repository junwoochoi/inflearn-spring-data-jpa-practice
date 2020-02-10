package com.ingang.datajpa.dto;

import com.ingang.datajpa.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;

    public MemberDto(Member member) {
        id = member.getId();
        username = member.getUsername();
        if (member.getTeam() != null) {
            teamname = member.getTeam().getName();
        }
    }
}
