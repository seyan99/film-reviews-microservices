package com.seyan.comment.comment;

import com.seyan.comment.dto.CommentCreationDTO;
import com.seyan.comment.dto.CommentMapper;
import com.seyan.comment.dto.CommentUpdateDTO;
import com.seyan.comment.exception.CommentNotFoundException;
import com.seyan.comment.external.filmlist.FilmListClient;
import com.seyan.comment.external.review.ReviewClient;
import com.seyan.comment.messaging.ListMessageProducer;
import com.seyan.comment.messaging.ReviewMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final ReviewClient reviewClient;
    private final FilmListClient filmListClient;

    private final ReviewMessageProducer reviewMessageProducer;
    private final ListMessageProducer listMessageProducer;

    /*@Transactional
    public Comment createComment(CommentCreationDTO dto, PostType postType) {
        Comment comment = commentMapper.mapCommentCreationDTOToComment(dto);
        comment.setPostType(postType);
        comment.setIsEdited(false);
        Comment saved = commentRepository.save(comment);

        if (postType == PostType.REVIEW) {
            reviewClient.addReviewComment(dto.postId(), saved.getId());
        } else {
            filmListClient.addListComment(dto.postId(), saved.getId());
        }

        return saved;
    }*/

    public Comment createComment(CommentCreationDTO dto, PostType postType) {
        Comment comment = commentMapper.mapCommentCreationDTOToComment(dto);
        comment.setPostType(postType);
        comment.setIsEdited(false);
        Comment saved = commentRepository.save(comment);

        if (postType == PostType.REVIEW) {
            reviewMessageProducer.addReviewComment(dto.postId(), saved.getId());
        } else {
            listMessageProducer.addListComment(dto.postId(), saved.getId());
        }

        return saved;
    }

    /*public Page<Comment> getCommentsFromList(List<Long> commentIds, int pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "commentDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 25, sort);
        return commentRepository.findAllById(commentIds, pageable);
    }
*/
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("No Comment found with the provided ID: %s", id)
        ));
    }

    public List<Comment> getCommentsByPost(Long postId, PostType postType) {
        return commentRepository.findByPostIdAndPostType(postId, postType)
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentDate).reversed())
                .toList();
    }

    public Page<Comment> getCommentsByPost(Long postId, PostType postType, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "commentDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return commentRepository.findByPostIdAndPostType(postId, postType, pageable);
    }

    public Page<Comment> getCommentsByPost(Long postId, PostType postType, int pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "commentDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 25, sort);
        return commentRepository.findByPostIdAndPostType(postId, postType, pageable);
    }

    public Page<Comment> getAllComments(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "commentDate");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return commentRepository.findAll(pageable);
    }

    public Comment updateComment(CommentUpdateDTO dto, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("Cannot update comment:: No comment found with the provided ID: %s", id)
        ));
        Comment mapped = commentMapper.mapCommentUpdateDTOToComment(dto, comment);
        mapped.setIsEdited(true);
        return commentRepository.save(mapped);
    }

    /*@Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("Cannot delete comment:: No comment found with the provided ID: %s", id)));
        commentRepository.deleteById(id);

        if (comment.getPostType() == PostType.REVIEW) {
            reviewClient.deleteReviewComment(comment.getPostId(), comment.getId());
        } else {
            filmListClient.deleteListComment(comment.getPostId(), comment.getId());
        }
    }*/

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(
                String.format("Cannot delete comment:: No comment found with the provided ID: %s", id)));
        commentRepository.deleteById(id);

        if (comment.getPostType() == PostType.REVIEW) {
            reviewMessageProducer.deleteReviewComment(comment.getPostId(), comment.getId());
        } else {
            listMessageProducer.deleteListComment(comment.getPostId(), comment.getId());
        }
    }

    public List<Comment> getLatestCommentsByPost(Long postId, PostType postType) {
        return commentRepository.findByPostIdAndPostType(postId, postType)
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentDate).reversed())
                .limit(5)
                .toList();
    }
}
