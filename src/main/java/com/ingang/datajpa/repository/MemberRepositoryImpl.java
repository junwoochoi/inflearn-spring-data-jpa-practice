package com.ingang.datajpa.repository;

import com.ingang.datajpa.dto.MemberSearchCondition;
import com.ingang.datajpa.entity.Member;
import com.ingang.datajpa.entity.QMember;
import com.ingang.datajpa.entity.QTeam;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.ingang.datajpa.entity.QMember.*;
import static com.ingang.datajpa.entity.QTeam.*;

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

    @Override
    public List<Member> search(MemberSearchCondition searchCondition) {
        return queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(searchCondition.getUsername()),
                        teamNameEq(searchCondition.getTeamName()),
                        ageGoe(searchCondition.getAgeGoe()),
                        ageLoe(searchCondition.getAgeLoe())
                        )
                .fetch();
    }

    @Override
    public Page<Member> searchPageSimple(MemberSearchCondition searchCondition, Pageable pageable) {
        final QueryResults<Member> memberQueryResults = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(searchCondition.getUsername()),
                        teamNameEq(searchCondition.getTeamName()),
                        ageGoe(searchCondition.getAgeGoe()),
                        ageLoe(searchCondition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(memberQueryResults.getResults(), pageable, memberQueryResults.getTotal());
    }

    @Override
    public Page<Member> searchPageComplex(MemberSearchCondition searchCondition, Pageable pageable) {
        queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(searchCondition.getUsername()),
                        teamNameEq(searchCondition.getTeamName()),
                        ageGoe(searchCondition.getAgeGoe()),
                        ageLoe(searchCondition.getAgeLoe())
                )
                .fetch();
        return null;
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : member.username.eq(username);
    }

    private BooleanExpression teamNameEq(String teamName) {
        return teamName == null ? null : member.team.name.eq(teamName);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }


}
