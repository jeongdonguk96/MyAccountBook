package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
    // 아이디 중복 조회
    Member findByLoginId(String loginId);
}
