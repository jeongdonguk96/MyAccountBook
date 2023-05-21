package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

@Data
public class ExpenseWriteDto {
    private int mid;
    private int expenseMoney;
    private String expenseReason;
    private String expenseCategory;
    private String date;
}
