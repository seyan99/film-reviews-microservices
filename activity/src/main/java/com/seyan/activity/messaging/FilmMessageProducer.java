package com.seyan.activity.messaging;

import com.seyan.activity.dto.FilmStatsUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void updateLikeCount(Long id, Boolean toAdd) {
        FilmStatsUpdateDTO dto = new FilmStatsUpdateDTO(id, toAdd);
        rabbitTemplate.convertAndSend("filmLikeQueue", dto);
    }

    public void updateAvgRating(Long id, Boolean toAdd) {
        FilmStatsUpdateDTO dto = new FilmStatsUpdateDTO(id, toAdd);
        rabbitTemplate.convertAndSend("filmRatingQueue", dto);
    }

    public void updateWatchedCount(Long id, Boolean toAdd) {
        FilmStatsUpdateDTO dto = new FilmStatsUpdateDTO(id, toAdd);
        rabbitTemplate.convertAndSend("filmWatchedQueue", dto);
    }
}
