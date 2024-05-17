package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record IncomeModifyDto(
    @NotNull
    int mid,

    @NotNull
    int inid,

    @NotNull
    int incomeMoney,

    @NotBlank
    String incomeReason
) {
}