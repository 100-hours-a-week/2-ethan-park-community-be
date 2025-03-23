import { validateEmail, validatePassword, validateNickname } from '../js/validationUser.js';

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("signupForm");
  const emailInput = document.getElementById("email");
  const password1Input = document.getElementById("password1");
  const password2Input = document.getElementById("password2");
  const nicknameInput = document.getElementById("nickname");
  const profileInput = document.getElementById("signup-profile-img-input");
  const profilePreview = document.getElementById("profile-preview");
  const submitButton = document.getElementById("signupBtn");

  // 이미지 미리보기
  profileInput.addEventListener("change", (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        profilePreview.src = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  });

  // 유효성 검사 이벤트
  emailInput.addEventListener("input", () => validateEmail(emailInput));
  password1Input.addEventListener("input", () => validatePassword(password1Input, password2Input));
  password2Input.addEventListener("input", () => validatePassword(password1Input, password2Input));
  nicknameInput.addEventListener("input", () => validateNickname(nicknameInput));

  // 로그인 이동 버튼
  document.getElementById("loginGoBtn").addEventListener("click", () => {
    window.location.href = "/login"; // 타임리프 방식으로 수정
  });

  // 폼 제출
  if (form) {
    form.addEventListener("submit", async (event) => {
      event.preventDefault();

      const isValidEmail = validateEmail(emailInput);
      const isValidPassword = validatePassword(password1Input, password2Input);
      const isValidNickname = validateNickname(nicknameInput);

      if (isValidEmail && isValidPassword && isValidNickname) {
        const formData = new FormData();
        formData.append("email", emailInput.value);
        formData.append("password", password1Input.value);
        formData.append("nickname", nicknameInput.value);

        const file = profileInput.files[0];
        if (file) {
          formData.append("profileImage", file); // ✅ 서버 DTO 필드명과 일치
        }

        try {
          const response = await fetch("/api/users", {
            method: "POST",
            body: formData, // Content-Type은 생략해야 브라우저가 자동 생성
          });

          if (!response.ok) throw new Error("회원가입 실패");

          alert("회원가입에 성공했습니다.");
          setTimeout(() => {
            window.location.href = "/login"; // 타임리프 라우팅에 맞게 수정
          }, 1000);
        } catch (err) {
          alert("회원가입에 실패했습니다.");
          console.error(err);
        }
      } else {
        alert("입력값을 다시 확인해주세요.");
      }
    });
  } else {
    console.error("form 요소를 찾을 수 없습니다.");
  }
});
