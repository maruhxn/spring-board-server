package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.dto.response.DataResponseDto;
import com.maruhxn.boardserver.dto.response.object.MemberItem;
import com.maruhxn.boardserver.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public DataResponseDto<MemberItem> getMemberDetail(@PathVariable Long memberId) {
        return DataResponseDto.ok("회원 정보 조회 성공", memberService.getMemberDetail(memberId));
    }

    @PatchMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMemberProfile(
            @PathVariable Long memberId,
            @RequestBody @Valid UpdateMemberProfileRequest updateMemberProfileRequest) {
        log.info("회원 정보 수정 | username={}, profileImage={}",
                updateMemberProfileRequest.getUsername(),
                updateMemberProfileRequest.getProfileImage());
        memberService.updateProfile(memberId, updateMemberProfileRequest);
    }

    @PatchMapping("/{memberId}/change-password")
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

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable Long memberId) {
        log.info("회원 탈퇴 | memberId={}", memberId);
        memberService.membershipWithdrawal(memberId);
    }
}
