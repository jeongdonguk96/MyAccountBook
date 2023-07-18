package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.persistence.ExpenseCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExpenseWriteDto {

    @NotNull
    private int mid;

    @NotNull
    private int expenseMoney;

    @NotBlank
    private String expenseReason;

    @NotBlank
    private ExpenseCategory expenseCategory;

    @NotBlank
    private String date;
}
