package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotNull;

public record IncomeDeleteDto(
    @NotNull
    int mid,

    @NotNull
    int inid
) {
}