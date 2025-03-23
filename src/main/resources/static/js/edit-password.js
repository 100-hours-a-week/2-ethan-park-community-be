// 비밀번호 변경 폼 제출 시 유효성 검사
document.getElementById('passwordChangeForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼 제출 기본 동작을 막습니다.

    //값
    const password1 = document.getElementById('password1').value;
    const password2 = document.getElementById('password2').value;

    //비밀번호 관련 에러
    const password1EmptyError = document.getElementById('password1Empty-error');
    const password2EmptyError = document.getElementById('password2Empty-error');
    const passwordDiffError = document.getElementById('passwordDiff-error');
    const passwordSizeError = document.getElementById('passwordSize-error');
    const passwordPattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,20}$/;


    //로그인 버튼
    const submitButton = document.querySelector('#passwordChangeBtn'); 
    
    let isValid = true;

    // 비밀번호 검사
    if (password1 == "") {
        password1EmptyError.style.display = "block"; // 비밀번호를 입력하지 않으면 오류 메시지 표시
        password1EmptyError.textContent = "비밀번호를 입력하세요."; // 오류 메시지 수정

        isValid = false;
    } else if (!passwordPattern.test(password1) || password1.length < 8 || password1.length > 20) {
        passwordSizeError.style.display = "block";
        passwordSizeError.textContent = "비밀번호는 8자 이상, 20자 이하이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다.";
        isValid = false;
    } else {
        password1EmptyError.style.display = "none"; 
        passwordSizeError.style.display = "none"; 

        // 로그인 처리 로직을 여기에 추가
    }

    if (password2 == "") {
        password2EmptyError.style.display = "block";
        password2EmptyError.textContent = "비밀번호를 한 번더 입력해주세요.";
        isValid = false;
        
    } else if (password1 != password2) {
        passwordDiffError.style.display = "block";
        passwordDiffError.textContent = "비밀번호가 일치하지 않습니다."; // 오류 메시지 수정

        isValid = false;

    } else {
        password2EmptyError.style.display = "none"; // 비밀번호 입력이 있으면 오류 메시지 숨김
        passwordDiffError.style.display = "none"; // 비밀번호 입력이 있으면 오류 메시지 숨김
        // 로그인 처리 로직을 여기에 추가
    }

    if (isValid) {
        submitButton.style.backgroundColor = "#7F6AEE";
        
        alert("비밀번호 변경이 완료됐습니다.");

        setTimeout(function() {
            window.location.href = "/login";
        }, 1000);

    }
    else {
        alert("비밀번호 변경에 실패했습니다.");
    }

});