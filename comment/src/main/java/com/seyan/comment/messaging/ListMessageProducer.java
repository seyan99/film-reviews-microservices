package com.seyan.comment.messaging;

import com.seyan.comment.dto.CommentPostIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void addListComment(Long listId, Long commentId) {
        CommentPostIdDTO dto = new CommentPostIdDTO(listId, commentId);
        rabbitTemplate.convertAndSend("addListCommentQueue", dto);
    }

    public void deleteListComment(Long listId, Long commentId) {
        CommentPostIdDTO dto = new CommentPostIdDTO(listId, commentId);
        rabbitTemplate.convertAndSend("deleteListCommentQueue", dto);
    }
}
