package com.accountbook.myaccountbook.dto.accounthistory;

import com.accountbook.myaccountbook.entity.AccountHistory;
import lombok.Data;

import java.util.Date;

@Data
public class AccountHistoryDto {
    private int ahid;
    private int mid;
    private int monthIncome;
    private int monthExpense;
    private int monthSum;
    private String year;
    private String month;
    private Date regDate;

    public static AccountHistoryDto convertToDto(AccountHistory accountHistory) {
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        accountHistoryDto.setAhid(accountHistory.getAhid());
        accountHistoryDto.setMid(accountHistory.getMid());
        accountHistoryDto.setMonthIncome(accountHistory.getMonthIncome());
        accountHistoryDto.setMonthExpense(accountHistory.getMonthExpense());
        accountHistoryDto.setMonthSum(accountHistory.getMonthSum());
        accountHistoryDto.setYear(accountHistory.getYear());
        accountHistoryDto.setMonth(accountHistory.getMonth());
        accountHistoryDto.setRegDate(accountHistory.getRegDate());

        return accountHistoryDto;
    }

}
