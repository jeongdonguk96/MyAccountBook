package com.accountbook.myaccountbook.test;

import com.accountbook.myaccountbook.domain.Job;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.member.LoginDto;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Date;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MemberTest {
    private final MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트")
    @Order(1)
//    @Disabled
    public void joinTest() {
        Member member = new Member();
        member.setLoginId("user1");
        member.setPwd("1234");
        member.setName("김민수");
        member.setAge(20);
        member.setJob(new Job("Programmer", 1, 30000000));
        member.setRest(0);
        member.setRegDate(new Date());

        System.out.println("member = " + member);
        memberService.join(member);
    }

    @Test
    @DisplayName("로그인 테스트")
    @Order(2)
//    @Disabled
    public void loginTest() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLoginId("user1");
        loginDto.setPwd("1234");

        Member member = memberService.login(loginDto);
    }

}













//    @Test
//    @DisplayName("멤버삭제")
//    @Disabled
//    public void deleteAll() {
//        memberRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("멤버조회")
//    @Disabled
//    public void findAll() {
//        List<Member> members = memberRepository.findAll();
//        System.out.println("members = " + members);
//    }











