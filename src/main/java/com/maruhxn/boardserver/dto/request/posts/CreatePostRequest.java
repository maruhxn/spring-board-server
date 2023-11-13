package com.maruhxn.boardserver.dto.request.posts;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private List<MultipartFile> images;
}
