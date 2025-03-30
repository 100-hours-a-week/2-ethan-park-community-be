document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("jwt");
  if (!token) {
    alert("로그인이 필요합니다.");
    location.href = "/login";
    return;
  }

  try {
    const res = await fetch("/api/users/me", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) throw new Error("인증 실패");

    const user = await res.json();

    // 이메일, 닉네임 설정
    document.getElementById("emailDisplay").innerText = `이메일: ${user.email}`;
    document.getElementById("nickname").value = user.nickname;

    const container = document.getElementById("profileImage");
    if (user.profilePath) {
      container.innerHTML = `<img class="profile-img" src="${user.profilePath}" alt="프로필 이미지">`;
    }

  } catch (err) {
    console.error("사용자 정보 오류:", err);
    alert("로그인이 필요합니다.");
    location.href = "/login";
  }

  // 수정 버튼 이벤트
  document.getElementById("updateProfileBtn").addEventListener("click", updateProfile);

  // 회원탈퇴 확인 버튼 이벤트
  document.getElementById("deleteConfirmBtn").addEventListener("click", confirmDelete);
});

// 닉네임 수정 함수
async function updateProfile() {
  const newNickname = document.getElementById("nickname").value.trim();
  const errorText = document.getElementById("errorText");
  errorText.textContent = "";

  if (!newNickname) {
    errorText.textContent = "*닉네임을 입력해주세요.";
    return;
  }
  if (newNickname.length > 10) {
    errorText.textContent = "*닉네임은 최대 10자까지 가능합니다.";
    return;
  }

  try {
    const res = await fetch("/api/me/profile", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
      },
      body: JSON.stringify({ nickname: newNickname }),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(`수정 실패: ${res.status} - ${text}`);
    }

    const toast = document.getElementById("toast");
    toast.style.display = "block";
    setTimeout(() => (toast.style.display = "none"), 2000);
  } catch (err) {
    console.error("닉네임 수정 오류:", err);
    errorText.textContent = `*닉네임 수정 실패: ${err.message}`;
  }
}

// 회원 탈퇴 함수
async function confirmDelete() {
  const token = localStorage.getItem("jwt");

  if (!token) {
    alert("로그인이 필요합니다.");
    location.href = "/login";
    return;
  }

  try {
    const res = await fetch("/api/users", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(`탈퇴 실패: ${res.status} - ${text}`);
    }

    alert("회원 탈퇴가 완료되었습니다.");
    localStorage.removeItem("jwt");
    location.href = "/login";
  } catch (err) {
    console.error("회원 탈퇴 오류:", err);
    alert("회원 탈퇴 중 오류가 발생했습니다.");
  }
}

// 모달 열기
function showModal() {
  document.getElementById("overlay").style.display = "block";
  document.getElementById("modal").style.display = "block";
}

// 모달 닫기
function hideModal() {
  document.getElementById("overlay").style.display = "none";
  document.getElementById("modal").style.display = "none";
}
