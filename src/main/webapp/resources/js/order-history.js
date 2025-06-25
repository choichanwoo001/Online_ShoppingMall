// 샘플 주문 데이터 (ORDER_HISTORY를 기반으로)
const sampleOrders = [
    {
        orderId: 1001,
        orderNum: 'ORD-2025-001',
        status: 'DELIVERED',
        comment: '주문이 완료되었습니다.',
        createdAt: '2025-06-20',
        createdBy: 'system',
        totalAmount: 89000
    },
    {
        orderId: 1002,
        orderNum: 'ORD-2025-002',
        status: 'SHIPPED',
        comment: '배송이 시작되었습니다.',
        createdAt: '2025-06-22',
        createdBy: 'system',
        totalAmount: 156000
    },
    {
        orderId: 1003,
        orderNum: 'ORD-2025-003',
        status: 'CONFIRMED',
        comment: '주문을 확인하고 있습니다.',
        createdAt: '2025-06-24',
        createdBy: 'admin',
        totalAmount: 234000
    }
];

function setPeriod(period) {
    // 모든 기간 버튼 비활성화
    document.querySelectorAll('.period-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // 클릭된 버튼 활성화
    event.target.classList.add('active');

    const endDate = new Date();
    const startDate = new Date();

    switch(period) {
        case 'today':
            break;
        case '1month':
            startDate.setMonth(startDate.getMonth() - 1);
            break;
        case '3month':
            startDate.setMonth(startDate.getMonth() - 3);
            break;
        case '6month':
            startDate.setMonth(startDate.getMonth() - 6);
            break;
    }

    document.getElementById('startDate').value = startDate.toISOString().split('T')[0];
    document.getElementById('endDate').value = endDate.toISOString().split('T')[0];
}

function getStatusText(status) {
    const statusMap = {
        'PENDING': '주문접수',
        'CONFIRMED': '주문확인',
        'PROCESSING': '상품준비중',
        'SHIPPED': '배송중',
        'DELIVERED': '배송완료',
        'CANCELLED': '주문취소',
        'REFUNDED': '환불완료'
    };
    return statusMap[status] || status;
}

function getStatusClass(status) {
    const statusClassMap = {
        'CONFIRMED': 'status-confirmed',
        'SHIPPED': 'status-shipped',
        'DELIVERED': 'status-delivered'
    };
    return statusClassMap[status] || 'status-confirmed';
}

function searchOrders() {
    const statusFilter = document.getElementById('periodSelect').value;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    let filteredOrders = sampleOrders;

    // 상태 필터링
    if (statusFilter !== '전체') {
        filteredOrders = filteredOrders.filter(order => order.status === statusFilter);
    }

    // 날짜 필터링
    filteredOrders = filteredOrders.filter(order => {
        const orderDate = order.createdAt;
        return orderDate >= startDate && orderDate <= endDate;
    });

    renderOrders(filteredOrders);
}

function renderOrders(orders) {
    const orderList = document.getElementById('orderList');

    if (orders.length === 0) {
        orderList.innerHTML = '<div class="no-orders">주문 내역이 없습니다.</div>';
        return;
    }

    orderList.innerHTML = orders.map(order => `
                <div class="order-item">
                    <div class="order-header">
                        <div class="order-date">${order.createdAt}</div>
                        <div class="order-number">주문번호: ${order.orderNum}</div>
                        <div class="order-status ${getStatusClass(order.status)}">
                            ${getStatusText(order.status)}
                        </div>
                    </div>
                    <div class="order-details">
                        <div class="order-amount">${order.totalAmount.toLocaleString()}원</div>
                    </div>
                    <div class="order-comment">${order.comment}</div>
                </div>
            `).join('');
}

function goToPage(direction) {
    // 페이지네이션 로직
    console.log('Go to page:', direction);
}

// 탭 클릭 이벤트
document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', function() {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        tab.classList.add('active'); // 수정함 (this -> tab)
    });
});

// 초기 로딩 시 주문 목록 표시
searchOrders();