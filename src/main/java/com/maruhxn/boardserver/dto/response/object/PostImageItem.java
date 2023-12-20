package com.maruhxn.boardserver.dto.response.object;

import com.maruhxn.boardserver.domain.PostImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageItem {
    private Long imageId;
    private String originalName;
    private String storedName;

    @Builder
    public PostImageItem(Long imageId, String originalName, String storedName) {
        this.imageId = imageId;
        this.originalName = originalName;
        this.storedName = storedName;
    }

    public static PostImageItem fromEntity(PostImage postImage) {
        return PostImageItem.builder()
                .imageId(postImage.getId())
                .originalName(postImage.getOriginalName())
                .storedName(postImage.getStoredName())
                .build();
    }
}
