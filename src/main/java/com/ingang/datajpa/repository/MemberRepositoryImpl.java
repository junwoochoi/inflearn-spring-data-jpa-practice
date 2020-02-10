package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m order by m.username desc", Member.class)
                .getResultList();
    }

}
