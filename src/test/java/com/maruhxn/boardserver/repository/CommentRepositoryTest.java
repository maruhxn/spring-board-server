package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    void findAllByPostId() {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> page = commentRepository.findAllByPostId(1L, pageRequest);

        List<Comment> content = page.getContent();
        for (Comment comment : content) {
            System.out.println("comment = " + comment);
        }
    }
}