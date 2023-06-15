package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer> {

    // mid를 조건으로 AccountHistory 전체 조회
    List<AccountHistory> findAllByMid(int mid);
}
