document.addEventListener("DOMContentLoaded", () => {
    // ✅ 네비게이션 바 로드 함수 정의
    function loadNavbar() {
        fetch("/fragments/navbar.html") // 또는 "/navbar", "/fragment/navbar" 등
            .then(res => res.text())
            .then(html => {
                const temp = document.createElement("div");
                temp.innerHTML = html;
                const navbar = temp.querySelector("#navbar"); // 예시 ID
                if (navbar) {
                    document.body.prepend(navbar); // 혹은 원하는 위치에 append
                } else {
                    console.error("navbar 요소를 찾을 수 없습니다.");
                }
            })
            .catch(err => {
                console.error("navbar 로드 실패:", err);
            });
    }

    // 뒤로가기 함수
    function goBack(event) {
        event.preventDefault();
        window.history.back();
    }

    // 드롭다운 토글 및 클릭 이벤트 설정 함수
    function setupDropdownToggle() {
        const profileToggle = document.querySelector(".profile-toggle");
        const dropdown = document.getElementById("dropdownMenu");

        console.log("profileToggle:", profileToggle);
        console.log("dropdown:", dropdown);

        if (profileToggle && dropdown) {
            profileToggle.addEventListener("click", function(event) {
                event.stopPropagation();
                dropdown.classList.toggle("show");
            });

            const profileEdit = dropdown.querySelector("li:nth-child(1)");
            if (profileEdit) {
                profileEdit.addEventListener("click", function() {
                    window.location.href = "/edit-profile";
                });
            }

            const passwordEdit = dropdown.querySelector("li:nth-child(2)");
            if (passwordEdit) {
                passwordEdit.addEventListener("click", function() {
                    window.location.href = "/edit-password";
                });
            }

            const logout = dropdown.querySelector("li:nth-child(3)");
            if (logout) {
                logout.addEventListener("click", function() {
                    alert("로그아웃");
                });
            }
        } else {
            console.warn("Navbar 요소가 아직 로드되지 않았습니다.");
        }
    }

    // 드롭다운 닫기 기능 (클릭 시)
    document.addEventListener("click", function() {
        const dropdown = document.getElementById("dropdownMenu");
        if (dropdown && dropdown.classList.contains("show")) {
            dropdown.classList.remove("show");
        }
    });

    // ✅ 로딩 후 setupDropdownToggle 실행
    loadNavbar();
    setTimeout(setupDropdownToggle, 300); // 약간의 지연 후 실행 (navbar가 렌더링되었을 때)

    // 뒤로가기 함수 전역 등록
    window.goBack = goBack;
});
