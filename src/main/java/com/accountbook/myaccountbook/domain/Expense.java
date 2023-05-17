package com.accountbook.myaccountbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Expense {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exid;
    private int expenseMoney;
    private String expenseReason;

    @Enumerated(EnumType.STRING)
    private Category expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private AccountBook accountBook;

    public Expense(int expenseMoney, String expenseReason, Category expenseCategory) {
        this.expenseMoney = expenseMoney;
        this.expenseReason = expenseReason;
        this.expenseCategory = expenseCategory;
    }
}
