package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record IncomeWriteDto(
    @NotNull
    int mid,

    @NotNull
    int incomeMoney,

    @NotBlank
    String incomeReason,

    @NotBlank
    String month
) {
}