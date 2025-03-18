package hw6.integration.post.controller;

import hw6.integration.post.domain.Post;
import hw6.integration.post.dto.PostResponseDto;
import hw6.integration.post.dto.PostUpdateDto;
import hw6.integration.post.service.PostService;
import hw6.integration.user.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getPostByAll();

        if(posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(PostResponseDto::fromPost)
                .toList();

        return ResponseEntity.ok(postResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        PostResponseDto postResponseDto = PostResponseDto.fromPost(post);

        return ResponseEntity.ok(postResponseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostResponseDto> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Long userId = userPrincipal.getId();

        Post post = postService.createPost(title, content, images, userId);

        PostResponseDto postResponseDto = PostResponseDto.fromPost(post);

        return ResponseEntity.ok(postResponseDto);
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostResponseDto> updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId,
            @ModelAttribute PostUpdateDto postUpdateDto) {
        Post updatedPost = postService.updatePost(postId, postUpdateDto, userPrincipal.getId());

        PostResponseDto postResponseDto = PostResponseDto.fromPost(updatedPost);

        return ResponseEntity.ok(postResponseDto);
    }

}
