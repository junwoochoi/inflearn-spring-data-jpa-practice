package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.ingang.datajpa.entity.QMember.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> findMemberCustom() {
        return queryFactory.
                selectFrom(member)
                .orderBy(member.username.desc())
                .fetch();
    }

}
