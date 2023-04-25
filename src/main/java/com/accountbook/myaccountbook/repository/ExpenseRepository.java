package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findAllByMemberMid(int mid);
}
