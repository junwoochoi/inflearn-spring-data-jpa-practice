package com.ingang.datajpa;

import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class QuerydslApplicationTest {
    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Member member = new Member("1member1");
        em.persist(member);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = new QMember("m");

        final Member member1 = query
                .selectFrom(qMember)
                .where(qMember.username.eq("1member1"))
                .fetchOne();

        assertThat(member1).isNotNull();
        assertThat(member1.getUsername()).isEqualTo("1member1");
        assertThat(member1).isEqualTo(member);
    }
}
