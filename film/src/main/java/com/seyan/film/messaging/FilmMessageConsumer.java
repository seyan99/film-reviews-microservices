package com.seyan.film.messaging;


import com.seyan.film.dto.film.FilmStatsUpdateDTO;
import com.seyan.film.film.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmMessageConsumer {
    private final FilmService filmService;

    @RabbitListener(queues = "filmLikeQueue")
    public void updateLikeCount(FilmStatsUpdateDTO dto) {
        filmService.updateLikeCount(dto.id(), dto.toAdd());
    }

    @RabbitListener(queues = "filmRatingQueue")
    public void updateAvgRating(FilmStatsUpdateDTO dto) {
        filmService.updateAvgRating(dto.id(), dto.toAdd());
    }

    @RabbitListener(queues = "filmWatchedQueue")
    public void updateWatchedCount(FilmStatsUpdateDTO dto) {
        filmService.updateWatchedCount(dto.id(), dto.toAdd());
    }

    @RabbitListener(queues = "reviewCountQueue")
    public void updateReviewCount(FilmStatsUpdateDTO dto) {
        filmService.updateReviewCount(dto.id(), dto.toAdd());
    }
}
