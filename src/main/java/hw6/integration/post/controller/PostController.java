package hw6.integration.post.controller;

import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostResponseDto;
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
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {

        List<PostResponseDto> postResponseDtos = postService.getPostByAll()
                .stream()
                .map(PostResponseDto::fromPost)
                .toList();

        return ResponseEntity.ok(postResponseDtos);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("postId") Long postId) {

        Post post = postService.getPostById(postId);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + post.getImages());

        return ResponseEntity.ok(PostResponseDto.fromPost(postService.getPostById(postId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponseDto> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute PostCreateRequestDto postCreateRequestDto) {

        Long userId = userPrincipal.getId();

        return ResponseEntity.ok(PostResponseDto.fromPost(postService.createPost(postCreateRequestDto, userId)));
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostResponseDto> updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequestDto postUpdateRequestDto) {

        return ResponseEntity.ok(PostResponseDto.fromPost(postService.updatePost(postId, postUpdateRequestDto, userPrincipal.getId())));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostResponseDto> deletePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId) {

        Long userId = userPrincipal.getId();

        postService.deletePost(userId, postId);

        return ResponseEntity.noContent().build();
    }

}
