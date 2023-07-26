package com.accountbook.myaccountbook.persistence;

import com.accountbook.myaccountbook.enums.RoleEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
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

    public Member(Optional<Member> findMember) {
        this.mid = findMember.get().getMid();
        this.username = findMember.get().getUsername();
        this.pwd = findMember.get().getPwd();
        this.name = findMember.get().getName();
        this.age = findMember.get().getAge();
        this.job = findMember.get().getJob();
        this.rest = findMember.get().getRest();
        this.regDate = findMember.get().getRegDate();
        this.role = findMember.get().getRole();
    }
}
