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
      container.innerHTML = `<img id="profileImage" class="profile-img" src="${user.profilePath}" alt="프로필 이미지">`;
    }


  } catch (err) {
    console.error("사용자 정보 오류:", err);
    alert("로그인이 필요합니다.");
    location.href = "/login";
  }

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

    const toast = document.getElementById("toast");
    toast.style.display = "block";
    setTimeout(() => (toast.style.display = "none"), 2000);
  } catch (err) {
    console.error("닉네임 수정 오류:", err);
    errorText.textContent = `*닉네임 수정 실패: ${err.message}`;
  }
}
