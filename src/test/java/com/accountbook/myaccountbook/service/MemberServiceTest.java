package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.AbstractIntegrationTest;
import com.accountbook.myaccountbook.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends AbstractIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAllInBatch();
    }


    @Test
    void checkId() {
        // given
        String username = "donguk";

        // when
        int result = memberService.checkId(username);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }


}