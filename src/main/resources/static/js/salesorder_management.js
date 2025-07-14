/**
 * ===================================================================
 * salesorder_management.js - Refactored Version
 *
 * Description: Manages the sales order page, including creating,
 * reading, updating, and deleting orders.
 * Handles AG-Grid interactions, modal dialogs, and
 * server communication.
 * Author: Gemini
 * Last Modified: 2025-07-15
 * ===================================================================
 */
$(function () {
  /**
   * Main application module for Sales Order Management.
   * Encapsulates all state, UI logic, and event handling.
   */
  const OrderManagementApp = (function () {

      // -----------------------------------------------------------------
      // 1. State Management
      // -----------------------------------------------------------------
      const state = {
          gridApis: {
              header: null,
              detail: null,
              credit: null,
          },
          editMode: false,
          currentOrder: null,
          selectedHeaderNode: null,
          selectedDetailNode: null,
      };

      // -----------------------------------------------------------------
      // 2. AG-Grid Column Definitions
      // -----------------------------------------------------------------
      const columnDefs = {
          // =================================================================
          // ★★★★★★★★★★★★★★★★★★★★★ FIX START ★★★★★★★★★★★★★★★★★★★★★
          // =================================================================
          // Removed deprecated checkboxSelection and headerCheckboxSelection from column definitions.
          // These are now handled in the gridOptions.
          header: [
              { headerName:'주문번호', field:'orderNo', pinned:'left', width:150, cellClass:'font-weight-bold text-primary', editable:false },
              { headerName:'주문일자', field:'orderDt', minWidth:120, cellEditor: 'datePicker', valueFormatter: p => formatDate(p.value) },
              { headerName:'거래처', field:'customerNm', minWidth:180, cellRenderer: 'agAnimateShowChangeCellRenderer' },
              { headerName:'대표', field:'representativeNm', minWidth:110, editable:false },
              { headerName:'연락처', field:'phoneNo', minWidth:130, editable:false },
              { headerName:'담당자', field:'salesEmpCd', minWidth:100, editable:false },
              { headerName:'할인율(%)', field:'discountRate', width: 90, valueFormatter:p=> (p.value??0)+'%', editable:false },
              { headerName:'결제조건', field:'paymentTerms', minWidth:100, cellEditor:'agTextCellEditor' },
              { headerName:'작성자', field:'orderWriter', minWidth: 90, cellEditor:'agTextCellEditor' }
          ],
          credit: [
              { headerName:'여신한도', field:'creditLimit', flex:1, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value) },
              { headerName:'여신잔액', field:'remainingCredit', flex:1, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value) },
              { headerName:'여신사용액', field:'creditUsed', flex:1, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value) },
              { headerName:'여신상태', field:'creditStatus', flex:1 },
              { headerName:'보류여부', field:'creditHoldFlg', flex:1, cellRenderer: p => formatYN(p.value) },
              { headerName:'거래정지', field:'tradeStopFlg', flex:1, cellRenderer: p => formatYN(p.value) }
          ],
          detail: [
              { headerName:'순번', field:'lineNo', width:70, cellClass:'text-center', editable:false },
              { headerName:'품목코드', field:'itemCode', width:130, editable:false },
              { headerName:'품목명', field:'itemName', width:200, editable:false, cellRenderer: 'agAnimateShowChangeCellRenderer' },
              { headerName:'규격', field:'spec', width:120, editable:false },
              { headerName:'수량', field:'qty', width:90, type:'numericColumn', valueFormatter:p=>formatNumber(p.value) },
              { headerName:'단가', field:'unitPrice', width:110, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value, '') },
              { headerName:'공급가액', field:'supplyAmount', width:120, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value, ''), editable:false },
              { headerName:'부가세', field:'taxAmount', width:110, type:'numericColumn', valueFormatter:p=>formatCurrency(p.value, ''), editable:false },
              { headerName:'합계', field:'totalAmount', width:120, type:'numericColumn', cellClass:'font-weight-bold', valueFormatter:p=>formatCurrency(p.value, ''), editable:false },
              { headerName:'출고일', field:'outboundDt', width:120, cellEditor:'datePicker', valueFormatter:p=> formatDate(p.value) },
              { headerName:'납기일', field:'catchDt', width:120, cellEditor:'datePicker', valueFormatter:p=> formatDate(p.value) },
              { headerName:'출고상태', field:'outState', width:100 },
              { headerName:'비고', field:'remarks', flex:1, minWidth: 150 }
          ]
          // =================================================================
          // ★★★★★★★★★★★★★★★★★★★★★★ FIX END ★★★★★★★★★★★★★★★★★★★★★★
          // =================================================================
      };

      // -----------------------------------------------------------------
      // 3. API Service Layer
      // -----------------------------------------------------------------
      const apiService = {
          fetch: async function(url, options = {}) {
              try {
                  const ajaxOptions = {
                      url: url,
                      method: options.method || 'GET',
                      contentType: 'application/json; charset=utf-8',
                      dataType: options.dataType || 'json',
                      timeout: 15000
                  };
                  if (options.body) ajaxOptions.data = JSON.stringify(options.body);
                  if (options.data && (options.method || 'GET') === 'GET') ajaxOptions.data = options.data;
                  return await $.ajax(ajaxOptions);
              } catch (xhr) {
                  console.error(`API Error: ${options.method || 'GET'} ${url}`, xhr);
                  const errorMsg = xhr.responseJSON?.message || xhr.statusText || 'Server communication error.';
                  showErrorAlert('API Error', errorMsg);
                  throw new Error(errorMsg);
              }
          },
          getOrders: () => apiService.fetch('/bsn/sorlist/data'),
          getOrderDetails: (orderNo) => apiService.fetch(`/bsn/orders/${orderNo}/details`),
          getCreditInfo: (customerCd) => apiService.fetch(`/bsn/customer/${customerCd}/credit`),
          getNextOrderNo: () => apiService.fetch('/bsn/orders/nextOrderNo', { dataType: 'text' }),
          getQuotations: (filter) => apiService.fetch('/bsn/quotation/search', { data: filter, method: 'GET' }),
          getQuotationDetails: (quotationNo) => apiService.fetch(`/bsn/quotation/details?quotationNo=${encodeURIComponent(quotationNo)}`),
          getCustomers: (name) => apiService.fetch('/bsn/customers', { data: { name: name } }),
          getItems: (name) => apiService.fetch('/bsn/item/list', { data: { itemName: name } }),
          saveOrder: (orderData) => apiService.fetch('/bsn/orders', { method: 'POST', body: orderData }),
          deleteOrders: (orderNos) => Promise.all(orderNos.map(orderNo => apiService.fetch(`/bsn/orders/${orderNo}`, { method: 'DELETE' })))
      };

      // -----------------------------------------------------------------
      // 4. UI Control
      // -----------------------------------------------------------------
      const ui = {
          elements: {
              btnNew: $('#btnNew'),
              btnEdit: $('#btnEdit'),
              btnDelete: $('#btnDelete'),
              btnSave: $('#btnSave'),
              btnQuotationSearch: $('#btnQuotationSearch'),
              btnAddRow: $('#btnAddRow'),
              btnDeleteRow: $('#btnDeleteRow'),
              quotationModal: new bootstrap.Modal(document.getElementById('quotationModal')),
              customerModal: new bootstrap.Modal(document.getElementById('customerModal')),
              itemModal: new bootstrap.Modal(document.getElementById('itemModal')),
          },
          setEditMode: function(isEditing) {
              state.editMode = isEditing;
              const isNotEditing = !isEditing;
              
              this.elements.btnSave.prop('disabled', isNotEditing);
              this.elements.btnAddRow.prop('disabled', isNotEditing);
              this.elements.btnDeleteRow.prop('disabled', isNotEditing);
              this.elements.btnQuotationSearch.prop('disabled', isNotEditing);
              
              this.elements.btnNew.prop('disabled', isEditing);
              this.elements.btnEdit.prop('disabled', isEditing);
              this.elements.btnDelete.prop('disabled', isEditing);

              const updateColumnEditable = (api, defs) => {
                  if (!api) return;
                  const newDefs = defs.map(col => ({ ...col, editable: isEditing && col.editable !== false && (!!col.cellEditor || col.field === 'qty' || col.field === 'unitPrice') }));
                  api.setGridOption('columnDefs', newDefs);
              };
              
              if(state.gridApis.header) updateColumnEditable(state.gridApis.header, columnDefs.header);
              if(state.gridApis.detail) updateColumnEditable(state.gridApis.detail, columnDefs.detail);
          },
          updateCreditInfo: function(creditData) {
              const data = creditData ? [{
                  creditLimit: creditData.creditLimit || 0,
                  remainingCredit: creditData.remainingCredit || 0,
                  creditUsed: creditData.creditUsed || 0,
                  creditStatus: creditData.creditStatus || 'N/A',
                  creditHoldFlg: creditData.creditHoldFlg || 'N',
                  tradeStopFlg: creditData.tradeStopFlg || 'N'
              }] : [{}];
              safeSetRowData(state.gridApis.credit, data);
          },
          calculateDetailTotals: function() {
              let supply = 0, tax = 0, total = 0;
              if(state.gridApis.detail) {
                  state.gridApis.detail.forEachNode(node => {
                      supply += Number(node.data.supplyAmount || 0);
                      tax += Number(node.data.taxAmount || 0);
                      total += Number(node.data.totalAmount || 0);
                  });
              }
              const totalsRow = [{ itemCode: '합계', supplyAmount: supply, taxAmount: tax, totalAmount: total }];
              safeSetPinnedBottomRow(state.gridApis.detail, totalsRow);
          },
          renderModalTable: function(tbodyId, data, rowRenderer) {
              const tbody = $(tbodyId).empty();
              if (!data || data.length === 0) {
                  tbody.append('<tr><td colspan="100%" class="text-center">No data available.</td></tr>');
                  return;
              }
              data.forEach(item => tbody.append(rowRenderer(item)));
          }
      };

      // -----------------------------------------------------------------
      // 5. Grid Manager
      // -----------------------------------------------------------------
      const gridManager = {
          init: function() {
              this.initHeaderGrid();
              this.initDetailGrid();
              this.initCreditGrid();
          },
          initHeaderGrid: function() {
              const gridOptions = {
                  components: { datePicker: DatePicker },
                  getRowId: params => params.data.orderNo,
                  columnDefs: [
                      { width: 50, checkboxSelection: true, headerCheckboxSelection: true },
                      ...columnDefs.header
                  ],
                  onGridReady: p => { 
                      state.gridApis.header = p.api; 
                      eventHandlers.loadInitialData();
                      ui.setEditMode(false);
                  },
                  onRowClicked: e => eventHandlers.onHeaderRowClicked(e),
                  onCellClicked: e => eventHandlers.onHeaderCellClicked(e),
                  onCellValueChanged: e => eventHandlers.onHeaderCellValueChanged(e),
                  defaultColDef: { resizable: true, sortable: true, filter: true, editable: false },
                  pagination: true,
                  paginationPageSize: 10,
                  paginationPageSizeSelector: [10, 20, 50, 100],
                  rowSelection: 'multiple',
              };
              agGrid.createGrid(document.getElementById('headerGrid'), gridOptions);
          },
          initDetailGrid: function() {
               const gridOptions = {
                  components: { datePicker: DatePicker },
                  columnDefs: [
                      { width: 50, checkboxSelection: true, headerCheckboxSelection: true },
                      ...columnDefs.detail
                  ],
                  onGridReady: p => { state.gridApis.detail = p.api; },
                  onCellValueChanged: e => eventHandlers.onDetailCellValueChanged(e),
                  onCellClicked: e => eventHandlers.onDetailCellClicked(e),
                  defaultColDef: { resizable: true, sortable: true, filter: true, editable: false },
                  rowSelection: 'multiple',
              };
              agGrid.createGrid(document.getElementById('detailGrid'), gridOptions);
          },
          initCreditGrid: function() {
              const gridOptions = {
                  columnDefs: columnDefs.credit,
                  onGridReady: p => { state.gridApis.credit = p.api; ui.updateCreditInfo(null); },
                  defaultColDef: { resizable: true },
                  domLayout: 'autoHeight',
              };
              agGrid.createGrid(document.getElementById('creditGrid'), gridOptions);
          }
      };

      // -----------------------------------------------------------------
      // 6. Event Handlers & Core Logic
      // -----------------------------------------------------------------
      const eventHandlers = {
          bindEvents: function() {
              ui.elements.btnNew.on('click', () => this.handleNewOrder());
              ui.elements.btnSave.on('click', () => this.handleSaveOrder());
              ui.elements.btnDelete.on('click', () => this.handleDeleteOrder());
              ui.elements.btnQuotationSearch.on('click', () => this.handleQuotationSearch());
              ui.elements.btnAddRow.on('click', () => this.handleAddDetailRow());
              ui.elements.btnDeleteRow.on('click', () => this.handleDeleteDetailRow());
              
              $('#btnQuotationFilter').on('click', () => this.handleQuotationFilter());
              $('#quotationSearchResults').on('click', '.btn-select-quotation', (e) => this.handleQuotationSelect(e));
              $('#btnCustomerSearch').on('click', () => this.handleCustomerSearch());
              $('#customerSearchResults').on('click', '.btn-select-cust', (e) => this.handleCustomerSelect(e));
              $('#btnItemSearch').on('click', () => this.handleItemSearch());
              $('#itemSearchResults').on('click', '.btn-select-item', (e) => this.handleItemSelect(e));
          },
          loadInitialData: async function() {
              try {
                  const orders = await apiService.getOrders();
                  safeSetRowData(state.gridApis.header, orders || []);
              } catch (error) { /* Handled */ }
          },
          handleNewOrder: async function() {
              if (state.editMode) {
                  showWarningAlert('작업 중', '이미 새로운 주문서를 작성하고 있습니다. 먼저 저장하거나 취소해주세요.');
                  return;
              }
              try {
                  const nextOrderNo = await apiService.getNextOrderNo();
                  const today = dayjs().format('YYYY-MM-DD');
                  
                  state.currentOrder = { orderNo: nextOrderNo, orderDt: today, paymentTerms: 'Net 30', isNew: true };
                  
                  state.gridApis.header.applyTransaction({ add: [state.currentOrder], addIndex: 0 });
                  state.gridApis.header.ensureIndexVisible(0);
                  
                  safeSetRowData(state.gridApis.detail, [{ lineNo: 1, qty: 1 }]);
                  ui.calculateDetailTotals();
                  ui.updateCreditInfo(null);
                  ui.setEditMode(true);
              } catch (error) {
                  showErrorAlert('Error', 'Could not generate a new order number.');
              }
          },
          handleSaveOrder: async function() {
              if (!state.editMode || !state.currentOrder) return showInfoAlert('Info', 'Please create a new order first.');

              state.gridApis.header.stopEditing();
              state.gridApis.detail.stopEditing();

              const orderData = { ...state.currentOrder };
              const details = [];
              state.gridApis.detail.forEachNode(node => {
                  if (node.data.itemCode) details.push(node.data);
              });

              if (!orderData.customerCd) return showWarningAlert('Validation', 'Please select a customer.');
              if (details.length === 0) return showWarningAlert('Validation', 'Please add at least one item.');
              
              orderData.details = details;
              
              showLoadingAlert('Saving...');
              try {
                  const response = await apiService.saveOrder(orderData);
                  if (response.success) {
                      showSuccessAlert('Success', `Order ${response.orderNo} has been saved.`);
                      ui.setEditMode(false);
                      state.currentOrder = null;
                      this.loadInitialData();
                      safeSetRowData(state.gridApis.detail, []);
                      ui.updateCreditInfo(null);
                  } else {
                      showErrorAlert('Save Failed', response.message);
                  }
              } catch (error) { /* Handled */ }
          },
          handleDeleteOrder: async function() {
              const selectedRows = state.gridApis.header.getSelectedRows();
              if (selectedRows.length === 0) return showInfoAlert('Info', 'Please select orders to delete.');
              
              const result = await showConfirmAlert('Delete Orders', `Delete ${selectedRows.length} order(s)?`);
              if (result.isConfirmed) {
                  showLoadingAlert('Deleting...');
                  const orderNos = selectedRows.map(row => row.orderNo);
                  try {
                      await apiService.deleteOrders(orderNos);
                      showSuccessAlert('Deleted', 'Selected orders have been deleted.');
                      this.loadInitialData();
                      safeSetRowData(state.gridApis.detail, []);
                      ui.updateCreditInfo(null);
                  } catch (error) { /* Handled */ }
              }
          },
          onHeaderCellClicked: function(e) {
              if (state.editMode && e.colDef.field === 'customerNm') {
                  this.openCustomerModal(e.node);
              }
          },
          onHeaderRowClicked: async function(e) {
              if (state.editMode && e.data.orderNo !== state.currentOrder.orderNo) {
                  return showWarningAlert('Edit Mode', 'Please save or cancel the current new order first.');
              }
              if (!state.editMode) {
                  state.currentOrder = e.data;
                  try {
                      const [details, creditInfo] = await Promise.all([
                          apiService.getOrderDetails(e.data.orderNo),
                          apiService.getCreditInfo(e.data.customerCd)
                      ]);
                      safeSetRowData(state.gridApis.detail, details || []);
                      ui.updateCreditInfo(creditInfo);
                      ui.calculateDetailTotals();
                  } catch (error) { /* Handled */ }
              }
          },
          onHeaderCellValueChanged: function(e) {
              if (!state.editMode) return;
              state.currentOrder = { ...state.currentOrder, ...e.data };
          },
          onDetailCellClicked: function(e) {
              if (state.editMode && e.colDef.field === 'itemName') {
                  this.openItemModal(e.node);
              }
          },
          onDetailCellValueChanged: function(params) {
              if (params.colDef.field === 'qty' || params.colDef.field === 'unitPrice') {
                  const rowNode = params.node;
                  const data = rowNode.data;
                  const qty = Number(data.qty) || 0;
                  const price = Number(data.unitPrice) || 0;
                  const supply = qty * price;
                  const tax = supply * 0.1;
                  
                  rowNode.setDataValue('supplyAmount', supply);
                  rowNode.setDataValue('taxAmount', tax);
                  rowNode.setDataValue('totalAmount', supply + tax);
              }
              ui.calculateDetailTotals();
          },
          handleAddDetailRow: function() {
              const newRow = { lineNo: state.gridApis.detail.getDisplayedRowCount() + 1, qty: 1 };
              state.gridApis.detail.applyTransaction({ add: [newRow] });
          },
          handleDeleteDetailRow: function() {
              const selectedRows = state.gridApis.detail.getSelectedRows();
              if (selectedRows.length === 0) return showInfoAlert('Info', 'Please select detail rows to delete.');
              state.gridApis.detail.applyTransaction({ remove: selectedRows });
              ui.calculateDetailTotals();
          },
          handleQuotationSearch: function() {
              ui.elements.quotationModal.show();
              this.handleQuotationFilter();
          },
          handleQuotationFilter: async function() {
              const filter = {
                  quotationNo: $('#quotationNoFilter').val().trim(),
                  customerName: $('#customerNameFilter').val().trim(),
                  dateFrom: $('#quotationDateFrom').val(),
                  dateTo: $('#quotationDateTo').val()
              };
              try {
                  const quotations = await apiService.getQuotations(filter);
                  ui.renderModalTable('#quotationSearchResults', quotations, (q) => `
                      <tr>
                          <td><button class="btn btn-sm btn-primary btn-select-quotation" data-quotation='${JSON.stringify(q)}'>선택</button></td>
                          <td>${q.quotationNo}</td><td>${formatDate(q.quotationDt)}</td><td>${q.customerName}</td>
                          <td>${q.representativeNm}</td><td>${q.phone}</td>
                      </tr>`);
              } catch (error) { /* Handled */ }
          },
          handleQuotationSelect: async function(e) {
              const quotation = JSON.parse(e.target.dataset.quotation);
              showLoadingAlert('Converting...');
              try {
                  const details = await apiService.getQuotationDetails(quotation.quotationNo);
                  
                  const rowNode = state.gridApis.header.getRowNode(state.currentOrder.orderNo);
                  if (!rowNode) throw new Error("Could not find the new order row in the grid.");

                  const updatedData = {
                      ...state.currentOrder,
                      customerCd: quotation.customerCd,
                      customerNm: quotation.customerName,
                      representativeNm: quotation.representativeNm,
                      phoneNo: quotation.phone,
                      salesEmpCd: quotation.salesEmpCd,
                      discountRate: quotation.discountRate,
                      orderWriter: quotation.sender
                  };
                  
                  state.currentOrder = updatedData;
                  rowNode.setData(updatedData);

                  const orderDetails = details.map((d, i) => ({
                      lineNo: i + 1, itemCode: d.itemCode, itemName: d.itemName, spec: d.spec,
                      qty: d.qty, unitPrice: d.unitPrice, supplyAmount: d.supplyAmount,
                      taxAmount: d.taxAmount, totalAmount: d.totalMoney, remarks: d.remarks, outState: '대기'
                  }));
                  safeSetRowData(state.gridApis.detail, orderDetails);
                  ui.calculateDetailTotals();

                  if(quotation.customerCd) {
                      const creditInfo = await apiService.getCreditInfo(quotation.customerCd);
                      ui.updateCreditInfo(creditInfo);
                  }

                  ui.elements.quotationModal.hide();
                  showSuccessAlert('Success', 'Quotation converted to order.');
              } catch (error) {
                  showErrorAlert('Conversion Failed', error.message);
              }
          },
          openCustomerModal: function(node) {
              state.selectedHeaderNode = node;
              ui.elements.customerModal.show();
              this.handleCustomerSearch();
          },
          handleCustomerSearch: async function() {
              const name = $('#customerSearchInput').val();
              try {
                  const customers = await apiService.getCustomers(name);
                  ui.renderModalTable('#customerSearchResults', customers, c => `
                      <tr>
                          <td><button class="btn btn-sm btn-primary btn-select-cust" data-cust='${JSON.stringify(c)}'>선택</button></td>
                          <td>${c.customerCd}</td><td>${c.customerNm}</td><td>${c.representativeNm}</td>
                      </tr>`);
              } catch (error) { /* Handled */ }
          },
          handleCustomerSelect: async function(e) {
              const customer = JSON.parse(e.target.dataset.cust);
              const node = state.selectedHeaderNode;
              
              const updatedData = { ...node.data, ...customer };
              node.setData(updatedData);
              state.currentOrder = updatedData;

              ui.elements.customerModal.hide();
              try {
                  const creditInfo = await apiService.getCreditInfo(customer.customerCd);
                  ui.updateCreditInfo(creditInfo);
              } catch (error) { /* Handled */ }
          },
          openItemModal: function(node) {
              state.selectedDetailNode = node;
              ui.elements.itemModal.show();
              this.handleItemSearch();
          },
          handleItemSearch: async function() {
              const name = $('#itemSearchInput').val();
              try {
                  const items = await apiService.getItems(name);
                  ui.renderModalTable('#itemSearchResults', items, it => `
                      <tr>
                          <td><button class="btn btn-sm btn-primary btn-select-item" data-item='${JSON.stringify(it)}'>선택</button></td>
                          <td>${it.itemCode}</td><td>${it.itemName}</td><td>${it.spec}</td><td>${it.salePrice}</td>
                      </tr>`);
              } catch (error) { /* Handled */ }
          },
          handleItemSelect: function(e) {
              const item = JSON.parse(e.target.dataset.item);
              const node = state.selectedDetailNode;
              
              const updatedData = {
                  ...node.data,
                  itemCode: item.itemCode,
                  itemName: item.itemName,
                  spec: item.spec,
                  unitPrice: item.salePrice,
                  qty: node.data.qty || 1,
              };
              node.setData(updatedData);
              
              this.onDetailCellValueChanged({ node: node, data: updatedData, colDef: { field: 'unitPrice' } });
              
              ui.elements.itemModal.hide();
          }
      };
      
      // -----------------------------------------------------------------
      // 7. Helper Functions
      // -----------------------------------------------------------------
      function DatePicker() {}
      DatePicker.prototype.init = function (params) { this.eInput = document.createElement('input'); this.eInput.value = params.value || ''; $(this.eInput).datepicker({ dateFormat: 'yy-mm-dd' }); };
      DatePicker.prototype.getGui = function () { return this.eInput; };
      DatePicker.prototype.afterGuiAttached = function () { this.eInput.focus(); };
      DatePicker.prototype.getValue = function () { return this.eInput.value; };
      DatePicker.prototype.destroy = function () {};

      function safeSetRowData(api, data) { if (api) api.setGridOption('rowData', data); }
      function safeSetPinnedBottomRow(api, data) { if (api) api.setGridOption('pinnedBottomRowData', data); }
      function formatDate(dateStr) { return dateStr ? dayjs(dateStr).format('YYYY-MM-DD') : ''; }
      function formatCurrency(value, unit = '원') { return (value ? Number(value).toLocaleString() : '0') + unit; }
      function formatNumber(value) { return value ? Number(value).toLocaleString() : '0'; }
      function formatYN(value) { return value === 'Y' ? 'Yes' : 'No'; }

      function showSuccessAlert(title, text) { Swal.fire({title, text, icon: 'success', timer: 2000, showConfirmButton: false}); }
      function showErrorAlert(title, text) { Swal.fire(title, text, 'error'); }
      function showWarningAlert(title, text) { Swal.fire(title, text, 'warning'); }
      function showInfoAlert(title, text) { Swal.fire(title, text, 'info'); }
      function showLoadingAlert(title) { Swal.fire({ title, allowOutsideClick: false, showConfirmButton: false, didOpen: () => Swal.showLoading() }); }
      function showConfirmAlert(title, text) { return Swal.fire({ title, text, icon: 'warning', showCancelButton: true, confirmButtonColor: '#d33', confirmButtonText: 'Yes, delete it!' }); }

      // -----------------------------------------------------------------
      // 8. Public Interface
      // -----------------------------------------------------------------
      return {
          init: function () {
              console.log("Initializing Order Management App...");
              gridManager.init();
              eventHandlers.bindEvents();
          }
      };

  })();

  // Initialize the application
  OrderManagementApp.init();
});
