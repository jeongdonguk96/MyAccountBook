package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.persistence.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    // 수입 월별 조회
    List<Income> findAllByMonthAndMemberMid(String month, int mid);

    // 지난달 수입 금액 조회
    @Query(value = "SELECT i.incomeMoney " +
                    "FROM Income i " +
                    "WHERE i.member.mid = :mid " +
                    "AND i.month = :month")
    List<Integer> findAllIncomeMoney(@Param("mid") int mid, @Param("month") String month);
}
