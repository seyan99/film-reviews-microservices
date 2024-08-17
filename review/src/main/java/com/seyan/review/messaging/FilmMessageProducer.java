package com.seyan.review.messaging;

import com.seyan.review.dto.FilmStatsUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void updateReviewCount(Long id, Boolean toAdd) {
        FilmStatsUpdateDTO dto = new FilmStatsUpdateDTO(id, toAdd);
        rabbitTemplate.convertAndSend("reviewCountQueue", dto);
    }
}
