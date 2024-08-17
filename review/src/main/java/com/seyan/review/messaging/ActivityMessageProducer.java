package com.seyan.review.messaging;

import com.seyan.review.dto.UpdateHasReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void updateHasReview(Long filmId, Long userId) {
        UpdateHasReviewDTO dto = new UpdateHasReviewDTO(userId, filmId);
        rabbitTemplate.convertAndSend("updateHasReviewQueue", dto);
    }
}
