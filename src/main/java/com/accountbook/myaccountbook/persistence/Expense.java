package com.accountbook.myaccountbook.persistence;

import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exid;
    private int expenseMoney;
    private String expenseReason;
    
    @Enumerated(EnumType.STRING)
    private ExpenseCategoryEnum expenseCategory;
    private String year;
    private String month;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void modifyReasonAndMoneyAndCategory(String incomeReason, int incomeMoney, ExpenseCategoryEnum expenseCategory) {
        this.expenseReason = incomeReason;
        this.expenseMoney = incomeMoney;
        this.expenseCategory = expenseCategory;
    }
}
