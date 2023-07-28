package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.dto.accounthistory.AccountHistoryDto;
import com.accountbook.myaccountbook.persistence.AccountHistory;
import com.accountbook.myaccountbook.repository.AccountHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountHistoryService {

    private final AccountHistoryRepository accountHistoryRepository;


    /**
     * 월별 지출 목록 Dto 조회
     * @param mid 사용자의 id
     * @return accountHistory List
     */
    public List<AccountHistoryDto> findAllAccountHistoryToDto(int mid) {
        List<AccountHistory> findAccountHistory = accountHistoryRepository.findAllByMid(mid);

        return findAccountHistory.stream()
                .map(AccountHistoryDto::convertToDto)
                .collect(Collectors.toList());
    }
}
