package com.accountbook.myaccountbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class AccountBook {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int abid;
    @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Income> incomes = new ArrayList<>();
    @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Expense> expenses = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member member;
    private String date;
    private String year;
    private String month;

    public AccountBook(Member member, String date, String year, String month) {
        this.member = member;
        this.date = date;
        this.year = year;
        this.month = month;
    }
}
