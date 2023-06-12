package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer> {
}
