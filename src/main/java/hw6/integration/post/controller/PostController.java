package hw6.integration.post.controller;

import hw6.integration.like.service.LikeService;
import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostDetailResponseDto;
import hw6.integration.post.dto.PostListResponseDto;
import hw6.integration.post.dto.PostUpdateRequestDto;
import hw6.integration.post.service.PostService;
import hw6.integration.user.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {

        List<PostListResponseDto> postListResponseDtos = postService.getPostByAll()
                .stream()
                .map(PostListResponseDto::fromPost)
                .toList();

        return ResponseEntity.ok(postListResponseDtos);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("postId") Long postId) {

        return ResponseEntity.ok(PostDetailResponseDto.fromPost(postService.getPostById(postId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostListResponseDto> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute PostCreateRequestDto postCreateRequestDto) {

        Long userId = userPrincipal.getId();

        return ResponseEntity.ok(PostListResponseDto.fromPost(postService.createPost(postCreateRequestDto, userId)));
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostListResponseDto> updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequestDto postUpdateRequestDto) {

        return ResponseEntity.ok(PostListResponseDto.fromPost(postService.updatePost(postId, postUpdateRequestDto, userPrincipal.getId())));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostListResponseDto> deletePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId) {

        Long userId = userPrincipal.getId();

        postService.deletePost(userId, postId);

        return ResponseEntity.noContent().build();
    }

}
