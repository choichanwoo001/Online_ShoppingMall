let sessionData = {
    loggedIn: true,
    userId: 'user123',
    wishlist: [
        {
            id: 1,
            name: 'A. blusher 루민의 슬랙스',
            price: 40500,
            originalPrice: 45000,
            image: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA4MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjgwIiBoZWlnaHQ9IjgwIiBmaWxsPSIjRjVGNUY1Ii8+CjxyZWN0IHg9IjEwIiB5PSIyMCIgd2lkdGg9IjYwIiBoZWlnaHQ9IjQwIiBmaWxsPSIjNEE1NTY4Ii8+CjxyZWN0IHg9IjIwIiB5PSIzMCIgd2lkdGg9IjQwIiBoZWlnaHQ9IjIwIiBmaWxsPSIjMzY0MTUyIi8+Cjx0ZXh0IHg9IjQwIiB5PSI0NSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+UGFudHM8L3RleHQ+Cjwvc3ZnPgo=',
            option: '옵션변경',
            addedDate: '2025-06-20'
        },
        {
            id: 2,
            name: 'B. classic 데님 재킷',
            price: 58000,
            originalPrice: 68000,
            image: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA4MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjgwIiBoZWlnaHQ9IjgwIiBmaWxsPSIjRjVGNUY1Ii8+CjxyZWN0IHg9IjE1IiB5PSIxNSIgd2lkdGg9IjUwIiBoZWlnaHQ9IjUwIiBmaWxsPSIjMkE0RDY5Ii8+CjxyZWN0IHg9IjI1IiB5PSIyNSIgd2lkdGg9IjMwIiBoZWlnaHQ9IjMwIiBmaWxsPSIjMUY0MDVEIi8+Cjx0ZXh0IHg9IjQwIiB5PSI0NSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+SmFja2V0PC90ZXh0Pgo8L3N2Zz4K',
            option: '사이즈: L',
            addedDate: '2025-06-18'
        },
        {
            id: 3,
            name: 'C. minimal 니트 스웨터',
            price: 35000,
            originalPrice: 42000,
            image: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA4MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjgwIiBoZWlnaHQ9IjgwIiBmaWxsPSIjRjVGNUY1Ii8+CjxyZWN0IHg9IjEyIiB5PSIyMCIgd2lkdGg9IjU2IiBoZWlnaHQ9IjQwIiBmaWxsPSIjNzI0QzVFIi8+CjxyZWN0IHg9IjIwIiB5PSIyOCIgd2lkdGg9IjQwIiBoZWlnaHQ9IjI0IiBmaWxsPSIjNUYzQjRCIi8+Cjx0ZXh0IHg9IjQwIiB5PSI0NSIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEwIiBmaWxsPSJ3aGl0ZSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+S25pdDwvdGV4dD4KPC9zdmc+Cg==',
            option: '색상: 베이지',
            addedDate: '2025-06-15'
        }
    ]
};

let currentPage = 1;
const itemsPerPage = 5;

function initializePage() {
    if (!sessionData.loggedIn) {
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        alert('로그인이 필요합니다.');
        return;
    }

    renderWishlist();
    renderPagination();
    updateItemCount();
}

function renderWishlist() {
    const productList = document.getElementById('productList');
    const wishlist = sessionData.wishlist || [];

    if (wishlist.length === 0) {
        productList.innerHTML = `
                    <div class="empty-state">
                        <p>관심상품이 없습니다.</p>
                        <p>마음에 드는 상품을 찾아 관심상품에 추가해보세요!</p>
                    </div>
                `;
        return;
    }

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageItems = wishlist.slice(startIndex, endIndex);

    productList.innerHTML = pageItems.map(item => `
                <div class="product-item" data-id="${item.id}">
                    <div class="product-checkbox">
                        <input type="checkbox" id="item-${item.id}" onchange="toggleItemSelection(${item.id})">
                    </div>
                    <div class="product-image">
                        <img src="${item.image}" alt="${item.name}">
                    </div>
                    <div class="product-info">
                        <a href="#" class="product-name" onclick="viewProduct(${item.id})">${item.name}</a>
                        <div class="product-price">${item.price.toLocaleString()}원</div>
                        ${item.originalPrice ? `<div class="product-original-price">${item.originalPrice.toLocaleString()}원</div>` : ''}
                        <div class="product-option">${item.option}</div>
                    </div>
                    <div class="product-actions">
                        <a href="#" class="btn" onclick="addToCart(${item.id})">장바구니</a>
                        <a href="#" class="btn btn-primary" onclick="buyNow(${item.id})">주문하기</a>
                        <a href="#" class="btn-delete" onclick="removeFromWishlist(${item.id})">삭제</a>
                    </div>
                </div>
            `).join('');
}

