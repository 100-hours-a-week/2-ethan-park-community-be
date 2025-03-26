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

    // 프로필 이미지 설정
    if (user.profilePath) {
      const profileImage = document.getElementById("profileImage");
      profileImage.src = user.profilePath;
      profileImage.style.display = "block";
    }
  } catch (err) {
    console.error("사용자 정보 오류:", err);
    alert("로그인이 필요합니다.");
    location.href = "/login";
  }

  // 버튼 이벤트 연결
  document.getElementById("updateProfileBtn").addEventListener("click", updateProfile);
});

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
    // 수정 성공 시 토스트 메시지 표시
    const toast = document.getElementById("toast");
    toast.style.display = "block";
    setTimeout(() => (toast.style.display = "none"), 2000);
  } catch (err) {
    console.error("닉네임 수정 오류:", err);
    errorText.textContent = `*닉네임 수정 실패: ${err.message}`;
  }
}

function showModal() {
  document.getElementById("overlay").style.display = "block";
  document.getElementById("modal").style.display = "block";
}

function hideModal() {
  document.getElementById("overlay").style.display = "none";
  document.getElementById("modal").style.display = "none";
}

async function confirmDelete() {
  try {
    const res = await fetch("/api/users", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("jwt")}`,
      },
    });
    if (!res.ok) throw new Error("회원 탈퇴 실패");
    alert("회원 탈퇴 완료!");
    localStorage.removeItem("jwt");
    location.href = "/login";
  } catch (err) {
    console.error("회원 탈퇴 오류:", err);
    alert("탈퇴 중 문제가 발생했습니다.");
  } finally {
    hideModal();
  }
}
