package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // 사용자별 지출 조회
    List<Expense> findAllByAccountBook_Member_Mid(int mid);
}
