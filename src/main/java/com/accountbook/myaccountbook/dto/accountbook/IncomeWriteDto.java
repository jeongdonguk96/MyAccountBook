package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

@Data
public class IncomeWriteDto {
    private int mid;
    private int incomeMoney;
    private String incomeReason;
    private String month;
}
