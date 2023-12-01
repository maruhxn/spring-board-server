package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.service.FileService;
import com.maruhxn.boardserver.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("permitAll()")
    public Resource getImage(@PathVariable String fileName) {
        return fileService.getImage(fileName);
    }
}
