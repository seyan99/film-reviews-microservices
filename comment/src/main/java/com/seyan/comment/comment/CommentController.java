package com.seyan.comment.comment;

import com.seyan.comment.dto.*;
import com.seyan.comment.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/comment-review")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> commentReview(@RequestBody @Valid CommentCreationDTO dto) {
        Comment comment = commentService.createComment(dto, PostType.REVIEW);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        return ResponseHandler.generateResponse("Comment for review has been successfully created", HttpStatus.CREATED, response);
    }

    @PostMapping("/comment-list")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> commentList(@RequestBody @Valid CommentCreationDTO dto) {
        Comment comment = commentService.createComment(dto, PostType.LIST);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        return ResponseHandler.generateResponse("Comment for list has been successfully created", HttpStatus.CREATED, response);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateComment(@RequestBody @Valid CommentUpdateDTO dto, @PathVariable("id") Long id) {
        Comment comment = commentService.updateComment(dto, id);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        return ResponseHandler.generateResponse("Comment has been updated", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ResponseHandler.generateResponse("Comment has been deleted", HttpStatus.OK, null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> commentDetails(@PathVariable("id") Long id) {
        Comment comment = commentService.getCommentById(id);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);

        return ResponseHandler.generateResponse("Comment details", HttpStatus.OK, response);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<Comment> allComments = commentService.getAllComments(page, size);
        PageableCommentResponse response = commentMapper.mapCommentToPageableResponseDTO(allComments);

        return ResponseHandler.generateResponse("All comments", HttpStatus.OK, response);
    }

    @GetMapping("/all-by-post")
    public ResponseEntity<Object> getAllByPostPageable(
            @RequestParam("postId") Long postId, @RequestParam("postType") String postType,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<Comment> comments = commentService.getCommentsByPost(postId, PostType.valueOf(postType), page, size);
        PageableCommentResponse response = commentMapper.mapCommentToPageableResponseDTO(comments);

        return ResponseHandler.generateResponse("List of comments by post", HttpStatus.OK, response);
    }

    @GetMapping("/latest-by-post")
    public ResponseEntity<List<CommentResponseDTO>> getLatestByPost(@RequestParam("postId") Long postId,
                                                  @RequestParam("postType") String postType) {

        List<Comment> allComments = commentService.getLatestCommentsByPost(postId, PostType.valueOf(postType));
        List<CommentResponseDTO> response = commentMapper.mapCommentToCommentResponseDTO(allComments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
