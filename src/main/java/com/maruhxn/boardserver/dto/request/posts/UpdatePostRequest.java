package com.maruhxn.boardserver.dto.request.posts;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
