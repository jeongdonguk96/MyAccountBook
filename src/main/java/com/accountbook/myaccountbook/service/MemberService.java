package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.member.LoginDto;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    /**
     * 아이디 중복확인
     * @param loginId 회원가입 시 작성한 아이디
     * @return
     */
    @Transactional
    public int checkId(String loginId) {
        Member findMember = memberRepository.findByLoginId(loginId);
        int result = 0;

        if (findMember == null) {
            result = 1;
        }

        return result;
    }


    /**
     * 회원가입
     * @param member 회원가입 시 입력하는 정보
     */
    @Transactional
    public void join(Member member) {
        memberRepository.save(member);
    }


    /**
     * 로그인
     * @param loginDto 로그인 시 입력하는 정보
     * @return 성공 시 Member 객체, 실패 시 Null 반환
     */
    @Transactional
    public Member login(LoginDto loginDto) {
        Member findMember = memberRepository.findByLoginId(loginDto.getLoginId());

        if (findMember.getLoginId().equals(loginDto.getLoginId())) {
            if (findMember.getPwd().equals(loginDto.getPwd())) {
                return findMember;
            }
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
}
