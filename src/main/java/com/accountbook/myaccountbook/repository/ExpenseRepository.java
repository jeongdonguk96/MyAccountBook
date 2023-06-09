package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseByYearDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // 지출 월별 조회
    List<Expense> findAllByMonthAndMemberMid(String month, int mid);

    // 카테고리별 지출 목록 조회
    List<ExpenseCategoryDto> findAllExpenseCategoryByMonthAndMemberMid(String month, int mid);

    // 월별 지출 내역 조회(
    List<ExpenseByYearDto> findAllByYearAndMemberMid(String year, int mid);

    // 올해의 지출 목록을 월별로 파싱
    List<ExpenseByYearDto> findAllByMonth(String month);
}
