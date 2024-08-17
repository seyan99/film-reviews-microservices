package com.seyan.review.messaging;

import com.seyan.review.dto.CommentPostIdDTO;
import com.seyan.review.dto.ReviewCreationDTO;
import com.seyan.review.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageConsumer {
    private final ReviewService reviewService;

    @RabbitListener(queues = "reviewCreationQueue")
    public void createReview(ReviewCreationDTO dto) {
        reviewService.createReview(dto);
    }

    @RabbitListener(queues = "addReviewCommentQueue")
    public void addReviewComment(CommentPostIdDTO dto) {
        reviewService.addReviewComment(dto.postId(), dto.commentId());
    }

    @RabbitListener(queues = "deleteReviewCommentQueue")
    public void deleteReviewComment(CommentPostIdDTO dto) {
        reviewService.deleteReviewComment(dto.postId(), dto.commentId());
    }
}
