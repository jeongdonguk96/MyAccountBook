package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import com.accountbook.myaccountbook.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer>, ExpenseRepositoryCustom {

    // 지출 월별 조회
    List<Expense> findAllByMonthAndMemberMid(String month, int mid);

    // 카테고리별 지출 목록 조회
    List<ExpenseCategoryDto> findAllExpenseCategoryByMonthAndMemberMid(String month, int mid);

    // 지난달 지출 금액 조회
//    @Query(value = "SELECT e.expenseMoney " +
//            "FROM Expense e " +
//            "WHERE e.member.mid = :mid " +
//            "AND e.month = :month")
//    List<Integer> findAllExpenseMoney(@Param("mid") int mid, @Param("month") String month);
}
