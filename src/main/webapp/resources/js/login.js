document.addEventListener("DOMContentLoaded", initLoginForm);

function initLoginForm() {
    const form = document.getElementById("login-form");
    if (!form) return;

    form.addEventListener("submit", handleLoginSubmit);

    initPasswordToggle();
}

function handleLoginSubmit(event) {
    const idInput = document.getElementById("login-username");
    const pwInput = document.getElementById("login-password");

    const id = idInput?.value.trim() ?? "";
    const pw = pwInput?.value.trim() ?? "";

    if (!isValidUsername(id)) {
        showError("아이디는 5~20자의 영문자 또는 숫자여야 합니다.");
        event.preventDefault();
        return;
    }

    if (!isValidPassword(pw)) {
        showError("비밀번호는 8~20자, 영문+숫자+특수문자를 포함해야 합니다.");
        event.preventDefault();
        return;
    }

    // 통과 시 자동 submit
}

function isValidUsername(id) {
    const idRegex = /^[a-zA-Z0-9]{5,20}$/;
    return idRegex.test(id);
}

function isValidPassword(pw) {
    const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,20}$/;
    return pwRegex.test(pw);
}

function showError(message) {
    alert(message); // 또는 커스텀 UI 출력
}

function initPasswordToggle() {
    const toggleBtn = document.querySelector(".toggle-password");
    const passwordInput = document.getElementById("login-password");
    const icon = toggleBtn?.querySelector("i");

    if (!toggleBtn || !passwordInput || !icon) return;

    toggleBtn.addEventListener("click", () => {
        const isVisible = passwordInput.type === "text";
        passwordInput.type = isVisible ? "password" : "text";
        icon.classList.toggle("bi-eye");
        icon.classList.toggle("bi-eye-slash");
    });
}