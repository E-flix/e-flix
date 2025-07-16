document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 거래처 관리 페이지 초기화 시작');

    // ===== 전역 변수 및 API 객체 =====
    let customerGridApi;
    const form = document.getElementById('customerForm');
    const API = {
        getList: () => fetchData('/bsn/customer-management/list'),
        getDetails: (customerCd) => fetchData(`/bsn/customer-management/${customerCd}`),
        save: (data) => fetchData('/bsn/customer-management/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }),
        delete: (customerCd) => fetchData(`/bsn/customer-management/${customerCd}`, {
            method: 'DELETE'
        })
    };

    // ===== 유틸리티 함수 =====
    const fetchData = async (url, options = {}) => {
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: response.statusText }));
                throw new Error(errorData.message || `HTTP ${response.status}`);
            }
            return response.json();
        } catch (error) {
            console.error(`API 요청 실패: ${url}`, error);
            Swal.fire('오류', `데이터 처리 중 오류가 발생했습니다: ${error.message}`, 'error');
            throw error;
        }
    };

    // ===== 그리드 초기화 =====
    const initCustomerGrid = () => {
        const columnDefs = [
            { headerName: '거래처 코드', field: 'customerCd', width: 150, sort: 'desc' },
            { headerName: '거래처명', field: 'customerNm', flex: 1 },
            { headerName: '대표자명', field: 'representativeNm', width: 120 },
            { headerName: '할인율', field: 'discountRate', width: 100,
              valueFormatter: p => p.value ? `${p.value}%` : '0%',
              cellStyle: { textAlign: 'right' }
            },
            { headerName: '여신상태', field: 'creditStatus', width: 100,
              cellRenderer: params => {
                  if (!params.value) return '';
                  const status = params.data.tradeStopFlg === 'Y' ? 'stop' : (params.data.creditHoldFlg === 'Y' ? 'hold' : 'normal');
                  const text = params.data.tradeStopFlg === 'Y' ? '거래정지' : (params.data.creditHoldFlg === 'Y' ? '여신보류' : params.value);
                  return `<span class="status-badge status-${status}">${text}</span>`;
              }
            }
        ];

        const gridOptions = {
            columnDefs,
            defaultColDef: { sortable: true, filter: true, resizable: true },
            rowSelection: 'single',
            onGridReady: (params) => {
                customerGridApi = params.api;
                loadCustomerList();
            },
            onRowClicked: (event) => {
                if (event.data && event.data.customerCd) {
                    loadCustomerDetails(event.data.customerCd);
                }
            }
        };
        agGrid.createGrid(document.getElementById('customerGrid'), gridOptions);
    };

    // ===== 데이터 로딩 및 폼 처리 =====
    const loadCustomerList = async () => {
        try {
            const list = await API.getList();
            customerGridApi.setGridOption('rowData', list);
        } catch (error) {
            // fetchData에서 오류 처리
        }
    };

    const loadCustomerDetails = async (customerCd) => {
        try {
            const data = await API.getDetails(customerCd);
            populateForm(data);
            document.getElementById('btnDelete').disabled = false;
        } catch (error) {
            clearForm();
        }
    };

    const populateForm = (data) => {
        const { customer, creditInfo } = data;
        // Customer
        document.getElementById('customerCd').value = customer.customerCd || '';
        document.getElementById('customerNm').value = customer.customerNm || '';
        document.getElementById('representativeNm').value = customer.representativeNm || '';
        document.getElementById('businessRegNo').value = customer.businessRegNo || '';
        document.getElementById('phoneNo').value = customer.phoneNo || '';
        document.getElementById('address').value = customer.address || '';
        document.getElementById('email').value = customer.email || '';
        document.getElementById('salesEmpCd').value = customer.salesEmpCd || '';
        document.getElementById('discountRate').value = customer.discountRate || 0;
        // CreditInfo
        document.getElementById('creditLimit').value = creditInfo.creditLimit || 0;
        document.getElementById('paymentTerms').value = creditInfo.paymentTerms || 'Net 30';
        document.getElementById('creditStatus').value = creditInfo.creditStatus || '정상';
        document.getElementById('creditGrade').value = creditInfo.creditGrade || 'A';
        document.getElementById('tradeStopFlg').value = creditInfo.tradeStopFlg || 'N';
        document.getElementById('creditHoldFlg').value = creditInfo.creditHoldFlg || 'N';
    };
    
    const clearForm = () => {
        form.reset();
        document.getElementById('customerCd').value = '';
        document.getElementById('btnDelete').disabled = true;
        if(customerGridApi) customerGridApi.deselectAll();
    };

    // ===== 저장 및 삭제 로직 =====
    const saveCustomer = async () => {
        const customerNm = document.getElementById('customerNm').value;
        if (!customerNm || customerNm.trim() === '') {
            Swal.fire('입력 오류', '거래처명은 필수 항목입니다.', 'warning');
            return;
        }

        const dto = {
            customer: {
                customerCd: document.getElementById('customerCd').value,
                customerNm: document.getElementById('customerNm').value,
                representativeNm: document.getElementById('representativeNm').value,
                businessRegNo: document.getElementById('businessRegNo').value,
                phoneNo: document.getElementById('phoneNo').value,
                address: document.getElementById('address').value,
                email: document.getElementById('email').value,
                salesEmpCd: document.getElementById('salesEmpCd').value,
                creditGrade: document.getElementById('creditGrade').value,
                discountRate: document.getElementById('discountRate').value,
                useYn: 'Y'
            },
            creditInfo: {
                creditLimit: document.getElementById('creditLimit').value,
                paymentTerms: document.getElementById('paymentTerms').value,
                creditStatus: document.getElementById('creditStatus').value,
                tradeStopFlg: document.getElementById('tradeStopFlg').value,
                creditHoldFlg: document.getElementById('creditHoldFlg').value
            }
        };

        try {
            const result = await API.save(dto);
            if (result.success) {
                Swal.fire('저장 완료', result.message, 'success');
                clearForm();
                await loadCustomerList();
                setTimeout(() => {
                    customerGridApi.forEachNode(node => {
                        if (node.data.customerCd === result.customerCd) {
                            node.setSelected(true);
                            customerGridApi.ensureNodeVisible(node);
                        }
                    });
                }, 500);
            }
        } catch (error) {
            // fetchData에서 오류 처리
        }
    };
    
    const deleteCustomer = async () => {
        const customerCd = document.getElementById('customerCd').value;
        const customerNm = document.getElementById('customerNm').value;

        if (!customerCd) {
            Swal.fire('알림', '삭제할 거래처를 선택해주세요.', 'warning');
            return;
        }

        const result = await Swal.fire({
            title: '거래처 비활성화',
            html: `<strong>${customerNm} (${customerCd})</strong> 거래처와의 거래를 중단하시겠습니까?<br><small class="text-danger mt-2">이 작업은 거래처를 목록에서 숨기며, 되돌릴 수 있습니다.</small>`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#e74a3b',
            confirmButtonText: '예, 비활성화합니다',
            cancelButtonText: '취소'
        });

        if (result.isConfirmed) {
            try {
                const response = await API.delete(customerCd);
                if (response.success) {
                    Swal.fire('처리 완료', response.message, 'success');
                    // [수정] 삭제 성공 후 폼을 비우고, 그리드를 새로고침합니다.
                    clearForm();
                    await loadCustomerList();
                }
            } catch (error) {
                // fetchData에서 오류를 이미 처리하므로 별도 처리가 필요 없습니다.
            }
        }
    };

    // ===== 이벤트 리스너 바인딩 =====
    document.getElementById('btnNew').addEventListener('click', clearForm);
    document.getElementById('btnSave').addEventListener('click', saveCustomer);
    document.getElementById('btnDelete').addEventListener('click', deleteCustomer);

    // ===== 초기화 실행 =====
    initCustomerGrid();
});
