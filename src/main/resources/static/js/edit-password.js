document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById('passwordChangeForm');

  form.addEventListener('submit', async function(event) {
    event.preventDefault();

    // 입력 값
    const password1 = document.getElementById('password1').value.trim();
    const password2 = document.getElementById('password2').value.trim();

    // 에러 요소
    const password1EmptyError = document.getElementById('password1Empty-error');
    const password2EmptyError = document.getElementById('password2Empty-error');
    const passwordDiffError = document.getElementById('passwordDiff-error');
    const passwordSizeError = document.getElementById('passwordSize-error');

    // 유효성 검사 패턴
    const passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,20}$/;

    // 초기화
    password1EmptyError.textContent = "";
    password2EmptyError.textContent = "";
    passwordDiffError.textContent = "";
    passwordSizeError.textContent = "";

    let isValid = true;

    // 비밀번호1 유효성 검사
    if (!password1) {
      password1EmptyError.textContent = "비밀번호를 입력해주세요.";
      isValid = false;
    } else if (!passwordPattern.test(password1)) {
      passwordSizeError.textContent = "8~20자, 대소문자, 숫자, 특수문자 포함 필수입니다.";
      isValid = false;
    }

    // 비밀번호2 유효성 검사
    if (!password2) {
      password2EmptyError.textContent = "비밀번호 확인을 입력해주세요.";
      isValid = false;
    } else if (password1 !== password2) {
      passwordDiffError.textContent = "비밀번호가 일치하지 않습니다.";
      isValid = false;
    }

    // 유효하면 서버 요청
    if (isValid) {
      try {
        const response = await fetch("/api/me/password", {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + localStorage.getItem("jwt"),
          },
          body: JSON.stringify({ password: password1 })
        });

        if (!response.ok) throw new Error("수정 실패");

        const toast = document.getElementById("toast");
        toast.style.display = "block";
        setTimeout(() => {
          toast.style.display = "none";
          location.href = "/login";
        }, 2000);
      } catch (err) {
        alert("비밀번호 수정에 실패했습니다.");
        console.error("비밀번호 수정 오류:", err);
      }
    }
  });
});
