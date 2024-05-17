package com.accountbook.myaccountbook.dto.accounthistory;

import com.accountbook.myaccountbook.entity.AccountHistory;

import java.util.Date;


public record AccountHistoryDto(
    int ahid,
    int mid,
    int monthIncome,
    int monthExpense,
    int monthSum,
    String year,
    String month,
    Date regDate
) {

    public static AccountHistoryDto convertToDto(AccountHistory accountHistory) {
        return new AccountHistoryDto(
                accountHistory.getAhid(),
                accountHistory.getMid(),
                accountHistory.getMonthIncome(),
                accountHistory.getMonthExpense(),
                accountHistory.getMonthSum(),
                accountHistory.getYear(),
                accountHistory.getMonth(),
                accountHistory.getRegDate()
        );
    }

}
