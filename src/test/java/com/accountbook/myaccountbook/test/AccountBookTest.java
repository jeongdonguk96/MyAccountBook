package com.accountbook.myaccountbook.test;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.service.AccountBookService;
import com.accountbook.myaccountbook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import javax.transaction.Transactional;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class AccountBookTest {
    private final AccountBookService accountBookService;
    private final MemberService memberService;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;

    @Test
//    @BeforeEach
    @DisplayName("테스트 시작 전 모든 데이터 삭제")
    @Order(1)
    @Disabled
    public void deleteAll() {
        System.out.println("테스트 시작 전 모든 데이터 삭제");
    }

    @Test
    @DisplayName("가계부 작성 테스트")
    @Disabled
    public void write() {
        Member findMember = memberRepository.findById(1).get();

        for (int i = 1; i <= 5; i++) {

        }
    }

    @Test
    @DisplayName("가계부 수정 테스트")
    @Disabled
    public void modify() {

    }

    @Test
    @DisplayName("가계부 삭제 테스트")
    @Disabled
    public void delete() {

    }

    @Test
    @DisplayName("가계부 조회 테스트")
    @Disabled
    @Transactional
    public void selectAll() {

    }
}
