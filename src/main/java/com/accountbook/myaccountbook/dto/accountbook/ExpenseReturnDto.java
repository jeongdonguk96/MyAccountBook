package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.entity.Expense;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ExpenseReturnDto(
    @NotNull
    int exid,

    @NotNull
    int expenseMoney,

    @NotBlank
    String expenseReason,

    @NotBlank
    String expenseCategory,

    @NotBlank
    String date
) {

    // DB에서 조회한 엔티티를 Dto로 변환한다.
    public static ExpenseReturnDto convertToDto(Expense expense) {
        return new ExpenseReturnDto(
                expense.getExid(),
                expense.getExpenseMoney(),
                expense.getExpenseReason(),
                String.valueOf(expense.getExpenseCategory()),
                expense.getDate()
        );
    }

}
