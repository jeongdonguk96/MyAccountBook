package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.domain.Category;
import lombok.Data;

@Data
public class ExpenseWriteDto {
    private int mid;
    private int expenseMoney;
    private String expenseReason;
    private Category expenseCategory;
}
