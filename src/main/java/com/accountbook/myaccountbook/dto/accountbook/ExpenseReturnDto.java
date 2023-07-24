package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.persistence.Expense;
import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExpenseReturnDto {

    @NotNull
    private int exid;

    @NotNull
    private int expenseMoney;

    @NotBlank
    private String expenseReason;

    @NotBlank
    private ExpenseCategoryEnum expenseCategory;

    @NotBlank
    private String date;

    public void convetToDto(Expense expense) {
        this.exid = expense.getExid();
        this.expenseMoney = expense.getExpenseMoney();
        this.expenseReason = expense.getExpenseReason();
        this.expenseCategory = expense.getExpenseCategory();
        this.date = expense.getDate();
    }
}
