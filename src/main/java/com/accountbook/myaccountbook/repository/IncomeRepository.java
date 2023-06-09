package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.dto.accountbook.IncomeByYearDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

    // 수입 월별 조회
    List<Income> findAllByMonthAndMemberMid(String month, int mid);

    // 월별 지출 내역 조회(
    List<IncomeByYearDto> findAllByYearAndMemberMid(String year, int mid);

    // 올해의 지출 목록을 월별로 파싱
    List<IncomeByYearDto> findAllByMonth(String month);
}
