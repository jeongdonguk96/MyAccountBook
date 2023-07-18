package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.persistence.ExpenseCategory;

public interface ExpenseCategoryDto {
    ExpenseCategory getExpenseCategory();
    int getExpenseMoney();
}
