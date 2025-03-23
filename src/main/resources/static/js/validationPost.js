// 입력값이 채워졌는지 확인하는 함수
export function validateForm(input) {

    if (!input.value.trim()) {
        input.classList.add("invalid");
        return false;
    } else {
        input.classList.remove("invalid");
        return true;
    }
}