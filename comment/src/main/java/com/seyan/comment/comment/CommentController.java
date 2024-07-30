package com.seyan.comment.comment;

import com.seyan.comment.dto.CommentCreationDTO;
import com.seyan.comment.dto.CommentMapper;
import com.seyan.comment.dto.CommentResponseDTO;
import com.seyan.comment.dto.CommentUpdateDTO;
import com.seyan.comment.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<List<CommentResponseDTO>>> getAll() {
        List<Comment> allComments = commentService.getAllComments();
        List<CommentResponseDTO> response = commentMapper.mapCommentToCommentResponseDTO(allComments);
        CustomResponseWrapper<List<CommentResponseDTO>> wrapper = CustomResponseWrapper.<List<CommentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of all comments")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/all-by-post")
    public ResponseEntity<CustomResponseWrapper<List<CommentResponseDTO>>> getAllByPost(@RequestParam("postId") Long postId, @RequestParam("postType") String postType) {
        List<Comment> allComments = commentService.getCommentsByPost(postId, PostType.valueOf(postType));
        List<CommentResponseDTO> response = commentMapper.mapCommentToCommentResponseDTO(allComments);
        CustomResponseWrapper<List<CommentResponseDTO>> wrapper = CustomResponseWrapper.<List<CommentResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of comments by post")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
