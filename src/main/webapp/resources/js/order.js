// 전역 변수
let originalAvailableMileage = 0;
let currentUsedMileage = 0;
let totalItemPrice = 0;
let shippingFee = 0;
let currentCouponDiscount = 0;
let appliedCouponId = null;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    initializePrices();
    calculateTotalPrice();

    // 실시간 유효성 검사 이벤트 추가
    document.getElementById('name').addEventListener('blur', validateName);
    document.getElementById('emailId').addEventListener('blur', validateEmail);
    document.getElementById('emailDomain').addEventListener('blur', validateEmail);
    document.getElementById('midPhoneNum').addEventListener('blur', validatePhone);
    document.getElementById('lastPhoneNum').addEventListener('blur', validatePhone);
    document.getElementById('midPhoneNum').addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
    document.getElementById('lastPhoneNum').addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // 수령자 정보 이벤트 추가
    document.getElementById('receiverName').addEventListener('blur', validateReceiverName);
    document.getElementById('receiverMidPhone').addEventListener('blur', validateReceiverPhone);
    document.getElementById('receiverLastPhone').addEventListener('blur', validateReceiverPhone);
    document.getElementById('receiverMidPhone').addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
    document.getElementById('receiverLastPhone').addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    document.getElementById('agreeAll').addEventListener('change', validateAgreement);
});

// 가격 정보 초기화
function initializePrices() {
    const availableMileageElement = document.getElementById('availableMileage');
    if (availableMileageElement) {
        originalAvailableMileage = parseInt(availableMileageElement.textContent.replace(/[^0-9]/g, '')) || 0;
    }

    const shippingFeeElement = document.getElementById('shippingFee');
    if (shippingFeeElement) {
        shippingFee = parseInt(shippingFeeElement.value) || 0;
    }

    const itemPriceElements = document.querySelectorAll('[data-price]');
    totalItemPrice = 0;
    itemPriceElements.forEach(element => {
        totalItemPrice += parseInt(element.getAttribute('data-price')) || 0;
    });

    const totalItemPriceElement = document.getElementById('totalItemPrice');
    if (totalItemPriceElement) {
        totalItemPriceElement.textContent = totalItemPrice.toLocaleString() + '원';
    }

    // 배송비 표시 업데이트
    const shippingFeePriceElement = document.getElementById('shippingFeePrice');
    if (shippingFeePriceElement) {
        shippingFeePriceElement.textContent = shippingFee.toLocaleString() + '원';
    }
}

// 주문자와 동일 버튼 기능
window.copyOrdererToReceiver = function() {
    // 이름 복사
    const orderName = document.getElementById('name').value;
    document.getElementById('receiverName').value = orderName;

    // 전화번호 복사
    const orderFirstPhone = document.getElementById('firstPhoneNum').value;
    const orderMidPhone = document.getElementById('midPhoneNum').value;
    const orderLastPhone = document.getElementById('lastPhoneNum').value;

    document.getElementById('receiverFirstPhone').value = orderFirstPhone;
    document.getElementById('receiverMidPhone').value = orderMidPhone;
    document.getElementById('receiverLastPhone').value = orderLastPhone;

    // 주소 복사
    const zipcode = document.getElementById('zipcode').value;
    const address1 = document.getElementById('address1').value;
    const address2 = document.getElementById('address2').value;

    document.getElementById('receiverZipcode').value = zipcode;
    document.getElementById('receiverAddress1').value = address1;
    document.getElementById('receiverAddress2').value = address2;

    // 에러 상태 제거
    clearError('receiverName');
    clearError('receiverMidPhone');
    clearError('receiverLastPhone');
    clearError('receiverZipcode');
    clearError('receiverAddress1');

    alert('주문자 정보가 수령자 정보에 복사되었습니다.');
};

