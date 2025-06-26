document.addEventListener('DOMContentLoaded', function () {
    // --- 총 상품금액 계산 ---
    const unitPrice = parseInt(document.getElementById('unitPrice')?.value || "0");
    const qtyInput = document.getElementById('qty');
    const totalPriceEl = document.getElementById('totalPrice');

    function updateTotalPrice() {
        const qty = parseInt(qtyInput.value) || 1;
        const total = unitPrice * qty;
        totalPriceEl.textContent = total.toLocaleString();
    }

    if (qtyInput) {
        qtyInput.addEventListener('input', updateTotalPrice);
        updateTotalPrice(); // 초기 표시
    }

    // --- 썸네일 클릭 시 메인 이미지 변경 ---
    const mainImage = document.getElementById('mainImage');
    const thumbnails = document.querySelectorAll('.thumbnail-image');

    thumbnails.forEach(function (thumb) {
        thumb.addEventListener('click', function () {
            const newSrc = this.getAttribute('src');
            if (mainImage && newSrc) {
                mainImage.setAttribute('src', newSrc);
            }
        });
    });
});