function renderPagination() {
    const pagination = document.getElementById('pagination');
    const wishlist = sessionData.wishlist || [];
    const totalPages = Math.ceil(wishlist.length / itemsPerPage);

    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }

    let paginationHTML = '';

    // 처음으로 버튼
    paginationHTML += `<button onclick="goToPage(1)" ${currentPage === 1 ? 'disabled' : ''}>≪</button>`;

    // 이전 버튼
    paginationHTML += `<button onclick="goToPage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>‹</button>`;

    // 페이지 번호
    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += `<button onclick="goToPage(${i})" ${currentPage === i ? 'class="active"' : ''}>${i}</button>`;
    }

    // 다음 버튼
    paginationHTML += `<button onclick="goToPage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>›</button>`;

    // 마지막으로 버튼
    paginationHTML += `<button onclick="goToPage(${totalPages})" ${currentPage === totalPages ? 'disabled' : ''}>≫</button>`;

    pagination.innerHTML = paginationHTML;
}

function goToPage(page) {
    const totalPages = Math.ceil(sessionData.wishlist.length / itemsPerPage);
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        renderWishlist();
        renderPagination();
    }
}

function updateItemCount() {
    const itemCount = document.getElementById('itemCount');
    const wishlist = sessionData.wishlist || [];
    itemCount.textContent = `총 ${wishlist.length}개`;
}

function toggleItemSelection(itemId) {
    // 체크박스 선택 상태 관리
    console.log(`Item ${itemId} selection toggled`);
}

function deleteSelected() {
    const checkboxes = document.querySelectorAll('.product-checkbox input[type="checkbox"]:checked');
    const selectedIds = Array.from(checkboxes).map(cb => {
        const itemElement = cb.closest('.product-item');
        return parseInt(itemElement.dataset.id);
    });

    if (selectedIds.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    if (confirm(`선택한 ${selectedIds.length}개 상품을 관심상품에서 삭제하시겠습니까?`)) {
        // 선택된 아이템들을 위시리스트에서 제거
        sessionData.wishlist = sessionData.wishlist.filter(item => !selectedIds.includes(item.id));

        // 페이지 재계산
        const totalPages = Math.ceil(sessionData.wishlist.length / itemsPerPage);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (totalPages === 0) {
            currentPage = 1;
        }

        renderWishlist();
        renderPagination();
        updateItemCount();

        alert('선택한 상품이 관심상품에서 삭제되었습니다.');
    }
}

function removeFromWishlist(itemId) {
    if (confirm('이 상품을 관심상품에서 삭제하시겠습니까?')) {
        sessionData.wishlist = sessionData.wishlist.filter(item => item.id !== itemId);

        // 페이지 재계산
        const totalPages = Math.ceil(sessionData.wishlist.length / itemsPerPage);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        if (totalPages === 0) {
            currentPage = 1;
        }

        renderWishlist();
        renderPagination();
        updateItemCount();

        alert('상품이 관심상품에서 삭제되었습니다.');
    }
}

function addToCart(itemId) {
    const item = sessionData.wishlist.find(item => item.id === itemId);
    if (item) {
        // 실제 구현에서는 서버에 장바구니 추가 요청
        alert(`"${item.name}"을(를) 장바구니에 추가했습니다.`);
    }
}

function buyNow(itemId) {
    const item = sessionData.wishlist.find(item => item.id === itemId);
    if (item) {
        // 실제 구현에서는 주문 페이지로 이동
        alert(`"${item.name}" 주문 페이지로 이동합니다.`);
    }
}

function viewProduct(itemId) {
    // 실제 구현에서는 상품 상세 페이지로 이동
    alert(`상품 ${itemId} 상세 페이지로 이동합니다.`);
}

function navigateTo(page) {
    // 실제 구현에서는 해당 페이지로 이동
    alert(`${page} 페이지로 이동합니다.`);
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', initializePage);