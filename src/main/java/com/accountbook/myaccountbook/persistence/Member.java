package com.accountbook.myaccountbook.persistence;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString(exclude = {"incomes", "expenses"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;
    private String username;
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

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}