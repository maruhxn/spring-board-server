package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.PostImage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostImageItem {
    private Long imageId;
    private String originalName;
    private String storedName;

    public static PostImageItem fromEntity(PostImage postImage) {
        return PostImageItem.builder()
                .imageId(postImage.getId())
                .originalName(postImage.getOriginalName())
                .storedName(postImage.getStoredName())
                .build();
    }
}
