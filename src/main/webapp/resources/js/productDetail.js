//
// document.addEventListener('DOMContentLoaded', function () {
//     const unitPrice = parseInt(document.getElementById('unit-price')?.dataset.price || "0");
//     const qtyInput = document.getElementById('quantity');
//     const totalPriceEl = document.getElementById('total-amount');
//     const qtyDisplay = document.getElementById('quantity-display');
//
//     function updateTotalPrice() {
//         const qty = parseInt(qtyInput.value) || 1;
//         const total = unitPrice * qty;
//         totalPriceEl.textContent = total.toLocaleString() + '원';
//         qtyDisplay.textContent = qty;
//     }
//
//     if (qtyInput) {
//         qtyInput.addEventListener('input', updateTotalPrice);
//         updateTotalPrice();
//     }
//
//     const mainImage = document.querySelector('.main-product-image');
//     const thumbnails = document.querySelectorAll('.product-thumbnail');
//     thumbnails.forEach(thumb => {
//         thumb.addEventListener('click', () => {
//             const newSrc = thumb.getAttribute('src');
//             if (mainImage && newSrc) {
//                 mainImage.setAttribute('src', newSrc);
//             }
//         });
//     });
//
//     // // 옵션 추가 기능
//     // const addOptionBtn = document.getElementById('add-option');
//     // const optionList = document.getElementById('option-list');
//     // const colorSelect = document.getElementById('color-select');
//     // const sizeSelect = document.getElementById('size-select');
//     //
//     // if (addOptionBtn && colorSelect && sizeSelect && qtyInput && optionList) {
//     //     addOptionBtn.addEventListener('click', function () {
//     //         const colorId = colorSelect.value;
//     //         const sizeId = sizeSelect.value;
//     //         const colorText = colorSelect.options[colorSelect.selectedIndex].text;
//     //         const sizeText = sizeSelect.options[sizeSelect.selectedIndex].text;
//     //         const qty = parseInt(qtyInput.value);
//     //         const total = unitPrice * qty;
//     //
//     //         // 중복 방지
//     //         const isDuplicate = Array.from(optionList.children).some(item =>
//     //             item.dataset.colorId === colorId && item.dataset.sizeId === sizeId
//     //         );
//     //         if (isDuplicate) {
//     //             alert('이미 추가된 옵션입니다.');
//     //             return;
//     //         }
//     //
//     //         const li = document.createElement('li');
//     //         li.dataset.colorId = colorId;
//     //         li.dataset.sizeId = sizeId;
//     //         li.textContent = `${colorText} / ${sizeText} / ${qty}개 / ${total.toLocaleString()}원`;
//     //
//     //         const removeBtn = document.createElement('button');
//     //         removeBtn.textContent = '❌';
//     //         removeBtn.style.marginLeft = '0.5rem';
//     //         removeBtn.onclick = () => li.remove();
//     //
//     //         li.appendChild(removeBtn);
//     //         optionList.appendChild(li);
//     //     });
//     addOptionBtn.addEventListener('click', function () {
//         const colorId = colorSelect.value;
//         const sizeId = sizeSelect.value;
//         const colorText = colorSelect.options[colorSelect.selectedIndex].text;
//         const sizeText = sizeSelect.options[sizeSelect.selectedIndex].text;
//         const qty = parseInt(qtyInput.value);
//         const total = unitPrice * qty;
//
//         const isDuplicate = Array.from(optionList.children).some(item =>
//             item.dataset.colorId === colorId && item.dataset.sizeId === sizeId
//         );
//         if (isDuplicate) {
//             alert('이미 추가된 옵션입니다.');
//             return;
//         }
//
//         const li = document.createElement('li');
//         li.dataset.colorId = colorId;
//         li.dataset.sizeId = sizeId;
//         li.dataset.qty = qty;
//         li.dataset.price = unitPrice;
//         li.textContent = `${colorText} / ${sizeText} / ${qty}개 / ${total.toLocaleString()}원`;
//
//         const removeBtn = document.createElement('button');
//         removeBtn.textContent = '❌';
//         removeBtn.style.marginLeft = '0.5rem';
//         removeBtn.onclick = () => {
//             li.remove();
//             updateTotalSummary();  // 삭제 후 총합 재계산
//         };
//
//         li.appendChild(removeBtn);
//         optionList.appendChild(li);
//         updateTotalSummary(); // 추가 후 총합 계산
//     });
//
// });


