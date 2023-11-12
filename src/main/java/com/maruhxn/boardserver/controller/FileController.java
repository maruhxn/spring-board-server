package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.exception.GlobalException;
import com.maruhxn.boardserver.service.FileService;
import com.maruhxn.boardserver.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final PostService postService;

    @GetMapping(value = "/{fileName}", produces = {
            MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public Resource getImage(@PathVariable String fileName) {
        return fileService.getImage(fileName);
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(HttpSession session, @PathVariable Long imageId) {
        log.info("이미지 삭제 | imageId={}", imageId);
        Member member = (Member) Optional.ofNullable(session.getAttribute("member")).orElseThrow(
                () -> new GlobalException(ErrorCode.UNAUTHORIZED, "로그인이 필요한 서비스입니다.")
        );
        postService.deleteImage(member.getId(), imageId);
    }
}