// 다음 주소 API (주문자용)
window.execDaumPostcode = function() {
    new daum.Postcode({
        oncomplete: function(data) {
            var fullAddr = data.address;
            var extraAddr = '';

            if (data.addressType === 'R') {
                if (data.bname !== '') extraAddr += data.bname;
                if (data.buildingName !== '') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    fullAddr += ' (' + extraAddr + ')';
                }
            }

            document.getElementById('zipcode').value = data.zonecode;
            document.getElementById('address1').value = fullAddr;
            document.getElementById('address2').focus();

            clearError('zipcode');
            clearError('address1');
        }
    }).open();
};

// 다음 주소 API (수령자용)
window.execDaumPostcodeReceiver = function() {
    new daum.Postcode({
        oncomplete: function(data) {
            var fullAddr = data.address;
            var extraAddr = '';

            if (data.addressType === 'R') {
                if (data.bname !== '') extraAddr += data.bname;
                if (data.buildingName !== '') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    fullAddr += ' (' + extraAddr + ')';
                }
            }

            document.getElementById('receiverZipcode').value = data.zonecode;
            document.getElementById('receiverAddress1').value = fullAddr;
            document.getElementById('receiverAddress2').focus();

            clearError('receiverZipcode');
            clearError('receiverAddress1');
        }
    }).open();
};

// 쿠폰 적용 함수
window.applyCoupon = function() {
    const couponSelect = document.getElementById('couponSelect');
    const selectedCouponId = couponSelect.value;
    const selectedOption = couponSelect.options[couponSelect.selectedIndex];

    if (!selectedCouponId) {
        showError('couponSelect', 'couponError', '쿠폰을 선택해주세요.');
        return;
    }

    // data 속성에서 쿠폰 타입과 값 가져오기
    const couponType = selectedOption.dataset.type;  // 'PERCENTAGE' 또는 'FIXED_AMOUNT' 등
    const discountValue = parseInt(selectedOption.dataset.value);
    const couponName = selectedOption.text.split(' (')[0];

    console.log('쿠폰 타입:', couponType);
    console.log('할인 값:', discountValue);

    let discountAmount = 0;

    if (couponType === 'PERCENTAGE') {
        // 퍼센트 할인
        discountAmount = Math.floor(totalItemPrice * (discountValue / 100));
        console.log(`퍼센트 할인: ${discountValue}% = ${discountAmount}원`);
    } else {
        // 고정 금액 할인 (FIXED_AMOUNT, SHIPPING_FREE 등)
        discountAmount = discountValue;
        console.log(`고정 할인: ${discountValue}원`);
    }

    // 할인 금액이 상품 총액을 초과하지 않도록 제한
    if (discountAmount > totalItemPrice) {
        discountAmount = totalItemPrice;
    }

    // 전역 변수 업데이트
    currentCouponDiscount = discountAmount;
    appliedCouponId = selectedCouponId;

    // UI 업데이트
    document.getElementById('couponDiscountPrice').textContent = '-' + discountAmount.toLocaleString() + '원';
    document.getElementById('selectedCouponName').textContent = couponName;
    document.getElementById('couponInfo').style.display = 'block';

    // 숨겨진 필드 업데이트
    document.getElementById('appliedCouponId').value = selectedCouponId;

    clearError('couponSelect');
    showSuccess('couponSuccess', '쿠폰이 적용되었습니다.');

    calculateTotalPrice();
};

// 적립금 사용 함수 (수정된 버전)
window.applyMileage = function() {
    const useMileageInput = document.getElementById('useMileage');
    const inputMileage = parseInt(useMileageInput.value) || 0;

    if (inputMileage < 0) {
        showError('useMileage', 'mileageError', '적립금은 0원 이상 입력해주세요.');
        return;
    }

    if (inputMileage % 100 !== 0) {
        showError('useMileage', 'mileageError', '적립금은 100원 단위로 사용 가능합니다.');
        return;
    }

    if (inputMileage > originalAvailableMileage) {
        showError('useMileage', 'mileageError', '보유 적립금을 초과할 수 없습니다.');
        return;
    }

    // 적립금이 결제 가능 금액을 초과하지 않도록 제한
    const payableAmount = totalItemPrice + shippingFee - currentCouponDiscount;
    if (inputMileage > payableAmount) {
        showError('useMileage', 'mileageError', '적립금 사용 금액이 결제 금액을 초과할 수 없습니다.');
        return;
    }

    currentUsedMileage = inputMileage;
    const remainingMileage = originalAvailableMileage - currentUsedMileage;

    document.getElementById('availableMileage').textContent = remainingMileage.toLocaleString();
    document.getElementById('usedMileagePrice').textContent = '-' + currentUsedMileage.toLocaleString() + '원';

    // 숨겨진 필드 업데이트
    document.getElementById('finalUsedMileage').value = currentUsedMileage;

    clearError('useMileage');
    showSuccess('mileageSuccess', '적립금이 적용되었습니다.');

    calculateTotalPrice();
};

