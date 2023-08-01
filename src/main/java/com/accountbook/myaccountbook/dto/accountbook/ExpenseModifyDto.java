package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExpenseModifyDto {

    @NotNull
    private int mid;

    @NotNull
    private int exid;

    @NotNull
    private int expenseMoney;

    @NotBlank
    private String expenseReason;

    @NotBlank
    private String expenseCategory;
}
