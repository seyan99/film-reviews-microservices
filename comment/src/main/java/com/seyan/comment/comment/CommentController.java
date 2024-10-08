package com.seyan.comment.comment;

import com.seyan.comment.dto.*;
import com.seyan.comment.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/comment-review")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<CommentResponseDTO>> commentReview(@RequestBody @Valid CommentCreationDTO dto) {
        Comment comment = commentService.createComment(dto, PostType.REVIEW);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        CustomResponseWrapper<CommentResponseDTO> wrapper = CustomResponseWrapper.<CommentResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment for review has been successfully created")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PostMapping("/comment-list")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<CommentResponseDTO>> commentList(@RequestBody @Valid CommentCreationDTO dto) {
        Comment comment = commentService.createComment(dto, PostType.LIST);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        CustomResponseWrapper<CommentResponseDTO> wrapper = CustomResponseWrapper.<CommentResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment for list has been successfully created")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CustomResponseWrapper<CommentResponseDTO>> updateComment(@RequestBody @Valid CommentUpdateDTO dto, @PathVariable("id") Long id) {
        Comment comment = commentService.updateComment(dto, id);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        CustomResponseWrapper<CommentResponseDTO> wrapper = CustomResponseWrapper.<CommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Comment has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CustomResponseWrapper<CommentResponseDTO>> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        CustomResponseWrapper<CommentResponseDTO> wrapper = CustomResponseWrapper.<CommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Comment has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseWrapper<CommentResponseDTO>> commentDetails(@PathVariable("id") Long id) {
        Comment comment = commentService.getCommentById(id);
        CommentResponseDTO response = commentMapper.mapCommentToCommentResponseDTO(comment);
        CustomResponseWrapper<CommentResponseDTO> wrapper = CustomResponseWrapper.<CommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Comment details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo delete
    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<PageableCommentResponseDTO>> getAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<Comment> allComments = commentService.getAllComments(page, size);
        PageableCommentResponseDTO response = commentMapper.mapCommentsPageToPageableCommentResponseDTO(allComments);
        CustomResponseWrapper<PageableCommentResponseDTO> wrapper = CustomResponseWrapper.<PageableCommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of all comments")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/review/{reviewId}", "/review/{reviewId}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableCommentResponseDTO>> getAllByReview(
            @PathVariable Long reviewId, @PathVariable Optional<Integer> pageNo) {

        Page<Comment> allComments;
        if (pageNo.isPresent()) {
            allComments = commentService.getCommentsByPost(reviewId, PostType.REVIEW, pageNo.get());
        } else {
            allComments = commentService.getCommentsByPost(reviewId, PostType.REVIEW, 1);
        }

        PageableCommentResponseDTO response = commentMapper.mapCommentsPageToPageableCommentResponseDTO(allComments);
        CustomResponseWrapper<PageableCommentResponseDTO> wrapper = CustomResponseWrapper.<PageableCommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Comments for review with ID: %s", reviewId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/list/{listId}", "/list/{listId}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableCommentResponseDTO>> getAllByList(
            @PathVariable Long listId, @PathVariable Optional<Integer> pageNo) {

        Page<Comment> allComments;
        if (pageNo.isPresent()) {
            allComments = commentService.getCommentsByPost(listId, PostType.LIST, pageNo.get());
        } else {
            allComments = commentService.getCommentsByPost(listId, PostType.LIST, 1);
        }

        PageableCommentResponseDTO response = commentMapper.mapCommentsPageToPageableCommentResponseDTO(allComments);
        CustomResponseWrapper<PageableCommentResponseDTO> wrapper = CustomResponseWrapper.<PageableCommentResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Comments for list with ID: %s", listId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/latest-by-post")
    public ResponseEntity<CustomResponseWrapper<List<CommentResponseDTO>>> getLatestByPost(
            @RequestParam("postId") Long postId, @RequestParam("postType") String postType) {

        List<Comment> allComments = commentService.getLatestCommentsByPost(postId, PostType.valueOf(postType));
        List<CommentResponseDTO> response = commentMapper.mapCommentToCommentResponseDTO(allComments);
        CustomResponseWrapper<List<CommentResponseDTO>> wrapper = CustomResponseWrapper.<List<CommentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of comments by post")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