// 총 가격 계산 함수
function calculateTotalPrice() {
    const finalPrice = Math.max(0, totalItemPrice + shippingFee - currentCouponDiscount - currentUsedMileage);
    const finalPriceElement = document.getElementById('finalPrice');
    const finalAmountElement = document.getElementById('finalAmount');

    if (finalPriceElement) {
        finalPriceElement.textContent = finalPrice.toLocaleString() + '원';
    }
    if (finalAmountElement) {
        finalAmountElement.value = finalPrice;
    }
}

// 유효성 검사 함수들
function validateName() {
    const name = document.getElementById('name').value.trim();
    if (name === '') {
        showError('name', 'nameError', '이름을 입력해주세요.');
        return false;
    }
    if (name.length < 2) {
        showError('name', 'nameError', '이름은 2자 이상 입력해주세요.');
        return false;
    }
    clearError('name');
    return true;
}

function validateReceiverName() {
    const name = document.getElementById('receiverName').value.trim();
    if (name === '') {
        showError('receiverName', 'receiverNameError', '수령자 이름을 입력해주세요.');
        return false;
    }
    if (name.length < 2) {
        showError('receiverName', 'receiverNameError', '수령자 이름은 2자 이상 입력해주세요.');
        return false;
    }
    clearError('receiverName');
    return true;
}

function validateEmail() {
    const emailId = document.getElementById('emailId').value.trim();
    const emailDomain = document.getElementById('emailDomain').value.trim();

    if (emailId === '' || emailDomain === '') {
        showError('emailId', 'emailError', '이메일을 완전히 입력해주세요.');
        showError('emailDomain', 'emailError', '');
        return false;
    }

    const emailRegex = /^[a-zA-Z0-9._-]+$/;
    if (!emailRegex.test(emailId)) {
        showError('emailId', 'emailError', '올바른 이메일 형식을 입력해주세요.');
        showError('emailDomain', 'emailError', '');
        return false;
    }

    const domainRegex = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!domainRegex.test(emailDomain)) {
        showError('emailDomain', 'emailError', '올바른 도메인을 입력해주세요.');
        showError('emailId', 'emailError', '');
        return false;
    }

    clearError('emailId');
    clearError('emailDomain');
    return true;
}

function validatePhone() {
    const midPhone = document.getElementById('midPhoneNum').value.trim();
    const lastPhone = document.getElementById('lastPhoneNum').value.trim();

    if (midPhone === '' && lastPhone === '') {
        clearError('midPhoneNum');
        clearError('lastPhoneNum');
        return true;
    }

    if (midPhone === '' || lastPhone === '') {
        showError('midPhoneNum', 'phoneError', '휴대전화 번호를 완전히 입력해주세요.');
        showError('lastPhoneNum', 'phoneError', '');
        return false;
    }

    const phoneRegex = /^\d{3,4}$/;
    if (!phoneRegex.test(midPhone) || !phoneRegex.test(lastPhone)) {
        showError('midPhoneNum', 'phoneError', '올바른 휴대전화 번호를 입력해주세요.');
        showError('lastPhoneNum', 'phoneError', '');
        return false;
    }

    clearError('midPhoneNum');
    clearError('lastPhoneNum');
    return true;
}

