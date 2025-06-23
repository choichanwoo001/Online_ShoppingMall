// 전역 변수
let currentPage = 1;
let totalPages = 1;
let wishlistData = [];

// DOM 요소들
const selectAllCheckbox = document.getElementById('selectAll');
const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
const moveToCartBtn = document.getElementById('moveToCartBtn');
const deleteAllBtn = document.getElementById('deleteAllBtn');
const addAllToCartBtn = document.getElementById('addAllToCartBtn');
const productContainer = document.getElementById('productContainer');
const emptyMessage = document.getElementById('emptyMessage');
const productTemplate = document.getElementById('productTemplate');
const pagination = document.getElementById('pagination');

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    loadWishlist();
    setupEventListeners();
});

// 이벤트 리스너 설정
function setupEventListeners() {
    // 전체 선택 체크박스
    selectAllCheckbox.addEventListener('change', handleSelectAll);

    // 버튼 이벤트
    deleteSelectedBtn.addEventListener('click', handleDeleteSelected);
    moveToCartBtn.addEventListener('click', handleMoveToCart);
    deleteAllBtn.addEventListener('click', handleDeleteAll);
    addAllToCartBtn.addEventListener('click', handleAddAllToCart);

    // 페이지네이션 이벤트
    setupPaginationEvents();
}

// 관심상품 목록 로드
function loadWishlist(page = 1) {
    fetch(`/api/wishlist?page=${page}&size=10`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                wishlistData = data.content;
                currentPage = data.currentPage;
                totalPages = data.totalPages;

                renderWishlist();
                renderPagination();
            } else {
                alert(data.message || '관심상품 목록을 불러올 수 없습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('관심상품 목록 로딩 중 오류가 발생했습니다.');
        });
}

// 관심상품 목록 렌더링
function renderWishlist() {
    // 기존 상품 아이템들 제거
    const existingItems = productContainer.querySelectorAll('.product-item:not(#productTemplate)');
    existingItems.forEach(item => item.remove());

    if (wishlistData.length === 0) {
        emptyMessage.style.display = 'block';
        return;
    }

    emptyMessage.style.display = 'none';

    wishlistData.forEach(wishItem => {
        const productItem = createProductItem(wishItem);
        productContainer.appendChild(productItem);
    });

    updateSelectAllState();
}

