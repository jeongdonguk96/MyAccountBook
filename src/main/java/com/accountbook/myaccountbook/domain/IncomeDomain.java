package com.accountbook.myaccountbook.domain;

import com.accountbook.myaccountbook.persistence.Income;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class IncomeDomain {
    private int inid;
    private int incomeMoney;
    private String incomeReason;
    private String year;
    private String month;
    private int mid;

    public IncomeDomain(Income income) {
        this.inid = income.getInid();
        this.incomeMoney = income.getIncomeMoney();
        this.incomeReason = income.getIncomeReason();
        this.year = income.getYear();
        this.month = income.getMonth();
        this.mid = income.getMember().getMid();
    }

    public void modifyReasonAndMoney(String incomeReason, int incomeMoney) {
        this.incomeReason = incomeReason;
        this.incomeMoney = incomeMoney;
    }
}
