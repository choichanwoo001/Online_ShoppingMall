$(document).ready(function(){
    loadCartSummary();
    checkAndUpdateCartDisplay();
    // 이벤트 위임을 사용해서 동적으로 생성된 요소에도 이벤트 적용
    $(document).on('click', '.item-delete-button', function() {
        const cartItemId = $(this).closest('.cart-item').data('cart-item-id');
        deleteCartItems([cartItemId]); // 배열로 전달
    });
    // 선택삭제 버튼 (체크된 항목들 삭제)
    $(document).on('click', '#sel-item-delete-button', function() {
        deleteSelectedItems();
        console.log("선택삭제 버튼 클릭");
    });

    // 개별 체크박스 이벤트
    $(document).on('change', '.item-checkbox', function() {
        updateSelectAllState();
        updateDeleteButtonState();
    });

    // 전체선택 버튼 이벤트
    $(document).on('click', '.btn-secondary:contains("전체선택")', function() {
        const allCheckboxes = $('.item-checkbox');
        const checkedCount = $('.item-checkbox:checked').length;
        const totalCount = allCheckboxes.length;

        if (checkedCount === totalCount) {
            // 모두 선택된 상태면 모두 해제
            allCheckboxes.prop('checked', false);
        } else {
            // 일부만 선택되었거나 모두 해제된 상태면 모두 선택
            allCheckboxes.prop('checked', true);
        }

        updateSelectAllState();
        updateDeleteButtonState();
    });

    // 수량 증가 버튼 (+)
    $(document).on('click', '.quantity-plus', function() {
        const $quantityInput = $(this).siblings('.quantity-input');
        const currentQuantity = parseInt($quantityInput.val()) || 1;
        const newQuantity = currentQuantity + 1;

        if (newQuantity <= 99) {
            updateQuantity($(this).closest('.cart-item'), newQuantity);
        } else {
            alert('최대 99개까지 주문 가능합니다.');
        }
    });

    // 수량 감소 버튼 (-)
    $(document).on('click', '.quantity-minus', function() {
        const $quantityInput = $(this).siblings('.quantity-input');
        const currentQuantity = parseInt($quantityInput.val()) || 1;
        const newQuantity = currentQuantity - 1;

        if (newQuantity >= 1) {
            updateQuantity($(this).closest('.cart-item'), newQuantity);
        } else {
            alert('최소 1개는 주문해야 합니다.');
        }
    });
});
// 필수 유틸리티 함수들
function loadCartSummary() {
    $.get('/order/cart/api/summary')
        .done(function(data) {
            updateCartSummary(data.totalItemCount, data.totalAmount,
                data.originalTotalAmount, data.totalDiscountAmount);
        })
        .fail(function(xhr) {
            console.error('요약 정보 로드 실패:', xhr.responseJSON?.error);
        });
}

// addCartItem 테스트 함수
function addTestcartItem() {
    console.log("addTestcartItem 함수 실행");

    // 테스트용 데이터
    const testData = {
        productVariantId: 3,          // 테스트용 상품 변형 ID
        quantity: 2                   // 테스트 수량
    };

    console.log("테스트 데이터:", testData);

    $.ajax({
        url: '/order/cart/api/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(testData)
    })
        .done(function(response) {
            console.log('장바구니 추가 응답:', response);

            // 이미 담겨져 있었는지에 따라 다른 처리
            if (response.isAlreadyInCart) {
                // 이미 담겨있으면 alert만 띄우고 아무것도 하지 않음
                alert(`⚠️ ${response.message}\n\n상품 ID: ${response.productVariantId}\n\n장바구니를 확인해주세요.`);
            } else {
                // 새로 추가된 경우만 페이지 새로고침
                alert(`✅ ${response.message}\n\n상품 ID: ${response.productVariantId}\n수량: ${response.quantity}`);

                // 장바구니 정보 새로고침
                loadCartSummary();

            }
        })
        .fail(function(xhr) {
            console.error('장바구니 추가 실패:', xhr.responseJSON?.error);
            alert('장바구니 추가 실패: ' + (xhr.responseJSON?.error || '알 수 없는 오류'));
        });
}

