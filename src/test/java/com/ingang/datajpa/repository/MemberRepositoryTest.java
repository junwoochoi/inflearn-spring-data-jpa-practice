package com.ingang.datajpa.repository;

import com.ingang.datajpa.dto.MemberDto;
import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.Team;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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

    @Test
    void testDtoQuery() {
        Member member1 = new Member("AAA", 10);
        Team team = new Team("BBBBTEAM");
        memberRepository.save(member1);
        teamRepository.save(team);

        member1.changeTeam(team);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        assertThat(memberDtos.size()).isEqualTo(1);
        assertThat(memberDtos.get(0).getId()).isEqualTo(member1.getId());
        assertThat(memberDtos.get(0).getTeamname()).isEqualTo("BBBBTEAM");
        assertThat(memberDtos.get(0).getUsername()).isEqualTo("AAA");
    }

    @Test
    void testCollectionParameterBinding() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findByNames(Lists.list("AAA", "BBB"));

        assertThat(byNames.size()).isEqualTo(2);
        assertThat(byNames).contains(member1, member2);
    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 19));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 19));
        memberRepository.save(new Member("member4", 19));
        memberRepository.save(new Member("member5", 19));
        memberRepository.save(new Member("member6", 19));
        memberRepository.save(new Member("member7", 19));

        //when
        Page<Member> byPage = memberRepository.findByAge(19, PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username")));

        //then
        assertThat(byPage.getContent().size()).isEqualTo(3);
        assertThat(byPage.getTotalElements()).isEqualTo(7);
        assertThat(byPage.getTotalPages()).isEqualTo(3);

        List<Integer> content = byPage.map(Member::getAge).getContent();
        assertThat(content).containsOnly(19);
    }

    @Test
    void slicing() {
        //given
        memberRepository.save(new Member("member1", 18));
        memberRepository.save(new Member("member1", 19));
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member1", 22));
        memberRepository.save(new Member("member1", 23));
        memberRepository.save(new Member("member1", 25));
        memberRepository.save(new Member("member1", 26));

        //when
        Slice<Member> bySlice = memberRepository.findByUsername("member1", PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "age")));

        //then
        assertThat(bySlice.getContent().size()).isEqualTo(3);
    }

    @Test
    void testBulkUpdate() {
        //given
        for (int i = 0; i < 10; i++) {
            memberRepository.save(new Member("member" + i, 16 + i));
        }

        //when
        final int resultCount = memberRepository.bulkAgePlus(20);

        em.flush();
        em.clear();

        final Member member = memberRepository.findByNames(Lists.list("member5")).get(0);
        System.out.println(member);

        //then
        assertThat(resultCount).isEqualTo(6);
    }

    @Test
    void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");

        teamRepository.saveAll(Lists.list(teamA, teamB));

        final Member member1 = new Member("member1", 19, teamA);
        final Member member2 = new Member("member2", 19, teamB);

        memberRepository.saveAll(Lists.list(member1, member2));

        //영속성 컨텍스트 초기화
        em.flush();
        em.clear();

        // when
        // N+1 Problem
        final List<Member> all = memberRepository.findByUsernameLike("member");

        for (Member member : all) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member's team ClassName = "+ member.getTeam().getClass());
            System.out.println("team = " + member.getTeam().getName());
        }

    }

    @Test
    void callCustom() {
        final Member member1 = new Member("member1", 19);
        final Member member2 = new Member("member2", 19);

        memberRepository.saveAll(Lists.list(member1, member2));

        em.flush();
        em.clear();


        List<Member> memberCustom = memberRepository.findMemberCustom();


    }
}