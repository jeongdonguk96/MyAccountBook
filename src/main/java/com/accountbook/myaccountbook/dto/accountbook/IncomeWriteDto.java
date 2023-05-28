package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IncomeWriteDto {

    @NotNull
    private int mid;

    @NotNull
    private int incomeMoney;

    @NotBlank
    private String incomeReason;

    @NotBlank
    private String month;
}
