package com.accountbook.myaccountbook.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Income {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inid;
    private int incomeMoney;
    private String incomeReason;
    private String year;
    private String month;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void modifyReasonAndMoney(String incomeReason, int incomeMoney) {
        this.incomeReason = incomeReason;
        this.incomeMoney = incomeMoney;
    }
}
