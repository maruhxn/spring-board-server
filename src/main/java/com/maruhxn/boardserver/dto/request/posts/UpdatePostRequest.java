package com.maruhxn.boardserver.dto.request.posts;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<MultipartFile> images;

    @Builder
    public UpdatePostRequest(String title, String content, List<MultipartFile> images) {
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
