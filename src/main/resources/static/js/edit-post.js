// edit-post.js
document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("jwt");
  if (!token) {
    alert("로그인이 필요합니다.");
    window.location.href = "/login";
    return;
  }

  // body 태그에 data-post-id 속성이 있어야 합니다.
  const postId = document.body.dataset.postId;
  if (!postId) {
    console.error("Post ID가 설정되지 않았습니다. body 태그에 data-post-id 속성을 추가하세요.");
    return;
  }

  // 기존 게시글 데이터 불러오기
  try {
    const res = await fetch(`/api/posts/${postId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    if (!res.ok) throw new Error(await res.text());
    const post = await res.json();

    // 게시글 내용 채우기
    document.getElementById("title").value = post.title;
    document.getElementById("content").value = post.content;
    if (post.images && post.images.length > 0) {
      const previewImg = document.getElementById("preview");
      previewImg.src = post.images[0].image_path;
      previewImg.style.display = "block";
      document.getElementById("existingImageName").textContent = post.images[0].image_name || "기존 이미지";
    }
  } catch (error) {
    console.error("게시글 정보를 불러오는 데 실패했습니다:", error);
    alert("게시글 정보를 불러오는 데 실패했습니다.");
  }

  // 이미지 미리보기 기능 (첫 번째 이미지만 표시)
  const imageInput = document.getElementById("imageInput");
  imageInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        const previewImg = document.getElementById("preview");
        previewImg.src = event.target.result;
        previewImg.style.display = "block";
      };
      reader.readAsDataURL(file);
    }
  });

  // 게시글 수정 제출 처리
  const form = document.getElementById("editForm");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // FormData를 사용하면 브라우저가 자동으로 multipart/form-data 헤더를 설정합니다.
    const formData = new FormData(form);

    try {
      const response = await fetch(`/api/posts/${postId}`, {
        method: "PATCH",
        headers: { Authorization: `Bearer ${token}` },
        body: formData
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }
      alert("게시글이 수정되었습니다!");
      window.location.href = `/posts/${postId}`;
    } catch (error) {
      console.error("게시글 수정 오류:", error);
      alert("수정 중 오류 발생: " + error.message);
    }
  });
});
