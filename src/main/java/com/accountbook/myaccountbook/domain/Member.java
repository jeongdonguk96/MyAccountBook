package com.accountbook.myaccountbook.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;
    private String loginId;
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
    @CreationTimestamp
    private Date regDate;

    // JoinDto to Member
    public Member(String loginId, String pwd, String name, int age, Job job) {
        this.loginId = loginId;
        this.pwd = pwd;
        this.name = name;
        this.age = age;
        this.job = job;
    }
}
