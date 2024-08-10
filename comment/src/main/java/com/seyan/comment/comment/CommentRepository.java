package com.seyan.comment.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndPostType(Long postId, PostType postType);

    Page<Comment> findByPostIdAndPostType(Long postId, PostType postType, Pageable pageable);

    //Page<Comment> findAllById(List<Long> commentIds, Pageable pageable);
}
