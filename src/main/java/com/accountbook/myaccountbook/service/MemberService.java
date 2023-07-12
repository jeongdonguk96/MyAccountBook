package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.Job;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.domain.RoleEnum;
import com.accountbook.myaccountbook.dto.member.RequestJoinDto;
import com.accountbook.myaccountbook.dto.member.RequestLoginDto;
import com.accountbook.myaccountbook.exception.CustomApiException;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
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
                            .age(joinDto.getAge())
                            .job(job)
                            .role(RoleEnum.CUSTOMER)
                            .build();

        memberRepository.save(member);
    }


    /**
     * 로그인
     * @param loginDto 로그인 시 입력하는 정보
     * @return 성공 시 Member 객체, 실패 시 Null 반환
     */
    @Transactional
    public Member login(RequestLoginDto loginDto) {
        Member findMember = memberRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                ()-> new CustomApiException("등록된 아이디가 없습니다")
        );

        if (findMember.getPwd().equals(loginDto.getPwd())) {
            return findMember;
        }

        return null;
    }


    /**
     * 가계부 작성/수정 시 사용자 잔여금 계산
     * @param findMember 사용자
     * @param money 수입/지출 기입시 금액
     */
    @Transactional
    public void setRest(Member findMember, int money) {
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
