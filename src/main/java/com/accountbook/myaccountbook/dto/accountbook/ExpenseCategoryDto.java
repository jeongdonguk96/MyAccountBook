package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;

public interface ExpenseCategoryDto {
    ExpenseCategoryEnum getExpenseCategory();
    int getExpenseMoney();
}
