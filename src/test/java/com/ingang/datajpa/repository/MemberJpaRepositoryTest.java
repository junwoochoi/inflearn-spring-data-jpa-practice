package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testBasicCRUD() {
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        final Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        final Member findMember2 = memberJpaRepository.findById(member2.getId()).get();


        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        final long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        final long count2 = memberJpaRepository.count();
        assertThat(count2).isEqualTo(0);
    }

    @Test
    void paging() {
        //given
        memberJpaRepository.save(new Member("member1", 19));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 19));
        memberJpaRepository.save(new Member("member4", 19));
        memberJpaRepository.save(new Member("member5", 19));
        memberJpaRepository.save(new Member("member6", 19));
        memberJpaRepository.save(new Member("member7", 19));

        //when
        List<Member> byPage = memberJpaRepository.findByPage(19, 1, 3);
        long totalCount = memberJpaRepository.totalCount(19);

        //then
        assertThat(byPage.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(7L);
    }

    @Test
    void testBulkUpdate() {
        //given
        for (int i = 0; i < 10; i++) {
            memberJpaRepository.save(new Member("member"+i, 16+i));
        }

        //when
        final int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(6);
    }
}