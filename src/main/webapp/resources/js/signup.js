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
// setupValidation 함수에서 이메일 필드에 리스너 추가
function setupValidation() {
    const fields = ['userId', 'password', 'passwordConfirm', 'userName', 'email'];

    fields.forEach(field => {
        const element = document.getElementById(field);
        if (element) {
            element.addEventListener('input', function() {
                validateField(field);

                // 🔥 이메일이 변경되면 인증 상태 초기화
                if (field === 'email') {
                    isEmailVerified = false;

                    // 인증 관련 UI 초기화
                    document.getElementById('emailVerificationDiv').style.display = 'none';
                    clearInterval(emailTimer);

                    // 인증확인 버튼 초기화 (만약 있다면)
                    const verifyBtn = document.getElementById('verifyEmailBtn');
                    if (verifyBtn) {
                        verifyBtn.disabled = false;
                        verifyBtn.textContent = '인증확인';
                        verifyBtn.style.backgroundColor = ''; // 원래 색상으로
                    }

                    console.log('이메일 변경 - 인증 상태 초기화');
                }

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

    showMessage(fieldName + 'Message', result.message, result.type || (result.valid ? 'success' : 'error'));

    if (fieldName === 'userId' && result.valid) {
        isUserIdChecked = false;
        showMessage('userIdMessage', '중복확인이 필요합니다.', 'error');
    }

    if (fieldName === 'email' && result.valid) {
        isEmailVerified = true;
        document.getElementById('emailVerificationDiv').style.display = 'none';
    }

    return result.valid;
}

// 중복 아이디 확인
function setupDuplicateCheck() {
    const checkBtn = document.getElementById('checkDuplicateBtn');
    if (checkBtn) {
        checkBtn.addEventListener('click', async function() {
            const userId = document.getElementById('userId').value.trim();

            if (!validateField('userId')) {
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
                    if (data.data.available) {
                        isUserIdChecked = true;
                        showMessage('userIdMessage', '사용 가능한 아이디입니다.', 'success');
                    } else {
                        isUserIdChecked = false;
                        showMessage('userIdMessage', '이미 사용중인 아이디입니다.', 'error');
                    }
                } else {
                    throw new Error(data.message || '중복확인 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('중복확인 오류:', error);
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

            if (!validateField('email')) {
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
                        console.log('✅ 인증 성공 - UI 업데이트');
                        isEmailVerified = true;

                        // 🎯 이메일 입력란 아래에 성공 메시지 표시
                        showMessage('emailMessage', '이메일 인증이 완료되었습니다.', 'success');

                        // 🎯 인증코드 입력창 숨기기 (이제 실행됨)
                        document.getElementById('emailVerificationDiv').style.display = 'none';
                        clearInterval(emailTimer);

                        // 버튼 상태 변경
                        this.textContent = '인증완료';
                        this.disabled = true;
                        // 🚨 return 제거! - finally 블록이 실행되도록
                    } else {
                        console.log('❌ 인증 실패 - verified 값:', data.data ? data.data.verified : 'data.data가 없음');
                        showMessage('emailMessage', '인증코드가 일치하지 않습니다.', 'error');
                    }
                } else {
                    console.log('❌ 응답 실패');
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
        }
        timerSeconds--;
    }, 1000);
}

// 폼 유효성 전체 검사
function checkFormValid() {
    const requiredFields = ['userId', 'password', 'passwordConfirm', 'userName', 'email'];
    let allValid = true;

    // 필수 필드 검사
    requiredFields.forEach(field => {
        if (!validateField(field)) {
            allValid = false;
        }
    });

    // 중복확인 및 이메일 인증 확인
    if (!isUserIdChecked || !isEmailVerified) {
        allValid = false;
    }

    const submitBtn = document.getElementById('submitBtn');
    if (submitBtn) {
        submitBtn.disabled = !allValid;
    }
}

// 우편번호 검색 함수
function searchPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 🔍 디버깅
            console.log('주소 검색 결과:', data);

            // 우편번호와 주소 정보를 해당 필드에 넣는다
            document.getElementById('postcode').value = data.zonecode;

            // 기본 주소 처리
            let addr = '';
            let extraAddr = '';

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다
            if (data.userSelectedType === 'R') { // 도로명 주소
                addr = data.roadAddress;
            } else { // 지번 주소
                addr = data.jibunAddress;
            }

            // 도로명 주소인 경우 참고항목 조합
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                addr += extraAddr;
            }

            // 주소 정보를 필드에 입력
            document.getElementById('address').value = addr;

            // 상세주소 필드로 포커스 이동
            document.getElementById('detailAddress').focus();

            // 주소가 입력되면 폼 유효성 재검사
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

            // 폼 데이터 수집
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
                ].filter(part => part).join('-') // 빈 값 제거하고 조합
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
                    // 서버 유효성 검사 실패
                    if (data.data && typeof data.data === 'object') {
                        // 각 필드별 오류 메시지 표시
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

// 페이지 로드시 초기화
document.addEventListener('DOMContentLoaded', function() {
    setupValidation();
    setupDuplicateCheck();
    setupEmailVerification();
    setupEmailVerificationCheck();
    setupFormSubmit();
});