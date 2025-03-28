document.addEventListener("DOMContentLoaded", () => {
  const profileToggle = document.querySelector(".profile-toggle");
  const profileImage = document.getElementById("profileImage");
  const dropdown = document.getElementById("dropdownMenu");

  const editProfile = document.getElementById("edit-profile");
  const editPassword = document.getElementById("edit-password");
  const logoutBtn = document.getElementById("logout");
  const loginBtn = document.getElementById("login");

  const token = localStorage.getItem("jwt");

  // 👉 로그인 상태일 경우 사용자 정보 요청 후 프로필 이미지 설정
  if (token && profileImage) {
    fetch("/api/users/me", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => {
        if (!res.ok) throw new Error("사용자 정보 불러오기 실패");
        return res.json();
      })
      .then(user => {
        if (user.imagePath) {
          // 상대 경로일 경우 처리
          profileImage.src = user.imagePath.startsWith("http") ? user.imagePath : `/image_storage/profile/${user.imagePath}`;
        } else {
          profileImage.src = "/images/default-profile.png";
        }
      })
      .catch(err => {
        console.error("프로필 이미지 로딩 오류:", err);
        profileImage.src = "/images/default-profile.png";
      });
  }

  // 👉 드롭다운 토글
  if (profileToggle && dropdown) {
    profileToggle.addEventListener("click", function (event) {
      event.stopPropagation();
      dropdown.classList.toggle("show");
    });
  }

  // 👉 드롭다운 외부 클릭 시 닫기
  document.addEventListener("click", function () {
    if (dropdown && dropdown.classList.contains("show")) {
      dropdown.classList.remove("show");
    }
  });

  // 👉 로그인 여부에 따른 메뉴 표시
  if (token) {
    if (editProfile) editProfile.style.display = "block";
    if (editPassword) editPassword.style.display = "block";
    if (logoutBtn) logoutBtn.style.display = "block";
    if (loginBtn) loginBtn.style.display = "none";
  } else {
    if (editProfile) editProfile.style.display = "none";
    if (editPassword) editPassword.style.display = "none";
    if (logoutBtn) logoutBtn.style.display = "none";
    if (loginBtn) loginBtn.style.display = "block";
  }

  // 👉 로그아웃 함수 전역 등록
  window.logout = function () {
    localStorage.removeItem("jwt");
    alert("로그아웃 되었습니다.");
    location.href = "/login";
  };

  // 👉 뒤로가기 함수 전역 등록
  window.goBack = function (event) {
    event.preventDefault();
    window.history.back();
  };
});
