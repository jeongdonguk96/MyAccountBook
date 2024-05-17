package com.accountbook.myaccountbook.dto.accountbook;

import javax.validation.constraints.NotNull;

public record ExpenseDeleteDto(
    @NotNull
    int mid,

    @NotNull
    int exid
) {
}
