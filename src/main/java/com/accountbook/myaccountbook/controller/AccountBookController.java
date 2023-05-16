package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
@SessionAttributes("user")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;


    @ModelAttribute("user")
    public Member setMember(@ModelAttribute Member member) {
        if (member != null) {
            return member;
        } else {
            return null;
        }
    }


    // 가계부 조회
    @GetMapping("/book")
    public String getAccountBook(Model model) {
        int lengthOfMonth = getDays();
        System.out.println("lengthOfMonth = " + lengthOfMonth);

        model.addAttribute("days", lengthOfMonth);
        model.addAttribute("income", new Income());
        model.addAttribute("expense", new Expense());

        return "accountbook/book";
    }


    // 현재 달의 일수 계산
    private static int getDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 6));

        LocalDate findMonth = LocalDate.of(year, month, 1);
        return findMonth.lengthOfMonth();
    }


}
