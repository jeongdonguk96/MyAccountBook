package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

@Data
public class IncomeModifyDto {
    private int mid;
    private int inid;
    private int incomeMoney;
    private String incomeReason;
}
