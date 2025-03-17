package hw6.integration.post.controller;

import hw6.integration.post.domain.Post;
import hw6.integration.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
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

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        PostResponseDto postResponseDto = PostResponseDto.fromPost(post);

        return ResponseEntity.ok(postResponseDto);
    }

}
