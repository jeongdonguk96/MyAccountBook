package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
import com.accountbook.myaccountbook.persistence.Expense;
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

    // DB에서 조회한 엔티티를 Dto로 변환한다.
    public static ExpenseReturnDto convertToDto(Expense expense) {
        ExpenseReturnDto expenseReturnDto = new ExpenseReturnDto();
        expenseReturnDto.setExid(expense.getExid());
        expenseReturnDto.setExpenseMoney(expense.getExpenseMoney());
        expenseReturnDto.setExpenseReason(expense.getExpenseReason());
        expenseReturnDto.setExpenseCategory(expense.getExpenseCategory());
        expenseReturnDto.setDate(expense.getDate());

        return expenseReturnDto;
    }
}
