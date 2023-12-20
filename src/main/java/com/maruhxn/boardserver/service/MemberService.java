package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.auth.common.AccountContext;
import com.maruhxn.boardserver.auth.common.AjaxAuthenticationToken;
import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.dto.response.object.MemberItem;
import com.maruhxn.boardserver.exception.BadRequestException;
import com.maruhxn.boardserver.exception.EntityNotFoundException;
import com.maruhxn.boardserver.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final SecurityContextRepository securityContextRepository;

    /**
     * 회원 정보 조회
     *
     * @param memberId
     * @return MemberResponse
     */
    @Transactional(readOnly = true)
    public MemberItem getMemberDetail(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));
        return MemberItem.builder()
                .memberId(findMember.getId())
                .username(findMember.getUsername())
                .email(findMember.getEmail())
                .profileImage(findMember.getProfileImage())
                .role(findMember.getRole())
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
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));
        Boolean isMatch = passwordEncoder.matches(confirmPasswordRequest.getCurrPassword(), findMember.getPassword());

        if (!isMatch) throw new BadRequestException(ErrorCode.INCORRECT_PASSWORD);
    }

    /**
     * 회원 정보 수정 (유저명, 프로필 이미지)
     *
     * @param memberId
     * @param updateMemberProfileRequest
     * @param request
     * @param response
     * @return
     */
    public AjaxAuthenticationToken updateProfile(
            Long memberId,
            UpdateMemberProfileRequest updateMemberProfileRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws DataIntegrityViolationException {
        if (updateMemberProfileRequest.getUsername() == null && updateMemberProfileRequest.getProfileImage() == null) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
        String newProfileImageName = null;
        MultipartFile newProfileImage = updateMemberProfileRequest.getProfileImage();
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        if (newProfileImage != null) {
            newProfileImageName = fileService.saveAndExtractUpdatedProfileImage(newProfileImage);
            // 기존 이미지 삭제
            deleteProfileImageOfFindMember(findMember);
        }

        findMember.updateProfile(updateMemberProfileRequest.getUsername(), newProfileImageName);
        SecurityContextHolder.clearContext();
        AccountContext accountContext = new AccountContext(findMember);
        AjaxAuthenticationToken newAuthentication = AjaxAuthenticationToken.authenticated(
                accountContext.getMember(),
                null,
                accountContext.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(newAuthentication);
        securityContextRepository.saveContext(context, request, response);
        return newAuthentication;
    }

    private void deleteProfileImageOfFindMember(Member findMember) {
        if (!Objects.equals(findMember.getProfileImage(), Constants.BASIC_PROFILE_IMAGE_NAME)) {
            fileService.deleteFile(findMember.getProfileImage());
        }
    }

    /**
     * 회원 정보 수정 (비밀번호 수정)
     *
     * @param memberId
     * @param updatePasswordRequest
     */
    public void updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmNewPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_FAIL);
        }

        if (passwordEncoder.matches(
                updatePasswordRequest.getNewPassword(),
                findMember.getPassword()))
            throw new BadRequestException(ErrorCode.SAME_PASSWORD);

        if (!passwordEncoder.matches(
                updatePasswordRequest.getCurrPassword(),
                findMember.getPassword()))
            throw new BadRequestException(ErrorCode.INCORRECT_PASSWORD);

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
                () -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));
        deleteProfileImageOfFindMember(findMember);
        memberRepository.delete(findMember);
    }

}