// 상품 아이템 생성
function createProductItem(wishItem) {
    const template = productTemplate.cloneNode(true);
    template.id = '';
    template.style.display = 'grid';
    template.dataset.wishId = wishItem.wishId;
    template.dataset.productId = wishItem.productId;

    // 체크박스
    const checkbox = template.querySelector('.product-checkbox');
    checkbox.dataset.wishId = wishItem.wishId;
    checkbox.dataset.productId = wishItem.productId;
    checkbox.addEventListener('change', updateSelectAllState);

    // 상품 이미지
    const image = template.querySelector('.product-image');
    image.src = wishItem.thumbnail || '/resources/images/no-image.jpg';
    image.alt = wishItem.title;

    // 상품 정보
    template.querySelector('.product-name').textContent = wishItem.title;
    template.querySelector('.product-description').textContent = wishItem.summary || '';
    template.querySelector('.product-category').textContent = wishItem.categoryName || '';

    // 평점 표시
    const ratingStars = template.querySelector('.rating-stars');
    const ratingScore = template.querySelector('.rating-score');
    const reviewCount = template.querySelector('.review-count');

    if (wishItem.averageRating) {
        ratingStars.textContent = '★'.repeat(Math.floor(wishItem.averageRating)) +
            '☆'.repeat(5 - Math.floor(wishItem.averageRating));
        ratingScore.textContent = `${wishItem.averageRating}`;
        reviewCount.textContent = `(${wishItem.reviewCount || 0}개 리뷰)`;
    } else {
        template.querySelector('.product-rating').style.display = 'none';
    }

    // 태그 표시
    if (wishItem.tags) {
        template.querySelector('.product-tags').textContent = wishItem.tags;
    } else {
        template.querySelector('.product-tags').style.display = 'none';
    }

    // 가격 정보
    const originalPriceEl = template.querySelector('.original-price');
    const discountPriceEl = template.querySelector('.discount-price');
    const priceEl = template.querySelector('.price');

    if (wishItem.discountPrice && wishItem.discountPrice < wishItem.price) {
        originalPriceEl.textContent = formatPrice(wishItem.price) + '원';
        originalPriceEl.style.display = 'block';
        discountPriceEl.textContent = formatPrice(wishItem.discountPrice) + '원';
        discountPriceEl.style.display = 'block';
        priceEl.style.display = 'none';
    } else {
        priceEl.textContent = formatPrice(wishItem.price) + '원';
    }

    // 재고 상태
    const stockStatus = template.querySelector('.stock-status');
    if (wishItem.stockQuantity > 0) {
        if (wishItem.stockQuantity < 10) {
            stockStatus.textContent = '품절임박';
            stockStatus.className = 'stock-status low-stock';
        } else {
            stockStatus.textContent = '판매중';
            stockStatus.className = 'stock-status in-stock';
        }
    } else {
        stockStatus.textContent = '품절';
        stockStatus.className = 'stock-status out-of-stock';
    }

    // 버튼 이벤트
    const orderBtn = template.querySelector('.btn-order');
    const cartBtn = template.querySelector('.btn-cart');
    const removeBtn = template.querySelector('.btn-remove');

    // 품절시 주문/장바구니 버튼 비활성화
    if (wishItem.stockQuantity <= 0) {
        orderBtn.disabled = true;
        orderBtn.textContent = '품절';
        cartBtn.disabled = true;
    }

    orderBtn.addEventListener('click', () => handleOrder(wishItem.productId));
    cartBtn.addEventListener('click', () => handleAddToCart(wishItem.productId));
    removeBtn.addEventListener('click', () => handleRemoveItem(wishItem.wishId));

    return template;
}

// 가격 포맷팅
function formatPrice(price) {
    return new Intl.NumberFormat('ko-KR').format(price);
}

// 전체 선택 처리
function handleSelectAll() {
    const checkboxes = document.querySelectorAll('.product-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAllCheckbox.checked;
    });
}

// 전체 선택 상태 업데이트
function updateSelectAllState() {
    const checkboxes = document.querySelectorAll('.product-checkbox');
    const checkedBoxes = document.querySelectorAll('.product-checkbox:checked');

    if (checkboxes.length === 0) {
        selectAllCheckbox.checked = false;
        selectAllCheckbox.indeterminate = false;
    } else if (checkedBoxes.length === checkboxes.length) {
        selectAllCheckbox.checked = true;
        selectAllCheckbox.indeterminate = false;
    } else if (checkedBoxes.length > 0) {
        selectAllCheckbox.checked = false;
        selectAllCheckbox.indeterminate = true;
    } else {
        selectAllCheckbox.checked = false;
        selectAllCheckbox.indeterminate = false;
    }
}

// 선택된 관심상품 ID 가져오기
function getSelectedWishIds() {
    const checkedBoxes = document.querySelectorAll('.product-checkbox:checked');
    return Array.from(checkedBoxes).map(checkbox => checkbox.dataset.wishId);
}

// 선택된 상품 ID 가져오기
function getSelectedProductIds() {
    const checkedBoxes = document.querySelectorAll('.product-checkbox:checked');
    return Array.from(checkedBoxes).map(checkbox => checkbox.dataset.productId);
}

