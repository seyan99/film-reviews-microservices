package com.seyan.activity.messaging;

import com.seyan.activity.activity.ActivityService;
import com.seyan.activity.dto.UpdateHasReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMessageConsumer {
    private final ActivityService activityService;

    @RabbitListener(queues = "updateHasReviewQueue")
    public void updateHasReview(UpdateHasReviewDTO dto) {
        activityService.updateHasReview(dto.userId(), dto.filmId());
    }
}
