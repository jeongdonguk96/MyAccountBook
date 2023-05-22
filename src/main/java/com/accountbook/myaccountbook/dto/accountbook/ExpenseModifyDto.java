package com.accountbook.myaccountbook.dto.accountbook;

import lombok.Data;

@Data
public class ExpenseModifyDto {
    private int mid;
    private int exid;
    private int expenseMoney;
    private String expenseReason;
    private String expenseCategory;
}
