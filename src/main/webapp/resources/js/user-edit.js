// 폼 요소들
const userEditForm = document.getElementById('userEditForm');
const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('passwordConfirm');
const userNameInput = document.getElementById('userName');
const emailInput = document.getElementById('email');
const postcodeInput = document.getElementById('postcode');
const addressInput = document.getElementById('address');
const detailAddressInput = document.getElementById('detailAddress');
const submitBtn = document.getElementById('submitBtn');
const cancelBtn = document.getElementById('cancelBtn');

// 정규식 패턴
const PASSWORD_PATTERN   = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_\-+\=\[\]{};':",<.>\/?\\|`~]).{8,16}$/;
const USER_NAME_PATTERN  = /^[가-힣a-zA-Z]{2,20}$/;
const EMAIL_PATTERN      = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 1) 기존 초기화
    initializePage();
    setupEventListeners();
    generateBirthOptions();

    // 2) 실시간 유효성 검사 추가
    [
        passwordInput, passwordConfirmInput,
        userNameInput, emailInput,
        postcodeInput, addressInput
    ].forEach(el => {
        if (!el) return;
        el.addEventListener('input',  () => validateField(el));
        el.addEventListener('blur',   () => validateField(el));
    });

    // 3) 휴대폰 숫자만 입력
    setupPhoneNumberValidation();
});

// 현재 사용자 정보 로드
function initializePage() {
    fetch('/api/user/current', { method: 'GET', headers: {'Content-Type':'application/json'} })
        .then(r => r.json())
        .then(data => {
            if (data.success) populateForm(data.user);
            else alert('사용자 정보를 불러올 수 없습니다.');
        })
        .catch(() => alert('사용자 정보 로딩 중 오류가 발생했습니다.'));
}

// 폼에 데이터 채우기
function populateForm(user) {
    document.getElementById('userId').value = user.userId || '';
    document.getElementById('userName').value = user.userName || '';
    document.getElementById('email').value = user.email || '';

    // 주소 정보
    if (user.address) {
        postcodeInput.value      = user.postcode || '';
        addressInput.value       = user.address || '';
        detailAddressInput.value = user.detailAddress || '';
        document.getElementById('roadAddress1').value = user.roadAddress1 || '';
        document.getElementById('roadAddress2').value = user.roadAddress2 || '';
        document.getElementById('jibunAddress').value  = user.jibunAddress || '';
        document.getElementById('englishAddress').value = user.englishAddress || '';
        document.getElementById('zipCode').value       = user.zipCode || '';
        document.getElementById('addressName').value   = user.addressName || '';
    }

    // 휴대폰
    if (user.mobilePhone) {
        const parts = user.mobilePhone.split('-');
        if (parts.length === 3) {
            document.getElementById('mobilePhone1').value = parts[0];
            document.getElementById('mobilePhone2').value = parts[1];
            document.getElementById('mobilePhone3').value = parts[2];
        }
    }

    // 성별
    if (user.gender) {
        const r = document.querySelector(`input[name="gender"][value="${user.gender}"]`);
        if (r) r.checked = true;
    }

    // 생년월일
    if (user.birthDate) {
        const d = new Date(user.birthDate);
        document.getElementById('birthYear').value  = d.getFullYear();
        document.getElementById('birthMonth').value = d.getMonth() + 1;
        document.getElementById('birthDay').value   = d.getDate();
    }
}

// 이벤트 리스너 설정 (기존)
function setupEventListeners() {
    passwordInput.addEventListener('blur', validatePassword);
    passwordConfirmInput.addEventListener('blur', validatePasswordConfirm);
    userNameInput.addEventListener('blur', validateUserName);
    emailInput.addEventListener('blur', validateEmail);
    userEditForm.addEventListener('submit', handleFormSubmit);
    cancelBtn.addEventListener('click', handleCancel);
}

// 휴대폰 숫자만 입력 설정 (기존)
function setupPhoneNumberValidation() {
    document.querySelectorAll('.phone-input').forEach(input => {
        input.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    });
}

// 비밀번호 검증 (기존)
function validatePassword() {
    const pwd = passwordInput.value;
    const msgEl = document.getElementById('passwordMessage');
    if (pwd === '') {
        msgEl.textContent = '';
        passwordInput.classList.remove('error','success');
        return true;
    }
    if (!PASSWORD_PATTERN.test(pwd)) {
        msgEl.textContent = '영문 대소문자/숫자/특수문자 조합 8~16자로 입력해주세요.';
        passwordInput.classList.add('error');
        passwordInput.classList.remove('success');
        return false;
    }
    msgEl.textContent = '';
    passwordInput.classList.remove('error');
    passwordInput.classList.add('success');
    return true;
}

