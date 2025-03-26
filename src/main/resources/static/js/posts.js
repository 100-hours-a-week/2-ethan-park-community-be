document.addEventListener("DOMContentLoaded", () => {
  const writePostButton = document.getElementById("writePostBtn");
  const token = localStorage.getItem("jwt");

  // 로그인 상태인 경우 버튼을 보이게, 아니라면 숨김 처리
  if (writePostButton) {
    if (token) {
      writePostButton.style.display = "block"; // 또는 inline-block 등 원하는 값
    } else {
      writePostButton.style.display = "none";
    }
  }

  // 나머지 로직 (예: 게시글 목록 로드 등)
  const postList = document.getElementById("post-list");
  postList.innerHTML = "";

  fetch("/api/posts", {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("네트워크 응답에 문제가 있습니다.");
      }
      return response.json();
    })
    .then((posts) => {
      posts.forEach((post) => {
        const postContainer = document.createElement("div");
        postContainer.classList.add("post");

        const createdAt = new Date(post.created_at).toLocaleDateString("ko-KR");

        postContainer.innerHTML = `
          <a href="/posts/${post.id}" class="post-title">${truncateTitle(post.title)}</a>
          <div class="post-meta">
              <div class="meta-left">
                  <span>좋아요 ${formatNumber(post.like_count)}</span>
                  <span>댓글: ${formatNumber(post.comment_count)}</span>
                  <span>조회수: ${formatNumber(post.view_count)}</span>
              </div>
              <div class="meta-right">${createdAt}</div>
          </div>
          <div class="post-author">작성자: ${post.authorName}</div>
        `;

        postContainer.addEventListener("click", () => {
          window.location.href = `/posts/${post.id}`;
        });

        postList.appendChild(postContainer);
      });
    })
    .catch((error) => {
      console.error("게시글 데이터를 불러오는 중 오류 발생:", error);
      alert("게시글을 가져오는 중 오류가 발생했습니다.");
    });

  // 게시글 작성 버튼 클릭 이벤트
  writePostButton.addEventListener("click", () => {
    if (!token) {
      alert("로그인이 필요합니다.");
      window.location.href = "/login";
    } else {
      window.location.href = "/posts/create";
    }
  });
});

function truncateTitle(title) {
  return title.length > 26 ? title.slice(0, 26) + "..." : title;
}

function formatNumber(value) {
  if (value >= 1000) return Math.floor(value / 1000) + "k";
  return value;
}
