import { validateForm } from "../js/validationPost.js";

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("postForm");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const imageInput = document.getElementById("image");
    const imagePreview = document.getElementById("imagePreview");
    const submitButton = document.getElementById("submitButton");
    const errorMessage = document.getElementById("postInput-error");

    function checkFormValidity() {
        const isValidTitle = validateForm(titleInput);
        const isValidContent = validateForm(contentInput);
        submitButton.disabled = !(isValidTitle && isValidContent);
    }

    titleInput.addEventListener("input", checkFormValidity);
    contentInput.addEventListener("input", checkFormValidity);

    imageInput.addEventListener("change", function (event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                imagePreview.src = e.target.result;
                imagePreview.style.display = "block";
            };
            reader.readAsDataURL(file);
        } else {
            imagePreview.style.display = "none";
        }
    });

    checkFormValidity();

    if (form) {
        form.addEventListener("submit", async function (event) {
            event.preventDefault();

            const isValidTitle = validateForm(titleInput);
            const isValidContent = validateForm(contentInput);

            if (isValidTitle && isValidContent) {
                const formData = new FormData();
                formData.append("title", titleInput.value);
                formData.append("content", contentInput.value);
                if (imageInput.files.length > 0) {
                    formData.append("images", imageInput.files[0]);
                }

                try {
                    const response = await fetch("/api/posts", {
                        method: "POST",
                        headers: {
                            Authorization: "Bearer " + localStorage.getItem("jwt")
                        },
                        body: formData
                    });

                    if (!response.ok) throw new Error("작성 실패");

                    alert("게시글이 작성되었습니다!");
                    window.location.href = "/posts";
                } catch (error) {
                    console.error(error);
                    alert("작성 중 오류 발생");
                }
            } else {
                errorMessage.style.display = "block";
                errorMessage.textContent = "제목과 내용을 모두 작성해주세요.";
            }
        });
    }
});