// 비밀번호 확인 검증 (기존)
function validatePasswordConfirm() {
    const pwd = passwordInput.value;
    const cpw = passwordConfirmInput.value;
    const msgEl = document.getElementById('passwordConfirmMessage');
    if (pwd === '' && cpw === '') {
        msgEl.textContent = '';
        passwordConfirmInput.classList.remove('error','success');
        return true;
    }
    if (pwd !== cpw) {
        msgEl.textContent = '비밀번호가 일치하지 않습니다.';
        passwordConfirmInput.classList.add('error');
        passwordConfirmInput.classList.remove('success');
        return false;
    }
    msgEl.textContent = '';
    passwordConfirmInput.classList.remove('error');
    passwordConfirmInput.classList.add('success');
    return true;
}

// 이름 검증 (기존)
function validateUserName() {
    const name = userNameInput.value.trim();
    const msgEl = document.getElementById('userNameMessage');
    if (name === '') {
        msgEl.textContent = '이름을 입력해주세요.';
        userNameInput.classList.add('error');
        userNameInput.classList.remove('success');
        return false;
    }
    if (!USER_NAME_PATTERN.test(name)) {
        msgEl.textContent = '한글 또는 영문 2~20자로 입력해주세요.';
        userNameInput.classList.add('error');
        userNameInput.classList.remove('success');
        return false;
    }
    msgEl.textContent = '';
    userNameInput.classList.remove('error');
    userNameInput.classList.add('success');
    return true;
}

// 이메일 검증 (기존)
function validateEmail() {
    const em = emailInput.value.trim();
    const msgEl = document.getElementById('emailMessage');
    if (em === '') {
        msgEl.textContent = '이메일을 입력해주세요.';
        emailInput.classList.add('error');
        emailInput.classList.remove('success');
        return false;
    }
    if (!EMAIL_PATTERN.test(em)) {
        msgEl.textContent = '올바른 이메일 형식이 아닙니다.';
        emailInput.classList.add('error');
        emailInput.classList.remove('success');
        return false;
    }
    msgEl.textContent = '';
    emailInput.classList.remove('error');
    emailInput.classList.add('success');
    return true;
}

// 실시간 필드 유효성 검사 함수 (추가된 부분)
function validateField(el) {
    const id  = el.id;
    const val = el.value.trim();
    let ok = true, msg = '';
    switch (id) {
        case 'password':
            if (val && !PASSWORD_PATTERN.test(val)) {
                ok = false; msg = '영문·숫자·특수문자 포함 8~16자';
            }
            break;
        case 'passwordConfirm':
            if (val && val !== passwordInput.value.trim()) {
                ok = false; msg = '비밀번호가 일치하지 않습니다.';
            }
            break;
        case 'userName':
            if (val && !USER_NAME_PATTERN.test(val)) {
                ok = false; msg = '한글 또는 영문 2~20자';
            }
            break;
        case 'email':
            if (val && !EMAIL_PATTERN.test(val)) {
                ok = false; msg = '올바른 이메일 형식이 아닙니다.';
            }
            break;
        case 'postcode':
        case 'address':
            if (!val) {
                ok = false; msg = '필수 입력값입니다.';
            }
            break;
        default:
            return true;
    }
    const msgEl = document.getElementById(id + 'Message');
    if (ok) {
        el.classList.remove('error'); el.classList.add('success');
        if (msgEl) msgEl.textContent = '', msgEl.style.display = 'none';
    } else {
        el.classList.remove('success'); el.classList.add('error');
        if (msgEl) msgEl.textContent = msg, msgEl.style.display = 'block';
    }
    return ok;
}

// 생년월일 옵션 생성 (기존)
function generateBirthOptions() {
    const currentYear = new Date().getFullYear();
    const yearSelect  = document.getElementById('birthYear');
    const monthSelect = document.getElementById('birthMonth');
    const daySelect   = document.getElementById('birthDay');
    for (let y = currentYear; y >= 1950; y--) {
        const o = document.createElement('option');
        o.value = y; o.textContent = y;
        yearSelect.appendChild(o);
    }
    for (let m = 1; m <= 12; m++) {
        const o = document.createElement('option');
        o.value = m; o.textContent = String(m).padStart(2,'0');
        monthSelect.appendChild(o);
    }
    for (let d = 1; d <= 31; d++) {
        const o = document.createElement('option');
        o.value = d; o.textContent = String(d).padStart(2,'0');
        daySelect.appendChild(o);
    }
}

