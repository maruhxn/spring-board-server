package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.dto.request.members.UpdateMemberProfileRequest;
import com.maruhxn.boardserver.dto.request.members.UpdatePasswordRequest;
import com.maruhxn.boardserver.dto.response.MemberResponse;
import com.maruhxn.boardserver.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@Slf4j
public class MemberController {

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Object> getMemberDetail(@PathVariable Long memberId) {
        log.info("memberId={}", memberId);
        return ResponseDto.data("회원 정보 조회 성공", new MemberResponse(memberId, "test@test.com", "tester", "default-profile-image-path"));
    }

    @PatchMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMemberProfile(@PathVariable Long memberId, @RequestBody UpdateMemberProfileRequest updateMemberProfileRequest) {
        log.info("username={}, profileImage={}", updateMemberProfileRequest.getUsername(), updateMemberProfileRequest.getProfileImage());
    }

    @PatchMapping("/{memberId}/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long memberId, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        log.info("currPassword={}, newPassword={}, confirmNewPassword={}", updatePasswordRequest.getCurrPassword(), updatePasswordRequest.getNewPassword(), updatePasswordRequest.getConfirmNewPassword());
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable Long memberId) {
        log.info("memberId={}", memberId);
        return;
    }
}
