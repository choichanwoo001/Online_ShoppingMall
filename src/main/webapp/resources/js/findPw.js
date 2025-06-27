document.getElementById('findPasswordForm').addEventListener('submit', function(e) {
    e.preventDefault();
    sendVerification();
});

document.getElementById('verifyBtn').addEventListener('click', function() {
    verifyCode();
});

function sendVerification() {
    const email = document.getElementById('email').value;
    const emailError = document.getElementById('emailError');
    const sendBtn = document.getElementById('sendBtn');

    // 이메일 유효성 검사
    if (!email) {
        emailError.textContent = '이메일을 입력해주세요.';
        return;
    }

    if (!validateEmail(email)) {
        emailError.textContent = '올바른 이메일 형식이 아닙니다.';
        return;
    }

    emailError.textContent = '';
    sendBtn.disabled = true;
    sendBtn.textContent = '발송 중...';

    fetch('/api/login-password/send-verification', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('verificationSection').style.display = 'block';
                sendBtn.textContent = '인증번호 재발송';
                sendBtn.disabled = false;
                alert('인증번호가 발송되었습니다. 이메일을 확인해주세요.');
            } else {
                emailError.textContent = data.message;
                sendBtn.disabled = false;
                sendBtn.textContent = '인증번호 발송';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            emailError.textContent = '서버 오류가 발생했습니다.';
            sendBtn.disabled = false;
            sendBtn.textContent = '인증번호 발송';
        });
}

function verifyCode() {
    const email = document.getElementById('email').value;
    const code = document.getElementById('verificationCode').value;
    const verificationError = document.getElementById('verificationError');
    const verifyBtn = document.getElementById('verifyBtn');

    if (!code) {
        verificationError.textContent = '인증번호를 입력해주세요.';
        return;
    }

    verificationError.textContent = '';
    verifyBtn.disabled = true;
    verifyBtn.textContent = '확인 중...';

    fetch('/api/find/password/verify', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, code })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // ① 서버가 내려준 임시 비밀번호 읽어오기
                const tempPw = data.data.tempPassword;

                // ② resultSection 안의 resultMessage 요소에 임시 비밀번호 표시
                const resultMessageEl = document.getElementById('resultMessage');
                resultMessageEl.innerHTML = `
                <p>임시 비밀번호가 발급되었습니다.</p>
                <p><strong>${tempPw}</strong></p>
                <p>이메일로도 전송되었으니 확인해 주세요.</p>
            `;

                // ③ 섹션 표시/숨김
                document.getElementById('resultSection').style.display = 'block';
                document.getElementById('verificationSection').style.display = 'none';
                document.getElementById('findPasswordForm').style.display = 'none';
            } else {
                verificationError.textContent = data.message;
                verifyBtn.disabled = false;
                verifyBtn.textContent = '인증 확인';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            verificationError.textContent = '서버 오류가 발생했습니다.';
            verifyBtn.disabled = false;
            verifyBtn.textContent = '인증 확인';
        });
}

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}