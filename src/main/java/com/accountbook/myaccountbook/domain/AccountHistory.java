package com.accountbook.myaccountbook.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private int mid;
    private int monthIncome;
    private int monthExpense;
    private int monthSum;
    private String year;
    private String month;

    @CreationTimestamp
    private Date regDate;
}
