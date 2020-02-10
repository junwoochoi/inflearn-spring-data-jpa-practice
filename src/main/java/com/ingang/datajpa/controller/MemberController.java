package com.ingang.datajpa.controller;

import com.ingang.datajpa.dto.MemberDto;
import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void setup() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("memberA", i));
        }
    }

    @GetMapping("/members/{id}")
    public String findById(@PathVariable("id") Long id) {
        return memberRepository.findById(id).get().getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findById(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> findMembersPaging(@PageableDefault(size=5, sort = "age", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }
}
