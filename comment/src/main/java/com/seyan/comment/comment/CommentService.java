package com.seyan.comment.comment;

import com.seyan.comment.dto.CommentCreationDTO;
import com.seyan.comment.dto.CommentMapper;
import com.seyan.comment.dto.CommentUpdateDTO;
import com.seyan.comment.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final ReviewService reviewService;
    private final FilmListService filmListService;

    @Transactional //todo interservice transaction(?)
    public Comment createComment(CommentCreationDTO dto, PostType postType) {
        Comment comment = commentMapper.mapCommentCreationDTOToComment(dto);
        comment.setPostType(postType);
        comment.setIsEdited(false);
        Comment saved = commentRepository.save(comment);

        if (postType == PostType.REVIEW) {
            reviewService.addReviewComment(dto.postId(), saved.getId());
        } else {
            filmListService.addListComment(dto.postId(), saved.getId());
        }

        return saved;
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("No Comment found with the provided ID: %s", id)
        ));
    }

    public List<Comment> getCommentsByPost(Long postId, PostType postType) {
        return commentRepository.findByPostIdAndPostType(postId, postType)
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentDate))
                .toList();
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll().stream()
                .sorted(Comparator.comparing(Comment::getCommentDate))
                .toList();
    }

    public Comment updateComment(CommentUpdateDTO dto, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("Cannot update comment:: No comment found with the provided ID: %s", id)
        ));
        Comment mapped = commentMapper.mapCommentUpdateDTOToComment(dto, comment);
        mapped.setIsEdited(true);
        return commentRepository.save(mapped);
    }

    @Transactional //todo interservice transaction(?)
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("Cannot delete comment:: No comment found with the provided ID: %s", id)));
        commentRepository.deleteById(id);

        if (comment.getPostType() == PostType.REVIEW) {
            reviewService.deleteReviewComment(comment.getPostId(), comment.getId());
        } else {
            filmListService.deleteListComment(comment.getPostId(), comment.getId());
        }
    }
}
