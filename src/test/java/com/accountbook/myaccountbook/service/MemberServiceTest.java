package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.AbstractIntegrationTest;
import com.accountbook.myaccountbook.dto.member.JoinRequestDto;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest extends AbstractIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAllInBatch();
    }

    @AfterEach
    void teardown() {
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("중복확인이 정상적으로 동작한다.")
    class checkId {

        @Test
        @DisplayName("중복확인 시 중복된 이름이 없을 경우 1을 반환한다.")
        void checkIdWithNotExists() {
            // given
            String username = "donguk";

            // when
            int result = memberService.checkId(username);

            // then
            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("중복확인 시 중복된 이름이 있을 경우 0을 반환한다.")
        void checkIdWithExists() {
            // given
            JoinRequestDto requestDto = new JoinRequestDto(
                    "donguk",
                    "1234",
                    "정동욱",
                    30,
                    "dev",
                    2,
                    0
            );
            memberService.join(requestDto);
            String username = "donguk";

            // when
            int result = memberService.checkId(username);

            // then
            assertThat(result).isEqualTo(0);
        }

    }

    @Test
    @DisplayName("회원가입이 성공적으로 진행된다.")
    void join() {
        // given
        JoinRequestDto requestDto = new JoinRequestDto(
                "donguk",
                "1234",
                "정동욱",
                30,
                "dev",
                2,
                0
        );
        memberService.join(requestDto);

        // when
        Member member = memberRepository.findByUsername("donguk")
                .orElseThrow(IllegalAccessError::new);

        // then
        assertThat(member.getUsername()).isEqualTo(requestDto.username());
        assertThat(member.getName()).isEqualTo(requestDto.name());
    }

}
