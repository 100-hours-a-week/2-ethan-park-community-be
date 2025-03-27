package hw6.integration.post.controller;

import hw6.integration.post.dto.PostCreateRequestDto;
import hw6.integration.post.dto.PostDetailResponseDto;
import hw6.integration.post.dto.PostListResponseDto;
import hw6.integration.post.dto.PostUpdateRequestDto;
import hw6.integration.post.service.PostReadService;
import hw6.integration.post.service.PostWriterService;
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

    private final PostReadService postReadService;
    private final PostWriterService postWriterService;

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {

        List<PostListResponseDto> postListResponseDtos = postReadService.getPostByAll()
                .stream()
                .map(PostListResponseDto::fromPost)
                .toList();


        return ResponseEntity.ok(postListResponseDtos);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(
            @PathVariable("postId") Long postId) {

        return ResponseEntity.ok(PostDetailResponseDto.fromPost(postReadService.getPostById(postId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostListResponseDto> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute PostCreateRequestDto postCreateRequestDto) {

        Long userId = userPrincipal.getId();

        return ResponseEntity.ok(PostListResponseDto.fromPost(postWriterService.createPost(postCreateRequestDto, userId)));
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostListResponseDto> updatePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId,
            @ModelAttribute PostUpdateRequestDto postUpdateRequestDto) {

        //수정 시 이미지 데이터는 안바뀜
        //저장도 안됨

        return ResponseEntity.ok(PostListResponseDto.fromPost(postWriterService.updatePost(postId, postUpdateRequestDto, userPrincipal.getId())));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<PostListResponseDto> deletePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long postId) {

        Long userId = userPrincipal.getId();

        postWriterService.deletePost(userId, postId);

        return ResponseEntity.ok().build();
    }

}
