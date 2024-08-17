package com.seyan.activity.messaging;

import com.seyan.activity.external.review.ReviewCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void createReview(ReviewCreationDTO dto) {
        rabbitTemplate.convertAndSend("reviewCreationQueue", dto);
    }
}
