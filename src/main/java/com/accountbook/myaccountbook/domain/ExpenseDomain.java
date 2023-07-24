package com.accountbook.myaccountbook.domain;

import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
import com.accountbook.myaccountbook.persistence.Expense;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
public class ExpenseDomain {
    private int exid;
    private int expenseMoney;
    private String expenseReason;
    @Enumerated(EnumType.STRING)
    private ExpenseCategoryEnum expenseCategory;
    private String year;
    private String month;
    private String date;
    private int mid;

    public ExpenseDomain(Expense expense) {
        this.exid = expense.getExid();
        this.expenseMoney = expense.getExpenseMoney();
        this.expenseReason = expense.getExpenseReason();
        this.expenseCategory = expense.getExpenseCategory();
        this.year = expense.getYear();
        this.month = expense.getMonth();
        this.date = expense.getDate();
        this.mid = expense.getMember().getMid();
    }
}
