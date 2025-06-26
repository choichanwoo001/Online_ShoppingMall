let currentPage = 1;
const itemsPerPage = 20;
let totalPages = 1;
let selectedItems = new Set();

// 페이지 초기화
async function initializePage() {
    await loadWishlist();
    updateItemCount();
}

// 위시리스트 데이터 로드
async function loadWishlist(page = 1) {
    try {
        const response = await fetch(`/api/wishlist?page=${page}&size=${itemsPerPage}`, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        const data = await response.json();

        if (!data.success) {
            if (data.message === "로그인이 필요합니다.") {
                alert('로그인이 필요합니다.');
                window.location.href = '/login';
                return;
            }
        }

        if (data.success) {
            currentPage = data.currentPage;
            totalPages = data.totalPages;
            renderWishlist(data.content);
            renderPagination();
            updateItemCount(data.totalElements);
        }
    } catch (error) {
        console.error('위시리스트 로드 실패:', error);
        alert('위시리스트를 불러오는데 실패했습니다.');
    }
}

// 위시리스트 렌더링
function renderWishlist(wishlistItems) {
    const productList = document.getElementById('productList');

    if (!wishlistItems || wishlistItems.length === 0) {
        productList.innerHTML = `
            <div class="empty-state">
                <p>관심상품이 없습니다.</p>
                <p>마음에 드는 상품을 찾아 관심상품에 추가해보세요!</p>
            </div>
        `;
        return;
    }
// onerror="this.src='https://via.placeholder.com/200x200?text=No+Image'
    productList.innerHTML = wishlistItems.map(item => {
        const actualPrice = item.price || 0;
        const imageUrl = item.thumbnail;
        const isEnabled = item.enabled !== false;
        const title = item.title || '상품명 없음';
        const summary = item.summary || '';
        const categoryName = item.categoryName || '';
        const averageRating = item.averageRating || 0;

        return `
            <div class="product-item" data-id="${item.wishId}" data-product-id="${item.productId}">
                <div class="product-checkbox">
                    <input type="checkbox" id="item-${item.wishId}" 
                           onchange="toggleItemSelection(${item.wishId})"
                           ${selectedItems.has(item.wishId) ? 'checked' : ''}>
                </div>
                <div class="product-image">
                    <img src="${imageUrl}" 
                         alt="${title}"
                         "> 
                    ${!isEnabled ? '<div class="soldout-overlay">품절</div>' : ''}
                </div>
                <div class="product-info">
                    <a href="/product/${item.productId}" class="product-name">${title}</a>
                    ${summary ? `<div class="product-summary">${summary}</div>` : ''}
                    <div class="price-info">
                        <span class="product-price">${actualPrice.toLocaleString()}원</span>
                    </div>
                    <div class="product-meta">
                        ${averageRating > 0 ? `
                            <span class="rating">
                                <i class="fas fa-star"></i> ${averageRating.toFixed(1)}
                            </span>
                        ` : ''}
                    </div>
                    ${categoryName ? `
                        <div class="category-name">${categoryName}</div>
                    ` : ''}
                </div>
                <div class="product-actions">
                    <button class="btn" onclick="addToCart(${item.productId})" 
                            ${!isEnabled ? 'disabled' : ''}>
                        장바구니
                    </button>
                    <button class="btn btn-primary" onclick="buyNow(${item.productId})"
                            ${!isEnabled ? 'disabled' : ''}>
                        주문하기
                    </button>
                    <button class="btn-delete" onclick="removeFromWishlist(${item.productId})">
                        삭제
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

// 페이지네이션 렌더링
function renderPagination() {
    const pagination = document.getElementById('pagination');

    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }

    let paginationHTML = '';

    // 페이지 그룹 계산
    const pageGroup = Math.ceil(currentPage / 10);
    const startPage = (pageGroup - 1) * 10 + 1;
    const endPage = Math.min(pageGroup * 10, totalPages);

    // 처음으로 버튼
    paginationHTML += `
        <button onclick="goToPage(1)" ${currentPage === 1 ? 'disabled' : ''}>
            <i class="fas fa-angle-double-left"></i>
        </button>
    `;

    // 이전 버튼
    paginationHTML += `
        <button onclick="goToPage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>
            <i class="fas fa-angle-left"></i>
        </button>
    `;

    // 페이지 번호
    for (let i = startPage; i <= endPage; i++) {
        paginationHTML += `
            <button onclick="goToPage(${i})" ${currentPage === i ? 'class="active"' : ''}>
                ${i}
            </button>
        `;
    }

    // 다음 버튼
    paginationHTML += `
        <button onclick="goToPage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>
            <i class="fas fa-angle-right"></i>
        </button>
    `;

    // 마지막으로 버튼
    paginationHTML += `
        <button onclick="goToPage(${totalPages})" ${currentPage === totalPages ? 'disabled' : ''}>
            <i class="fas fa-angle-double-right"></i>
        </button>
    `;

    pagination.innerHTML = paginationHTML;
}

// 페이지 이동
function goToPage(page) {
    if (page >= 1 && page <= totalPages && page !== currentPage) {
        loadWishlist(page);
    }
}

// 아이템 수 업데이트
function updateItemCount(total = 0) {
    const itemCount = document.getElementById('itemCount');
    if (itemCount) {
        itemCount.textContent = `총 ${total}개`;
    }
}

// 아이템 선택 토글
function toggleItemSelection(wishId) {
    if (selectedItems.has(wishId)) {
        selectedItems.delete(wishId);
    } else {
        selectedItems.add(wishId);
    }
}

// 전체 선택/해제
function toggleSelectAll() {
    const selectAllCheckbox = document.getElementById('selectAll');
    const itemCheckboxes = document.querySelectorAll('.product-checkbox input[type="checkbox"]');

    if (selectAllCheckbox.checked) {
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = true;
            const wishId = parseInt(checkbox.id.replace('item-', ''));
            selectedItems.add(wishId);
        });
    } else {
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = false;
        });
        selectedItems.clear();
    }
}

// 선택 삭제
async function deleteSelected() {
    if (selectedItems.size === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    if (!confirm(`선택한 ${selectedItems.size}개 상품을 관심상품에서 삭제하시겠습니까?`)) {
        return;
    }

    try {
        const response = await fetch('/api/wishlist/remove-multiple', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                wishIds: Array.from(selectedItems)
            })
        });

        const result = await response.json();

        if (result.success) {
            selectedItems.clear();
            await loadWishlist(currentPage);
            alert(result.message || '선택한 상품이 삭제되었습니다.');
        } else {
            alert(result.message || '삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('삭제 실패:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
}

// 개별 삭제
async function removeFromWishlist(productId) {
    // productId 유효성 검사
    if (!productId || productId === 'undefined' || productId === 'null') {
        alert('상품 정보가 올바르지 않습니다.');
        return;
    }

    if (!confirm('이 상품을 관심상품에서 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/wishlist/remove?productId=${productId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        const result = await response.json();

        if (result.success) {
            await loadWishlist(currentPage);
            alert(result.message || '상품이 삭제되었습니다.');
        } else {
            alert(result.message || '삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('삭제 실패:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
}

// 장바구니 추가
async function addToCart(productId) {
    // productId 유효성 검사
    if (!productId || productId === 'undefined' || productId === 'null') {
        alert('상품 정보가 올바르지 않습니다.');
        return;
    }

    try {
        const response = await fetch('/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                productId: productId,
                quantity: 1
            })
        });

        const result = await response.json();

        if (result.success) {
            if (confirm('장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?')) {
                window.location.href = '/cart';
            }
        } else {
            alert(result.message || '장바구니 추가에 실패했습니다.');
        }
    } catch (error) {
        console.error('장바구니 추가 실패:', error);
        alert('장바구니 추가 중 오류가 발생했습니다.');
    }
}

// 바로 구매
function buyNow(productId) {
    // productId 유효성 검사
    if (!productId || productId === 'undefined' || productId === 'null') {
        alert('상품 정보가 올바르지 않습니다.');
        return;
    }

    // 구매 페이지로 이동
    window.location.href = `/order/direct?productId=${productId}&quantity=1`;
}

// 장바구니로 이동 후 삭제
async function moveToCartAndDelete() {
    if (selectedItems.size === 0) {
        alert('이동할 상품을 선택해주세요.');
        return;
    }

    try {
        // 선택된 상품들의 productId 추출
        const productIds = [];
        document.querySelectorAll('.product-item').forEach(item => {
            const wishId = parseInt(item.dataset.id);
            if (selectedItems.has(wishId)) {
                const productId = parseInt(item.dataset.productId);
                if (productId && productId !== 'undefined' && productId !== 'null') {
                    productIds.push(productId);
                }
            }
        });

        if (productIds.length === 0) {
            alert('유효한 상품이 없습니다.');
            return;
        }

        // 장바구니에 추가
        const cartResponse = await fetch('/cart/add-multiple', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                productIds: productIds
            })
        });

        const cartResult = await cartResponse.json();

        if (cartResult.success) {
            // 위시리스트에서 삭제
            const deleteResponse = await fetch('/api/wishlist/remove-multiple', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    wishIds: Array.from(selectedItems)
                })
            });

            const deleteResult = await deleteResponse.json();

            if (deleteResult.success) {
                selectedItems.clear();
                await loadWishlist(currentPage);

                if (confirm('장바구니로 이동되었습니다. 장바구니 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/cart';
                }
            }
        } else {
            alert(cartResult.message || '장바구니 추가에 실패했습니다.');
        }
    } catch (error) {
        console.error('처리 실패:', error);
        alert('처리 중 오류가 발생했습니다.');
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', initializePage);