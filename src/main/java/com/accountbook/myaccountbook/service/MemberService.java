package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param member 회원가입 시 입력하는 정보
     */
    @Transactional
    public void join(Member member) {
        memberRepository.save(member);
    }

    /**
     * 아이디 중복확인
     * @param loginId 회원가입 시 작성한 아이디
     * @return
     */
    @Transactional
    public int checkId(String loginId) {
        System.out.println("loginId = " + loginId);
        Member findMember = memberRepository.findByLoginId(loginId);
        int result = 0;
        System.out.println("findMember = " + findMember);

        if (findMember == null) {
            result = 1;
        }

        return result;
    }


    /**
     * 로그인
     * @param member 로그인 시 입력하는 정보
     * @return       성공 시 1, 실패 시 -1 반환
     */
    @Transactional
    public int login(Member member) {
        Member findMember = memberRepository.findById(member.getMid()).get();

        if (findMember.getLoginId().equals(member.getLoginId())) {
            if (findMember.getPwd().equals(member.getPwd())) {
                return 1;
            }
        }

        return -1;
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
