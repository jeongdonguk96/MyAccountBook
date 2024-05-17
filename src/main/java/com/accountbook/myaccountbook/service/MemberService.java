package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.dto.member.JoinRequestDto;
import com.accountbook.myaccountbook.enums.RoleEnum;
import com.accountbook.myaccountbook.entity.Job;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 아이디 중복확인
     * @param username 회원가입 시 작성한 아이디
     * @return
     */
    @Transactional(readOnly = true)
    public int checkId(String username) {
        int result = 0;
        Optional<Member> findMember = memberRepository.findByUsername(username);

        if (findMember.isEmpty()) {
            result = 1;
        }
        return result;
    }


    /**
     * 회원가입
     * @param joinDto 회원가입 시 입력하는 정보
     */
    @Transactional
    public void join(JoinRequestDto joinDto) {
        Job job = new Job(joinDto.field(), joinDto.year(), joinDto.salary());

        Member member = Member.builder()
                            .username(joinDto.username())
                            .pwd(passwordEncoder.encode(joinDto.pwd()))
                            .name(joinDto.name())
                            .age(joinDto.age())
                            .job(job)
                            .role(RoleEnum.CUSTOMER)
                            .build();

        memberRepository.save(member);
    }


    /**
     * 로그아웃
     * @param response 응답
     * @param accessToken 유효한 액세스 토큰
     * @param refreshToken 유효한 리프레시 토큰
     */
    @Transactional
    public void logout(HttpServletResponse response, String accessToken, String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
        CookieUtil.expireCookie(response, "accessToken", accessToken);
        CookieUtil.expireCookie(response, "refreshToken", refreshToken);
    }
}
