// 폼 요소들
const userEditForm = document.getElementById('userEditForm');
const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('passwordConfirm');
const userNameInput = document.getElementById('userName');
const emailInput = document.getElementById('email');
const submitBtn = document.getElementById('submitBtn');
const cancelBtn = document.getElementById('cancelBtn');

// 정규식 패턴
const PASSWORD_PATTERN = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{4,16}$/;
const USER_NAME_PATTERN = /^[가-힣a-zA-Z]{2,20}$/;
const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
    setupEventListeners();
    generateBirthOptions();
});

// 페이지 초기화
function initializePage() {
    // 현재 사용자 정보 로드
    loadCurrentUserInfo();
}

// 현재 사용자 정보 로드
function loadCurrentUserInfo() {
    fetch('/api/user/current', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                populateForm(data.user);
            } else {
                alert('사용자 정보를 불러올 수 없습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('사용자 정보 로딩 중 오류가 발생했습니다.');
        });
}

// 폼에 데이터 채우기
function populateForm(user) {
    document.getElementById('userId').value = user.userId || '';
    document.getElementById('userName').value = user.userName || '';
    document.getElementById('email').value = user.email || '';

    // 주소 정보
    if (user.address) {
        document.getElementById('postcode').value = user.postcode || '';
        document.getElementById('address').value = user.address || '';
        document.getElementById('detailAddress').value = user.detailAddress || '';

        // 숨김 필드들
        document.getElementById('roadAddress1').value = user.roadAddress1 || '';
        document.getElementById('roadAddress2').value = user.roadAddress2 || '';
        document.getElementById('jibunAddress').value = user.jibunAddress || '';
        document.getElementById('englishAddress').value = user.englishAddress || '';
        document.getElementById('zipCode').value = user.zipCode || '';
        document.getElementById('addressName').value = user.addressName || '';
    }

    if (user.mobilePhone) {
        const mobilePhoneParts = user.mobilePhone.split('-');
        if (mobilePhoneParts.length === 3) {
            document.getElementById('mobilePhone1').value = mobilePhoneParts[0];
            document.getElementById('mobilePhone2').value = mobilePhoneParts[1];
            document.getElementById('mobilePhone3').value = mobilePhoneParts[2];
        }
    }

    // 성별
    if (user.gender) {
        const genderRadio = document.querySelector(`input[name="gender"][value="${user.gender}"]`);
        if (genderRadio) {
            genderRadio.checked = true;
        }
    }

    // 생년월일
    if (user.birthDate) {
        const birthDate = new Date(user.birthDate);
        document.getElementById('birthYear').value = birthDate.getFullYear();
        document.getElementById('birthMonth').value = birthDate.getMonth() + 1;
        document.getElementById('birthDay').value = birthDate.getDate();
    }
}

// 이벤트 리스너 설정
function setupEventListeners() {
    // 비밀번호 검증
    passwordInput.addEventListener('blur', validatePassword);
    passwordConfirmInput.addEventListener('blur', validatePasswordConfirm);

    // 이름 검증
    userNameInput.addEventListener('blur', validateUserName);

    // 이메일 검증
    emailInput.addEventListener('blur', validateEmail);

    // 폼 제출
    userEditForm.addEventListener('submit', handleFormSubmit);

    // 취소 버튼
    cancelBtn.addEventListener('click', handleCancel);

    // 전화번호 숫자만 입력
    setupPhoneNumberValidation();
}

// 전화번호 숫자만 입력 설정
function setupPhoneNumberValidation() {
    const phoneInputs = document.querySelectorAll('.phone-input');
    phoneInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    });
}

// 비밀번호 검증
function validatePassword() {
    const password = passwordInput.value;
    const messageElement = document.getElementById('passwordMessage');

    if (password === '') {
        // 비밀번호가 비어있으면 현재 비밀번호 유지
        messageElement.textContent = '';
        passwordInput.classList.remove('error', 'success');
        return true;
    }

    if (!PASSWORD_PATTERN.test(password)) {
        messageElement.textContent = '영문 대소문자/숫자 조합 4~16자로 입력해주세요.';
        passwordInput.classList.add('error');
        passwordInput.classList.remove('success');
        return false;
    }

    messageElement.textContent = '';
    passwordInput.classList.remove('error');
    passwordInput.classList.add('success');
    return true;
}

