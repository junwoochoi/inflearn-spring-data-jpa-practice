package com.ingang.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void testEntity() {
        final Team team1 = new Team("team1");
        final Team team2 = new Team("team2");

        entityManager.persist(team1);
        entityManager.persist(team2);

        final Member member1 = new Member("member1", 18, team1);
        final Member member2 = new Member("member2", 18, team1);
        final Member member3 = new Member("member3", 18, team2);
        final Member member4 = new Member("member4", 18, team2);

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        entityManager.flush();
        entityManager.clear();

        final List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();


        for (Member member : members) {
            System.out.println(member);
            System.out.println(member.getTeam());
        }
    }

}