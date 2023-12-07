package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.auth.ConfirmPasswordRequest;
import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.dto.response.DataResponseDto;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import com.maruhxn.boardserver.dto.response.object.MemberItem;
import com.maruhxn.boardserver.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members/{memberId}")
@RestController
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("(principal.getId() == #memberId) or hasRole('ROLE_ADMIN')")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public DataResponseDto<MemberItem> getMemberDetail(@PathVariable Long memberId) {
        return DataResponseDto.ok("회원 정보 조회 성공", memberService.getMemberDetail(memberId));
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMemberProfile(
            @PathVariable Long memberId,
            @Valid UpdateMemberProfileRequest updateMemberProfileRequest) {
        log.info("회원 정보 수정 | username={}, profileImage={}",
                updateMemberProfileRequest.getUsername(),
                updateMemberProfileRequest.getProfileImage());
        memberService.updateProfile(memberId, updateMemberProfileRequest);
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @PathVariable Long memberId,
            @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        log.info("비밀번호 수정 | currPassword={}, newPassword={}, confirmNewPassword={}",
                updatePasswordRequest.getCurrPassword(),
                updatePasswordRequest.getNewPassword(),
                updatePasswordRequest.getConfirmNewPassword());
        memberService.updatePassword(memberId, updatePasswordRequest);
    }

    @PostMapping("/confirm-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto confirmPassword(
            @RequestBody @Valid ConfirmPasswordRequest confirmPasswordRequest,
            @PathVariable Long memberId
    ) {
        memberService.confirmPassword(memberId, confirmPasswordRequest);
        return ResponseDto.ok("비밀번호 인증 성공");
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable Long memberId) {
        log.info("회원 탈퇴 | memberId={}", memberId);
        memberService.membershipWithdrawal(memberId);
    }
}
