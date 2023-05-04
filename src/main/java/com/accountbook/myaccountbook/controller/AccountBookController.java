package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

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


        return "accountbook/book";
    }
}
