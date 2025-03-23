document.addEventListener("DOMContentLoaded", function () {
  // detail-post.js
  const postId = document.body.dataset.postId;
  console.log("✅ postId:", postId); // 이걸 추가해서 콘솔로 확인해보세요


  const postTitleElem = document.getElementById("post-title");
  const postContentElem = document.getElementById("post-content");
  const viewCountElem = document.getElementById("view-count");
  const likeCountElem = document.getElementById("like-count");

  const commentCountElem = document.getElementById("comment-count");
  const commentList = document.getElementById("comments");

  const commentInput = document.getElementById("comment-input");
  const commentSubmitBtn = document.getElementById("submit-comment");

  let editingCommentId = null;
  let currentUserId = null;

  async function fetchCurrentUserId() {
    try {
      const res = await fetch("/api/users/me", {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwt"),
        },
      });

      if (!res.ok) throw new Error("인증되지 않음");

      const user = await res.json();
      currentUserId = user.id;
    } catch (e) {
      console.warn("로그인 유저 확인 실패:", e);
    }
  }

  function formatCount(num) {
    if (num >= 1000000) return (num / 1000000).toFixed(1) + "M";
    if (num >= 1000) return (num / 1000).toFixed(1) + "k";
    return num.toString();
  }

  function fetchPost() {
    fetch(`/api/posts/${postId}`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
      .then((res) => res.json())
      .then((post) => {
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
      });
  }

  function fetchComments() {
    fetch(`/api/posts/${postId}/comments`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt"),
      },
    })
      .then((res) => res.json())
      .then((comments) => renderComments(comments))
      .catch((err) => {
        console.error("댓글 로드 실패:", err);
      });
  }

  function renderComments(comments) {
    commentList.innerHTML = "";

    comments.forEach((comment) => {
      const li = document.createElement("li");
      li.classList.add("comment");

      const createdAt = new Date(comment.created_at).toLocaleString("ko-KR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit"
      });

      const updatedAt = new Date(comment.updated_at).toLocaleString("ko-KR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit"
      });

      const isEdited = comment.created_at !== comment.updated_at;

      const timeText = isEdited ? `${updatedAt} 수정됨` : `${createdAt} 작성`;

      const contentDiv = document.createElement("div");
      contentDiv.innerHTML = `
        <strong>${comment.authorName}</strong>: <span>${comment.content}</span>
        <div class="comment-time">${timeText}</div>
      `;

      const actionDiv = document.createElement("div");
      actionDiv.classList.add("comment-actions");

      if (currentUserId && currentUserId === comment.userId) {
        const editBtn = document.createElement("button");
        editBtn.innerText = "수정";
        editBtn.onclick = () => startEdit(comment.id, comment.content);

        const deleteBtn = document.createElement("button");
        deleteBtn.innerText = "삭제";
        deleteBtn.onclick = () => {
          if (confirm("댓글을 삭제하시겠습니까?")) {
            deleteComment(comment.id);
          }
        };

        actionDiv.appendChild(editBtn);
        actionDiv.appendChild(deleteBtn);
      }

      li.appendChild(contentDiv);
      li.appendChild(actionDiv);
      commentList.appendChild(li);
    });

    commentCountElem.innerText = comments.length;
  }


  function startEdit(commentId, content) {
    editingCommentId = commentId;
    commentInput.value = content;
    commentSubmitBtn.textContent = "수정 완료";
    showCancelButton(true);
  }

  function cancelEdit() {
    editingCommentId = null;
    commentInput.value = "";
    commentSubmitBtn.textContent = "댓글 등록";
    showCancelButton(false);
  }

  function showCancelButton(show) {
    let cancelBtn = document.getElementById("cancel-comment");
    if (!cancelBtn) {
      cancelBtn = document.createElement("button");
      cancelBtn.id = "cancel-comment";
      cancelBtn.innerText = "취소";
      cancelBtn.classList.add("btn");
      cancelBtn.style.marginLeft = "8px";
      cancelBtn.addEventListener("click", cancelEdit);
      commentInput.parentNode.appendChild(cancelBtn);
    }
    cancelBtn.style.display = show ? "inline-block" : "none";
  }

  async function submitComment() {
      const content = commentInput.value.trim();
      if (!content) return alert("댓글을 입력하세요");

      const url = `/api/posts/${postId}/comments`;
      const method = editingCommentId ? "PUT" : "POST";
      const endpoint = editingCommentId ? `${url}/${editingCommentId}` : url;

      console.log("✅ 보내는 데이터 확인:", { content }); // 🔥🔥 이 로그 추가!

      try {
        const res = await fetch(endpoint, {
          method: method,
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + localStorage.getItem("jwt"),
          },
          body: JSON.stringify({ content }),
        });

        if (!res.ok) {
          const errorText = await res.text(); // 🔥🔥 에러 원인 명확히 보기 위한 추가
          throw new Error("댓글 처리 실패:" + errorText);
        }

        commentInput.value = "";
        editingCommentId = null;
        commentSubmitBtn.textContent = "댓글 등록";
        showCancelButton(false);
        fetchComments();
      } catch (err) {
        console.error(err);
        alert("댓글 등록 또는 수정에 실패했습니다. " + err.message); // 🔥🔥 메시지 더 명확하게
      }
  }


  async function deleteComment(commentId) {
    try {
      const res = await fetch(`/api/posts/${postId}/comments/${commentId}`, {
        method: "DELETE",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwt"),
        },
      });

      if (!res.ok) throw new Error("삭제 실패");
      fetchComments();
    } catch (err) {
      console.error(err);
      alert("댓글 삭제에 실패했습니다.");
    }
  }

  document.getElementById("edit-btn").onclick = (e) => {
    const postId = e.target.dataset.postId; // 명시적으로 DOM에서 가져오기
    window.location.href = `/posts/${postId}/edit`;
  };


  // 이벤트 등록
  commentSubmitBtn.addEventListener("click", submitComment);

  // 초기 호출
  fetchCurrentUserId().then(() => {
    fetchPost();
    fetchComments();
  });
});
