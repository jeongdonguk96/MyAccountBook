package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.dto.member.RequestJoinDto;
import com.accountbook.myaccountbook.enums.RoleEnum;
import com.accountbook.myaccountbook.persistence.Job;
import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.redis.RefreshTokenRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
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
    @Transactional
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
    public void join(RequestJoinDto joinDto) {
        Job job = new Job(joinDto.getField(), joinDto.getYear(), joinDto.getSalary());

        Member member = Member.builder()
                            .username(joinDto.getUsername())
                            .pwd(passwordEncoder.encode(joinDto.getPwd()))
                            .name(joinDto.getName())
                            .age(joinDto.getAge())
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
     * @return
     */
    @Transactional
    public void logout(HttpServletResponse response, String accessToken, String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
        CookieUtil.removeCookie(response, "accessToken", accessToken);
        CookieUtil.removeCookie(response, "refreshToken", refreshToken);
    }


    /**
     * 가계부 작성/수정 시 사용자 잔여금 계산
     * @param findMember 사용자
     * @param money 수입/지출 기입시 금액
     */
    @Transactional
    public void calculateRest(Member findMember, int money) {
        findMember.setRest(findMember.getRest()+money);
    }


    /**
     * 가계부 지출 수정 시 기존 지출금액 되돌리기
     * @param findMember 사용자
     * @param money 수정 전 기존 기입 금액
     */
    @Transactional
    public void rollbackRest(Member findMember, int money) {
        findMember.setRest(findMember.getRest()+money);
    }
}
