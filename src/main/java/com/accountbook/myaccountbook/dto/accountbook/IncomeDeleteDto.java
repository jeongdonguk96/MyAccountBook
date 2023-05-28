package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IncomeDeleteDto {

    @NotNull
    private int mid;

    @NotNull
    private int inid;
}