// 비밀번호 확인 검증
function validatePasswordConfirm() {
    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;
    const messageElement = document.getElementById('passwordConfirmMessage');

    if (password === '' && passwordConfirm === '') {
        messageElement.textContent = '';
        passwordConfirmInput.classList.remove('error', 'success');
        return true;
    }

    if (password !== passwordConfirm) {
        messageElement.textContent = '비밀번호가 일치하지 않습니다.';
        passwordConfirmInput.classList.add('error');
        passwordConfirmInput.classList.remove('success');
        return false;
    }

    messageElement.textContent = '';
    passwordConfirmInput.classList.remove('error');
    passwordConfirmInput.classList.add('success');
    return true;
}

// 이름 검증
function validateUserName() {
    const userName = userNameInput.value.trim();
    const messageElement = document.getElementById('userNameMessage');

    if (userName === '') {
        messageElement.textContent = '이름을 입력해주세요.';
        userNameInput.classList.add('error');
        userNameInput.classList.remove('success');
        return false;
    }

    if (!USER_NAME_PATTERN.test(userName)) {
        messageElement.textContent = '한글 또는 영문 2~20자로 입력해주세요.';
        userNameInput.classList.add('error');
        userNameInput.classList.remove('success');
        return false;
    }

    messageElement.textContent = '';
    userNameInput.classList.remove('error');
    userNameInput.classList.add('success');
    return true;
}

// 이메일 검증
function validateEmail() {
    const email = emailInput.value.trim();
    const messageElement = document.getElementById('emailMessage');

    if (email === '') {
        messageElement.textContent = '이메일을 입력해주세요.';
        emailInput.classList.add('error');
        emailInput.classList.remove('success');
        return false;
    }

    if (!EMAIL_PATTERN.test(email)) {
        messageElement.textContent = '올바른 이메일 형식이 아닙니다.';
        emailInput.classList.add('error');
        emailInput.classList.remove('success');
        return false;
    }

    messageElement.textContent = '';
    emailInput.classList.remove('error');
    emailInput.classList.add('success');
    return true;
}

// 생년월일 옵션 생성
function generateBirthOptions() {
    const currentYear = new Date().getFullYear();
    const yearSelect = document.getElementById('birthYear');
    const monthSelect = document.getElementById('birthMonth');
    const daySelect = document.getElementById('birthDay');

    // 년도 옵션 (1950년부터 현재까지)
    for (let year = currentYear; year >= 1950; year--) {
        const option = document.createElement('option');
        option.value = year;
        option.textContent = year;
        yearSelect.appendChild(option);
    }

    // 월 옵션
    for (let month = 1; month <= 12; month++) {
        const option = document.createElement('option');
        option.value = month;
        option.textContent = month.toString().padStart(2, '0');
        monthSelect.appendChild(option);
    }

    // 일 옵션
    for (let day = 1; day <= 31; day++) {
        const option = document.createElement('option');
        option.value = day;
        option.textContent = day.toString().padStart(2, '0');
        daySelect.appendChild(option);
    }
}

// 우편번호 검색 (기존 signup.js와 동일)
function searchPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            console.log('주소 검색 결과:', data);

            // 화면 표시용 필드
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('zipCode').value = parseInt(data.zonecode);
            document.getElementById('roadAddress1').value = data.roadAddress;
            document.getElementById('jibunAddress').value = data.jibunAddress;

            if (data.roadAddressEnglish) {
                document.getElementById('englishAddress').value = data.roadAddressEnglish;
            }

            let displayAddr = '';
            let extraAddr = '';

            if (data.userSelectedType === 'R') {
                displayAddr = data.roadAddress;
            } else {
                displayAddr = data.jibunAddress;
            }

            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                displayAddr += extraAddr;
                document.getElementById('roadAddress2').value = extraAddr;
            }

            document.getElementById('address').value = displayAddr;

            let addressName = '';
            if (data.sido && data.sigungu) {
                addressName = data.sido + ' ' + data.sigungu;
                document.getElementById('addressName').value = addressName;
            }

            document.getElementById('detailAddress').focus();
        },
        width: '100%',
        height: '100%',
        maxSuggestItems: 5
    }).open();
}

// 폼 제출 처리
function handleFormSubmit(e) {
    e.preventDefault();

    // 유효성 검사
    const isValidPassword = validatePassword();
    const isValidPasswordConfirm = validatePasswordConfirm();
    const isValidUserName = validateUserName();
    const isValidEmail = validateEmail();

    if (!isValidUserName || !isValidEmail || !isValidPassword || !isValidPasswordConfirm) {
        alert('입력된 정보를 다시 확인해주세요.');
        return;
    }

    // 폼 데이터 수집
    const formData = collectFormData();

    // 서버로 전송
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