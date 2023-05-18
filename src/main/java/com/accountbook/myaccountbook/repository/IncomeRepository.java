package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    // 수입 월별 조회
    List<Income> findAllByMonthAndMemberMid(String month, int mid);

}
