package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.domain.Category;
import com.accountbook.myaccountbook.domain.Expense;
import lombok.Data;

@Data
public class ExpenseReturnDto {
    private int exid;
    private int expenseMoney;
    private String expenseReason;
    private Category expenseCategory;
    private String date;

    public void convetToDto(Expense expense) {
        this.exid = expense.getExid();
        this.expenseMoney = expense.getExpenseMoney();
        this.expenseReason = expense.getExpenseReason();
        this.expenseCategory = expense.getExpenseCategory();
        this.date = expense.getDate();
    }
}
