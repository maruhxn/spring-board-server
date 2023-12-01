package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.dto.response.object.MemberItem;
import com.maruhxn.boardserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 조회
     *
     * @param memberId
     * @return MemberResponse
     */
    @Transactional(readOnly = true)
    public MemberItem getMemberDetail(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        return MemberItem.builder()
                .memberId(findMember.getId())
                .username(findMember.getUsername())
                .email(findMember.getEmail())
                .profileImage(findMember.getProfileImage())
                .build();
    }

    /**
     * 비밀번호 확인(인증)
     *
     * @param memberId
     * @param confirmPasswordRequest
     */
    public void confirmPassword(Long memberId, ConfirmPasswordRequest confirmPasswordRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        Boolean isMatch = passwordEncoder.matches(confirmPasswordRequest.getCurrPassword(), findMember.getPassword());

        if (!isMatch) throw new GlobalException(ErrorCode.INCORRECT_PASSWORD);
    }

    /**
     * 회원 정보 수정 (유저명, 프로필 이미지)
     *
     * @param memberId
     * @param updateMemberProfileRequest
     */
    public void updateProfile(
            Long memberId, UpdateMemberProfileRequest updateMemberProfileRequest
    ) throws DataIntegrityViolationException {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER));

        findMember.updateProfile(
                updateMemberProfileRequest.getUsername(), updateMemberProfileRequest.getProfileImage());
    }

    /**
     * 회원 정보 수정 (비밀번호 수정)
     *
     * @param memberId
     * @param updatePasswordRequest
     */
    public void updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER));

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmNewPassword())) {
            throw new GlobalException(ErrorCode.PASSWORD_CONFIRM_FAIL);
        }

        if (passwordEncoder.matches(
                updatePasswordRequest.getNewPassword(),
                findMember.getPassword()))
            throw new GlobalException(ErrorCode.SAME_PASSWORD);

        if (!passwordEncoder.matches(
                updatePasswordRequest.getCurrPassword(),
                findMember.getPassword()))
            throw new GlobalException(ErrorCode.INCORRECT_PASSWORD);

        findMember.updatePassword(
                passwordEncoder.encode(updatePasswordRequest.getNewPassword())
        );
    }

    /**
     * 회원 탈퇴
     *
     * @param memberId
     */
    public void membershipWithdrawal(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NOT_FOUND_USER));

        memberRepository.delete(findMember);
    }

}
