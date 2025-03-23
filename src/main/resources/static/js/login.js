document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const submitButton = document.getElementById("loginBtn");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const email = emailInput.value;
        const password = passwordInput.value;

        try {
            const res = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({ email, password })
            });

            if (!res.ok) {
                throw new Error("로그인 실패");
            }

            const data = await res.json();

            const token = data.token;

            if (token) {
                localStorage.setItem("jwt", token);
                alert("로그인 성공!");
                window.location.href = "/posts";
            }
        } catch (err) {
            console.error("로그인 오류:", err);
            alert("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
    });

    // 회원가입 버튼 클릭 시 이동
    document.getElementById("signupGoBtn").addEventListener("click", () => {
        window.location.href = "/signup";
    });
});
