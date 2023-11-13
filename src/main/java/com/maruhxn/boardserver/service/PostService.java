package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.PostImage;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.object.PostDetailItem;
import com.maruhxn.boardserver.dto.response.object.PostItem;
import com.maruhxn.boardserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    /**
     * 게시글 생성
     *
     * @param member
     * @param createPostRequest
     */
    public void createPost(Member member, CreatePostRequest createPostRequest) {

        List<PostImage> postImages = new ArrayList<>();
        if (createPostRequest.getImages() != null) {
            postImages = fileService.storeFiles(createPostRequest.getImages());
        }
        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .member(member)
                .viewCount(0L)
                .build();
        postImages.forEach(post::addPostImage);
        postRepository.save(post);
    }

    /**
     * 게시글 전체 조회
     *
     * @param postSearchCond
     * @return
     */
    @Transactional(readOnly = true)
    public List<PostItem> getPostList(PostSearchCond postSearchCond) {
        List<Post> posts = postRepository.findAllWithMember(postSearchCond.getPage());
        return posts.stream()
                .map(PostItem::fromEntity)
                .toList();
    }

    /**
     * 게시글 상세 조회
     *
     * @param postId
     * @return
     */
    public PostDetailItem getPostDetail(Long postId) {
        Post findPost = getPostFromRepository(postId);
        findPost.addViewCount();
        return PostDetailItem.fromEntity(findPost);
    }

    /**
     * 게시글 단건 조회 (없으면 에러 반환)
     *
     * @param postId
     * @return
     */
    private Post getPostFromRepository(Long postId) {
        return postRepository.findOneWithAuthorAndImages(postId)
                .orElseThrow(() ->
                        new GlobalException(ErrorCode.NOT_FOUND_POST));
    }

    /**
     * 게시글 수정
     *
     * @param postId
     * @param updatePostRequest
     */
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        List<PostImage> postImages = new ArrayList<>();
        Post findPost = getPostFromRepository(postId);
        if (updatePostRequest.getImages() != null) {
            postImages = fileService.storeFiles(updatePostRequest.getImages());
        }
        findPost.updatePost(updatePostRequest.getTitle(), updatePostRequest.getContent(), postImages);
    }

    /**
     * 게시글 삭제
     *
     * @param postId
     */
    public void deletePost(Long postId) {
        Post findPost = getPostFromRepository(postId);
        // 실제 이미지들 삭제
        // TODO: 폴더로 한번에 관리하고, 이미지 하나하나 삭제가 아니라 폴더를 삭제하는 방식으로 변경
        findPost.getImages()
                .forEach(postImage -> deleteImage(postImage.getId()));

        postRepository.removePost(findPost);
    }

    /**
     * 이미지 삭제
     *
     * @param imageId
     */
    public void deleteImage(Long imageId) {
        PostImage findImage = postRepository.findPostImageById(imageId).orElseThrow(() ->
                new GlobalException(ErrorCode.NOT_FOUND_IMAGE));
        postRepository.removeImage(findImage); // DB에서 이미지 정보 삭제
        fileService.deleteFile(findImage.getStoredName()); // 실제 이미지 삭제
    }
}
