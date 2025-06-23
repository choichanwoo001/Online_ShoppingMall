// 전역 변수
let isUserIdChecked = false;
let isEmailVerified = false;
let emailTimer = null;
let timerSeconds = 300; // 5분

// 유효성 검사 함수들
const validators = {
    userId: function(value) {
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{4,16}$/;
        if (!value) return { valid: false, message: '아이디를 입력해주세요.' };
        if (!regex.test(value)) return { valid: false, message: '영문+숫자 혼용 4~16자로 입력해주세요.' };
        return { valid: true, message: '' };
    },

    password: function(value) {
        const regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{4,16}$/;
        if (!value) return { valid: false, message: '비밀번호를 입력해주세요.' };
        if (!regex.test(value)) return { valid: false, message: '영문 대소문자/숫자 조합 4~16자로 입력해주세요.' };
        return { valid: true, message: '' };
    },

    passwordConfirm: function(value, password) {
        if (!value) return { valid: false, message: '비밀번호 확인을 입력해주세요.' };
        if (value !== password) return { valid: false, message: '비밀번호가 일치하지 않습니다.' };
        return { valid: true, message: '비밀번호가 일치합니다.', type: 'success' };
    },

    userName: function(value) {
        const regex = /^[가-힣a-zA-Z]{2,20}$/;
        if (!value) return { valid: false, message: '이름을 입력해주세요.' };
        if (!regex.test(value)) return { valid: false, message: '한글 또는 영문 2~20자로 입력해주세요.' };
        return { valid: true, message: '' };
    },

    email: function(value) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!value) return { valid: false, message: '이메일을 입력해주세요.' };
        if (!regex.test(value)) return { valid: false, message: '올바른 이메일 형식이 아닙니다.' };
        return { valid: true, message: '' };
    }
};

// 메시지 표시 함수
function showMessage(elementId, message, type = 'error') {
    const messageElement = document.getElementById(elementId);
    const inputElement = document.getElementById(elementId.replace('Message', ''));

    messageElement.textContent = message;
    messageElement.style.display = message ? 'block' : 'none';

    if (type === 'error') {
        messageElement.className = 'error-message';
        if (inputElement) {
            inputElement.classList.add('error');
            inputElement.classList.remove('success');
        }
    } else if (type === 'success') {
        messageElement.className = 'success-message';
        if (inputElement) {
            inputElement.classList.add('success');
            inputElement.classList.remove('error');
        }
    }
}

// 실시간 유효성 검사
function setupValidation() {
    const fields = ['userId', 'password', 'passwordConfirm', 'userName', 'email'];

    fields.forEach(field => {
        const element = document.getElementById(field);
        if (element) {
            element.addEventListener('input', function() {
                // 이메일 변경 시 인증 상태 초기화
                if (field === 'email' && isEmailVerified) {
                    isEmailVerified = false;
                    document.getElementById('emailVerificationDiv').style.display = 'none';
                    clearInterval(emailTimer);

                    const verifyBtn = document.getElementById('verifyEmailBtn');
                    if (verifyBtn) {
                        verifyBtn.disabled = false;
                        verifyBtn.textContent = '인증확인';
                    }
                }

                // 아이디 변경 시 중복확인 상태 초기화
                if (field === 'userId' && isUserIdChecked) {
                    isUserIdChecked = false;
                }

                validateField(field);
                checkFormValid();
            });
        }
    });
}

function validateField(fieldName) {
    const element = document.getElementById(fieldName);
    const value = element.value.trim();

    let result;
    if (fieldName === 'passwordConfirm') {
        const password = document.getElementById('password').value;
        result = validators[fieldName](value, password);
    } else {
        result = validators[fieldName](value);
    }

    // 특별 처리가 필요한 필드들
    if (fieldName === 'userId') {
        if (result.valid) {
            if (!isUserIdChecked) {
                showMessage('userIdMessage', '중복확인이 필요합니다.', 'error');
                return false; // 중복확인 안됨
            } else {
                showMessage('userIdMessage', '사용 가능한 아이디입니다.', 'success');
                return true; // 중복확인 완료
            }
        } else {
            showMessage(fieldName + 'Message', result.message, 'error');
            return false;
        }
    } else if (fieldName === 'email') {
        if (result.valid) {
            if (!isEmailVerified) {
                showMessage('emailMessage', '이메일 인증이 필요합니다.', 'error');
                return false; // 인증 안됨
            } else {
                showMessage('emailMessage', '이메일 인증이 완료되었습니다.', 'success');
                return true; // 인증 완료
            }
        } else {
            showMessage(fieldName + 'Message', result.message, 'error');
            return false;
        }
    } else {
        // 일반 필드
        showMessage(fieldName + 'Message', result.message, result.type || (result.valid ? 'success' : 'error'));
        return result.valid;
    }
}

