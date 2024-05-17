package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ExpenseWriteDto(
    @NotNull
    int mid,

    @NotNull
    int expenseMoney,

    @NotBlank
    String expenseReason,

    @NotBlank
    String expenseCategory,

    @NotBlank
    String date
) {
}