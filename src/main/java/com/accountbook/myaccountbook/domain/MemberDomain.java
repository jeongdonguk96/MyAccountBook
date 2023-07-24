package com.accountbook.myaccountbook.domain;

import com.accountbook.myaccountbook.enums.RoleEnum;
import com.accountbook.myaccountbook.persistence.Job;
import com.accountbook.myaccountbook.persistence.Member;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class MemberDomain {
    private int mid;
    private String username;
    private String pwd;
    private String name;
    private int age;
    private Job job;
    private int rest;
    private Date regDate;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public MemberDomain(Member member) {
        this.mid = member.getMid();
        this.username = member.getUsername();
        this.pwd = member.getPwd();
        this.name = member.getName();
        this.age = member.getAge();
        this.job = member.getJob();
        this.rest = member.getRest();
        this.regDate = member.getRegDate();
        this.role = member.getRole();
    }

    public void calculateRest(int money) {
        this.rest = money;
    }
}
