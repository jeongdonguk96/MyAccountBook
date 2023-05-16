package com.accountbook.myaccountbook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Income {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inid;
    private int incomeMoney;
    private String incomeReason;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private AccountBook accountBook;
}
