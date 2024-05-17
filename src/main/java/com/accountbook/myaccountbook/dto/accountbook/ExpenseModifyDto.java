package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ExpenseModifyDto(
    @NotNull
    int mid,

    @NotNull
    int exid,

    @NotNull
    int expenseMoney,

    @NotBlank
    String expenseReason,

    @NotBlank
    String expenseCategory
) {
}
