package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Post;
import com.maruhxn.boardserver.domain.PostImage;
import com.maruhxn.boardserver.dto.PostSearchCond;
import com.maruhxn.boardserver.dto.request.posts.CreatePostRequest;
import com.maruhxn.boardserver.dto.request.posts.UpdatePostRequest;
import com.maruhxn.boardserver.dto.response.object.PostDetailItem;
import com.maruhxn.boardserver.dto.response.object.PostItem;
import com.maruhxn.boardserver.exception.EntityNotFoundException;
import com.maruhxn.boardserver.repository.PostImageRepository;
import com.maruhxn.boardserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
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
    public Page<PostItem> getPostList(PostSearchCond postSearchCond, Pageable pageable) {
        return postRepository.findAllByConditions(postSearchCond, pageable);
    }

    /**
     * 게시글 상세 조회
     *
     * @param postId
     * @return
     */
    public PostDetailItem getPostDetail(Long postId) {
        Post findPost = postRepository.findWithMemberAndImagesFirstById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST));
        System.out.println("findPost = " + findPost);
        findPost.addViewCount();
        return PostDetailItem.fromEntity(findPost);
    }

    /**
     * 게시글 수정
     *
     * @param postId
     * @param updatePostRequest
     */
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest) {
        List<PostImage> postImages = new ArrayList<>();
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST));
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
        Post findPost = postRepository.findWithMemberAndImagesFirstById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_POST));
        // 실제 이미지들 삭제
        // TODO: 폴더로 한번에 관리하고, 이미지 하나하나 삭제가 아니라 폴더를 삭제하는 방식으로 변경
        postRepository.delete(findPost);
        findPost.getImages()
                .forEach(postImage -> fileService.deleteFile(postImage.getStoredName()));
    }

    /**
     * 이미지 삭제
     *
     * @param imageId
     */
    public void deleteImage(Long imageId) {
        PostImage findImage = postImageRepository.findById(imageId).orElseThrow(() ->
                new EntityNotFoundException(ErrorCode.NOT_FOUND_IMAGE));
        postImageRepository.delete(findImage); // DB에서 이미지 정보 삭제
        fileService.deleteFile(findImage.getStoredName()); // 실제 이미지 삭제
    }
}
