package com.accountbook.myaccountbook.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Income {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inid;
    private int incomeMoney;
    private String incomeReason;
    private String year;
    private String month;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Income(int incomeMoney, String incomeReason, String year, String month, Member member) {
        this.incomeMoney = incomeMoney;
        this.incomeReason = incomeReason;
        this.year = year;
        this.month = month;
        this.member = member;
    }
}
