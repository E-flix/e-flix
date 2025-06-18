// Modal Forms Validation Handler - 기존 SB Forms 라이브러리 확장
(() => {
    "use strict";

    // 유틸리티 함수들
    const getElements = (selector, parent = document.body) => {
        const elements = [].slice.call(parent.querySelectorAll(selector));
        if (elements.length === 0) {
            throw new Error(`GET_ELEMENTS: ${parent.id} -> ${selector}`);
        }
        return elements;
    };

    const getElement = (selector, parent = document.body) => {
        const element = parent.querySelector(selector);
        if (!element) {
            throw new Error(`GET_ELEMENT: ${parent.id} -> ${selector}`);
        }
        return element;
    };

    // 유효성 검사 타입 열거형
    const ValidationTypes = {
        required: "required",
        email: "email",
        length: "length",
        checked: "checked",
        password_match: "password_match"
    };

    // 유효성 검사 함수들
    const validateEmail = (value) => {
        const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return emailRegex.test(String(value).toLowerCase());
    };

    const validateLength = (value, minLength = 8) => {
        return value.length >= minLength;
    };

    const validateRequired = (value) => {
        return value && value.trim().length > 0;
    };

    const validatePasswordMatch = (password, confirmPassword) => {
        return password === confirmPassword && password.length > 0;
    };

    // 개별 필드 유효성 검사 클래스
    class FieldValidator {
        constructor(input, validations, feedbackPrefix, onChange) {
            this.input = input;
            this.validations = validations;
            this.feedbackPrefix = feedbackPrefix;
            this.onChange = onChange;
            this.isPure = true;

            this.inputBlurHandler = () => this.inputBlur();
            this.inputInputHandler = () => this.inputInput();

            input.addEventListener("blur", this.inputBlurHandler);
            input.addEventListener("input", this.inputInputHandler);
        }

        inputBlur() {
            this.isPure = false;
            this._handleInputAction();
        }

        inputInput() {
            this._handleInputAction();
        }

        initValidation() {
            this._handleInputAction();
        }

        _handleInputAction() {
            let isValid = true;

            this.validations.forEach((validation) => {
                const valid = this._validateField(this.input, validation);
                isValid = isValid && valid;
            });

            // 데이터 속성 설정
            this.input.dataset.sbCanSubmit = isValid ? "yes" : "no";

            // 시각적 피드백
            if (isValid) {
                if (!this.isPure) {
                    this.input.classList.remove("is-invalid");
                }
            } else {
                if (!this.isPure) {
                    this.input.classList.add("is-invalid");
                }
            }

            this.onChange();
        }

        _validateField(input, validation) {
            let isValid = true;
            let feedbackElement;

            try {
                feedbackElement = getElement(`[data-sb-${this.feedbackPrefix}="${input.id}:${validation}"]`);
            } catch (e) {
                console.warn(`Feedback element not found for: ${input.id}:${validation}`);
                return true;
            }

            switch (validation) {
                case ValidationTypes.required:
                    isValid = validateRequired(input.value);
                    break;
                case ValidationTypes.email:
                    isValid = validateEmail(input.value);
                    break;
                case ValidationTypes.length:
                    isValid = validateLength(input.value);
                    break;
                case "password_match":
                    // 회원가입 폼의 비밀번호 확인 특별 처리
                    if (input.id === "user_pw_check") {
                        const passwordInput = document.querySelector("#registerForm #user_pw");
                        if (passwordInput) {
                            isValid = validatePasswordMatch(passwordInput.value, input.value);
                        }
                    }
                    break;
            }

            // 피드백 표시/숨김
            if (isValid) {
                feedbackElement.classList.add("d-none");
            } else {
                feedbackElement.classList.remove("d-none");
            }

            return isValid;
        }

        reset() {
            this.isPure = true;
            this.input.value = "";
            this.input.classList.remove("is-invalid");
            this.input.dataset.sbCanSubmit = "no";
        }

        tearDown() {
            this.reset();
            this.input.removeEventListener("blur", this.inputBlurHandler);
            this.input.removeEventListener("input", this.inputInputHandler);
        }
    }

    // 모달 폼 관리자 클래스
    class ModalFormManager {
        constructor(formElement, feedbackPrefix) {
            this.form = formElement;
            this.feedbackPrefix = feedbackPrefix;
            this.validators = [];
            this.submitButton = null;
            this.isEnabled = true;

            this.init();
        }

        init() {
            try {
                this.submitButton = getElement(`#${this.form.id.replace('Form', 'Button')}`, this.form.parentElement);
            } catch (e) {
                console.warn(`Submit button not found for form: ${this.form.id}`);
                return;
            }

            const inputs = getElements("input, textarea, select", this.form);
            
            inputs.forEach((input) => {
                const validations = input.dataset.sbValidations;
                if (validations) {
                    input.dataset.sbCanSubmit = "no";
                    const validationArray = validations.split(",").map(v => v.trim());
                    
                    // 회원가입 폼의 비밀번호 확인 필드 특별 처리
                    if (input.id === "user_pw_check" && this.feedbackPrefix === "register") {
                        validationArray.push("password_match");
                    }
                    
                    this.validators.push(
                        new FieldValidator(
                            input,
                            validationArray,
                            this.feedbackPrefix,
                            () => this.updateSubmitButton()
                        )
                    );
                } else {
                    input.dataset.sbCanSubmit = "yes";
                }
            });

            // 폼 제출 이벤트 처리
            this.form.addEventListener("submit", (e) => this.handleSubmit(e));

            // 초기 유효성 검사 실행
            this.validators.forEach(validator => validator.initValidation());
            this.updateSubmitButton();
        }

        updateSubmitButton() {
            if (!this.submitButton) return;

            const inputs = getElements("input, textarea, select", this.form);
            const isFormValid = inputs.every(input => input.dataset.sbCanSubmit === "yes");

            if (isFormValid) {
                this.submitButton.classList.remove("disabled");
            } else {
                this.submitButton.classList.add("disabled");
            }
        }

        handleSubmit(event) {
            event.preventDefault();
            
            if (this.submitButton.classList.contains("disabled")) {
                return;
            }

            // 폼 데이터 수집
            const formData = this.collectFormData();
            
            // 폼 타입에 따른 처리
            switch (this.feedbackPrefix) {
                case "qna":
                    this.handleQnaSubmit(formData);
                    break;
                case "login":
                    this.handleLoginSubmit(formData);
                    break;
                case "register":
                    this.handleRegisterSubmit(formData);
                    break;
            }
        }

        collectFormData() {
            const formData = {};
            const inputs = getElements("input, textarea, select", this.form);
            
            inputs.forEach((input) => {
                if (input.type === "checkbox" || input.type === "radio") {
                    formData[input.name] = input.checked;
                } else {
                    formData[input.id] = input.value;
                }
            });
            
            return formData;
        }

        handleQnaSubmit(formData) {
            console.log("문의 폼 제출:", formData);
            
            // 실제 API 호출 시뮬레이션
            this.simulateApiCall()
                .then(() => {
                    alert("문의가 성공적으로 제출되었습니다.");
                    this.resetForm();
                    // 모달 닫기
                    const modal = bootstrap.Modal.getInstance(document.getElementById('qnaModal'));
                    if (modal) modal.hide();
                })
                .catch((error) => {
                    console.error("문의 제출 실패:", error);
                    alert("문의 제출에 실패했습니다. 다시 시도해주세요.");
                });
        }

        handleLoginSubmit(formData) {
            console.log("로그인 폼 제출:", formData);
            
            // 실제 로그인 API 호출 시뮬레이션
            this.simulateApiCall()
                .then(() => {
                    alert("로그인 성공!");
                    this.resetForm();
                    // 모달 닫기
                    const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
                    if (modal) modal.hide();
                })
                .catch((error) => {
                    console.error("로그인 실패:", error);
                    alert("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
                });
        }

        handleRegisterSubmit(formData) {
            console.log("회원가입 폼 제출:", formData);
            
            // 실제 회원가입 API 호출 시뮬레이션
            this.simulateApiCall()
                .then(() => {
                    alert("회원가입이 완료되었습니다!");
                    this.resetForm();
                    // 모달 닫기
                    const modal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
                    if (modal) modal.hide();
                })
                .catch((error) => {
                    console.error("회원가입 실패:", error);
                    alert("회원가입에 실패했습니다. 다시 시도해주세요.");
                });
        }

        simulateApiCall() {
            return new Promise((resolve, reject) => {
                // API 호출 시뮬레이션 (2초 후 성공)
                setTimeout(() => {
                    if (Math.random() > 0.1) { // 90% 성공률
                        resolve("Success");
                    } else {
                        reject(new Error("API Error"));
                    }
                }, 1000);
            });
        }

        resetForm() {
            this.validators.forEach(validator => validator.reset());
            this.updateSubmitButton();
        }

        tearDown() {
            this.validators.forEach(validator => validator.tearDown());
            this.validators = [];
        }
    }

    // CSS 스타일 추가
    const addStyles = () => {
        const style = document.createElement("style");
        style.textContent = `
            .invalid-qna,
            .invalid-login,
            .invalid-register {
                display: none;
                width: 100%;
                margin-top: 0.25rem;
                font-size: 0.875em;
                color: #dc3545;
            }
            
            .invalid-qna:not(.d-none),
            .invalid-login:not(.d-none),
            .invalid-register:not(.d-none) {
                display: block;
            }
            
            .form-control.is-invalid {
                border-color: #dc3545;
                box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
            }

            .d-none {
                display: none !important;
            }

            .btn.disabled {
                pointer-events: none;
                opacity: 0.65;
            }
        `;
        document.head.appendChild(style);
    };

    // DOM 로드 완료 시 초기화
    const initializeModalForms = () => {
        addStyles();

        // 각 모달 폼 초기화
        const forms = [
            { id: "contactForm", prefix: "qna" },
            { id: "loginForm", prefix: "login" },
            { id: "registerForm", prefix: "register" }
        ];

        const managers = [];

        forms.forEach(({ id, prefix }) => {
            try {
                const formElement = getElement(`#${id}`);
                const manager = new ModalFormManager(formElement, prefix);
                managers.push(manager);
            } catch (e) {
                console.warn(`Form not found: ${id}`);
            }
        });

        // 전역 객체에 관리자들 저장 (필요시 외부에서 접근 가능)
        window.modalFormManagers = managers;
    };

    // DOM 로드 완료 시 실행
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeModalForms);
    } else {
        initializeModalForms();
    }

    // 외부 접근을 위한 API
    window.ModalFormsAPI = {
        reinitialize: initializeModalForms,
        getManagers: () => window.modalFormManagers || []
    };
})();