package com.accountbook.myaccountbook.domain;

import com.accountbook.myaccountbook.persistence.AccountHistory;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class AccountHistoryDomain {
    private int ahid;
    private int mid;
    private int monthIncome;
    private int monthExpense;
    private int monthSum;
    private String year;
    private String month;
    private Date regDate;

    public AccountHistoryDomain(AccountHistory accountHistory) {
        this.ahid = accountHistory.getAhid();
        this.mid = accountHistory.getMid();
        this.monthIncome = accountHistory.getMonthIncome();
        this.monthExpense = accountHistory.getMonthExpense();
        this.monthSum = accountHistory.getMonthSum();
        this.year = accountHistory.getYear();
        this.month = accountHistory.getMonth();
        this.regDate = accountHistory.getRegDate();
    }
}
