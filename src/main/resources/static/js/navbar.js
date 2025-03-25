document.addEventListener("DOMContentLoaded", () => {
  // 👉 드롭다운 토글 기능
  const profileToggle = document.querySelector(".profile-toggle");
  const dropdown = document.getElementById("dropdownMenu");

  const editProfile = document.getElementById("edit-profile");
  const editPassword = document.getElementById("edit-password");
  const logoutBtn = document.getElementById("logout");
  const loginBtn = document.getElementById("login");

  const token = localStorage.getItem("jwt");

  if (profileToggle && dropdown) {
    profileToggle.addEventListener("click", function (event) {
      event.stopPropagation();
      dropdown.classList.toggle("show");
    });
  }

  // 👉 드롭다운 바깥 클릭 시 닫기
  document.addEventListener("click", function () {
    if (dropdown && dropdown.classList.contains("show")) {
      dropdown.classList.remove("show");
    }
  });

  if(token) {
    // 로그인 상태
      if (editProfile) editProfile.style.display = "block";
      if (editPassword) editPassword.style.display = "block";
      if (logoutBtn) logoutBtn.style.display = "block";
      if (loginBtn) loginBtn.style.display = "none";
    } else {
    // 비로그인 상태
      if(editProfile) editProfile.style.display = "none";
      if(editPassword) editPassword.style.display = "none";
      if(logoutBtn) logoutBtn.style.display = "none";
      if(loginBtn) loginBtn.style.display = "block";
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