// 선택 삭제
function handleDeleteSelected() {
    const selectedWishIds = getSelectedWishIds();
    if (selectedWishIds.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    if (!confirm(`선택한 ${selectedWishIds.length}개 상품을 삭제하시겠습니까?`)) {
        return;
    }

    deleteFromWishlist(selectedWishIds);
}

// 선택 상품 장바구니 담기
function handleMoveToCart() {
    const selectedProductIds = getSelectedProductIds();
    const selectedWishIds = getSelectedWishIds();

    if (selectedProductIds.length === 0) {
        alert('장바구니에 담을 상품을 선택해주세요.');
        return;
    }

    addToCart(selectedProductIds, selectedWishIds, true); // true = 관심상품에서 제거
}

// 전체 삭제
function handleDeleteAll() {
    if (wishlistData.length === 0) {
        alert('삭제할 상품이 없습니다.');
        return;
    }

    if (!confirm('모든 관심상품을 삭제하시겠습니까?')) {
        return;
    }

    const allWishIds = wishlistData.map(item => item.wishId);
    deleteFromWishlist(allWishIds);
}

// 관심상품 비우기 (전체 장바구니 담기)
function handleAddAllToCart() {
    if (wishlistData.length === 0) {
        alert('장바구니에 담을 상품이 없습니다.');
        return;
    }

    if (!confirm('모든 관심상품을 장바구니에 담고 관심상품을 비우시겠습니까?')) {
        return;
    }

    const allProductIds = wishlistData.map(item => item.productId);
    const allWishIds = wishlistData.map(item => item.wishId);
    addToCart(allProductIds, allWishIds, true);
}

// 개별 주문
function handleOrder(productId) {
    window.location.href = `/order?productId=${productId}`;
}

// 개별 장바구니 담기
function handleAddToCart(productId) {
    addToCart([productId], [], false);
}

// 개별 삭제
function handleRemoveItem(wishId) {
    if (!confirm('이 상품을 관심상품에서 삭제하시겠습니까?')) {
        return;
    }

    deleteFromWishlist([wishId]);
}

// 관심상품에서 삭제
function deleteFromWishlist(wishIds) {
    fetch('/api/wishlist/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ wishIds: wishIds })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(`${wishIds.length}개 상품이 삭제되었습니다.`);
                loadWishlist(currentPage);
            } else {
                alert(data.message || '삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('삭제 중 오류가 발생했습니다.');
        });
}

// 장바구니에 추가
function addToCart(productIds, wishIds = [], removeFromWishlist = false) {
    fetch('/api/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            productIds: productIds,
            wishIds: wishIds,
            removeFromWishlist: removeFromWishlist
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const message = removeFromWishlist ?
                    `${productIds.length}개 상품이 장바구니에 담겼습니다.` :
                    `${productIds.length}개 상품이 장바구니에 담겼습니다.`;
                alert(message);

                if (removeFromWishlist) {
                    loadWishlist(currentPage);
                }
            } else {
                alert(data.message || '장바구니 담기에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('장바구니 담기 중 오류가 발생했습니다.');
        });
}

// 페이지네이션 설정
function setupPaginationEvents() {
    const pageFirst = pagination.querySelector('.page-first');
    const pagePrev = pagination.querySelector('.page-prev');
    const pageNext = pagination.querySelector('.page-next');
    const pageLast = pagination.querySelector('.page-last');

    pageFirst.addEventListener('click', () => goToPage(1));
    pagePrev.addEventListener('click', () => goToPage(currentPage - 1));
    pageNext.addEventListener('click', () => goToPage(currentPage + 1));
    pageLast.addEventListener('click', () => goToPage(totalPages));
}

// 페이지 이동
function goToPage(page) {
    if (page < 1 || page > totalPages || page === currentPage) {
        return;
    }

    loadWishlist(page);
}

// 페이지네이션 렌더링
function renderPagination() {
    const pageNumbers = pagination.querySelector('.page-numbers');
    pageNumbers.innerHTML = '';

    // 페이지 번호 범위 계산
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
        const pageNumber = document.createElement('button');
        pageNumber.type = 'button';
        pageNumber.className = `page-number ${i === currentPage ? 'active' : ''}`;
        pageNumber.textContent = i;
        pageNumber.addEventListener('click', () => goToPage(i));
        pageNumbers.appendChild(pageNumber);
    }

    // 버튼 상태 업데이트
    pagination.querySelector('.page-first').disabled = currentPage === 1;
    pagination.querySelector('.page-prev').disabled = currentPage === 1;
    pagination.querySelector('.page-next').disabled = currentPage === totalPages;
    pagination.querySelector('.page-last').disabled = currentPage === totalPages;

    // 페이지가 없으면 페이지네이션 숨김
    pagination.style.display = totalPages <= 1 ? 'none' : 'flex';
}