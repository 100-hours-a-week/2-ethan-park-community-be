package hw6.integration.comment.controller;

import hw6.integration.comment.domain.Comment;
import hw6.integration.comment.dto.CommentCreateRequestDto;
import hw6.integration.comment.dto.CommentResponseDto;
import hw6.integration.comment.dto.CommentUpdateRequestDto;
import hw6.integration.comment.service.CommentService;
import hw6.integration.user.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {

        List<CommentResponseDto> commentResponseDtos = commentService.getCommentByAll()
                .stream()
                .map(CommentResponseDto::fromComment)
                .toList();;

        return ResponseEntity.ok(commentResponseDtos);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CommentCreateRequestDto commentCreateDto,
            @PathVariable Long postId) {

        Long userId = userPrincipal.getId();

        Comment comment = commentService.createComment(commentCreateDto, userId, postId);

        return ResponseEntity.ok(CommentResponseDto.fromComment(comment));
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<CommentResponseDto> updateComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CommentUpdateRequestDto commentUpdateRequestDto,
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        Long userId = userPrincipal.getId();

        commentService.updateComment(commentUpdateRequestDto, commentId, userId, postId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        Long userId = userPrincipal.getId();

        commentService.deleteComment(commentId, userId, postId);

        return ResponseEntity.noContent().build();
    }
}