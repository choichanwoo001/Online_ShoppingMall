document.addEventListener('DOMContentLoaded', function () {
    // 가격 계산
    const unitPrice = parseInt(document.getElementById('unit-price')?.dataset.price || "0");
    const qtyInput = document.getElementById('quantity');
    const totalPriceEl = document.getElementById('total-amount');
    const qtyDisplay = document.getElementById('quantity-display');

    function updateTotalPrice() {
        const qty = parseInt(qtyInput.value) || 1;
        const total = unitPrice * qty;
        totalPriceEl.textContent = total.toLocaleString() + '원';
        qtyDisplay.textContent = qty;
    }

    if (qtyInput) {
        qtyInput.addEventListener('input', updateTotalPrice);
        updateTotalPrice();
    }

    // 썸네일 클릭 시 메인 이미지 변경
    const mainImage = document.querySelector('.main-product-image');
    const thumbnails = document.querySelectorAll('.product-thumbnail');

    thumbnails.forEach(function (thumb) {
        thumb.addEventListener('click', function () {
            const newSrc = this.getAttribute('src');
            if (mainImage && newSrc) {
                mainImage.setAttribute('src', newSrc);
            }
        });
    });

    // 옵션 선택 시 목록에 추가
    const addOptionBtn = document.getElementById('add-option');
    const optionList = document.getElementById('option-list');
    const colorSelect = document.querySelector('select[name="color"]');
    const sizeSelect = document.querySelector('select[name="size"]');

    addOptionBtn.addEventListener('click', function () {
        const color = colorSelect.options[colorSelect.selectedIndex].text;
        const size = sizeSelect.options[sizeSelect.selectedIndex].text;
        const qty = parseInt(qtyInput.value);
        const total = unitPrice * qty;

        const li = document.createElement('li');
        li.textContent = `${color} / ${size} / ${qty}개 / ${total.toLocaleString()}원`;

        const removeBtn = document.createElement('button');
        removeBtn.textContent = '❌';
        removeBtn.style.marginLeft = '0.5rem';
        removeBtn.onclick = () => li.remove();

        li.appendChild(removeBtn);
        optionList.appendChild(li);
    });
});
