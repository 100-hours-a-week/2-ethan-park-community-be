package hw6.integration.post.controller;

import hw6.integration.post.dto.PostListResponseDto;
import hw6.integration.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostPageController {

    private final PostReadService postReadService;

    // 전체 게시글 페이지
    @GetMapping("/posts")
    public String postListPage(Model model) {
        model.addAttribute("posts", postReadService.getPostByAll()
                .stream()
                .map(PostListResponseDto::fromPost)
                .toList());
        return "posts/posts"; // templates/posts/posts.html
    }

    // 게시글 상세 페이지
    @GetMapping("/posts/{postId}")
    public String postDetailPage(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("postId", postId); // JavaScript에서 사용 가능
        return "detail-post/detail-post"; // templates/detail-post/detail-post.html
    }

    // 게시글 수정 페이지
    @GetMapping("/posts/{postId}/edit")
    public String postEditPage(@PathVariable Long postId, Model model) {
        model.addAttribute("postId", postId);
        return "edit-post/edit-post";
    }

    // 게시글 작성 페이지
    @GetMapping("/posts/create")
    public String postCreatePage() {

        return "make-post/make-post";
    }
}
