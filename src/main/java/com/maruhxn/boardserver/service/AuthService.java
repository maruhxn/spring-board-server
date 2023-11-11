package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.common.PasswordEncoder;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.auth.LoginRequest;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        // 비밀번호 == 비밀번호 확인 체크
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        // 이메일 & 유저명 중복 여부 확인
        List<Member> findMembers = memberRepository.findByEmailOrUsername(registerRequest.getEmail(), registerRequest.getUsername());
        if (findMembers.size() > 0) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일 혹은 유저명입니다.");
        }
        // 유저 저장
        Member member = Member.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getEmail(), registerRequest.getPassword()))
                .profileImage("/img/defaultProfileImage.jfif")
                .build();

        memberRepository.save(member);
    }

    public Member login(LoginRequest loginRequest) {
        // 유저 조회
        Member findMember = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "유저 정보가 없습니다."));
        // 비밀번호 일치 여부 확인
        Boolean isMatch = passwordEncoder.isMatch(loginRequest.getEmail(), loginRequest.getPassword(), findMember.getPassword());

        if (!isMatch) throw new GlobalException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

        return findMember;
    }

    public void confirmPassword(Long memberId, ConfirmPasswordRequest confirmPasswordRequest) {
        Member findMember = memberRepository.findOne(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "유저 정보가 없습니다."));
        Boolean isMatch = passwordEncoder.isMatch(findMember.getEmail(), confirmPasswordRequest.getCurrPassword(), findMember.getPassword());

        if (!isMatch) throw new GlobalException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }
}
