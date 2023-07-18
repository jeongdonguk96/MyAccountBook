package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.persistence.AccountHistory;
import com.accountbook.myaccountbook.repository.AccountHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountHistoryService {

    private final AccountHistoryRepository accountHistoryRepository;


    /**
     * 월별 지출 목록 조회
     * @param mid 사용자의 id
     * @return accountHistory List
     */
    public List<AccountHistory> findAllAccountHistory(int mid) {
        return accountHistoryRepository.findAllByMid(mid);
    }

}
