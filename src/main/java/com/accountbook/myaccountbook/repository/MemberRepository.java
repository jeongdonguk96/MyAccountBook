package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
    // 아이디 중복 조회
    Member findByLoginId(String loginId);

    // 전체 사용자의 id 조회
    @Query(value = "SELECT m.mid FROM Member m")
    List<Integer> findAllMemberMid();

    // id로 전체 사용자의 id 조회
    List<Integer> findAllByMid(int mid);
}
