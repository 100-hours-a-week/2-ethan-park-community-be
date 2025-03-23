// user.json에서 id가 1인 사용자의 이메일과 닉네임 불러오기
document.addEventListener("DOMContentLoaded", () => {
    fetch("../data/user.json")
      .then(response => {
        if (!response.ok) {
          throw new Error("네트워크 응답에 문제가 있습니다.");
        }
        return response.json();
      })
      .then(data => {
        const user = data.users.find(u => u.id === 1);
        if (user) {
          // 이메일을 표시할 영역에 user.email을 출력
          const emailDisplay = document.getElementById("emailDisplay");
          emailDisplay.innerText = `이메일: ${user.email}`;
          
          // 닉네임 입력란에 기본값으로 user.nickname 설정
          const nicknameInput = document.getElementById("nickname");
          nicknameInput.value = user.nickname;
        } else {
          console.error("id가 1인 사용자를 찾을 수 없습니다.");
        }
      })
      .catch(error => {
        console.error("사용자 데이터를 불러오는 중 오류 발생:", error);
      });
  });
  
  // 회원정보 수정 및 탈퇴 관련 함수들
  function updateProfile() {
    const nickname = document.getElementById("nickname").value.trim();
    const errorText = document.getElementById("errorText");
    errorText.textContent = "";
  
    if (!nickname) {
      errorText.textContent = "*닉네임을 입력해주세요.";
      return;
    }
    if (nickname.length > 10) {
      errorText.textContent = "*닉네임은 최대 10자까지 가능합니다.";
      return;
    }
  
    const toast = document.getElementById("toast");
    toast.style.display = "block";
    setTimeout(() => (toast.style.display = "none"), 2000);
  
    // 실제 서버 전송 로직이 있다면 이곳에서 처리
  }
  
  function showModal() {
    document.getElementById("overlay").style.display = "block";
    document.getElementById("modal").style.display = "block";
  }
  
  function hideModal() {
    document.getElementById("overlay").style.display = "none";
    document.getElementById("modal").style.display = "none";
  }
  
  function confirmDelete() {
    alert("회원 탈퇴 완료! 로그인 페이지로 이동");
    hideModal();
    window.location.href = "/login";
  }
  
  // 드롭다운 닫기 (프로필 이미지 외부 클릭 시)
  window.onclick = function (event) {
    const dropdown = document.getElementById("dropdownMenu");
    if (dropdown && !event.target.matches(".profile-img")) {
      dropdown.style.display = "none";
    }
  };
  