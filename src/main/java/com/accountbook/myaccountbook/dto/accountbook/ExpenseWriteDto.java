package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
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
    private ExpenseCategoryEnum expenseCategory;

    @NotBlank
    private String date;
}