document.addEventListener('DOMContentLoaded', function () {
    const unitPrice = parseInt(document.getElementById('unit-price')?.dataset.price || "0");
    const qtyInput = document.getElementById('quantity');
    const totalPriceEl = document.getElementById('total-amount');
    const qtyDisplay = document.getElementById('quantity-display');

    function updateTotalSummary() {
        const optionItems = document.querySelectorAll('#option-list li');
        let totalAmount = 0;
        let totalQty = 0;

        optionItems.forEach(li => {
            const qty = parseInt(li.dataset.qty || '1');
            const price = parseInt(li.dataset.price || '0');
            totalAmount += qty * price;
            totalQty += qty;
        });

        totalPriceEl.textContent = totalAmount.toLocaleString() + '원';
        qtyDisplay.textContent = totalQty;
    }

    function updateSingleTotalPrice() {
        const qty = parseInt(qtyInput.value) || 1;
        const total = unitPrice * qty;
        totalPriceEl.textContent = total.toLocaleString() + '원';
        qtyDisplay.textContent = qty;
    }

    if (qtyInput) {
        qtyInput.addEventListener('input', () => {
            // 단일 총금액 계산은 옵션 미사용 시만 반영
            const hasOption = document.querySelectorAll('#option-list li').length > 0;
            if (!hasOption) updateSingleTotalPrice();
        });
        updateSingleTotalPrice();
    }

    const mainImage = document.querySelector('.main-product-image');
    const thumbnails = document.querySelectorAll('.product-thumbnail');
    thumbnails.forEach(thumb => {
        thumb.addEventListener('click', () => {
            const newSrc = thumb.getAttribute('src');
            if (mainImage && newSrc) {
                mainImage.setAttribute('src', newSrc);
            }
        });
    });

    const addOptionBtn = document.getElementById('add-option');
    const optionList = document.getElementById('option-list');
    const colorSelect = document.getElementById('color-select');
    const sizeSelect = document.getElementById('size-select');

    if (addOptionBtn && colorSelect && sizeSelect && qtyInput && optionList) {
        addOptionBtn.addEventListener('click', function () {
            const colorId = colorSelect.value;
            const sizeId = sizeSelect.value;
            const colorText = colorSelect.options[colorSelect.selectedIndex].text;
            const sizeText = sizeSelect.options[sizeSelect.selectedIndex].text;
            const qty = parseInt(qtyInput.value);
            const total = unitPrice * qty;

            const isDuplicate = Array.from(optionList.children).some(item =>
                item.dataset.colorId === colorId && item.dataset.sizeId === sizeId
            );
            if (isDuplicate) {
                alert('이미 추가된 옵션입니다.');
                return;
            }

            const li = document.createElement('li');
            li.dataset.colorId = colorId;
            li.dataset.sizeId = sizeId;
            li.dataset.qty = qty;
            li.dataset.price = unitPrice;
            li.textContent = `${colorText} / ${sizeText} / ${qty}개 / ${total.toLocaleString()}원`;

            const removeBtn = document.createElement('button');
            removeBtn.textContent = '❌';
            removeBtn.style.marginLeft = '0.5rem';
            removeBtn.onclick = () => {
                li.remove();
                updateTotalSummary();
            };

            li.appendChild(removeBtn);
            optionList.appendChild(li);
            updateTotalSummary();
        });
    }

    const form = document.getElementById('cart-form');
    form.addEventListener('submit', function (e) {
        // 개발 중이면 방지
        e.preventDefault();
    });
});
