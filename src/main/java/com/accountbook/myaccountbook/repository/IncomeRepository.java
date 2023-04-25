package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findAllByMemberMid(int mid);

}
