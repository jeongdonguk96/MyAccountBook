package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.persistence.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>, MemberRepositoryCustom {
    
    // 아이디 중복 조회
    Optional<Member> findByUsername(String username);
}
