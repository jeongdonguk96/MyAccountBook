package com.accountbook.myaccountbook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exid;
    private int money;
    private String reason;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private AccountBook accountBook;
}
