package com.accountbook.myaccountbook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;
    private String id;
    private String pwd;
    private String name;
    private int age;
    @Embedded
    private Job job;
    private int rest;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Income> incomes = new ArrayList<>();
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Expense> expenses = new ArrayList<>();
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
}
