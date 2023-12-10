package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.dto.response.object.MemberInfo;
import com.maruhxn.boardserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberInfo generateMemberInfo(Member member) {
        return MemberInfo.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .build();
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        // 비밀번호 == 비밀번호 확인 체크
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new GlobalException(ErrorCode.PASSWORD_CONFIRM_FAIL);
        }
        // 이메일 & 유저명 중복 여부 확인
        List<Member> findMembers = memberRepository.findByEmailOrUsername(registerRequest.getEmail(), registerRequest.getUsername());
        if (!findMembers.isEmpty()) throw new GlobalException(ErrorCode.EXISTING_USER);
        // 유저 저장
        Member member = Member.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .profileImage(Constants.BASIC_PROFILE_IMAGE_NAME)
                .build();

        memberRepository.save(member);
    }

//    public Member login(LoginRequest loginRequest) {
//        // 유저 조회
//        Member findMember = memberRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
//        // 비밀번호 일치 여부 확인
//        Boolean isMatch = passwordEncoder.matches(loginRequest.getPassword(), findMember.getPassword());
//
//        if (!isMatch) throw new GlobalException(ErrorCode.INCORRECT_PASSWORD);
//
//        return findMember;
//    }

}
