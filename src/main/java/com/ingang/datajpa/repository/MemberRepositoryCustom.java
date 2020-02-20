package com.ingang.datajpa.repository;

import com.ingang.datajpa.dto.MemberSearchCondition;
import com.ingang.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();

    List<Member> search(MemberSearchCondition searchCondition);
    Page<Member> searchPageSimple(MemberSearchCondition searchCondition, Pageable pageable);
    Page<Member> searchPageComplex(MemberSearchCondition searchCondition, Pageable pageable);
}
