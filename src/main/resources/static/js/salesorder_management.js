/**
 * ===================================================================
 * salesorder_management.js - 완전히 새로운 접근법 (rowData 재구성 방식)
 * 
 * 주요 변경 사항:
 * 1. applyTransaction 대신 전체 rowData 재구성 방식 사용
 * 2. 기존 데이터를 보존하면서 신규 행만 추가/제거하는 로직
 * 3. 출하관리 시스템과 동일한 패턴 적용
 * ===================================================================
 */

$(document).ready(function() {
  console.log('📋 주문서 관리 시스템 초기화 시작');

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
      originalData: [] // ★★★ 원본 데이터 저장용 ★★★
  };

  let modals = {
      quotation: new bootstrap.Modal(document.getElementById('quotationModal')),
      customer: new bootstrap.Modal(document.getElementById('customerModal')),
      item: new bootstrap.Modal(document.getElementById('itemModal'))
  };

  // =================== 유틸리티 함수 ===================
  const Utils = {
      formatDate: (dateStr) => {
          if (!dateStr) return '';
          try {
              return dayjs(dateStr).format('YYYY-MM-DD');
          } catch (e) {
              return dateStr;
          }
      },

      formatCurrency: (value) => {
          if (!value) return '0';
          return Number(value).toLocaleString();
      },

      formatNumber: (value) => {
          if (!value) return '0';
          return Number(value).toLocaleString();
      },

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
                  headers: {
                      'Content-Type': 'application/json',
                      ...options.headers
                  },
                  body: options.body ? JSON.stringify(options.body) : undefined
              });

              if (!response.ok) {
                  throw new Error(`HTTP ${response.status}: ${response.statusText}`);
              }

              const contentType = response.headers.get('content-type');
              if (contentType && contentType.includes('application/json')) {
                  return await response.json();
              } else {
                  return await response.text();
              }
          } catch (error) {
              console.error('API 요청 실패:', error);
              Utils.showAlert.error('API 오류', error.message);
              throw error;
          }
      },

      // 주문서 관련 API
      getOrders: () => API.request('/bsn/sorlist/data'),
      getOrderDetails: (orderNo) => API.request(`/bsn/orders/${orderNo}/details`),
      saveOrder: (orderData) => API.request('/bsn/orders', { method: 'POST', body: orderData }),
      deleteOrder: (orderNo) => API.request(`/bsn/orders/${orderNo}`, { method: 'DELETE' }),
      getNextOrderNo: () => API.request('/bsn/orders/nextOrderNo'),

      // 견적서 관련 API
      getQuotations: (filter) => {
          const params = new URLSearchParams();
          Object.keys(filter).forEach(key => {
              if (filter[key]) params.append(key, filter[key]);
          });
          return API.request(`/bsn/quotation/search?${params.toString()}`);
      },
      getQuotationDetails: (quotationNo) => API.request(`/bsn/quotation/details?quotationNo=${encodeURIComponent(quotationNo)}`),

      // 거래처/품목 관련 API
      getCustomers: (name) => API.request(`/bsn/customers${name ? `?name=${encodeURIComponent(name)}` : ''}`),
      getItems: (itemName) => API.request(`/bsn/item/list${itemName ? `?itemName=${encodeURIComponent(itemName)}` : ''}`),
      getCreditInfo: (customerCd) => API.request(`/bsn/customer/${customerCd}/credit`)
  };

  // =================== DatePicker 및 Select 컴포넌트 ===================
  class CustomDatePicker {
      init(params) {
          this.eInput = document.createElement('input');
          this.eInput.type = 'date';
          this.eInput.style.width = '100%';
          this.eInput.style.height = '100%';
          this.eInput.style.border = 'none';
          this.eInput.style.outline = 'none';
          this.eInput.style.padding = '4px';
          
          if (params.value) {
              try {
                  const dateValue = new Date(params.value);
                  if (!isNaN(dateValue.getTime())) {
                      this.eInput.value = dateValue.toISOString().split('T')[0];
                  }
              } catch (e) {
                  console.warn('날짜 변환 실패:', params.value);
              }
          }
      }

      getGui() { return this.eInput; }
      afterGuiAttached() { this.eInput.focus(); this.eInput.select(); }
      getValue() { return this.eInput.value || null; }
      destroy() {}
      isPopup() { return false; }
  }

  class OutboundStatusSelect {
      init(params) {
          this.eSelect = document.createElement('select');
          this.eSelect.style.width = '100%';
          this.eSelect.style.height = '100%';
          this.eSelect.style.border = 'none';
          this.eSelect.style.outline = 'none';
          this.eSelect.style.padding = '2px';

          const options = ['대기', '준비중', '출고완료', '취소'];
          options.forEach(option => {
              const optionElement = document.createElement('option');
              optionElement.value = option;
              optionElement.text = option;
              this.eSelect.appendChild(optionElement);
          });

          if (params.value) {
              this.eSelect.value = params.value;
          }
      }

      getGui() { return this.eSelect; }
      afterGuiAttached() { this.eSelect.focus(); }
      getValue() { return this.eSelect.value; }
      destroy() {}
      isPopup() { return false; }
  }

  // =================== 그리드 관리자 ===================
  const GridManager = {
      // 헤더 그리드 초기화
      initHeaderGrid() {
          const columnDefs = [
              { headerName: '선택', width: 60, checkboxSelection: true, headerCheckboxSelection: true },
              { headerName: '주문번호', field: 'orderNo', width: 150, pinned: 'left', cellClass: 'font-weight-bold text-primary' },
              { 
                  headerName: '주문일자', 
                  field: 'orderDt', 
                  width: 120, 
                  valueFormatter: p => Utils.formatDate(p.value),
                  cellEditor: 'customDatePicker',
                  cellEditorPopup: false,
                  editable: false
              },
              { 
                  headerName: '거래처', 
                  field: 'customerNm', 
                  width: 180, 
                  cellRenderer: 'agAnimateShowChangeCellRenderer',
                  cellClass: 'clickable-cell',
                  editable: false
              },
              { headerName: '대표', field: 'representativeNm', width: 110, editable: false },
              { headerName: '연락처', field: 'phoneNo', width: 130, editable: false },
              { 
                  headerName: '담당자', 
                  field: 'salesEmpCd', 
                  width: 100,
                  editable: false,
                  cellEditor: 'agTextCellEditor'
              },
              { 
                  headerName: '할인율(%)', 
                  field: 'discountRate', 
                  width: 90, 
                  valueFormatter: p => (p.value || 0) + '%',
                  editable: false,
                  cellEditor: 'agNumberCellEditor'
              },
              { 
                  headerName: '결제조건', 
                  field: 'paymentTerms', 
                  width: 120,
                  editable: false,
                  cellEditor: 'agTextCellEditor'
              },
              { 
                  headerName: '작성자', 
                  field: 'orderWriter', 
                  width: 90,
                  editable: false,
                  cellEditor: 'agTextCellEditor'
              }
          ];

          const gridOptions = {
              columnDefs: columnDefs,
              components: {
                  customDatePicker: CustomDatePicker,
                  outboundStatusSelect: OutboundStatusSelect
              },
              defaultColDef: {
                  resizable: true,
                  sortable: true,
                  filter: true
              },
              rowSelection: 'multiple',
              pagination: true,
              paginationPageSize: 10,
              getRowId: params => params.data.orderNo,
              getRowClass: params => {
                  if (params.data && params.data.isNew) {
                      return 'new-order-row';
                  }
                  return '';
              },
              onGridReady: (params) => {
                  gridApis.header = params.api;
                  this.loadHeaderData();
              },
              onRowClicked: (event) => {
                  if (currentState.editMode && currentState.isNewOrder) {
                      if (!event.data.isNew) {
                          Utils.showAlert.warning('편집 중', '새로운 주문서 작성을 완료하거나 취소해주세요.');
                          return;
                      }
                  }
                  this.selectOrder(event.data);
              },
              onCellClicked: (event) => {
                  if (currentState.editMode && event.colDef.field === 'customerNm') {
                      this.openCustomerModal();
                  }
              }
          };

          agGrid.createGrid(document.getElementById('headerGrid'), gridOptions);
      },

      // 디테일 그리드 초기화
      initDetailGrid() {
          const columnDefs = [
              { headerName: '선택', width: 60, checkboxSelection: true, headerCheckboxSelection: true },
              { headerName: '순번', field: 'lineNo', width: 70, cellClass: 'text-center' },
              { headerName: '품목코드', field: 'itemCode', width: 130, cellClass: 'clickable-cell' },
              { headerName: '품목명', field: 'itemName', width: 200, cellRenderer: 'agAnimateShowChangeCellRenderer' },
              { headerName: '규격', field: 'spec', width: 120 },
              { headerName: '수량', field: 'qty', width: 90, type: 'numericColumn', valueFormatter: p => Utils.formatNumber(p.value), editable: true },
              { headerName: '단가', field: 'unitPrice', width: 110, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value), editable: true },
              { headerName: '공급가액', field: 'supplyAmount', width: 120, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value) },
              { headerName: '부가세', field: 'taxAmount', width: 110, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value) },
              { headerName: '합계', field: 'totalAmount', width: 120, type: 'numericColumn', cellClass: 'font-weight-bold', valueFormatter: p => Utils.formatCurrency(p.value) },
              { 
                  headerName: '출고일', 
                  field: 'outboundDt', 
                  width: 120, 
                  valueFormatter: p => Utils.formatDate(p.value),
                  cellEditor: 'customDatePicker',
                  cellEditorPopup: false
              },
              { 
                  headerName: '납기일', 
                  field: 'catchDt', 
                  width: 120, 
                  valueFormatter: p => Utils.formatDate(p.value),
                  cellEditor: 'customDatePicker',
                  cellEditorPopup: false
              },
              { 
                  headerName: '출고상태', 
                  field: 'outState', 
                  width: 100,
                  cellEditor: 'outboundStatusSelect',
                  cellEditorPopup: false
              },
              { 
                  headerName: '비고', 
                  field: 'remarks', 
                  flex: 1, 
                  minWidth: 150,
                  cellEditor: 'agTextCellEditor'
              }
          ];

          const gridOptions = {
              columnDefs: columnDefs,
              components: {
                  customDatePicker: CustomDatePicker
              },
              defaultColDef: {
                  resizable: true,
                  sortable: true,
                  filter: true,
                  editable: false
              },
              rowSelection: 'multiple',
              onGridReady: (params) => {
                  gridApis.detail = params.api;
              },
              onCellClicked: (event) => {
                  if (currentState.editMode && event.colDef.field === 'itemCode') {
                      this.openItemModal(event.node);
                  }
              },
              onCellValueChanged: (event) => {
                  if (event.colDef.field === 'qty' || event.colDef.field === 'unitPrice') {
                      this.calculateRowTotal(event.node);
                      this.updateTotalSummary();
                  }
              }
          };

          agGrid.createGrid(document.getElementById('detailGrid'), gridOptions);
      },

      // 여신 그리드 초기화
      initCreditGrid() {
          const columnDefs = [
              { headerName: '여신한도', field: 'creditLimit', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value) },
              { headerName: '여신잔액', field: 'remainingCredit', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value) },
              { headerName: '여신사용액', field: 'creditUsed', flex: 1, type: 'numericColumn', valueFormatter: p => Utils.formatCurrency(p.value) },
              { headerName: '여신상태', field: 'creditStatus', flex: 1 },
              { headerName: '보류여부', field: 'creditHoldFlg', flex: 1, cellRenderer: p => p.value === 'Y' ? '예' : '아니오' },
              { headerName: '거래정지', field: 'tradeStopFlg', flex: 1, cellRenderer: p => p.value === 'Y' ? '예' : '아니오' }
          ];

          const gridOptions = {
              columnDefs: columnDefs,
              defaultColDef: { resizable: true },
              domLayout: 'autoHeight',
              onGridReady: (params) => {
                  gridApis.credit = params.api;
                  this.updateCreditInfo(null);
              }
          };

          agGrid.createGrid(document.getElementById('creditGrid'), gridOptions);
      },

      // ★★★ 헤더 데이터 로드 (원본 데이터 보관) ★★★
      async loadHeaderData() {
          try {
              const orders = await API.getOrders();
              currentState.originalData = orders || []; // ★★★ 원본 데이터 저장 ★★★
              
              if (gridApis.header) {
                  gridApis.header.setGridOption('rowData', [...currentState.originalData]); // 복사본 사용
              }
              console.log('✅ 주문서 목록 로드 완료:', orders?.length || 0, '건');
              console.log('📦 원본 데이터 백업 완료');
          } catch (error) {
              console.error('❌ 주문서 목록 로드 실패:', error);
          }
      },

      // ★★★ 전체 rowData 재구성 방식으로 신규 행 추가 ★★★
      addNewOrderRow(newOrder) {
          if (!gridApis.header || !currentState.originalData) return;

          console.log('➕ 신규 행 추가 (rowData 재구성 방식)');
          
          // 1. 현재 원본 데이터에서 신규 행이 있는지 확인하고 제거
          const cleanOriginalData = currentState.originalData.filter(item => !item.isNew);
          
          // 2. 신규 행을 맨 앞에 추가한 새로운 배열 생성
          const newRowData = [newOrder, ...cleanOriginalData];
          
          // 3. 전체 rowData 재설정
          gridApis.header.setGridOption('rowData', newRowData);
          
          // 4. 첫 번째 행으로 스크롤 및 선택
          setTimeout(() => {
              gridApis.header.ensureIndexVisible(0);
              const firstRowNode = gridApis.header.getRowNode(newOrder.orderNo);
              if (firstRowNode) {
                  firstRowNode.setSelected(true);
                  console.log('✅ 신규 행 선택 완료');
              }
          }, 100);
          
          console.log('✅ rowData 재구성 완료 - 총', newRowData.length, '건');
      },

      // ★★★ 신규 행 제거 (원본 데이터로 복구) ★★★
      removeNewOrderRow() {
          if (!gridApis.header || !currentState.originalData) return;

          console.log('❌ 신규 행 제거 (원본 데이터 복구)');
          
          // 원본 데이터로 완전 복구
          gridApis.header.setGridOption('rowData', [...currentState.originalData]);
          
          console.log('✅ 원본 데이터 복구 완료 - 총', currentState.originalData.length, '건');
      },

      // ★★★ 헤더 데이터 업데이트 (견적서 선택 시) ★★★
      updateNewOrderRow(orderNo, updatedData) {
          if (!gridApis.header) return;

          console.log('🔄 신규 행 업데이트:', orderNo);
          
          // 현재 그리드의 모든 데이터를 가져옴
          const currentRowData = [];
          gridApis.header.forEachNode(node => {
              if (node.data.orderNo === orderNo) {
                  // 해당 주문번호의 행을 업데이트된 데이터로 교체
                  currentRowData.push(updatedData);
              } else {
                  // 다른 행은 그대로 유지
                  currentRowData.push(node.data);
              }
          });
          
          // 전체 rowData 재설정
          gridApis.header.setGridOption('rowData', currentRowData);
          
          // 업데이트된 행 다시 선택
          setTimeout(() => {
              const updatedNode = gridApis.header.getRowNode(orderNo);
              if (updatedNode) {
                  updatedNode.setSelected(true);
                  console.log('✅ 업데이트된 행 선택 완료');
              }
          }, 100);
          
          console.log('✅ 신규 행 업데이트 완료');
      },

      // 주문 선택
      async selectOrder(orderData) {
          currentState.currentOrder = orderData;
          currentState.selectedHeaderRow = orderData;

          try {
              // 디테일 데이터 로드
              const details = await API.getOrderDetails(orderData.orderNo);
              if (gridApis.detail) {
                  gridApis.detail.setGridOption('rowData', details || []);
              }
              this.updateTotalSummary();

              // 여신 정보 로드
              if (orderData.customerCd) {
                  const creditInfo = await API.getCreditInfo(orderData.customerCd);
                  this.updateCreditInfo(creditInfo);
              }

              console.log('✅ 주문 선택 완료:', orderData.orderNo);
          } catch (error) {
              console.error('❌ 주문 데이터 로드 실패:', error);
          }
      },

      // 여신 정보 업데이트
      updateCreditInfo(creditData) {
          const data = creditData ? [creditData] : [{}];
          if (gridApis.credit) {
              gridApis.credit.setGridOption('rowData', data);
          }

          // 상태 배지 업데이트
          const badge = document.getElementById('credit-status-badge');
          if (creditData) {
              const status = creditData.creditStatus || '정상';
              const holdFlg = creditData.creditHoldFlg === 'Y';
              const stopFlg = creditData.tradeStopFlg === 'Y';

              if (stopFlg) {
                  badge.className = 'badge bg-danger';
                  badge.textContent = '거래정지';
              } else if (holdFlg) {
                  badge.className = 'badge bg-warning';
                  badge.textContent = '여신보류';
              } else {
                  badge.className = 'badge bg-success';
                  badge.textContent = status;
              }
          } else {
              badge.className = 'badge bg-secondary';
              badge.textContent = '거래처를 선택하세요';
          }
      },

      // 행 총액 계산
      calculateRowTotal(rowNode) {
          const data = rowNode.data;
          const qty = Number(data.qty) || 0;
          const unitPrice = Number(data.unitPrice) || 0;
          const supplyAmount = qty * unitPrice;
          const taxAmount = Math.round(supplyAmount * 0.1);
          const totalAmount = supplyAmount + taxAmount;

          rowNode.setDataValue('supplyAmount', supplyAmount);
          rowNode.setDataValue('taxAmount', taxAmount);
          rowNode.setDataValue('totalAmount', totalAmount);
      },

      // 총합계 업데이트
      updateTotalSummary() {
          if (!gridApis.detail) return;

          let totalSupply = 0;
          let totalTax = 0;
          let totalAmount = 0;

          gridApis.detail.forEachNode(node => {
              totalSupply += Number(node.data.supplyAmount) || 0;
              totalTax += Number(node.data.taxAmount) || 0;
              totalAmount += Number(node.data.totalAmount) || 0;
          });

          const summaryData = [{
              itemCode: '합계',
              supplyAmount: totalSupply,
              taxAmount: totalTax,
              totalAmount: totalAmount
          }];

          gridApis.detail.setGridOption('pinnedBottomRowData', summaryData);
      },

      // 거래처 선택 모달 열기
      openCustomerModal() {
          modals.customer.show();
          this.loadCustomerList();
      },

      // 거래처 목록 로드
      async loadCustomerList(name = '') {
          try {
              const customers = await API.getCustomers(name);
              this.renderCustomerTable(customers);
          } catch (error) {
              console.error('거래처 목록 로드 실패:', error);
          }
      },

      // 거래처 테이블 렌더링
      renderCustomerTable(customers) {
          const tbody = document.getElementById('customerSearchResults');
          tbody.innerHTML = '';

          if (!customers || customers.length === 0) {
              tbody.innerHTML = '<tr><td colspan="5" class="text-center">검색 결과가 없습니다.</td></tr>';
              return;
          }

          customers.forEach(customer => {
              const row = document.createElement('tr');
              row.innerHTML = `
                  <td><button class="btn btn-sm btn-primary btn-select-customer" data-customer='${JSON.stringify(customer)}'>선택</button></td>
                  <td>${customer.customerCd}</td>
                  <td>${customer.customerNm}</td>
                  <td>${customer.representativeNm || ''}</td>
                  <td>${customer.phoneNo || ''}</td>
              `;
              tbody.appendChild(row);
          });
      },

      // 품목 선택 모달 열기
      openItemModal(rowNode) {
          currentState.selectedDetailRow = rowNode;
          document.getElementById('itemSearchInput').value = '';
          modals.item.show();
          this.loadItemList();
          setTimeout(() => {
              document.getElementById('itemSearchInput').focus();
          }, 500);
      },

      // 품목 목록 로드
      async loadItemList(itemName = '') {
          try {
              const items = await API.getItems(itemName);
              this.renderItemTable(items);
          } catch (error) {
              console.error('품목 목록 로드 실패:', error);
          }
      },

      // 품목 테이블 렌더링
      renderItemTable(items) {
          const tbody = document.getElementById('itemSearchResults');
          tbody.innerHTML = '';

          if (!items || items.length === 0) {
              tbody.innerHTML = '<tr><td colspan="6" class="text-center">검색 결과가 없습니다.</td></tr>';
              return;
          }

          items.forEach(item => {
              const row = document.createElement('tr');
              const price = item.salePrice ? Number(item.salePrice).toLocaleString() + '원' : '0원';
              row.innerHTML = `
                  <td><button class="btn btn-sm btn-primary btn-select-item" data-item='${JSON.stringify(item)}'>선택</button></td>
                  <td>${item.itemCode}</td>
                  <td>${item.itemName}</td>
                  <td>${item.spec || ''}</td>
                  <td>${item.unit || ''}</td>
                  <td>${price}</td>
              `;
              tbody.appendChild(row);
          });
      }
  };

  // =================== 주문서 관리자 ===================
  const OrderManager = {
      // 편집 모드 설정
      setEditMode(isEdit) {
          currentState.editMode = isEdit;

          // 버튼 상태 변경
          document.getElementById('btnSave').disabled = !isEdit;
          document.getElementById('btnAddRow').disabled = !isEdit;
          document.getElementById('btnDeleteRow').disabled = !isEdit;
          document.getElementById('btnQuotationSearch').disabled = !isEdit;

          document.getElementById('btnNew').disabled = isEdit;
          document.getElementById('btnEdit').disabled = isEdit;
          document.getElementById('btnDelete').disabled = isEdit;

          // 취소 버튼 표시/숨김
          const cancelBtn = document.getElementById('btnCancel');
          if (cancelBtn) {
              cancelBtn.style.display = (isEdit && currentState.isNewOrder) ? 'inline-block' : 'none';
          }

          // 헤더 그리드 편집 모드 설정
          if (gridApis.header) {
              const headerColumnDefs = gridApis.header.getColumnDefs();
              headerColumnDefs.forEach(col => {
                  if (col.field === 'orderDt' ||
                      col.field === 'salesEmpCd' ||
                      col.field === 'discountRate' ||
                      col.field === 'paymentTerms' ||
                      col.field === 'orderWriter') {
                      col.editable = isEdit;
                  }
              });
              gridApis.header.setGridOption('columnDefs', headerColumnDefs);
          }

          // 디테일 그리드 편집 모드 설정
          if (gridApis.detail) {
              const detailColumnDefs = gridApis.detail.getColumnDefs();
              detailColumnDefs.forEach(col => {
                  if (col.field === 'qty' || 
                      col.field === 'unitPrice' || 
                      col.field === 'outboundDt' || 
                      col.field === 'catchDt' ||
                      col.field === 'outState' ||
                      col.field === 'remarks') {
                      col.editable = isEdit;
                  }
              });
              gridApis.detail.setGridOption('columnDefs', detailColumnDefs);
          }

          console.log('편집 모드:', isEdit ? '활성화' : '비활성화');
      },

      // ★★★ 완전히 새로운 신규 주문서 생성 방식 ★★★
      async createNewOrder() {
          if (currentState.editMode) {
              Utils.showAlert.warning('편집 중', '이미 새로운 주문서를 작성하고 있습니다.');
              return;
          }

          try {
              Utils.showAlert.loading('새 주문번호 생성 중...');

              const nextOrderNo = await API.getNextOrderNo();
              const today = dayjs().format('YYYY-MM-DD');

              const newOrder = {
                  orderNo: nextOrderNo,
                  orderDt: today,
                  customerCd: '',
                  customerNm: '거래처를 선택하세요',
                  representativeNm: '',
                  phoneNo: '',
                  salesEmpCd: '',
                  discountRate: 0,
                  paymentTerms: 'Net 30',
                  orderWriter: '',
                  isNew: true,
                  _isNewOrder: true
              };

              console.log('🆕 신규 주문서 데이터:', newOrder);

              // ★★★ 새로운 방식: rowData 재구성으로 신규 행 추가 ★★★
              GridManager.addNewOrderRow(newOrder);

              // 디테일 그리드 초기화
              const newDetails = [
                  { 
                      lineNo: 1, 
                      itemCode: '', 
                      itemName: '품목을 선택하세요',
                      spec: '',
                      qty: 1, 
                      unitPrice: 0,
                      supplyAmount: 0,
                      taxAmount: 0,
                      totalAmount: 0,
                      outState: '대기',
                      remarks: ''
                  }
              ];
              
              if (gridApis.detail) {
                  gridApis.detail.setGridOption('rowData', newDetails);
                  GridManager.updateTotalSummary();
              }

              // 여신 정보 초기화
              GridManager.updateCreditInfo(null);

              // 상태 설정
              currentState.currentOrder = newOrder;
              currentState.isNewOrder = true;
              currentState.selectedHeaderRow = newOrder;
              this.setEditMode(true);

              Swal.close();
              
              await Swal.fire({
                  title: '신규 주문서 생성 완료! 🎉',
                  html: `
                      <div class="text-start">
                          <p><strong>주문번호:</strong> ${nextOrderNo}</p>
                          <hr>
                          <p><strong>📋 입력 순서:</strong></p>
                          <ol class="text-start">
                              <li><span class="text-primary">거래처명</span> 클릭 → 거래처 선택</li>
                              <li><span class="text-success">담당자</span> 직접 입력</li>
                              <li><span class="text-warning">품목코드</span> 클릭 → 품목 선택</li>
                              <li><span class="text-info">수량, 단가</span> 입력</li>
                              <li><span class="text-danger">등록</span> 버튼 클릭</li>
                          </ol>
                          <p class="text-success"><strong>✅ 기존 주문서들은 그대로 유지됩니다!</strong></p>
                      </div>
                  `,
                  icon: 'info',
                  confirmButtonText: '시작하기',
                  width: '500px'
              });

              console.log('✅ 새 주문서 생성 완료:', nextOrderNo);

          } catch (error) {
              Swal.close();
              Utils.showAlert.error('오류', '새 주문번호 생성에 실패했습니다.');
              console.error('❌ 새 주문서 생성 실패:', error);
          }
      },

      // 주문서 저장
      async saveOrder() {
          if (!currentState.editMode || !currentState.currentOrder) {
              Utils.showAlert.info('알림', '먼저 새 주문서를 생성해주세요.');
              return;
          }

          // 헤더 데이터 가져오기
          const headerNode = gridApis.header.getRowNode(currentState.currentOrder.orderNo);
          if (!headerNode) {
              Utils.showAlert.error('오류', '주문서 데이터를 찾을 수 없습니다.');
              return;
          }

          const headerData = headerNode.data;

          // 유효성 검증
          if (!headerData.customerCd) {
              Utils.showAlert.warning('입력 확인', '거래처를 선택해주세요.');
              return;
          }

          if (!headerData.salesEmpCd || headerData.salesEmpCd.trim() === '') {
              Utils.showAlert.warning('입력 확인', '담당자를 입력해주세요.');
              return;
          }

          // 디테일 데이터 수집
          const details = [];
          gridApis.detail.forEachNode(node => {
              if (node.data.itemCode && node.data.itemCode.trim() !== '') {
                  details.push({
                      ...node.data,
                      lineNo: details.length + 1
                  });
              }
          });

          if (details.length === 0) {
              Utils.showAlert.warning('입력 확인', '최소 하나의 품목을 추가해주세요.');
              return;
          }

          // 저장 확인
          const result = await Utils.showAlert.confirm(
              '주문서 저장',
              `주문번호 ${headerData.orderNo}를 저장하시겠습니까?`
          );

          if (!result.isConfirmed) return;

          try {
              Utils.showAlert.loading('저장 중...');

              const orderData = {
                  ...headerData,
                  details: details
              };

              const response = await API.saveOrder(orderData);

              if (response.success) {
                  Swal.close();
                  
                  await Swal.fire({
                      title: '저장 완료! 🎉',
                      html: `
                          <div class="text-center">
                              <p><strong>주문번호:</strong> ${response.orderNo || headerData.orderNo}</p>
                              <p><strong>거래처:</strong> ${headerData.customerNm}</p>
                              <p><strong>담당자:</strong> ${headerData.salesEmpCd}</p>
                              <p><strong>품목 수:</strong> ${details.length}개</p>
                              <hr>
                              <p class="text-success">기존 주문서들과 함께 저장되었습니다!</p>
                          </div>
                      `,
                      icon: 'success',
                      timer: 3000,
                      confirmButtonText: '확인'
                  });

                  // 상태 초기화
                  this.setEditMode(false);
                  currentState.currentOrder = null;
                  currentState.isNewOrder = false;

                  // ★★★ 전체 데이터 새로고침으로 최신 상태 반영 ★★★
                  await GridManager.loadHeaderData();

                  // 저장된 주문서 선택
                  setTimeout(() => {
                      const savedOrderNo = response.orderNo || headerData.orderNo;
                      const savedNode = gridApis.header.getRowNode(savedOrderNo);
                      if (savedNode) {
                          savedNode.setSelected(true);
                          gridApis.header.ensureNodeVisible(savedNode);
                          GridManager.selectOrder(savedNode.data);
                      }
                  }, 500);

                  console.log('✅ 주문서 저장 완료:', response.orderNo || headerData.orderNo);

              } else {
                  Swal.close();
                  Utils.showAlert.error('저장 실패', response.message || '저장 중 오류가 발생했습니다.');
              }

          } catch (error) {
              Swal.close();
              Utils.showAlert.error('저장 실패', '저장 중 오류가 발생했습니다.');
              console.error('❌ 주문서 저장 실패:', error);
          }
      },

      // 주문서 삭제
      async deleteOrder() {
          const selectedRows = gridApis.header.getSelectedRows();
          if (selectedRows.length === 0) {
              Utils.showAlert.info('알림', '삭제할 주문서를 선택해주세요.');
              return;
          }

          const result = await Utils.showAlert.confirm(
              '주문서 삭제',
              `선택한 ${selectedRows.length}건의 주문서를 삭제하시겠습니까?`
          );

          if (!result.isConfirmed) return;

          try {
              Utils.showAlert.loading('삭제 중...');

              for (const order of selectedRows) {
                  await API.deleteOrder(order.orderNo);
              }

              Swal.close();
              Utils.showAlert.success('성공', '선택한 주문서가 삭제되었습니다.');

              await GridManager.loadHeaderData();

              if (gridApis.detail) {
                  gridApis.detail.setGridOption('rowData', []);
              }
              GridManager.updateCreditInfo(null);

              console.log('✅ 주문서 삭제 완료:', selectedRows.length, '건');

          } catch (error) {
              Swal.close();
              Utils.showAlert.error('삭제 실패', '삭제 중 오류가 발생했습니다.');
              console.error('❌ 주문서 삭제 실패:', error);
          }
      },

      // 디테일 행 추가
      addDetailRow() {
          if (!currentState.editMode) return;

          const newRowCount = gridApis.detail.getDisplayedRowCount() + 1;
          const newRow = {
              lineNo: newRowCount,
              qty: 1,
              outState: '대기'
          };

          gridApis.detail.applyTransaction({ add: [newRow] });
          console.log('✅ 새 디테일 행 추가');
      },

      // 디테일 행 삭제
      deleteDetailRow() {
          if (!currentState.editMode) return;

          const selectedRows = gridApis.detail.getSelectedRows();
          if (selectedRows.length === 0) {
              Utils.showAlert.info('알림', '삭제할 항목을 선택해주세요.');
              return;
          }

          gridApis.detail.applyTransaction({ remove: selectedRows });
          GridManager.updateTotalSummary();
          console.log('✅ 디테일 행 삭제:', selectedRows.length, '건');
      },

      // ★★★ 신규 작성 취소 기능 (완전 복구) ★★★
      async cancelNewOrder() {
          if (!currentState.editMode || !currentState.isNewOrder) {
              return;
          }

          const result = await Utils.showAlert.confirm(
              '작업 취소',
              '신규 주문서 작성을 취소하시겠습니까?\n입력한 모든 내용이 사라집니다.'
          );

          if (!result.isConfirmed) return;

          try {
              // 상태 초기화
              this.setEditMode(false);
              currentState.currentOrder = null;
              currentState.isNewOrder = false;
              currentState.selectedHeaderRow = null;

              // ★★★ 신규 행 제거하고 원본 데이터로 복구 ★★★
              GridManager.removeNewOrderRow();

              // 디테일 그리드 초기화
              if (gridApis.detail) {
                  gridApis.detail.setGridOption('rowData', []);
              }

              // 여신 정보 초기화
              GridManager.updateCreditInfo(null);

              Utils.showAlert.info('취소됨', '신규 주문서 작성이 취소되었습니다.');
              console.log('✅ 신규 주문서 작성 취소 - 기존 데이터 완전 복구');

          } catch (error) {
              console.error('❌ 취소 처리 실패:', error);
              Utils.showAlert.error('오류', '취소 처리 중 오류가 발생했습니다.');
          }
      }
  };

  // =================== 견적서 관리자 ===================
  const QuotationManager = {
      // 견적서 검색 모달 열기
      openSearchModal() {
          if (!currentState.editMode) {
              Utils.showAlert.warning('알림', '먼저 신규 주문서를 생성해주세요.');
              return;
          }

          modals.quotation.show();
          this.searchQuotations();
      },

      // 견적서 검색
      async searchQuotations() {
          const filter = {
              quotationNo: document.getElementById('quotationNoFilter').value.trim(),
              customerName: document.getElementById('customerNameFilter').value.trim(),
              dateFrom: document.getElementById('quotationDateFrom').value,
              dateTo: document.getElementById('quotationDateTo').value
          };

          try {
              const quotations = await API.getQuotations(filter);
              this.renderQuotationTable(quotations);
              console.log('✅ 견적서 검색 완료:', quotations?.length || 0, '건');
          } catch (error) {
              console.error('❌ 견적서 검색 실패:', error);
          }
      },

      // 견적서 테이블 렌더링
      renderQuotationTable(quotations) {
          const tbody = document.getElementById('quotationSearchResults');
          tbody.innerHTML = '';

          if (!quotations || quotations.length === 0) {
              tbody.innerHTML = '<tr><td colspan="9" class="text-center">검색 결과가 없습니다.</td></tr>';
              return;
          }

          quotations.forEach(quotation => {
              const row = document.createElement('tr');
              row.innerHTML = `
                  <td><button class="btn btn-sm btn-success btn-select-quotation" data-quotation='${JSON.stringify(quotation)}'>선택</button></td>
                  <td>${quotation.quotationNo}</td>
                  <td>${Utils.formatDate(quotation.quotationDt)}</td>
                  <td>${quotation.customerName}</td>
                  <td>${quotation.representativeNm || ''}</td>
                  <td>${quotation.phone || ''}</td>
                  <td>${Utils.formatDate(quotation.validPeriod)}</td>
                  <td>${Utils.formatDate(quotation.expectedDeliveryDt)}</td>
                  <td>${quotation.remarks || ''}</td>
              `;
              tbody.appendChild(row);
          });
      },

      // ★★★ 견적서 선택 (헤더 업데이트 방식 개선) ★★★
      async selectQuotation(quotationData) {
          try {
              Utils.showAlert.loading('견적서 변환 중...');

              // 견적서 상세 데이터 가져오기
              const details = await API.getQuotationDetails(quotationData.quotationNo);

              // 현재 신규 주문서 데이터 업데이트
              const updatedHeaderData = {
                  ...currentState.currentOrder,
                  customerCd: quotationData.customerCd,
                  customerNm: quotationData.customerName,
                  representativeNm: quotationData.representativeNm,
                  phoneNo: quotationData.phone,
                  salesEmpCd: quotationData.salesEmpCd || quotationData.sender,
                  discountRate: quotationData.discountRate,
                  orderWriter: quotationData.sender
              };

              // ★★★ 새로운 방식: rowData 재구성으로 헤더 업데이트 ★★★
              GridManager.updateNewOrderRow(currentState.currentOrder.orderNo, updatedHeaderData);
              
              currentState.currentOrder = updatedHeaderData;

              // 디테일 데이터 설정
              const orderDetails = details.map((detail, index) => ({
                  lineNo: index + 1,
                  itemCode: detail.itemCode,
                  itemName: detail.itemName,
                  spec: detail.spec,
                  qty: detail.qty,
                  unitPrice: detail.unitPrice,
                  supplyAmount: detail.supplyAmount,
                  taxAmount: detail.taxAmount,
                  totalAmount: detail.totalMoney,
                  remarks: detail.remarks,
                  outState: '대기'
              }));

              gridApis.detail.setGridOption('rowData', orderDetails);
              GridManager.updateTotalSummary();

              // 여신 정보 로드
              if (quotationData.customerCd) {
                  const creditInfo = await API.getCreditInfo(quotationData.customerCd);
                  GridManager.updateCreditInfo(creditInfo);
              }

              modals.quotation.hide();

              Swal.close();
              
              await Swal.fire({
                  title: '견적서 변환 완료! 🎉',
                  html: `
                      <div class="text-center">
                          <p><strong>견적서:</strong> ${quotationData.quotationNo}</p>
                          <p><strong>→ 주문서:</strong> ${currentState.currentOrder.orderNo}</p>
                          <hr>
                          <p><strong>거래처:</strong> ${quotationData.customerName}</p>
                          <p><strong>품목 수:</strong> ${orderDetails.length}개</p>
                          <p class="text-success">이제 등록 버튼을 눌러 저장하세요!</p>
                      </div>
                  `,
                  icon: 'success',
                  timer: 3000,
                  confirmButtonText: '확인'
              });

              console.log('✅ 견적서 변환 완료:', quotationData.quotationNo);

          } catch (error) {
              Swal.close();
              Utils.showAlert.error('변환 실패', '견적서 변환 중 오류가 발생했습니다.');
              console.error('❌ 견적서 변환 실패:', error);
          }
      }
  };

  // =================== 이벤트 리스너 ===================
  const EventHandlers = {
      init() {
          // 버튼 이벤트
          document.getElementById('btnNew').addEventListener('click', () => OrderManager.createNewOrder());
          document.getElementById('btnSave').addEventListener('click', () => OrderManager.saveOrder());
          document.getElementById('btnDelete').addEventListener('click', () => OrderManager.deleteOrder());
          document.getElementById('btnAddRow').addEventListener('click', () => OrderManager.addDetailRow());
          document.getElementById('btnDeleteRow').addEventListener('click', () => OrderManager.deleteDetailRow());
          document.getElementById('btnQuotationSearch').addEventListener('click', () => QuotationManager.openSearchModal());
          
          // 취소 버튼 이벤트
          document.getElementById('btnCancel')?.addEventListener('click', () => OrderManager.cancelNewOrder());

          // 견적서 관련 이벤트
          document.getElementById('btnQuotationFilter').addEventListener('click', () => QuotationManager.searchQuotations());
          document.getElementById('btnQuotationFilterReset').addEventListener('click', () => {
              document.getElementById('quotationNoFilter').value = '';
              document.getElementById('customerNameFilter').value = '';
              document.getElementById('quotationDateFrom').value = '';
              document.getElementById('quotationDateTo').value = '';
              QuotationManager.searchQuotations();
          });

          // 견적서 선택 이벤트
          document.getElementById('quotationSearchResults').addEventListener('click', (e) => {
              if (e.target.classList.contains('btn-select-quotation')) {
                  const quotationData = JSON.parse(e.target.dataset.quotation);
                  QuotationManager.selectQuotation(quotationData);
              }
          });

          // 거래처 선택 이벤트
          document.getElementById('customerSearchResults').addEventListener('click', (e) => {
              if (e.target.classList.contains('btn-select-customer')) {
                  const customerData = JSON.parse(e.target.dataset.customer);
                  this.selectCustomer(customerData);
              }
          });

          // 거래처 검색 이벤트
          document.getElementById('btnCustomerSearch').addEventListener('click', () => {
              const customerName = document.getElementById('customerSearchInput').value.trim();
              GridManager.loadCustomerList(customerName);
          });

          document.getElementById('customerSearchInput').addEventListener('keypress', (e) => {
              if (e.key === 'Enter') {
                  e.preventDefault();
                  const customerName = document.getElementById('customerSearchInput').value.trim();
                  GridManager.loadCustomerList(customerName);
              }
          });

          // 품목 선택 이벤트
          document.getElementById('itemSearchResults').addEventListener('click', (e) => {
              if (e.target.classList.contains('btn-select-item')) {
                  const itemData = JSON.parse(e.target.dataset.item);
                  this.selectItem(itemData);
              }
          });

          // 품목 검색 이벤트
          document.getElementById('btnItemSearch').addEventListener('click', () => {
              const itemName = document.getElementById('itemSearchInput').value.trim();
              GridManager.loadItemList(itemName);
          });

          document.getElementById('itemSearchInput').addEventListener('keypress', (e) => {
              if (e.key === 'Enter') {
                  e.preventDefault();
                  const itemName = document.getElementById('itemSearchInput').value.trim();
                  GridManager.loadItemList(itemName);
              }
          });

          // 디버그 버튼
          document.getElementById('btnDebug').addEventListener('click', () => {
              console.log('=== 디버그 정보 ===');
              console.log('현재 상태:', currentState);
              console.log('그리드 API:', gridApis);
              console.log('원본 데이터:', currentState.originalData?.length || 0, '건');
              console.log('헤더 데이터 개수:', gridApis.header?.getDisplayedRowCount() || 0);
              console.log('디테일 데이터 개수:', gridApis.detail?.getDisplayedRowCount() || 0);
              
              if (currentState.currentOrder && gridApis.header) {
                  const headerNode = gridApis.header.getRowNode(currentState.currentOrder.orderNo);
                  if (headerNode) {
                      console.log('현재 헤더 데이터:', headerNode.data);
                  }
              }
          });

          console.log('✅ 이벤트 리스너 초기화 완료');
      },

      // 거래처 선택 메서드
      selectCustomer(customerData) {
          if (!currentState.currentOrder || !gridApis.header) return;

          const updatedData = {
              ...currentState.currentOrder,
              customerCd: customerData.customerCd,
              customerNm: customerData.customerNm,
              representativeNm: customerData.representativeNm,
              phoneNo: customerData.phoneNo,
              salesEmpCd: customerData.salesEmpCd || currentState.currentOrder.salesEmpCd,
              discountRate: customerData.discountRate
          };

          // ★★★ rowData 재구성 방식으로 업데이트 ★★★
          GridManager.updateNewOrderRow(currentState.currentOrder.orderNo, updatedData);
          currentState.currentOrder = updatedData;

          GridManager.updateCreditInfo(null);
          if (customerData.customerCd) {
              API.getCreditInfo(customerData.customerCd)
                  .then(creditInfo => GridManager.updateCreditInfo(creditInfo))
                  .catch(error => console.error('여신 정보 로드 실패:', error));
          }

          modals.customer.hide();
          console.log('✅ 거래처 선택 완료:', customerData.customerCd);
      },

      // 품목 선택
      selectItem(itemData) {
          if (!currentState.selectedDetailRow) return;

          const rowNode = currentState.selectedDetailRow;
          const currentQty = rowNode.data.qty || 1;
          
          const updatedData = {
              ...rowNode.data,
              itemCode: itemData.itemCode,
              itemName: itemData.itemName,
              spec: itemData.spec || '',
              unitPrice: itemData.salePrice || 0,
              qty: currentQty
          };

          rowNode.setData(updatedData);
          GridManager.calculateRowTotal(rowNode);
          GridManager.updateTotalSummary();

          modals.item.hide();
          console.log('✅ 품목 선택 완료:', itemData.itemCode);
      }
  };

  // =================== 초기화 ===================
  function initialize() {
      console.log('🚀 주문서 관리 시스템 초기화 중...');

      GridManager.initHeaderGrid();
      GridManager.initDetailGrid();
      GridManager.initCreditGrid();

      EventHandlers.init();

      OrderManager.setEditMode(false);

      console.log('✅ 주문서 관리 시스템 초기화 완료');
      console.log('💡 rowData 재구성 방식으로 기존 데이터 보존이 확실해졌습니다!');
  }

  initialize();

  // 전역 함수로 노출 (디버깅용)
  window.OrderManagement = {
      state: currentState,
      gridApis: gridApis,
      utils: Utils,
      orderManager: OrderManager,
      gridManager: GridManager
  };
});