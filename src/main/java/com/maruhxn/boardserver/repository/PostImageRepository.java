package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
