package com.seyan.list.messaging;

import com.seyan.list.dto.CommentPostIdDTO;
import com.seyan.list.list.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListMessageConsumer {
    private final ListService listService;

    @RabbitListener(queues = "addListCommentQueue")
    public void addListComment(CommentPostIdDTO dto) {
        listService.addListComment(dto.postId(), dto.commentId());
    }

    @RabbitListener(queues = "deleteListCommentQueue")
    public void deleteListComment(CommentPostIdDTO dto) {
        listService.deleteListComment(dto.postId(), dto.commentId());
    }
}
