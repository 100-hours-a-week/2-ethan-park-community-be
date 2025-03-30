import { validateEmail, validatePassword, validateNickname } from '../js/validationUser.js';

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("signupForm");
  const emailInput = document.getElementById("email");
  const password1Input = document.getElementById("password1");
  const password2Input = document.getElementById("password2");
  const nicknameInput = document.getElementById("nickname");
  const submitButton = document.getElementById("signupBtn");


  const profileInput = document.getElementById("profile");
    const profileImage = document.getElementById("profile-image");
    const profilePlus = document.getElementById("profile-plus");
    const profilePreview = document.getElementById("profile-preview");

    if (!profileInput || !profileImage || !profilePlus || !profilePreview) {
      console.error("필수 DOM 요소가 없습니다.");
      return;
    }

    // 이미지 선택 시 미리보기 및 + 아이콘 숨기기
    profileInput.addEventListener("change", (event) => {
      const file = event.target.files[0];

      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          profileImage.src = e.target.result;
          profileImage.style.display = "block";      // 이미지 보이기
          profilePlus.style.display = "none";        // + 숨기기
        };
        reader.readAsDataURL(file);
      }
    });

    // preview 클릭 시 input 클릭 트리거
    profilePreview.addEventListener("click", () => {
      profileInput.value = ""; // 이 줄이 중요! 같은 이미지 다시 선택 가능하게 만듦

      profileInput.click();
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

          console.log(response.status);
            const resultText = await response.text();  // ✅ 내용도 확인


          if (response.status === 201) {
            alert("회원가입에 성공했습니다.");
            location.href = "/login";
          } else {
              console.warn("서버 응답 본문:", resultText);

            throw new Error("회원가입 실패");
          }

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
