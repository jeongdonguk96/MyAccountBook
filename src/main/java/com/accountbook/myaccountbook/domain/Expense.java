package com.accountbook.myaccountbook.domain;

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
    private String expenseCategory;
    private String year;
    private String month;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