function validateReceiverPhone() {
    const midPhone = document.getElementById('receiverMidPhone').value.trim();
    const lastPhone = document.getElementById('receiverLastPhone').value.trim();

    if (midPhone === '' && lastPhone === '') {
        clearError('receiverMidPhone');
        clearError('receiverLastPhone');
        return true;
    }

    if (midPhone === '' || lastPhone === '') {
        showError('receiverMidPhone', 'receiverPhoneError', '수령자 휴대전화 번호를 완전히 입력해주세요.');
        showError('receiverLastPhone', 'receiverPhoneError', '');
        return false;
    }

    const phoneRegex = /^\d{3,4}$/;
    if (!phoneRegex.test(midPhone) || !phoneRegex.test(lastPhone)) {
        showError('receiverMidPhone', 'receiverPhoneError', '올바른 수령자 휴대전화 번호를 입력해주세요.');
        showError('receiverLastPhone', 'receiverPhoneError', '');
        return false;
    }

    clearError('receiverMidPhone');
    clearError('receiverLastPhone');
    return true;
}

function validateAddress() {
    const zipcode = document.getElementById('zipcode').value.trim();
    const address1 = document.getElementById('address1').value.trim();

    if (zipcode === '' || address1 === '') {
        showError('zipcode', 'addressError', '주소를 입력해주세요.');
        showError('address1', 'addressError', '');
        return false;
    }

    clearError('zipcode');
    clearError('address1');
    return true;
}

function validateReceiverAddress() {
    const zipcode = document.getElementById('receiverZipcode').value.trim();
    const address1 = document.getElementById('receiverAddress1').value.trim();

    if (zipcode === '' || address1 === '') {
        showError('receiverZipcode', 'receiverAddressError', '수령자 주소를 입력해주세요.');
        showError('receiverAddress1', 'receiverAddressError', '');
        return false;
    }

    clearError('receiverZipcode');
    clearError('receiverAddress1');
    return true;
}

function validatePaymentMethod() {
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
    if (!paymentMethod) {
        showError('', 'paymentError', '결제수단을 선택해주세요.');
        return false;
    }
    clearError('');
    return true;
}

function validateAgreement() {
    const agreeAll = document.getElementById('agreeAll').checked;
    if (!agreeAll) {
        showError('agreeAll', 'agreeError', '모든 약관에 동의해주세요.');
        return false;
    }
    clearError('agreeAll');
    return true;
}

// 전체 유효성 검사 및 제출
window.validateAndSubmit = function() {
    let isValid = true;

    isValid = validateName() && isValid;
    isValid = validateEmail() && isValid;
    isValid = validatePhone() && isValid;
    isValid = validateAddress() && isValid;
    isValid = validateReceiverName() && isValid;
    isValid = validateReceiverPhone() && isValid;
    isValid = validateReceiverAddress() && isValid;
    isValid = validatePaymentMethod() && isValid;
    isValid = validateAgreement() && isValid;

    if (!isValid) {
        alert('입력 정보를 다시 확인해주세요.');
        const firstError = document.querySelector('.error');
        if (firstError) {
            firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        return;
    }

    // 최종 주문 검증
    const finalAmount = totalItemPrice + shippingFee - currentCouponDiscount - currentUsedMileage;
    if (finalAmount < 0) {
        alert('할인 금액이 주문 금액을 초과합니다.');
        return;
    }

    // 실제 주문 제출
    document.getElementById('orderForm').submit();
};

// 에러 표시 함수
function showError(inputId, errorId, message) {
    if (inputId) {
        const input = document.getElementById(inputId);
        if (input) input.classList.add('error');
    }

    const errorElement = document.getElementById(errorId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

// 에러 제거 함수
function clearError(inputId) {
    if (inputId) {
        const input = document.getElementById(inputId);
        if (input) input.classList.remove('error');
    }
}

// 성공 메시지 표시 함수
function showSuccess(successId, message) {
    const successElement = document.getElementById(successId);
    if (successElement) {
        successElement.textContent = message;
        successElement.style.display = 'block';
        setTimeout(() => {
            successElement.style.display = 'none';
        }, 3000);
    }
}