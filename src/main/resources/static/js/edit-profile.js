document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwt");

  // ✅ 인증 안된 경우 로그인 페이지로 리다이렉트
  if (!token) {
    alert("로그인이 필요합니다.");
    location.href = "/login";
    return;
  }

  // ✅ 사용자 정보 불러오기
  fetch("/api/users/me", {
    headers: {
      Authorization: "Bearer " + token,
    },
  })
    .then((res) => {
      if (!res.ok) throw new Error("인증 실패");
      return res.json();
    })
    .then((user) => {
      // ✅ 이메일, 닉네임 설정
      document.getElementById("emailDisplay").innerText = `이메일: ${user.email}`;
      document.getElementById("nickname").value = user.nickname;

      // ✅ 프로필 이미지 설정
      if (user.profilePath) {
        const profileImage = document.getElementById("profileImage");
        profileImage.src = user.profilePath;
        profileImage.style.display = "block";
      }
    })
    .catch((err) => {
      console.error("사용자 정보 오류:", err);
      alert("로그인이 필요합니다.");
      location.href = "/login";
    });


  // ✅ 수정 버튼 이벤트 연결
  document.getElementById("updateProfileBtn").addEventListener("click", updateProfile);
});

function updateProfile() {
   const nickname = document.getElementById("nickname").value.trim();
   const errorText = document.getElementById("errorText");
   const imageFile = document.getElementById("profileImage").files[0];

   errorText.textContent = "";

   if (!nickname) {
     errorText.textContent = "*닉네임을 입력해주세요.";
     return;
   }
   if (nickname.length > 10) {
     errorText.textContent = "*닉네임은 최대 10자까지 가능합니다.";
     return;
   }

   const formData = new FormData();
   formData.append("nickname", nickname);
   if (imageFile) {
     formData.append("profileImage", imageFile);
   }

   fetch("/api/me/profile", {
     method: "PUT",
     headers: {
       Authorization: "Bearer " + localStorage.getItem("jwt"),
     },
     body: formData,
   })
     .then((res) => {
       if (!res.ok) throw new Error("수정 실패");
       return res.json();
     })
     .then(() => {
       const toast = document.getElementById("toast");
       toast.style.display = "block";
       setTimeout(() => (toast.style.display = "none"), 2000);
     })
     .catch((err) => {
       console.error("닉네임 수정 오류:", err);
       errorText.textContent = "*닉네임 수정 실패";
     });
 }


// ✅ 탈퇴 관련
function showModal() {
  document.getElementById("overlay").style.display = "block";
  document.getElementById("modal").style.display = "block";
}

function hideModal() {
  document.getElementById("overlay").style.display = "none";
  document.getElementById("modal").style.display = "none";
}

function confirmDelete() {
  fetch("/api/users/me", {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("jwt"),
    },
  })
    .then((res) => {
      if (!res.ok) throw new Error("회원 탈퇴 실패");
      alert("회원 탈퇴 완료!");
      localStorage.removeItem("jwt");
      location.href = "/login";
    })
    .catch((err) => {
      console.error("회원 탈퇴 오류:", err);
      alert("탈퇴 중 문제가 발생했습니다.");
    });

  hideModal();
}
