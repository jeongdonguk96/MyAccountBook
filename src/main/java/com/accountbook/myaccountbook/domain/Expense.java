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
    private String expenseCategory;
    private String year;
    private String month;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Expense(int expenseMoney, String expenseReason, String expenseCategory, String year, String month, String date, Member member) {
        this.expenseMoney = expenseMoney;
        this.expenseReason = expenseReason;
        this.expenseCategory = expenseCategory;
        this.year = year;
        this.month = month;
        this.date = date;
        this.member = member;
    }
}
