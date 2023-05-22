package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.domain.Income;
import lombok.Data;

@Data
public class IncomeReturnDto {
    private int inid;
    private int incomeMoney;
    private String incomeReason;
    private String month;

    public void convetToDto(Income income) {
        this.inid = income.getInid();
        this.incomeMoney = income.getIncomeMoney();
        this.incomeReason = income.getIncomeReason();
        this.month = income.getMonth();
    }
}
