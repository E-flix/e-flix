/*
 * 작성자: 복성민
 * 기능: 모달 validation
 * 최초작성일: 2025-06-18
 * 최근수정일: 2025-06-18
 */

 $(document).ready(function() {
    // 폼 요소들
    const form = $('#contactForm');
    const nameField = $('#name');
    const emailField = $('#email');
    const phoneField = $('#phone');
    const titleField = $('#title');
    const contentField = $('#content');
    const submitButton = $('#submitButton');
    
    // 유효성 검사 상태 추적
    let validationState = {
        name: false,
        email: false,
        phone: false,
        title: false,
        content: false
    };

    // 이메일 유효성 검사 정규식
    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // 전화번호 유효성 검사 (기본적인 형태)
    function isValidPhone(phone) {
        const phoneRegex = /^[\d\s\-\(\)\+]+$/;
        return phoneRegex.test(phone) && phone.replace(/\D/g, '').length >= 10;
    }

    // 에러 메시지 표시/숨김
    function showError(fieldName, errorType) {
        $(`[data-sb-qna="${fieldName}:${errorType}"]`).show();
        $(`[data-sb-feedback="${fieldName}:${errorType}"]`).show();
    }

    function hideErrors(fieldName) {
        $(`[data-sb-qna^="${fieldName}:"]`).hide();
        $(`[data-sb-feedback^="${fieldName}:"]`).hide();
    }

    // 필드 유효성 검사 및 스타일 적용
    function validateField(field, fieldName, value) {
        hideErrors(fieldName);
        field.removeClass('is-invalid is-valid');

        let isValid = false;

        switch(fieldName) {
            case 'name':
                isValid = value.trim().length > 0;
                if (!isValid) {
                    showError('name', 'required');
                }
                break;

            case 'email':
                if (value.trim().length === 0) {
                    showError('email', 'required');
                } else if (!isValidEmail(value)) {
                    showError('email', 'email');
                } else {
                    isValid = true;
                }
                break;

            case 'phone':
                if (value.trim().length === 0) {
                    showError('phone', 'required');
                } else if (!isValidPhone(value)) {
                    // 전화번호 형식이 잘못된 경우에 대한 에러 메시지가 HTML에 없으므로 required 에러로 처리
                    showError('phone', 'required');
                } else {
                    isValid = true;
                }
                break;

            case 'title':
                isValid = value.trim().length > 0;
                if (!isValid) {
                    showError('title', 'required');
                }
                break;

            case 'content':
                isValid = value.trim().length > 0;
                if (!isValid) {
                    showError('content', 'required');
                }
                break;
        }

        // 필드 스타일 적용
        if (isValid) {
            field.addClass('is-valid');
        } else {
            field.addClass('is-invalid');
        }

        return isValid;
    }

    // 전체 폼 유효성 검사 상태 확인
    function checkFormValidity() {
        const isFormValid = Object.values(validationState).every(state => state === true);
        
        if (isFormValid) {
            submitButton.removeClass('disabled').prop('disabled', false);
        } else {
            submitButton.addClass('disabled').prop('disabled', true);
        }
    }

    // 실시간 유효성 검사 이벤트 리스너
    nameField.on('input blur', function() {
        validationState.name = validateField($(this), 'name', $(this).val());
        checkFormValidity();
    });

    emailField.on('input blur', function() {
        validationState.email = validateField($(this), 'email', $(this).val());
        checkFormValidity();
    });

    phoneField.on('input blur', function() {
        validationState.phone = validateField($(this), 'phone', $(this).val());
        checkFormValidity();
    });

    titleField.on('input blur', function() {
        validationState.title = validateField($(this), 'title', $(this).val());
        checkFormValidity();
    });

    contentField.on('input blur', function() {
        validationState.content = validateField($(this), 'content', $(this).val());
        checkFormValidity();
    });

    // 폼 제출 처리
    form.on('submit', function(e) {
        e.preventDefault();
        
        // 모든 필드 재검증
        validationState.name = validateField(nameField, 'name', nameField.val());
        validationState.email = validateField(emailField, 'email', emailField.val());
        validationState.phone = validateField(phoneField, 'phone', phoneField.val());
        validationState.title = validateField(titleField, 'title', titleField.val());
        validationState.content = validateField(contentField, 'content', contentField.val());
        
        checkFormValidity();

        // 모든 필드가 유효한 경우에만 제출
        if (Object.values(validationState).every(state => state === true)) {
            // 제출 버튼 비활성화 (중복 제출 방지)
            submitButton.prop('disabled', true).text('제출 중...');
            
            // 실제 폼 제출 또는 AJAX 호출을 여기에 구현
            // 예시: 성공 메시지 표시
            setTimeout(() => {
                $('#submitSuccessMessage').removeClass('d-none');
                $('#submitErrorMessage').addClass('d-none');
                
                // 폼 리셋
                form[0].reset();
                $('.form-control').removeClass('is-valid is-invalid');
                hideAllErrors();
                
                // 버튼 원래 상태로 복원
                submitButton.prop('disabled', false).addClass('disabled').text('Submit');
                
                // 유효성 검사 상태 리셋
                Object.keys(validationState).forEach(key => {
                    validationState[key] = false;
                });
                
            }, 1000);
        }
    });

    // 모든 에러 메시지 숨김
    function hideAllErrors() {
        $('[data-sb-qna]').hide();
        $('[data-sb-feedback]').hide();
    }

    // 모달이 열릴 때 초기화
    $('#qnaModal').on('show.bs.modal', function() {
        form[0].reset();
        $('.form-control').removeClass('is-valid is-invalid');
        hideAllErrors();
        $('#submitSuccessMessage').addClass('d-none');
        $('#submitErrorMessage').addClass('d-none');
        submitButton.addClass('disabled').prop('disabled', true).text('Submit');
        
        // 유효성 검사 상태 리셋
        Object.keys(validationState).forEach(key => {
            validationState[key] = false;
        });
    });

    // 초기 상태에서 모든 에러 메시지 숨김
    hideAllErrors();
});