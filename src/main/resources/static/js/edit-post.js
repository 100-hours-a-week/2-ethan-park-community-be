document.addEventListener("DOMContentLoaded", function () {
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const form = document.getElementById("editForm");
    const imageInput = document.getElementById("imageInput");
    const previewContainer = document.getElementById("previewContainer");

    // 초기값 설정
    titleInput.value = localStorage.getItem("postTitle") || "";
    contentInput.value = localStorage.getItem("postContent") || "";

    titleInput.addEventListener("input", function () {
        if (this.value.length > 26) {
            alert("제목은 최대 26자까지 입력 가능합니다.");
            this.value = this.value.slice(0, 26);
        }
    });

    // 이미지 미리보기
    imageInput.addEventListener("change", function () {
        const files = Array.from(this.files);
        previewContainer.innerHTML = "";

        if (files.length < 1 || files.length > 10) {
            alert("이미지는 최소 1개, 최대 10개까지 업로드 가능합니다.");
            imageInput.value = "";
            return;
        }

        files.forEach(file => {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = document.createElement("img");
                img.src = e.target.result;
                img.style.width = "100px";
                img.style.height = "100px";
                img.style.objectFit = "cover";
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        });
    });

    // 폼 제출 처리
    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const files = Array.from(imageInput.files);
        if (files.length < 1 || files.length > 10) {
            alert("이미지는 최소 1개, 최대 10개까지 업로드해야 합니다.");
            return;
        }

        const formData = new FormData();
        formData.append("title", titleInput.value);
        formData.append("content", contentInput.value);
        files.forEach(file => {
            formData.append("newImages", file);
        });

        try {
            const postId = localStorage.getItem("postId");
            const response = await fetch(`/api/posts/${postId}`, {
                method: "PATCH",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("jwt")
                },
                body: formData,
            });

            if (!response.ok) throw new Error("수정 실패");

            alert("수정이 완료되었습니다.");
            window.location.href = "/posts";
        } catch (error) {
            console.error("수정 오류:", error);
            alert("수정 중 문제가 발생했습니다.");
        }
    });
});