// 아이디 중복확인
function setupDuplicateCheck() {
    const checkBtn = document.getElementById('checkDuplicateBtn');
    if (checkBtn) {
        checkBtn.addEventListener('click', async function() {
            const userId = document.getElementById('userId').value.trim();

            // 형식 검사
            const validationResult = validators.userId(userId);
            if (!validationResult.valid) {
                showMessage('userIdMessage', validationResult.message, 'error');
                return;
            }

            this.disabled = true;
            this.textContent = '확인중...';

            try {
                const response = await fetch('/api/check-duplicate-id', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ userId: userId })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    if (data.data && data.data.available) {
                        isUserIdChecked = true;
                        showMessage('userIdMessage', '사용 가능한 아이디입니다.', 'success');
                    } else {
                        isUserIdChecked = false;
                        showMessage('userIdMessage', '이미 사용중인 아이디입니다.', 'error');
                    }
                } else {
                    isUserIdChecked = false;
                    throw new Error(data.message || '중복확인 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('중복확인 오류:', error);
                isUserIdChecked = false;
                showMessage('userIdMessage', error.message, 'error');
            } finally {
                this.disabled = false;
                this.textContent = '중복확인';
                checkFormValid();
            }
        });
    }
}

// 이메일 인증 발송
function setupEmailVerification() {
    const sendBtn = document.getElementById('sendEmailBtn');
    if (sendBtn) {
        sendBtn.addEventListener('click', async function() {
            const email = document.getElementById('email').value.trim();

            // 형식 검사
            const validationResult = validators.email(email);
            if (!validationResult.valid) {
                showMessage('emailMessage', validationResult.message, 'error');
                return;
            }

            this.disabled = true;
            this.textContent = '발송중...';

            try {
                const response = await fetch('/api/send-email-verification', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email: email })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    showMessage('emailMessage', '인증메일이 발송되었습니다.', 'success');
                    document.getElementById('emailVerificationDiv').style.display = 'block';
                    startEmailTimer();
                } else {
                    throw new Error(data.message || '인증메일 발송 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('인증메일 발송 오류:', error);
                showMessage('emailMessage', error.message, 'error');
            } finally {
                this.disabled = false;
                this.textContent = '인증메일발송';
            }
        });
    }
}

// 이메일 인증 확인
function setupEmailVerificationCheck() {
    const verifyBtn = document.getElementById('verifyEmailBtn');
    if (verifyBtn) {
        verifyBtn.addEventListener('click', async function() {
            const email = document.getElementById('email').value.trim();
            const code = document.getElementById('emailVerificationCode').value.trim();

            if (!code) {
                alert('인증코드를 입력해주세요.');
                return;
            }

            this.disabled = true;
            this.textContent = '확인중...';

            try {
                const response = await fetch('/api/verify-email', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email: email, code: code })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    if (data.data && data.data.verified) {
                        isEmailVerified = true;
                        showMessage('emailMessage', '이메일 인증이 완료되었습니다.', 'success');
                        document.getElementById('emailVerificationDiv').style.display = 'none';
                        clearInterval(emailTimer);
                        this.textContent = '인증완료';
                        this.disabled = true;
                    } else {
                        showMessage('emailMessage', '인증코드가 일치하지 않습니다.', 'error');
                        this.disabled = false;
                        this.textContent = '인증확인';
                    }
                } else {
                    throw new Error(data.message || '이메일 인증 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('이메일 인증 오류:', error);
                showMessage('emailMessage', error.message, 'error');
                this.disabled = false;
                this.textContent = '인증확인';
            } finally {
                checkFormValid();
            }
        });
    }
}