// 개별 삭제와 다중 삭제를 모두 처리하는 통합 함수
function deleteCartItems(cartItemIds) {
    if (!Array.isArray(cartItemIds)) {
        cartItemIds = [cartItemIds]; // 개별 삭제면 배열로 변환
    }

    if (cartItemIds.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    const message = cartItemIds.length === 1
        ? '선택한 상품을 장바구니에서 삭제하시겠습니까?'
        : `선택한 ${cartItemIds.length}개 상품을 장바구니에서 삭제하시겠습니까?`;

    if (!confirm(message)) {
        return;
    }

    $.ajax({
        url: '/order/cart/api/items',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(cartItemIds)
    })
        .done(function() {
            // 성공 시 DOM에서 삭제된 아이템들 제거
            cartItemIds.forEach(cartItemId => {
                $(`.cart-item[data-cart-item-id="${cartItemId}"]`).remove();
            });

            // 요약 정보 갱신
            loadCartSummary();
            checkAndUpdateCartDisplay();

            const successMessage = cartItemIds.length === 1
                ? '상품이 장바구니에서 삭제되었습니다.'
                : `${cartItemIds.length}개 상품이 장바구니에서 삭제되었습니다.`;
            alert(successMessage);
        })
        .fail(function(xhr) {
            alert('삭제 중 오류가 발생했습니다: ' + (xhr.responseJSON?.error || '알 수 없는 오류'));
        });
}
function deleteSelectedItems() {
    const selectedItems = $('.item-checkbox:checked');

    if (selectedItems.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    const cartItemIds = [];
    selectedItems.each(function() {
        const cartItemId = $(this).closest('.cart-item').data('cart-item-id');
        if (cartItemId) {
            cartItemIds.push(cartItemId);
        }
    });

    if (cartItemIds.length === 0) {
        alert('삭제할 상품의 ID를 찾을 수 없습니다.');
        return;
    }

    deleteCartItems(cartItemIds);
}


function updateQuantity($cartItem, newQuantity) {
    const cartItemId = $cartItem.data('cart-item-id');
    const $quantityInput = $cartItem.find('.quantity-input');
    const originalQuantity = parseInt($quantityInput.val());

    // 기존 수량과 같으면 요청하지 않음
    if (newQuantity === originalQuantity) {
        return;
    }
    // 해당 아이템의 버튼들만 비활성화
    const $plusBtn = $cartItem.find('.quantity-plus');
    const $minusBtn = $cartItem.find('.quantity-minus');

    $quantityInput.prop('disabled', true);
    $plusBtn.prop('disabled', true);
    $minusBtn.prop('disabled', true);

    $.ajax({
        url: `/order/cart/api/items/${cartItemId}/quantity`,
        type: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify({ quantity: newQuantity })
    })
        .done(function(response) {
            // UI 업데이트
            $quantityInput.val(response.quantity);

            // 전체 요약 정보만 다시 로드 (서버에서 계산)
            loadCartSummary();

            console.log('수량 변경 완료:', response);
        })
        .fail(function(xhr) {
            // 실패 시 원래 수량으로 되돌리기
            $quantityInput.val(originalQuantity);

            const errorMessage = xhr.responseJSON?.error || '수량 변경 중 오류가 발생했습니다.';
            alert(errorMessage);

            console.error('수량 변경 실패:', errorMessage);
        })
        .always(function() {
            // 상태 복구 (가장 중요!)
            $cartItem.removeClass('updating-quantity');

            // 버튼들 다시 활성화
            $quantityInput.prop('disabled', false);
            $plusBtn.prop('disabled', false);
            $minusBtn.prop('disabled', false);

            console.log('수량 변경 완료 - 버튼 활성화됨');
        });
}
function updateSelectAllState() {
    const totalItems = $('.item-checkbox').length;
    const checkedItems = $('.item-checkbox:checked').length;
    $('#selectAll').prop('checked', totalItems > 0 && checkedItems === totalItems);
}

function updateDeleteButtonState() {
    const checkedItems = $('.item-checkbox:checked').length;
    const deleteButton = $('.item-delete-button');

    if (checkedItems > 0) {
        deleteButton.text(`선택 삭제 (${checkedItems})`);
        deleteButton.prop('disabled', false);
        deleteButton.removeClass('disabled');
    } else {
        deleteButton.text('삭제');
        deleteButton.prop('disabled', false); // 비활성화하지 않음
    }
}

// 장바구니 상태 확인 및 화면 업데이트
function checkAndUpdateCartDisplay() {
    const remainingItems = $('.cart-item').length;
    console.log('남은 아이템 수:', remainingItems);

    if (remainingItems === 0) {
        // 장바구니가 비어있으면 빈 장바구니 화면 표시
        $('.cart-items-section').html(`
                    <div class="empty-cart">
                        <div class="empty-cart-icon">🛒</div>
                        <h3 class="empty-cart-title">장바구니가 비어있습니다</h3>
                        <p class="empty-cart-message">장바구니에 상품을 담아보세요!</p>
                        <a href="/products" class="btn-shopping">쇼핑 계속하기</a>
                    </div>
                `);

        // 요약 정보도 0으로 초기화
        updateCartSummary(0, 0, 0, 0);
    } else {
        // 체크박스 상태 업데이트
        updateSelectAllState();
        updateDeleteButtonState();
    }
}

// DOM 업데이트 (데이터를 받아서 화면에 반영)
function updateCartSummary(totalCount, totalAmount, originalAmount, discountAmount) {
    $('.summary-value').eq(0).text(originalAmount.toLocaleString() + '원');
    $('.summary-value').eq(1).text(discountAmount.toLocaleString() + '원');
    $('.summary-total-value').text(totalAmount.toLocaleString() + '원');

    console.log('요약 정보 업데이트 완료:', {
        totalCount,
        totalAmount: totalAmount.toLocaleString() + '원',
        originalAmount: originalAmount.toLocaleString() + '원',
        discountAmount: discountAmount.toLocaleString() + '원'
    });
}
