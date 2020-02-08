package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() {
        final Member member = new Member("memberA", 3);
        final Member savedMember = memberRepository.save(member);

        final Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void testBasicCRUD() {
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        final Member findMember1 = memberRepository.findById(member1.getId()).get();
        final Member findMember2 = memberRepository.findById(member2.getId()).get();


        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        final long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        final long count2 = memberRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    void testQueryMethod() {
        Member member1 = new Member("username", 10);
        Member member2 = new Member("username", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("username", 15);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("username");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    void testQueryInAnnotation() {
        Member member1 = new Member("username", 10);
        Member member2 = new Member("username", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("username", 10);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("username");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    void testGetUsername() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains("AAA", "BBB");
    }
}