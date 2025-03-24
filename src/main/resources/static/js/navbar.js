document.addEventListener("DOMContentLoaded", () => {
  // 👉 드롭다운 토글 기능
  const profileToggle = document.querySelector(".profile-toggle");
  const dropdown = document.getElementById("dropdownMenu");

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
