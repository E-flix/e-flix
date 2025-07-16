/**
 * ===================================================================
 * salesorder_management.js
 * - 여신 관리 기능 프론트엔드 구현
 * 1. [수정] 등록 버튼은 '거래정지/보류' 시에만 비활성화. 한도 초과는 저장 시 알림창으로 확인.
 * ===================================================================
 */

$(document).ready(function() {
    console.log('📋 주문서 관리 시스템 초기화 시작 (v7-credit-alert)');
  
    // =================== 전역 변수 ===================
    let gridApis = {
        header: null,
        detail: null,
        credit: null
    };
  
    let currentState = {
        editMode: false,
        currentOrder: null,
        selectedHeaderRow: null,
        isNewOrder: false,
        originalData: [],
        creditInfo: null
    };
  
    let modals = {
        quotation: new bootstrap.Modal(document.getElementById('quotationModal')),
        customer: new bootstrap.Modal(document.getElementById('customerModal')),
        item: new bootstrap.Modal(document.getElementById('itemModal'))
    };
  
    // =================== 유틸리티 함수 ===================
    const Utils = {
        formatDate: (dateStr) => !dateStr ? '' : dayjs(dateStr).format('YYYY-MM-DD'),
        formatCurrency: (value) => !value ? '0' : Number(value).toLocaleString(),
        formatNumber: (value) => !value ? '0' : Number(value).toLocaleString(),
        showAlert: {
            success: (title, text) => Swal.fire({ title, text, icon: 'success', timer: 2000 }),
            error: (title, text) => Swal.fire({ title, text, icon: 'error' }),
            warning: (title, text) => Swal.fire({ title, text, icon: 'warning' }),
            info: (title, text) => Swal.fire({ title, text, icon: 'info' }),
            loading: (title) => Swal.fire({ title, allowOutsideClick: false, showConfirmButton: false, didOpen: () => Swal.showLoading() }),
            confirm: (title, text) => Swal.fire({ title, text, icon: 'warning', showCancelButton: true, confirmButtonColor: '#d33', confirmButtonText: '확인', cancelButtonText: '취소' })
        }
    };
  
    // =================== API 서비스 ===================
    const API = {
        async request(url, options = {}) {
            try {
                const response = await fetch(url, {
                    method: options.method || 'GET',
                    headers: { 'Content-Type': 'application/json', ...options.headers },
                    body: options.body ? JSON.stringify(options.body) : undefined
                });
                if (!response.ok) {
                    const errorData = await response.json().catch(() => ({ message: response.statusText }));
                    throw new Error(errorData.message || `HTTP ${response.status}`);
                }
                const contentType = response.headers.get('content-type');
                return contentType?.includes('application/json') ? await response.json() : await response.text();
            } catch (error) {
                console.error('API 요청 실패:', error);
                Utils.showAlert.error('API 오류', error.message);
                throw error;
            }
        },
        getOrders: () => API.request('/bsn/sorlist/data'),
        getOrderDetails: (orderNo) => API.request(`/bsn/orders/${orderNo}/details`),
        saveOrder: (orderData) => API.request('/bsn/orders', { method: 'POST', body: orderData }),
        deleteOrder: (orderNo) => API.request(`/bsn/orders/${orderNo}`, { method: 'DELETE' }),
        getNextOrderNo: () => API.request('/bsn/orders/nextOrderNo'),
        getQuotations: (filter) => {
            const params = new URLSearchParams(Object.entries(filter).filter(([, value]) => value));
            return API.request(`/bsn/quotation/search?${params.toString()}`);
        },
        getQuotationDetails: (quotationNo) => API.request(`/bsn/quotation/details?quotationNo=${encodeURIComponent(quotationNo)}`),
        getCustomers: (name) => API.request(`/bsn/customers${name ? `?name=${encodeURIComponent(name)}` : ''}`),
        getItems: (itemName) => API.request(`/bsn/item/list${itemName ? `?itemName=${encodeURIComponent(itemName)}` : ''}`),
        getCreditInfo: (customerCd) => API.request(`/bsn/customer/${customerCd}/credit`)
    };
  
    // =================== 그리드 컴포넌트 ===================
    class CustomDatePicker {
        init(params) {
            this.eInput = document.createElement('input');
            this.eInput.type = 'date';
            this.eInput.className = 'ag-input-field-input ag-text-field-input';
            if (params.value) {
                try { this.eInput.value = new Date(params.value).toISOString().split('T')[0]; } 
                catch (e) { console.warn('날짜 변환 실패:', params.value); }
            }
        }
        getGui() { return this.eInput; }
        afterGuiAttached() { this.eInput.focus(); this.eInput.select(); }
        getValue() { return this.eInput.value || null; }
        destroy() {}
        isPopup() { return false; }
    }
  
    // =================== 그리드 관리자 ===================
    const GridManager = {
        initHeaderGrid() {
            const columnDefs = [
                { headerName: '선택', width: 60, checkboxSelection: true, headerCheckboxSelection: true, sortable: false, filter: false },
                { headerName: '주문번호', field: 'orderNo', width: 150, pinned: 'left', cellClass: 'font-weight-bold text-primary' },
                { headerName: '주문일자', field: 'orderDt', flex: 1, minWidth: 120, valueFormatter: p => Utils.formatDate(p.value), cellEditor: CustomDatePicker, editable: false, cellStyle: { textAlign: 'center' } },
                { headerName: '거래처', field: 'customerNm', flex: 2, minWidth: 180, cellRenderer: 'agAnimateShowChangeCellRenderer', cellClass: 'clickable-cell', editable: false },
                { headerName: '대표', field: 'representativeNm', flex: 1, minWidth: 110, editable: false, cellStyle: { textAlign: 'center' } },
                { headerName: '연락처', field: 'phoneNo', flex: 1.2, minWidth: 130, editable: false, cellStyle: { textAlign: 'center' } },
                { headerName: '담당자', field: 'salesEmpCd', flex: 1, minWidth: 100, editable: false, cellEditor: 'agTextCellEditor', cellStyle: { textAlign: 'center' } },
                { headerName: '할인율(%)', field: 'discountRate', flex: 1, minWidth: 100, valueFormatter: p => (p.value || 0) + '%', editable: false, cellEditor: 'agNumberCellEditor', cellStyle: { textAlign: 'center' } },
                { headerName: '결제조건', field: 'paymentTerms', flex: 1, minWidth: 120, editable: false, cellEditor: 'agTextCellEditor', cellStyle: { textAlign: 'center' } },
                { headerName: '작성자', field: 'orderWriter', flex: 1, minWidth: 100, editable: false, cellEditor: 'agTextCellEditor', cellStyle: { textAlign: 'center' } }
            ];
            const gridOptions = {
                columnDefs,
                defaultColDef: { resizable: true, sortable: true, filter: true },
                rowSelection: 'multiple', pagination: true, paginationPageSize: 10,
                getRowId: params => params.data.orderNo,
                getRowClass: params => params.data?.isNew ? 'new-order-row' : '',
                onGridReady: (params) => { gridApis.header = params.api; this.loadHeaderData(); },
                onRowClicked: (event) => {
                    if (currentState.editMode && currentState.isNewOrder && !event.data.isNew) {
                        Utils.showAlert.warning('편집 중', '새로운 주문서 작성을 완료하거나 취소해주세요.');
                        return;
                    }
                    this.selectOrder(event.data);
                },
                onCellClicked: (event) => {
                    if (currentState.editMode && event.colDef.field === 'customerNm') this.openCustomerModal();
                }
            };
            agGrid.createGrid(document.getElementById('headerGrid'), gridOptions);
        },
        initDetailGrid() {
            const columnDefs = [
                { headerName: '선택', width: 60, checkboxSelection: true, headerCheckboxSelection: true },
                { headerName: '순번', field: 'lineNo', width: 70, cellStyle: { textAlign: 'center' } },
                { headerName: '품목코드', field: 'itemCode', width: 130, cellClass: 'clickable-cell' },
                { headerName: '품목명', field: 'itemName', width: 200, cellRenderer: 'agAnimateShowChangeCellRenderer' },
                { headerName: '규격', field: 'spec', width: 120 },
                { headerName: '수량', field: 'qty', width: 90, type: 'numericColumn', valueFormatter: p => Utils.formatNumber(p.value), editable: true, cellStyle: { textAlign: 'right' } },
                { headerName: '단가', field: 'unitPrice', width: 110, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), editable: true, cellStyle: { textAlign: 'right' } },
                { headerName: '공급가액', field: 'supplyAmount', width: 120, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '부가세', field: 'taxAmount', width: 110, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '합계', field: 'totalAmount', width: 120, type: 'numericColumn', cellClass: 'font-weight-bold', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '출고일', field: 'outboundDt', width: 120, valueFormatter: p => Utils.formatDate(p.value), cellEditor: CustomDatePicker, cellStyle: { textAlign: 'center' } },
                { headerName: '납기일', field: 'catchDt', width: 120, valueFormatter: p => Utils.formatDate(p.value), cellEditor: CustomDatePicker, cellStyle: { textAlign: 'center' } },
                { headerName: '비고', field: 'remarks', flex: 1, minWidth: 150, cellEditor: 'agTextCellEditor' }
            ];
            const gridOptions = {
                columnDefs,
                defaultColDef: { resizable: true, sortable: true, filter: true, editable: false },
                rowSelection: 'multiple',
                onGridReady: (params) => { gridApis.detail = params.api; },
                onCellClicked: (event) => {
                    if (currentState.editMode && event.colDef.field === 'itemCode') this.openItemModal(event.node);
                },
                onCellValueChanged: (event) => {
                    if (['qty', 'unitPrice'].includes(event.colDef.field)) {
                        this.calculateRowTotal(event.node);
                    }
                }
            };
            agGrid.createGrid(document.getElementById('detailGrid'), gridOptions);
        },
        initCreditGrid() {
            const columnDefs = [
                { headerName: '여신한도', field: 'creditLimit', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '여신잔액', field: 'remainingCredit', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '여신사용액', field: 'creditUsed', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), cellStyle: { textAlign: 'right' } },
                { headerName: '여신상태', field: 'creditStatus', flex: 1, cellStyle: { textAlign: 'center' } },
                { headerName: '보류여부', field: 'creditHoldFlg', flex: 1, cellRenderer: p => p.value === 'Y' ? '예' : '아니오', cellStyle: { textAlign: 'center' } },
                { headerName: '거래정지', field: 'tradeStopFlg', flex: 1, cellRenderer: p => p.value === 'Y' ? '예' : '아니오', cellStyle: { textAlign: 'center' } }
            ];
            const gridOptions = {
                columnDefs,
                defaultColDef: { resizable: true },
                domLayout: 'autoHeight',
                onGridReady: (params) => { gridApis.credit = params.api; this.updateCreditInfo(null); }
            };
            agGrid.createGrid(document.getElementById('creditGrid'), gridOptions);
        },
        async loadHeaderData() {
            try {
                const orders = await API.getOrders();
                currentState.originalData = orders || [];
                if (gridApis.header) gridApis.header.setGridOption('rowData', [...currentState.originalData]);
            } catch (error) {
                console.error('❌ 주문서 목록 로드 실패:', error);
            }
        },
        addNewOrderRow(newOrder) {
            if (!gridApis.header) return;
            const cleanOriginalData = currentState.originalData.filter(item => !item.isNew);
            const newRowData = [newOrder, ...cleanOriginalData];
            gridApis.header.setGridOption('rowData', newRowData);
            setTimeout(() => {
                gridApis.header.ensureIndexVisible(0);
                const firstRowNode = gridApis.header.getRowNode(newOrder.orderNo);
                if (firstRowNode) firstRowNode.setSelected(true);
            }, 100);
        },
        removeNewOrderRow() {
            if (!gridApis.header) return;
            gridApis.header.setGridOption('rowData', [...currentState.originalData]);
        },
        updateNewOrderRow(orderNo, updatedData) {
            if (!gridApis.header) return;
            const currentRowData = [];
            gridApis.header.forEachNode(node => {
                currentRowData.push(node.data.orderNo === orderNo ? updatedData : node.data);
            });
            gridApis.header.setGridOption('rowData', currentRowData);
            setTimeout(() => {
                const updatedNode = gridApis.header.getRowNode(orderNo);
                if (updatedNode) updatedNode.setSelected(true);
            }, 100);
        },
        async selectOrder(orderData) {
            currentState.currentOrder = orderData;
            currentState.selectedHeaderRow = orderData;
            try {
                const details = await API.getOrderDetails(orderData.orderNo);
                if (gridApis.detail) gridApis.detail.setGridOption('rowData', details || []);
                this.updateTotalSummary();
                if (orderData.customerCd) {
                    const creditInfo = await API.getCreditInfo(orderData.customerCd);
                    this.updateCreditInfo(creditInfo);
                } else {
                    this.updateCreditInfo(null);
                }
            } catch (error) {
                console.error('❌ 주문 데이터 로드 실패:', error);
            }
        },
        updateCreditInfo(creditData) {
            currentState.creditInfo = creditData;
            if (gridApis.credit) gridApis.credit.setGridOption('rowData', creditData ? [creditData] : [{}]);
            const badge = document.getElementById('credit-status-badge');
            if (creditData) {
                const isTradeStop = creditData.tradeStopFlg === 'Y';
                const isCreditHold = creditData.creditHoldFlg === 'Y';
                if (isTradeStop) {
                    badge.className = 'badge bg-danger';
                    badge.textContent = '거래정지';
                } else if (isCreditHold) {
                    badge.className = 'badge bg-warning text-dark';
                    badge.textContent = '여신보류';
                } else {
                    badge.className = 'badge bg-success';
                    badge.textContent = creditData.creditStatus || '정상';
                }
            } else {
                badge.className = 'badge bg-secondary';
                badge.textContent = '거래처를 선택하세요';
            }
            OrderManager.updateSaveButtonState();
        },
        calculateRowTotal(rowNode) {
            const data = rowNode.data;
            const qty = Number(data.qty) || 0;
            const unitPrice = Number(data.unitPrice) || 0;
            const supplyAmount = qty * unitPrice;
            const taxAmount = Math.round(supplyAmount * 0.1);
            rowNode.setDataValue('supplyAmount', supplyAmount);
            rowNode.setDataValue('taxAmount', taxAmount);
            rowNode.setDataValue('totalAmount', supplyAmount + taxAmount);
            this.updateTotalSummary();
        },
        updateTotalSummary() {
            if (!gridApis.detail) return;
            let totalSupply = 0, totalTax = 0, totalAmount = 0;
            gridApis.detail.forEachNode(node => {
                totalSupply += Number(node.data.supplyAmount) || 0;
                totalTax += Number(node.data.taxAmount) || 0;
                totalAmount += Number(node.data.totalAmount) || 0;
            });
            const summaryData = [{ itemCode: '합계', supplyAmount: totalSupply, taxAmount: totalTax, totalAmount }];
            gridApis.detail.setGridOption('pinnedBottomRowData', summaryData);
            OrderManager.updateSaveButtonState();
        },
        openCustomerModal() { modals.customer.show(); this.loadCustomerList(); },
        async loadCustomerList(name = '') {
            try { this.renderCustomerTable(await API.getCustomers(name)); } 
            catch (error) { console.error('거래처 목록 로드 실패:', error); }
        },
        renderCustomerTable(customers) {
            const tbody = document.getElementById('customerSearchResults');
            tbody.innerHTML = !customers || customers.length === 0
                ? '<tr><td colspan="5" class="text-center">검색 결과가 없습니다.</td></tr>'
                : customers.map(c => `
                    <tr>
                        <td><button class="btn btn-sm btn-primary btn-select-customer" data-customer='${JSON.stringify(c)}'>선택</button></td>
                        <td>${c.customerCd}</td><td>${c.customerNm}</td>
                        <td>${c.representativeNm || ''}</td><td>${c.phoneNo || ''}</td>
                    </tr>`).join('');
        },
        openItemModal(rowNode) {
            currentState.selectedDetailRow = rowNode;
            document.getElementById('itemSearchInput').value = '';
            modals.item.show();
            this.loadItemList();
            setTimeout(() => document.getElementById('itemSearchInput').focus(), 500);
        },
        async loadItemList(itemName = '') {
            try { this.renderItemTable(await API.getItems(itemName)); } 
            catch (error) { console.error('품목 목록 로드 실패:', error); }
        },
        renderItemTable(items) {
            const tbody = document.getElementById('itemSearchResults');
            tbody.innerHTML = !items || items.length === 0
                ? '<tr><td colspan="6" class="text-center">검색 결과가 없습니다.</td></tr>'
                : items.map(i => `
                    <tr>
                        <td><button class="btn btn-sm btn-primary btn-select-item" data-item='${JSON.stringify(i)}'>선택</button></td>
                        <td>${i.itemCode}</td><td>${i.itemName}</td><td>${i.spec || ''}</td>
                        <td>${i.unit || ''}</td><td>${Number(i.salePrice || 0).toLocaleString()}원</td>
                    </tr>`).join('');
        }
    };
  
    // =================== 주문서 관리자 ===================
    const OrderManager = {
        setEditMode(isEdit) {
            currentState.editMode = isEdit;
            ['btnAddRow', 'btnDeleteRow', 'btnQuotationSearch'].forEach(id => document.getElementById(id).disabled = !isEdit);
            ['btnNew', 'btnEdit', 'btnDelete'].forEach(id => document.getElementById(id).disabled = isEdit);
            const cancelBtn = document.getElementById('btnCancel');
            if (cancelBtn) cancelBtn.style.display = (isEdit && currentState.isNewOrder) ? 'inline-block' : 'none';
            
            this.updateSaveButtonState();
  
            const setEditable = (api, fields, editable) => {
                if (!api) return;
                const colDefs = api.getColumnDefs();
                colDefs.forEach(c => { if (fields.includes(c.field)) c.editable = editable; });
                api.setGridOption('columnDefs', colDefs);
            };
            setEditable(gridApis.header, ['orderDt', 'salesEmpCd', 'discountRate', 'paymentTerms', 'orderWriter'], isEdit);
            setEditable(gridApis.detail, ['qty', 'unitPrice', 'outboundDt', 'catchDt', 'remarks'], isEdit);
        },
        // [수정] 한도 초과 시에도 버튼은 활성화. 거래정지/보류 시에만 비활성화.
        updateSaveButtonState() {
            const btnSave = document.getElementById('btnSave');
            if (!currentState.editMode) {
                btnSave.disabled = true;
                return;
            }
  
            const creditInfo = currentState.creditInfo;
            let isSaveable = true;
            let reason = "주문서를 저장합니다.";
  
            if (!creditInfo) {
                isSaveable = false;
                reason = "거래처의 여신 정보가 없습니다.";
            } else if (creditInfo.tradeStopFlg === 'Y') {
                isSaveable = false;
                reason = "거래가 정지된 거래처입니다.";
            } else if (creditInfo.creditHoldFlg === 'Y') {
                isSaveable = false;
                reason = "여신이 보류된 거래처입니다.";
            }
            
            btnSave.disabled = !isSaveable;
            btnSave.setAttribute('title', reason);
        },
        async createNewOrder() {
            if (currentState.editMode) {
                Utils.showAlert.warning('편집 중', '이미 새로운 주문서를 작성하고 있습니다.');
                return;
            }
            try {
                Utils.showAlert.loading('새 주문번호 생성 중...');
                const nextOrderNo = await API.getNextOrderNo();
  
                if (gridApis.header && gridApis.header.getRowNode(nextOrderNo)) {
                    Swal.close();
                    Utils.showAlert.error('주문번호 중복', `백엔드에서 전달받은 주문번호 [${nextOrderNo}]가 이미 존재합니다. 잠시 후 다시 시도해주세요.`);
                    return;
                }
  
                const newOrder = {
                    orderNo: nextOrderNo, orderDt: dayjs().format('YYYY-MM-DD'),
                    customerCd: '', customerNm: '거래처를 선택하세요',
                    paymentTerms: 'Net 30', discountRate: 0,
                    isNew: true, _isNewOrder: true
                };
                
                GridManager.addNewOrderRow(newOrder);
                const newDetails = [{ lineNo: 1, itemCode: '', itemName: '품목을 선택하세요', qty: 1, unitPrice: 0, supplyAmount: 0, taxAmount: 0, totalAmount: 0 }];
                if (gridApis.detail) {
                    gridApis.detail.setGridOption('rowData', newDetails);
                    GridManager.updateTotalSummary();
                }
                GridManager.updateCreditInfo(null);
  
                currentState.currentOrder = newOrder;
                currentState.isNewOrder = true;
                currentState.selectedHeaderRow = newOrder;
                this.setEditMode(true);
                Swal.close();
                await Swal.fire({
                    title: '신규 주문서 생성 완료! 🎉',
                    html: `<div class="text-start"><p><strong>주문번호:</strong> ${nextOrderNo}</p><hr><p><strong>📋 입력 순서:</strong></p><ol class="text-start"><li><span class="text-primary">거래처명</span> 클릭 → 거래처 선택</li><li><span class="text-success">담당자</span> 직접 입력</li><li><span class="text-warning">품목코드</span> 클릭 → 품목 선택</li><li><span class="text-info">수량, 단가</span> 입력</li><li><span class="text-danger">등록</span> 버튼 클릭</li></ol></div>`,
                    icon: 'info', confirmButtonText: '시작하기', width: '500px'
                });
            } catch (error) {
                Swal.close();
                Utils.showAlert.error('오류', '새 주문번호 생성에 실패했습니다.');
            }
        },
        // [수정] 저장 로직에 여신 한도 초과 시 알림창 추가
        async saveOrder() {
            if (!currentState.editMode || !currentState.currentOrder) return;
            const headerNode = gridApis.header.getRowNode(currentState.currentOrder.orderNo);
            if (!headerNode) return Utils.showAlert.error('오류', '주문서 데이터를 찾을 수 없습니다.');
            
            const headerData = headerNode.data;
            if (!headerData.customerCd) return Utils.showAlert.warning('입력 확인', '거래처를 선택해주세요.');
            if (!headerData.salesEmpCd?.trim()) return Utils.showAlert.warning('입력 확인', '담당자를 입력해주세요.');
  
            const details = [];
            let currentOrderAmount = 0;
            gridApis.detail.forEachNode(node => {
                if (node.data.itemCode?.trim()) {
                    details.push({ ...node.data, lineNo: details.length + 1 });
                    currentOrderAmount += Number(node.data.supplyAmount) || 0;
                }
            });
            if (details.length === 0) return Utils.showAlert.warning('입력 확인', '최소 하나의 품목을 추가해주세요.');
  
            // 여신 한도 체크
            const creditInfo = currentState.creditInfo;
            if (creditInfo) {
                const remainingCredit = Number(creditInfo.remainingCredit) || 0;
                if (remainingCredit < currentOrderAmount) {
                    const confirmResult = await Utils.showAlert.confirm(
                        '여신 한도 초과',
                        `여신 한도를 초과합니다. (잔여: ${Utils.formatCurrency(remainingCredit)} / 주문: ${Utils.formatCurrency(currentOrderAmount)})`
                    );
                    if (!confirmResult.isConfirmed) {
                        return; // 사용자가 취소하면 저장 중단
                    }
                }
            }
  
            const result = await Utils.showAlert.confirm('주문서 저장', `주문번호 ${headerData.orderNo}를 저장하시겠습니까?`);
            if (!result.isConfirmed) return;
  
            try {
                Utils.showAlert.loading('저장 중...');
                const response = await API.saveOrder({ ...headerData, details });
                
                Swal.close();
                await Utils.showAlert.success('저장 완료!', `주문번호 [${response.orderNo || headerData.orderNo}]가 성공적으로 저장되었습니다.`);
                
                this.setEditMode(false);
                currentState.currentOrder = null;
                currentState.isNewOrder = false;
                
                await GridManager.loadHeaderData();
                
                setTimeout(() => {
                    const savedNode = gridApis.header.getRowNode(response.orderNo || headerData.orderNo);
                    if (savedNode) {
                        savedNode.setSelected(true);
                        gridApis.header.ensureNodeVisible(savedNode);
                        GridManager.selectOrder(savedNode.data);
                    }
                }, 500);
  
            } catch (error) {
                Swal.close();
            }
        },
        async deleteOrder() {
            const selectedRows = gridApis.header.getSelectedRows();
            if (selectedRows.length === 0) return Utils.showAlert.info('알림', '삭제할 주문서를 선택해주세요.');
            const result = await Utils.showAlert.confirm('주문서 삭제', `선택한 ${selectedRows.length}건의 주문서를 삭제하시겠습니까?`);
            if (!result.isConfirmed) return;
            try {
                Utils.showAlert.loading('삭제 중...');
                await Promise.all(selectedRows.map(order => API.deleteOrder(order.orderNo)));
                Swal.close();
                Utils.showAlert.success('성공', '선택한 주문서가 삭제되었습니다.');
                await GridManager.loadHeaderData();
                if (gridApis.detail) gridApis.detail.setGridOption('rowData', []);
                GridManager.updateCreditInfo(null);
            } catch (error) {
                Swal.close();
                Utils.showAlert.error('삭제 실패', '삭제 중 오류가 발생했습니다.');
            }
        },
        addDetailRow() {
            if (!currentState.editMode) return;
            const newRow = { lineNo: gridApis.detail.getDisplayedRowCount() + 1, qty: 1, outState: '대기' };
            gridApis.detail.applyTransaction({ add: [newRow] });
        },
        deleteDetailRow() {
            if (!currentState.editMode) return;
            const selectedRows = gridApis.detail.getSelectedRows();
            if (selectedRows.length === 0) return Utils.showAlert.info('알림', '삭제할 항목을 선택해주세요.');
            gridApis.detail.applyTransaction({ remove: selectedRows });
            GridManager.updateTotalSummary();
        },
        async cancelNewOrder() {
            if (!currentState.editMode || !currentState.isNewOrder) return;
            const result = await Utils.showAlert.confirm('작업 취소', '신규 주문서 작성을 취소하시겠습니까?\n입력한 모든 내용이 사라집니다.');
            if (!result.isConfirmed) return;
            this.setEditMode(false);
            currentState.currentOrder = null;
            currentState.isNewOrder = false;
            currentState.selectedHeaderRow = null;
            GridManager.removeNewOrderRow();
            if (gridApis.detail) gridApis.detail.setGridOption('rowData', []);
            GridManager.updateCreditInfo(null);
            Utils.showAlert.info('취소됨', '신규 주문서 작성이 취소되었습니다.');
        }
    };
  
    // =================== 견적서 관리자 ===================
    const QuotationManager = {
        openSearchModal() {
            if (!currentState.editMode) return Utils.showAlert.warning('알림', '먼저 신규 주문서를 생성해주세요.');
            modals.quotation.show();
            this.searchQuotations();
        },
        async searchQuotations() {
            const filter = {
                quotationNo: document.getElementById('quotationNoFilter').value.trim(),
                customerName: document.getElementById('customerNameFilter').value.trim(),
                dateFrom: document.getElementById('quotationDateFrom').value,
                dateTo: document.getElementById('quotationDateTo').value
            };
            try { this.renderQuotationTable(await API.getQuotations(filter)); } 
            catch (error) { console.error('❌ 견적서 검색 실패:', error); }
        },
        renderQuotationTable(quotations) {
            const tbody = document.getElementById('quotationSearchResults');
            tbody.innerHTML = !quotations || quotations.length === 0
                ? '<tr><td colspan="9" class="text-center">검색 결과가 없습니다.</td></tr>'
                : quotations.map(q => `
                    <tr>
                        <td><button class="btn btn-sm btn-success btn-select-quotation" data-quotation='${JSON.stringify(q)}'>선택</button></td>
                        <td>${q.quotationNo}</td><td>${Utils.formatDate(q.quotationDt)}</td><td>${q.customerName}</td>
                        <td>${q.representativeNm || ''}</td><td>${q.phone || ''}</td><td>${Utils.formatDate(q.validPeriod)}</td>
                        <td>${Utils.formatDate(q.expectedDeliveryDt)}</td><td>${q.remarks || ''}</td>
                    </tr>`).join('');
        },
        async selectQuotation(quotationData) {
            try {
                Utils.showAlert.loading('견적서 변환 중...');
                const details = await API.getQuotationDetails(quotationData.quotationNo);
                const updatedHeaderData = {
                    ...currentState.currentOrder,
                    customerCd: quotationData.customerCd, customerNm: quotationData.customerName,
                    representativeNm: quotationData.representativeNm, phoneNo: quotationData.phone,
                    salesEmpCd: quotationData.salesEmpCd || quotationData.sender,
                    discountRate: quotationData.discountRate, orderWriter: quotationData.sender
                };
                GridManager.updateNewOrderRow(currentState.currentOrder.orderNo, updatedHeaderData);
                currentState.currentOrder = updatedHeaderData;
                const orderDetails = details.map((d, i) => ({
                    lineNo: i + 1, itemCode: d.itemCode, itemName: d.itemName, spec: d.spec,
                    qty: d.qty, unitPrice: d.unitPrice, supplyAmount: d.supplyAmount,
                    taxAmount: d.taxAmount, totalAmount: d.totalMoney, remarks: d.remarks, outState: '대기'
                }));
                gridApis.detail.setGridOption('rowData', orderDetails);
                GridManager.updateTotalSummary();
                if (quotationData.customerCd) {
                    const creditInfo = await API.getCreditInfo(quotationData.customerCd);
                    GridManager.updateCreditInfo(creditInfo);
                }
                modals.quotation.hide();
                Swal.close();
                await Utils.showAlert.success('견적서 변환 완료!', '주문서 정보가 업데이트 되었습니다.');
            } catch (error) {
                Swal.close();
                Utils.showAlert.error('변환 실패', '견적서 변환 중 오류가 발생했습니다.');
            }
        }
    };
  
    // =================== 이벤트 리스너 ===================
    const EventHandlers = {
        init() {
            document.getElementById('btnNew').addEventListener('click', () => OrderManager.createNewOrder());
            document.getElementById('btnSave').addEventListener('click', () => OrderManager.saveOrder());
            document.getElementById('btnDelete').addEventListener('click', () => OrderManager.deleteOrder());
            document.getElementById('btnAddRow').addEventListener('click', () => OrderManager.addDetailRow());
            document.getElementById('btnDeleteRow').addEventListener('click', () => OrderManager.deleteDetailRow());
            document.getElementById('btnCancel')?.addEventListener('click', () => OrderManager.cancelNewOrder());
            document.getElementById('btnQuotationSearch').addEventListener('click', () => QuotationManager.openSearchModal());
            document.getElementById('btnQuotationFilter').addEventListener('click', () => QuotationManager.searchQuotations());
            document.getElementById('btnQuotationFilterReset').addEventListener('click', () => {
                ['quotationNoFilter', 'customerNameFilter', 'quotationDateFrom', 'quotationDateTo'].forEach(id => document.getElementById(id).value = '');
                QuotationManager.searchQuotations();
            });
  
            const addClickListener = (selector, handler) => {
                document.querySelector(selector).addEventListener('click', e => {
                    const target = e.target.closest(handler.selector);
                    if (target) handler.action(JSON.parse(target.dataset[handler.dataKey]));
                });
            };
            addClickListener('#quotationSearchResults', { selector: '.btn-select-quotation', dataKey: 'quotation', action: QuotationManager.selectQuotation });
            addClickListener('#customerSearchResults', { selector: '.btn-select-customer', dataKey: 'customer', action: (data) => this.selectCustomer(data) });
            addClickListener('#itemSearchResults', { selector: '.btn-select-item', dataKey: 'item', action: (data) => this.selectItem(data) });
            
            const addSearchListener = (inputId, buttonId, searchFn) => {
                const input = document.getElementById(inputId);
                document.getElementById(buttonId).addEventListener('click', () => searchFn(input.value.trim()));
                input.addEventListener('keypress', e => { if (e.key === 'Enter') { e.preventDefault(); searchFn(input.value.trim()); } });
            };
            addSearchListener('customerSearchInput', 'btnCustomerSearch', GridManager.loadCustomerList.bind(GridManager));
            addSearchListener('itemSearchInput', 'btnItemSearch', GridManager.loadItemList.bind(GridManager));
        },
        selectCustomer(customerData) {
            if (!currentState.currentOrder) return;
            const updatedData = {
                ...currentState.currentOrder,
                customerCd: customerData.customerCd, customerNm: customerData.customerNm,
                representativeNm: customerData.representativeNm, phoneNo: customerData.phoneNo,
                salesEmpCd: customerData.salesEmpCd || currentState.currentOrder.salesEmpCd,
                discountRate: customerData.discountRate
            };
            GridManager.updateNewOrderRow(currentState.currentOrder.orderNo, updatedData);
            currentState.currentOrder = updatedData;
            
            API.getCreditInfo(customerData.customerCd)
               .then(creditInfo => {
                   GridManager.updateCreditInfo(creditInfo);
               })
               .catch(error => {
                   GridManager.updateCreditInfo(null);
               });
  
            modals.customer.hide();
        },
        selectItem(itemData) {
            if (!currentState.selectedDetailRow) return;
            const rowNode = currentState.selectedDetailRow;
            const updatedData = {
                ...rowNode.data,
                itemCode: itemData.itemCode, itemName: itemData.itemName,
                spec: itemData.spec || '', unitPrice: itemData.salePrice || 0,
                qty: rowNode.data.qty || 1
            };
            rowNode.setData(updatedData);
            GridManager.calculateRowTotal(rowNode);
            modals.item.hide();
        } 
    };
  
    // =================== 초기화 ===================
    function initialize() {
        GridManager.initHeaderGrid();
        GridManager.initDetailGrid();
        GridManager.initCreditGrid();
        EventHandlers.init();
        OrderManager.setEditMode(false);
        console.log('✅ 주문서 관리 시스템 초기화 완료');
    }
  
    initialize();
  });
  