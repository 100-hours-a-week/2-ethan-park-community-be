document.addEventListener("DOMContentLoaded", () => {
    const postId = document.body.dataset.postId;
    const form = document.getElementById("editForm");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const imageInput = document.getElementById("imageInput");
    const previewImg = document.getElementById("preview");
    const existingImageName = document.getElementById("existingImageName");

    console.log("postId:", postId)

    // 📌 기존 게시글 데이터 불러오기
    fetch(`/api/posts/${postId}`, {
        headers: { Authorization: "Bearer " + localStorage.getItem("jwt") }
    })
    .then(res => res.json())
    .then(post => {
        titleInput.value = post.title;
        contentInput.value = post.content;
        if (post.images && post.images.length > 0) {
            previewImg.src = post.images[0].image_path;
            previewImg.style.display = "block";
            existingImageName.textContent = post.images[0].image_name || "기존 이미지";
        }
    });

    // 📌 이미지 미리보기 기능
    imageInput.addEventListener("change", (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (event) => {
                previewImg.src = event.target.result;
                previewImg.style.display = "block";
            };
            reader.readAsDataURL(file);
        }
    });

    // 📌 게시글 수정 제출 처리
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(form);

        try {
            const response = await fetch(`/api/posts/${postId}`, {
                method: "PATCH",
                headers: { Authorization: "Bearer " + localStorage.getItem("jwt") },
                body: formData
            });

            if (!response.ok) throw new Error(await response.text());

            alert("게시글이 수정되었습니다!");
            window.location.href = `/posts/${postId}`;
        } catch (error) {
            console.error("❌ 수정 오류:", error);
            alert("수정 중 오류 발생: " + error.message);
        }
    });
});
