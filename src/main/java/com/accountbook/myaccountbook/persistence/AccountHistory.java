package com.accountbook.myaccountbook.persistence;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ahid;

    @Column(nullable = false)
    private int mid;
    private int monthIncome;
    private int monthExpense;
    private int monthSum;
    private String year;
    private String month;

    @CreationTimestamp
    private Date regDate;
}
