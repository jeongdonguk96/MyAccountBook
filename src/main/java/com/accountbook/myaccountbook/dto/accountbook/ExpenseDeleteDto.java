package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExpenseDeleteDto {

    @NotNull
    private int mid;

    @NotNull
    private int exid;
}
