package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.persistence.Income;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IncomeReturnDto {

    @NotNull
    private int inid;

    @NotNull
    private int incomeMoney;

    @NotBlank
    private String incomeReason;

    @NotBlank
    private String month;

    public void convetToDto(Income income) {
        this.inid = income.getInid();
        this.incomeMoney = income.getIncomeMoney();
        this.incomeReason = income.getIncomeReason();
        this.month = income.getMonth();
    }
}
