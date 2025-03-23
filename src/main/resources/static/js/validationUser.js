// 오류 메시지 표시
export function showErrorMessage(element, message) {
    element.textContent = message;
    element.style.display = "block";
}

// 오류 메시지 초기화
export function clearErrorMessage(element) {
    element.textContent = "";
    element.style.display = "none";
}

// 이메일 유효성 검사 함수
export function validateEmail(emailInput) {
    const emailPattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
    const errorElement = document.getElementById("emailInput-error");

    if (!emailInput.value.trim()) {
        showErrorMessage(errorElement, "이메일을 입력하세요.");
        return false;
    }

    if (!emailPattern.test(emailInput.value)) {
        showErrorMessage(errorElement, "올바른 이메일 형식을 입력하세요.");
        return false;
    }

    clearErrorMessage(errorElement);
    return true;
}

export function validatePassword(password1Input, password2Input) {
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
        const password1Error = document.getElementById("password1Input-error");
        const password2Error = document.getElementById("password2Input-error");
        const passwordDiffError = document.getElementById("passwordDiff-error");
        const passwordSizeError = document.getElementById("passwordSize-error");

        if (!password1Input.value.trim()) {
            showErrorMessage(password1Error, "비밀번호를 입력하세요.");
            return false;
        }

        if (!passwordPattern.test(password1Input.value)) {
            showErrorMessage(passwordSizeError, "비밀번호는 8~20자, 대문자, 소문자, 숫자, 특수문자 포함 필수입니다.");
            return false;
        }

        if (!password2Input.value.trim()) {
            showErrorMessage(password2Error, "비밀번호를 한 번 더 입력하세요.");
            return false;
        }

        if (password1Input.value !== password2Input.value) {
            showErrorMessage(passwordDiffError, "비밀번호가 일치하지 않습니다.");
            return false;
        }

        clearErrorMessage(password1Error);
        clearErrorMessage(password2Error);
        clearErrorMessage(passwordDiffError);
        clearErrorMessage(passwordSizeError);
        return true;
}

export function validateNickname(nicknameInput) {
    const nicknameError = document.getElementById("nicknameEmpty-error");
    const nicknameSpaceError = document.getElementById("nicknameSpace-error");
    const nicknameSizeError = document.getElementById("nicknameSize-error");

    if (!nicknameInput.value.trim()) {
        showErrorMessage(nicknameError, "닉네임을 입력해주세요.");
        return false;
    }

    if (/\s/.test(nicknameInput.value)) {
        showErrorMessage(nicknameSpaceError, "띄어쓰기는 사용할 수 없습니다.");
        return false;
    }

    if (nicknameInput.value.length > 10) {
        showErrorMessage(nicknameSizeError, "닉네임은 최대 10자리까지 가능합니다.");
        return false;
    }

    clearErrorMessage(nicknameError);
    clearErrorMessage(nicknameSpaceError);
    clearErrorMessage(nicknameSizeError);
    return true;
}

export function validateLoginPassword(passwordInput) {
    // 예를 들어, 비밀번호 길이가 8자 이상인지 확인하는 간단한 검사
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
    const errorElement = document.getElementById("password1Input-error");
    
    if (!passwordInput.value.trim()) {
        errorElement.textContent = "비밀번호를 입력하세요.";
        errorElement.style.display = "block";
        return false;
    }
    if (!passwordPattern.test(passwordInput.value)) {
        errorElement.textContent = "비밀번호는 8~20자, 대문자, 소문자, 숫자, 특수문자 포함 필수입니다.";
        errorElement.style.display = "block";
        return false;
    }
    errorElement.textContent = "";
    errorElement.style.display = "none";
    return true;
}