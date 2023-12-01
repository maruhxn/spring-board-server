package com.maruhxn.boardserver.controller;

import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/{fileName}", produces = {
            MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    @PreAuthorize("permitAll()")
    public Resource getImage(@PathVariable String fileName) {
        if (Objects.equals(fileName, Constants.BASIC_PROFILE_IMAGE_NAME)) {
            return new ClassPathResource("static/img/" + fileName);
        }
        return fileService.getImage(fileName);
    }
}
