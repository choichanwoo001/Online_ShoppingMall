function registerCoupon() {
    const couponNumber = document.getElementById('couponNumber').value.trim();

    if (!couponNumber) {
        alert('쿠폰 번호를 입력해주세요.');
        return;
    }

    if (couponNumber.length < 10 || couponNumber.length > 35) {
        alert('쿠폰 번호는 10~35자 사이여야 합니다.');
        return;
    }

    fetch('/mypage/coupons/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'couponNumber=' + encodeURIComponent(couponNumber)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message);
            if (data.success) {
                document.getElementById('couponNumber').value = '';
                location.reload();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('쿠폰 등록 중 오류가 발생했습니다.');
        });
}

// Enter 키로 쿠폰 등록
document.addEventListener('DOMContentLoaded', function() {
    const couponInput = document.getElementById('couponNumber');
    if (couponInput) {
        couponInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                registerCoupon();
            }
        });
    }
});