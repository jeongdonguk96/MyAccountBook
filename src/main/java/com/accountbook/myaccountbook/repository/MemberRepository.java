package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByLoginId(String loginId);
}
