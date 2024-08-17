package com.seyan.comment.messaging;

import com.seyan.comment.dto.CommentPostIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void addReviewComment(Long reviewId, Long commentId) {
        CommentPostIdDTO dto = new CommentPostIdDTO(reviewId, commentId);
        rabbitTemplate.convertAndSend("addReviewCommentQueue", dto);
    }

    public void deleteReviewComment(Long reviewId, Long commentId) {
        CommentPostIdDTO dto = new CommentPostIdDTO(reviewId, commentId);
        rabbitTemplate.convertAndSend("deleteReviewCommentQueue", dto);
    }
}