// 이메일 인증 타이머
function startEmailTimer() {
    timerSeconds = 300; // 5분
    emailTimer = setInterval(function() {
        const minutes = Math.floor(timerSeconds / 60);
        const seconds = timerSeconds % 60;
        const timerElement = document.getElementById('emailTimer');
        if (timerElement) {
            timerElement.textContent =
                `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        }

        if (timerSeconds <= 0) {
            clearInterval(emailTimer);
            document.getElementById('emailVerificationDiv').style.display = 'none';
            showMessage('emailMessage', '인증시간이 만료되었습니다. 다시 발송해주세요.', 'error');
            isEmailVerified = false;
        }
        timerSeconds--;
    }, 1000);
}

// 폼 유효성 전체 검사
function checkFormValid() {
    const requiredFields = ['userId', 'password', 'passwordConfirm', 'userName', 'email'];
    let allValid = true;

    requiredFields.forEach(field => {
        const element = document.getElementById(field);
        const value = element.value.trim();

        // 기본 형식 검사
        let basicValid = false;
        if (field === 'passwordConfirm') {
            const password = document.getElementById('password').value;
            basicValid = validators[field](value, password).valid;
        } else {
            basicValid = validators[field](value).valid;
        }

        // 추가 검사 (중복확인, 인증)
        if (field === 'userId') {
            allValid = allValid && basicValid && isUserIdChecked;
        } else if (field === 'email') {
            allValid = allValid && basicValid && isEmailVerified;
        } else {
            allValid = allValid && basicValid;
        }
    });

    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.disabled = !allValid;
    }

    return allValid;
}

// 우편번호 검색 함수 (ERD 구조에 맞춰 개별 필드 저장)
function searchPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            console.log('주소 검색 결과:', data);

            // ====== 화면 표시용 필드 ======
            document.getElementById('postcode').value = data.zonecode;           // → postcode

            // ====== DB 저장용 필드들 (ERD 매핑) ======
            document.getElementById('zipCode').value = parseInt(data.zonecode);  // → zipCode
            document.getElementById('roadAddress1').value = data.roadAddress;    // → roadAddress1
            document.getElementById('jibunAddress').value = data.jibunAddress;   // → jibunAddress

            // 영문 주소 (있는 경우만)
            if (data.roadAddressEnglish) {
                document.getElementById('englishAddress').value = data.roadAddressEnglish; // → englishAddress
            }

            // 사용자가 선택한 주소를 기본 주소로 표시 (화면용)
            let displayAddr = '';
            let extraAddr = '';

            if (data.userSelectedType === 'R') {
                displayAddr = data.roadAddress;
            } else {
                displayAddr = data.jibunAddress;
            }

            // 도로명주소인 경우 추가 정보 처리
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

                // 도로명 주소 2에 추가 정보 저장
                document.getElementById('roadAddress2').value = extraAddr;       // → roadAddress2
            }

            // 화면에 표시할 주소 (사용자가 보는 용도)
            document.getElementById('address').value = displayAddr;              // → address

            // 주소명 설정 (시/구 정보)
            let addressName = '';
            if (data.sido && data.sigungu) {
                addressName = data.sido + ' ' + data.sigungu;
                document.getElementById('addressName').value = addressName;      // → addressName
            }

            // 상세주소 입력 포커스 (사용자가 직접 입력 → detailAddress)
            document.getElementById('detailAddress').focus();
            checkFormValid();
        },
        width: '100%',
        height: '100%',
        maxSuggestItems: 5
    }).open();
}

// 폼 제출
function setupFormSubmit() {
    const form = document.getElementById('signupForm');
    if (form) {
        form.addEventListener('submit', async function(e) {
            e.preventDefault();

            if (!checkFormValid()) {
                alert('모든 필수 항목을 올바르게 입력해주세요.');
                return;
            }

            const submitBtn = document.getElementById('submitBtn');
            submitBtn.disabled = true;
            submitBtn.textContent = '가입중...';

            const formData = {
                userId: document.getElementById('userId').value.trim(),
                password: document.getElementById('password').value,
                userName: document.getElementById('userName').value.trim(),
                email: document.getElementById('email').value.trim(),
                address: document.getElementById('address').value.trim(),
                detailAddress: document.getElementById('detailAddress').value.trim(),
                mobilePhone: [
                    document.getElementById('mobilePhone1').value,
                    document.getElementById('mobilePhone2').value,
                    document.getElementById('mobilePhone3').value
                ].filter(part => part).join('-')
            };

            try {
                const response = await fetch('/api/signup', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    alert('회원가입이 완료되었습니다!');
                    window.location.href = '/login';
                } else {
                    if (data.data && typeof data.data === 'object') {
                        Object.keys(data.data).forEach(field => {
                            const messageId = field + 'Message';
                            showMessage(messageId, data.data[field], 'error');
                        });
                    } else {
                        alert(data.message || '회원가입 중 오류가 발생했습니다.');
                    }
                }
            } catch (error) {
                console.error('회원가입 오류:', error);
                alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
            } finally {
                submitBtn.disabled = false;
                submitBtn.textContent = '회원가입';
                checkFormValid();
            }
        });
    }
}

// 생년월일 선택 옵션 생성
const currentYear = new Date().getFullYear();
const yearSelect = document.getElementById('birthYear');
for (let y = currentYear; y >= currentYear - 100; y--) {
    const option = document.createElement('option');
    option.value = y;
    option.textContent = y + '년';
    yearSelect.appendChild(option);
}

const monthSelect = document.getElementById('birthMonth');
for (let m = 1; m <= 12; m++) {
    const option = document.createElement('option');
    option.value = m;
    option.textContent = m + '월';
    monthSelect.appendChild(option);
}

const daySelect = document.getElementById('birthDay');
function updateDays() {
    const year = parseInt(yearSelect.value);
    const month = parseInt(monthSelect.value);
    daySelect.innerHTML = '<option value="">일</option>';

    if (!year || !month) return;

    const lastDay = new Date(year, month, 0).getDate();
    for (let d = 1; d <= lastDay; d++) {
        const option = document.createElement('option');
        option.value = d;
        option.textContent = d + '일';
        daySelect.appendChild(option);
    }
}

yearSelect.addEventListener('change', updateDays);
monthSelect.addEventListener('change', updateDays);

// 페이지 로드시 초기화
document.addEventListener('DOMContentLoaded', function() {
    setupValidation();
    setupDuplicateCheck();
    setupEmailVerification();
    setupEmailVerificationCheck();
    setupFormSubmit();
});