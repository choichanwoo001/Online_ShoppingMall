// resources/js/popup-controller.js

document.addEventListener('DOMContentLoaded', () => {
    // 모든 팝업 오버레이와 닫기 버튼을 선택
    const popupOverlays = document.querySelectorAll('.common-popup-overlay');
    const closeButtons = document.querySelectorAll('.common-popup-close-button');

    // 팝업 열기 함수 (전역)
    window.openCommonPopup = function(popupId) {
        const targetPopup = document.getElementById(popupId);
        if (targetPopup) {
            targetPopup.classList.add('active');
            // 스크롤 방지 (선택 사항)
            document.body.style.overflow = 'hidden';
        }
    };

    // 팝업 닫기 함수 (전역)
    window.closeCommonPopup = function(popupId) {
        const targetPopup = document.getElementById(popupId);
        if (targetPopup) {
            targetPopup.classList.remove('active');
            // 스크롤 허용 (선택 사항)
            document.body.style.overflow = '';
        }
    };

    // 각 팝업에 대한 이벤트 리스너 설정
    popupOverlays.forEach(overlay => {
        // 오버레이 클릭 시 팝업 닫기
        overlay.addEventListener('click', (event) => {
            if (event.target === overlay) {
                window.closeCommonPopup(overlay.id);
            }
        });
    });

    // 각 닫기 버튼에 대한 이벤트 리스너 설정
    closeButtons.forEach(button => {
        button.addEventListener('click', () => {
            const popupToClose = button.closest('.common-popup-overlay');
            if (popupToClose) {
                window.closeCommonPopup(popupToClose.id);
            }
        });
    });

    // ESC 키 눌렀을 때 모든 활성화된 팝업 닫기
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            popupOverlays.forEach(overlay => {
                if (overlay.classList.contains('active')) {
                    window.closeCommonPopup(overlay.id);
                }
            });
        }
    });

    // ====================================================================
    // 페이지별 버튼 이벤트 연결은 이제 각 페이지의 JS 파일에 있거나
    // 필요하다면 여기에서 전역적으로 처리 (권장하지 않음, 각 페이지에서 처리)
    // ====================================================================
    // 예시: showMyPopupBtn 버튼 연결 (이전 cart.html에서 사용하던 버튼)
    // 이 부분은 사실 해당 페이지의 스크립트 파일에서 처리하는 것이 더 적절합니다.
    // 하지만 일단 여기에 두어 동작하는 것을 확인하는 용도로 사용 가능합니다.
    /*
    const openCartPopupBtn = document.getElementById('showMyPopupBtn');
    if (openCartPopupBtn) {
        openCartPopupBtn.addEventListener('click', () => {
            alert('alert from popup-controller.js!');
            window.openCommonPopup('commonModalPopup');
        });
    }
    */
});