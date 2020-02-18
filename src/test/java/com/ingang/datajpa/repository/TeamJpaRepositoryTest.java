package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Address;
import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AddressRepository addressRepository;
    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void setup() {
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        for (int i = 0; i < 13; i++) {
            Address address = new Address(UUID.randomUUID().toString().substring(0, 6));
            addressRepository.save(address);
            Member member = new Member("member" + i, 10, team1, address);
            memberRepository.save(member);
        }

        em.flush();
        em.clear();
    }


    @Test
    @Transactional
    void testNPlusOneProblem() {
        final Team team1 = teamRepository.findByName("team1");

        System.out.println("team1 : "+team1.getName());

        for (Member member : team1.getMembers()) {
            System.out.println(member.getUsername() +"'s zipcode : "+ member.getAddress().getZipcode());
        }
    }

    @Test
    @Transactional
    void testWithJoinFetch() {
        final Team team1 = teamRepository.findByNameWithMemberAndAddress("team1");

        System.out.println("team1 : "+team1.getName());

        for (Member member : team1.getMembers()) {
            System.out.println(member.getUsername() +"'s zipcode : "+ member.getAddress().getZipcode());
        }
    }

    @Test
    @Transactional
    void testWithBatchSize() {
        final Team team1 = teamRepository.findByNameBatchSize("team1");

        System.out.println("team1 : "+team1.getName());

        for (Member member : team1.getMembers()) {
            System.out.println(member.getUsername() +"'s zipcode : "+ member.getAddress().getZipcode());
        }
    }

}