// 우편번호 검색 (기존)
function searchPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            postcodeInput.value        = data.zonecode;
            document.getElementById('zipCode').value = parseInt(data.zonecode);
            document.getElementById('roadAddress1').value = data.roadAddress;
            document.getElementById('jibunAddress').value   = data.jibunAddress;
            if (data.roadAddressEnglish) {
                document.getElementById('englishAddress').value = data.roadAddressEnglish;
            }
            let displayAddr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            let extraAddr = '';
            if (data.userSelectedType === 'R') {
                if (data.bname && /[동|로|가]$/.test(data.bname)) extraAddr += data.bname;
                if (data.buildingName && data.apartment === 'Y') {
                    extraAddr += extraAddr ? ', ' + data.buildingName : data.buildingName;
                }
                if (extraAddr) extraAddr = ' (' + extraAddr + ')';
                displayAddr += extraAddr;
                document.getElementById('roadAddress2').value = extraAddr;
            }
            document.getElementById('address').value = displayAddr;
            let addressName = data.sido && data.sigungu ? data.sido + ' ' + data.sigungu : '';
            document.getElementById('addressName').value = addressName;
            detailAddressInput.focus();
        },
        width: '100%', height: '100%', maxSuggestItems: 5
    }).open();
}

// 폼 제출 처리 (기존)
function handleFormSubmit(e) {
    e.preventDefault();
    if (!validatePassword() ||
        !validatePasswordConfirm() ||
        !validateUserName() ||
        !validateEmail()) {
        alert('입력된 정보를 다시 확인해주세요.');
        return;
    }
    const formData = collectFormData();
    submitUserUpdate(formData);
}

// 폼 데이터 수집
function collectFormData() {

    const mobilePhone1 = document.getElementById('mobilePhone1').value;
    const mobilePhone2 = document.getElementById('mobilePhone2').value;
    const mobilePhone3 = document.getElementById('mobilePhone3').value;
    const mobilePhone = (mobilePhone1 && mobilePhone2 && mobilePhone3) ?
        `${mobilePhone1}-${mobilePhone2}-${mobilePhone3}` : '';

    // 생년월일 조합
    const birthYear = document.getElementById('birthYear').value;
    const birthMonth = document.getElementById('birthMonth').value;
    const birthDay = document.getElementById('birthDay').value;
    const birthDate = (birthYear && birthMonth && birthDay) ?
        `${birthYear}-${birthMonth.padStart(2, '0')}-${birthDay.padStart(2, '0')}` : '';

    // 성별
    const genderElement = document.querySelector('input[name="gender"]:checked');
    const gender = genderElement ? genderElement.value : '';

    return {
        userId: document.getElementById('userId').value,
        password: document.getElementById('password').value || null, // 빈 값이면 null로 전송
        userName: document.getElementById('userName').value,
        email: document.getElementById('email').value,
        mobilePhone: mobilePhone,
        gender: gender,
        birthDate: birthDate,

        // 주소 정보
        postcode: document.getElementById('postcode').value,
        address: document.getElementById('address').value,
        detailAddress: document.getElementById('detailAddress').value,
        roadAddress1: document.getElementById('roadAddress1').value,
        roadAddress2: document.getElementById('roadAddress2').value,
        jibunAddress: document.getElementById('jibunAddress').value,
        englishAddress: document.getElementById('englishAddress').value,
        zipCode: document.getElementById('zipCode').value,
        addressName: document.getElementById('addressName').value
    };
}

// 사용자 정보 업데이트 서버 전송
function submitUserUpdate(formData) {
    submitBtn.disabled = true;
    submitBtn.textContent = '수정 중...';

    fetch('/api/user/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('회원정보가 성공적으로 수정되었습니다.');
                // 마이페이지로 이동 또는 새로고침
                window.location.href = '/mypage';
            } else {
                alert(data.message || '회원정보 수정에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('회원정보 수정 중 오류가 발생했습니다.');
        })
        .finally(() => {
            submitBtn.disabled = false;
            submitBtn.textContent = '회원정보수정';
        });
}

// 취소 버튼 처리
function handleCancel() {
    if (confirm('수정을 취소하시겠습니까? 변경된 내용은 저장되지 않습니다.')) {
        window.location.href = '/mypage';
    }
}