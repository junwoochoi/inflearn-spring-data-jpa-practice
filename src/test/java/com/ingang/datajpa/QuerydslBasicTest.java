package com.ingang.datajpa;

import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.QMember;
import com.ingang.datajpa.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.ingang.datajpa.entity.QMember.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    @BeforeEach
    void setup() {
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        final Member member1 = new Member("member1", 19, teamA);
        final Member member2 = new Member("member2", 19, teamB);

        em.persist(member1);
        em.persist(member2);

        //영속성 컨텍스트 초기화
        em.flush();
        em.clear();
    }

    @Test
    void startJPQL() {
        //member1 찾기
        final Member findByJPQL = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findByJPQL.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQueryDsl() {
        final JPAQueryFactory query = new JPAQueryFactory(em);


        final Member member1 = query
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(member1.getUsername()).isEqualTo("member1");

    }

}
