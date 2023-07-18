package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.persistence.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
    // 아이디 중복 조회
    Optional<Member> findByUsername(String username);

    // 전체 사용자의 id 조회
    @Query(value = "SELECT m.mid FROM Member m")
    List<Integer> findAllMemberMid();

    // id로 전체 사용자의 id 조회
    List<Integer> findAllByMid(int mid);
}
