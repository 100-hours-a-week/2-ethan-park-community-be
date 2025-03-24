document.addEventListener("DOMContentLoaded", function () {
  const postId = document.body.dataset.postId;
  const postTitleElem = document.getElementById("post-title");
  const postContentElem = document.getElementById("post-content");
  const viewCountElem = document.getElementById("view-count");
  const likeCountElem = document.getElementById("like-count");
  const commentCountElem = document.getElementById("comment-count");
  const commentList = document.getElementById("comments");
  const commentInput = document.getElementById("comment-input");
  const commentSubmitBtn = document.getElementById("submit-comment");
  const editBtn = document.getElementById("edit-btn");
  const deleteBtn = document.getElementById("delete-btn");
  const deleteModal = document.getElementById("delete-modal");
  const confirmDeleteBtn = document.getElementById("confirm-delete-btn");
  const cancelDeleteBtn = document.getElementById("cancel-btn");

  let editingCommentId = null;
  let currentUserId = null;

  async function fetchCurrentUserId() {
    try {
      const res = await fetch("/api/users/me", {
        headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
      });
      if (!res.ok) throw new Error("로그인이 필요합니다.");
      const user = await res.json();
      currentUserId = user.id;
    } catch (e) {
      console.warn("로그인 유저 확인 실패:", e);
    }
  }

  function formatCount(num) {
    if (num >= 1e6) return (num / 1e6).toFixed(1) + "M";
    if (num >= 1e3) return (num / 1e3).toFixed(1) + "k";
    return num.toString();
  }

  async function fetchPost() {
    const res = await fetch(`/api/posts/${postId}`, {
      headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
    });
    const post = await res.json();
    const postAuthorId = post.userId; // ✅ 작성자 ID 가져오기


    // 👇 여기서 비교!
      if (currentUserId === postAuthorId) {
        document.getElementById("edit-btn").style.display = "inline-block";
        document.getElementById("delete-btn").style.display = "inline-block";
      } else {
        // 작성자가 아니라면 숨김 처리
        document.getElementById("edit-btn").style.display = "none";
        document.getElementById("delete-btn").style.display = "none";
      }

    postTitleElem.innerText = post.title;
    postContentElem.innerText = post.content;
    likeCountElem.innerText = formatCount(post.like_count);
    viewCountElem.innerText = formatCount(post.view_count);

    const imageElem = document.getElementById("post-image");
    if (post.images && post.images.length > 0) {
      imageElem.src = post.images[0].image_path;
      imageElem.style.display = "block";
    } else {
      imageElem.style.display = "none";
    }
  }

  async function fetchComments() {
    const res = await fetch(`/api/posts/${postId}/comments`, {
      headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
    });
    const comments = await res.json();
    renderComments(comments);
  }

  function renderComments(comments) {
    commentList.innerHTML = "";
    comments.forEach((comment) => {
      const li = document.createElement("li");
      li.classList.add("comment");

      const createdAt = new Date(comment.created_at).toLocaleString("ko-KR");
      const updatedAt = new Date(comment.updated_at).toLocaleString("ko-KR");
      const isEdited = comment.created_at !== comment.updated_at;
      const timeText = isEdited ? `${updatedAt} 수정됨` : `${createdAt} 작성`;

      li.innerHTML = `
        <div><strong>${comment.authorName}</strong>: ${comment.content}</div>
        <div class="comment-time">${timeText}</div>
      `;

      if (currentUserId === comment.userId) {
        const actions = document.createElement("div");
        actions.classList.add("comment-actions");

        actions.innerHTML = `
          <button onclick="startEdit(${comment.id}, '${comment.content}')">수정</button>
          <button onclick="deleteComment(${comment.id})">삭제</button>
        `;
        li.appendChild(actions);
      }

      commentList.appendChild(li);
    });
    commentCountElem.innerText = comments.length;
  }

  async function submitComment() {
    const content = commentInput.value.trim();
    if (!content) return alert("댓글을 입력하세요");

    const method = editingCommentId ? "PUT" : "POST";
    const endpoint = `/api/posts/${postId}/comments${editingCommentId ? `/${editingCommentId}` : ""}`;

    const res = await fetch(endpoint, {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("jwt"),
      },
      body: JSON.stringify({ content }),
    });

    if (!res.ok) return alert("댓글 등록 또는 수정 실패");

    commentInput.value = "";
    editingCommentId = null;
    commentSubmitBtn.innerText = "댓글 등록";
    fetchComments();
  }

  window.startEdit = (id, content) => {
    editingCommentId = id;
    commentInput.value = content;
    commentSubmitBtn.innerText = "수정 완료";
  };

  window.deleteComment = async (id) => {
    if (!confirm("댓글을 삭제하시겠습니까?")) return;
    const res = await fetch(`/api/posts/${postId}/comments/${id}`, {
      method: "DELETE",
      headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
    });
    if (!res.ok) return alert("댓글 삭제 실패");
    fetchComments();
  };

  editBtn.onclick = () => (location.href = `/posts/${postId}/edit`);
  deleteBtn.onclick = () => (deleteModal.style.display = "block");
  cancelDeleteBtn.onclick = () => (deleteModal.style.display = "none");

  confirmDeleteBtn.onclick = async () => {
    const res = await fetch(`/api/posts/${postId}`, {
      method: "DELETE",
      headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
    });
    if (!res.ok) return alert("게시글 삭제 실패");
    alert("게시글 삭제 완료");
    location.href = "/posts";
  };

  commentSubmitBtn.onclick = submitComment;

  fetchCurrentUserId().then(() => {
    fetchPost();
    fetchComments();
  });
});
