package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.auth.RegisterRequest;
import com.maruhxn.boardserver.dto.response.object.MemberInfo;
import com.maruhxn.boardserver.exception.AlreadyExistsResourceException;
import com.maruhxn.boardserver.exception.BadRequestException;
import com.maruhxn.boardserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_FAIL);
        }
        // 이메일 & 유저명 중복 여부 확인
        Boolean isExists = memberRepository.existsAllByEmailOrUsername(registerRequest.getEmail(), registerRequest.getUsername());
        if (isExists) throw new AlreadyExistsResourceException(ErrorCode.EXISTING_USER);
        // 유저 저장
        Member member = Member.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .profileImage(Constants.BASIC_PROFILE_IMAGE_NAME)
                .build();

        memberRepository.save(member);
    }
}
