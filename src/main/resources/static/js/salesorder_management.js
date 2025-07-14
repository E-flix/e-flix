$(function () {

  function DatePicker() {}

  DatePicker.prototype.init = function (params) {
    this.eInput = document.createElement('input');
    this.eInput.classList.add('ag-input');
    this.eInput.value = params.value || '';
  
    $(this.eInput).datepicker({
      dateFormat: 'yy-mm-dd',
      changeMonth: true,
      changeYear: true
    });
  
    // 최초 포커스 시 달력 바로 표시(선택 사항)
    setTimeout(() => $(this.eInput).datepicker('show'), 0);
  };

  DatePicker.prototype.getGui = function () {
    return this.eInput;
  };

  DatePicker.prototype.afterGuiAttached = function () {
    this.eInput.focus();
  };

  DatePicker.prototype.getValue = function () {
    return this.eInput.value;
  };

  DatePicker.prototype.destroy = function () {
    $(this.eInput).datepicker('destroy');
  };

  DatePicker.prototype.isPopup = function () {
    return false;
  };

  /*─────────────── 전역 변수 ───────────────*/
  let headerGridApi, detailGridApi, creditGridApi;
  let currentOrder = null;      // 선택(또는 신규) 헤더 레코드
  let editMode     = false;
  let selectedHeaderNode = null;
  let selectedDetailNode = null;
  let quotationList = [];       // 견적서 목록 데이터

  /*─────────────── 공통 유틸 ───────────────*/
  function safeSetRowData(api, rows){
    if (!api) return;
    if (api.setRowData)            api.setRowData(rows);
    else if (api.setGridOption)    api.setGridOption('rowData', rows);
  }
  function safeSetPinnedBottomRow(api, rows){
    if (!api) return;
    if (api.setPinnedBottomRowData) api.setPinnedBottomRowData(rows);
    else if (api.setGridOption)     api.setGridOption('pinnedBottomRowData', rows);
  }

  /*─────────────── 날짜 포맷 유틸리티 ───────────────*/
  function formatDate(dateString) {
    if (!dateString) return '';
    try {
      // 다양한 날짜 형식 지원
      let date;
      if (dateString instanceof Date) {
        date = dateString;
      } else if (typeof dateString === 'string') {
        date = new Date(dateString);
      } else {
        return '';
      }
      
      // 유효한 날짜인지 확인
      if (isNaN(date.getTime())) {
        return dateString; // 원본 반환
      }
      
      // YYYY-MM-DD 형식으로 반환
      return date.toISOString().split('T')[0];
    } catch (e) {
      console.warn('날짜 포맷 오류:', e, dateString);
      return dateString || '';
    }
  }

  /*──────────────── 1. 헤더 그리드 ────────────────*/
  const headerGridOptions = {
    components: { datePicker: DatePicker },
    // =================================================================
    // ★★★★★★★★★★★★★★★★★★★★★ FIX START ★★★★★★★★★★★★★★★★★★★★★
    // =================================================================
    // Add getRowNodeId to use 'orderNo' as the unique ID for each row.
    // This allows headerGridApi.getRowNode(orderNo) to work correctly.
    getRowNodeId: (data) => data.orderNo,
    // =================================================================
    // ★★★★★★★★★★★★★★★★★★★★★★ FIX END ★★★★★★★★★★★★★★★★★★★★★★
    // =================================================================
    columnDefs: [
      { headerName:'주문번호',   field:'orderNo',        pinned:'left', width:150,
        cellClass:'font-weight-bold text-primary', editable:false },
      { headerName:'주문일자',   field:'orderDt',        minWidth:110, flex:1,
        editable:true,  cellEditor:'datePicker' },
      { headerName:'거래처',     field:'customerNm',     minWidth:150, flex:1,
        editable:false },
      { headerName:'대표',       field:'representativeNm',minWidth:110, flex:1,
        editable:false },
      { headerName:'연락처',     field:'phoneNo',        minWidth:120, flex:1,
        editable:false },
      { headerName:'담당자',     field:'salesEmpCd',     minWidth:100, flex:1,
        editable:false },
      { headerName:'할인율(%)',  field:'discountRate',   minWidth: 90, flex:1,
        valueFormatter:p=> (p.value??0)+'%', editable:false },
      { headerName:'결제조건',   field:'paymentTerms',   minWidth:100, flex:1,
        editable:true },
      { headerName:'작성자',     field:'orderWriter',    minWidth: 90, flex:1,
        editable:true, cellEditor:'agTextCellEditor' }
    ],
    headerHeight: 35,
    rowHeight: 35,
    rowSelection: { mode:'multiRow', checkboxSelection:true, headerCheckboxSelection:true },
    onRowClicked:       handleHeaderRowClicked,
    onCellClicked:      onHeaderCellClicked,
    onCellValueChanged: headerCellValueChanged,
    pagination:         true,
    paginationPageSize: 10,
    defaultColDef:      { resizable:true, sortable:true, filter:true, editable:true },
    onGridReady:        p=>{ headerGridApi = p.api; loadHeaderData(); }
  };

  /*──────────────── 2. 여신 그리드 ────────────────*/
  const creditGridOptions = {
    columnDefs: [
      { headerName:'여신한도',     field:'creditLimit',     flex:1, minWidth:110,
        type:'numericColumn', valueFormatter:p=>numFmt(p.value) },
      { headerName:'여신잔액',     field:'remainingCredit', flex:1, minWidth:110,
        type:'numericColumn', valueFormatter:p=>numFmt(p.value) },
      { headerName:'여신사용액',   field:'creditUsed',      flex:1, minWidth:110,
        type:'numericColumn', valueFormatter:p=>numFmt(p.value) },
      { headerName:'여신상태',     field:'creditStatus',    flex:1, minWidth: 90 },
      { headerName:'보류여부',     field:'creditHoldFlg',   flex:1, minWidth: 80 },
      { headerName:'거래정지',     field:'tradeStopFlg',    flex:1, minWidth: 80 }
    ],
    headerHeight: 35,
    rowHeight:    35,
    defaultColDef:{ resizable:true },
    rowData:      [ getInitCreditObj() ],
    suppressRowClickSelection: true,
    suppressMovableColumns:     true,
    domLayout:   'autoHeight',
    onGridReady: p=> creditGridApi = p.api
  };

  /*──────────────── 3. 디테일 그리드 ────────────────*/
  const detailColDefs = [
    { checkboxSelection:true, headerCheckboxSelection:true, width:50 },
    { headerName:'순번',     field:'lineNo',       width:60,
      cellClass:'text-center', editable:false },
    { headerName:'품목코드', field:'itemCode',     width:120, editable:true },
    { headerName:'품목명',   field:'itemName',     width:180, editable:true },
    { headerName:'규격',     field:'spec',         width:120, editable:true },
    { headerName:'수량',     field:'qty',          width:90,  type:'numericColumn',
      valueFormatter:p=>Number(p.value||0).toLocaleString(), editable:true },
    { headerName:'단가',     field:'unitPrice',    width:110, type:'numericColumn',
      valueFormatter:p=>Number(p.value||0).toLocaleString(), editable:true },
    { headerName:'공급가액', field:'supplyAmount', width:120, type:'numericColumn',
      valueFormatter:p=>Number(p.value||0).toLocaleString(), editable:true },
    { headerName:'부가세',   field:'taxAmount',    width:100, type:'numericColumn',
      valueFormatter:p=>Number(p.value||0).toLocaleString(), editable:true },
    { headerName:'합계',     field:'totalAmount',  width:120, type:'numericColumn',
      cellClass:'font-weight-bold',
      valueFormatter:p=>Number(p.value||0).toLocaleString(), editable:false },
    { headerName:'출고일',   field:'outboundDt',   width:110,
      editable:true, cellEditor:'datePicker', 
      valueFormatter:p=> p.value ? dayjs(p.value).format('YYYY-MM-DD') : '' },
    { headerName:'납기일',   field:'catchDt',      width:110,
      editable:true, cellEditor:'datePicker',
      valueFormatter:p=> p.value ? dayjs(p.value).format('YYYY-MM-DD') : '' },
    { headerName:'출고상태', field:'outState',     width:100, editable:true },
    { headerName:'비고',     field:'remarks',      flex:1,   editable:true }
  ];

  const detailGridOptions = {
    components: { datePicker: DatePicker },
    columnDefs:         detailColDefs,
    defaultColDef:      { resizable:true, sortable:true, filter:true, editable:true },
    frameworkComponents: { datePicker: DatePicker },
    headerHeight:       35,
    rowHeight:          35,
    rowSelection:       'multiple',
    animateRows:        true,
    onGridReady:        p=>{ detailGridApi = p.api; setEditMode(false); },
    onCellClicked:      onDetailCellClicked,
    onCellValueChanged: ()=> setTimeout(calcTotals,80)
  };

  /*─────────────── 그리드 생성 ───────────────*/
  headerGridApi = agGrid.createGrid(document.getElementById('headerGrid'),headerGridOptions).api;
  detailGridApi = agGrid.createGrid(document.getElementById('detailGrid'), detailGridOptions).api;
  creditGridApi = agGrid.createGrid($('#creditGrid')[0],  creditGridOptions).api;

  /*──────────────── 4. 신규 버튼 ────────────────*/
  $('#btnNew').click(createNew);
  function createNew(){
    $.get('/bsn/orders/nextOrderNo').done(no=>{
      const today = dayjs().format('YYYY-MM-DD');
      const newHeader = {
        orderNo: no, orderDt: today,
        customerNm:'', paymentTerms:'Net 30'
      };
      headerGridApi.applyTransaction({ add:[newHeader], addIndex:0 });
      headerGridApi.ensureIndexVisible(0);
      headerGridApi.setFocusedCell(0,'customerNm');
      safeSetRowData(detailGridApi,[{ lineNo:1, qty:1 }]);
      calcTotals();
      safeSetRowData(creditGridApi, getInitCreditRows());
      currentOrder = newHeader;
      setEditMode(true);
      
      // ★ 신규 모드일 때 견적서 조회 버튼 활성화
      $('#btnQuotationSearch').prop('disabled', false);
    });
  }

  /*──────────────── 견적서 조회 버튼 클릭 ────────────────*/
  $('#btnQuotationSearch').click(function() {
    console.log('🔍 견적서 조회 버튼 클릭');
    console.log('editMode:', editMode);
    console.log('currentOrder:', currentOrder);
    
    if (!editMode) {
      Swal.fire('알림', '신규 모드에서만 견적서 조회가 가능합니다.', 'info');
      return;
    }
    
    // 모달 표시 전 로딩 상태 초기화
    $('#quotationSearchResults').html('<tr><td colspan="9" class="text-center"><i class="fas fa-spinner fa-spin"></i> 견적서를 불러오는 중...</td></tr>');
    
    $('#quotationModal').modal('show');
    
    // 모달이 완전히 열린 후 데이터 로드
    setTimeout(() => {
      loadQuotationList();
    }, 300);
  });

  /*──────────────── 견적서 목록 로드 ────────────────*/
  function loadQuotationList(filter = {}) {
    console.log('📋 견적서 목록 로드 시작:', filter);
    
    const $tbody = $('#quotationSearchResults');
    
    // 검색 필터 유무 확인
    const hasFilter = filter.quotationNo || filter.customerName || filter.dateFrom || filter.dateTo;
    const apiUrl = hasFilter ? '/bsn/quotation/search' : '/bsn/quotation/list';
    
    console.log('🌐 API 호출:', apiUrl, hasFilter ? filter : '전체 목록');
    
    // AJAX 요청 설정
    const ajaxConfig = {
      url: apiUrl,
      method: 'GET',
      dataType: 'json',
      timeout: 15000, // 15초 타임아웃
      cache: false
    };
    
    // 필터가 있으면 파라미터 추가
    if (hasFilter) {
      ajaxConfig.data = filter;
    }
    
    $.ajax(ajaxConfig)
      .done(function(data) {
        console.log('✅ 견적서 목록 조회 성공:', data);
        console.log('📊 데이터 타입:', typeof data, Array.isArray(data));
        console.log('📊 데이터 길이:', data?.length);
        
        quotationList = data || [];
        displayQuotationList(quotationList);
      })
      .fail(function(xhr, status, error) {
        console.error('❌ 견적서 목록 조회 실패:', {
          status: xhr.status,
          statusText: xhr.statusText,
          responseText: xhr.responseText,
          error: error
        });
        
        quotationList = [];
        displayQuotationList([]);
        
        Swal.fire({
          title: '오류',
          text: `견적서 목록을 불러올 수 없습니다.\n상태: ${xhr.status} ${xhr.statusText}`,
          icon: 'error',
          footer: '잠시 후 다시 시도하거나 관리자에게 문의하세요.'
        });
      });
  }

  /*──────────────── 견적서 목록 표시 ────────────────*/
  function displayQuotationList(list) {
    console.log('🖼️ 견적서 목록 표시:', list);
    
    const $tbody = $('#quotationSearchResults').empty();
    
    if (!list || list.length === 0) {
      $tbody.append(`
        <tr>
          <td colspan="9" class="text-center text-muted">
            <i class="fas fa-search"></i><br>
            검색된 견적서가 없습니다.
          </td>
        </tr>
      `);
      return;
    }

    list.forEach((quotation, index) => {
      try {
        // JSON 데이터를 안전하게 인코딩
        const quotationJson = encodeURIComponent(JSON.stringify(quotation));
        
        $tbody.append(`
          <tr>
            <td>
              <button class="btn btn-select-quotation btn-sm" 
                      data-quotation="${quotationJson}"
                      data-quotation-no="${quotation.quotationNo || ''}"
                      data-index="${index}">
                <i class="fas fa-check"></i> 선택
              </button>
            </td>
            <td class="fw-bold text-primary">${quotation.quotationNo || '-'}</td>
            <td>${formatDate(quotation.quotationDt)}</td>
            <td>${quotation.customerName || quotation.customerNm || '-'}</td>
            <td>${quotation.representativeNm || '-'}</td>
            <td>${quotation.phone || '-'}</td>
            <td>${formatDate(quotation.validPeriod)}</td>
            <td>${formatDate(quotation.expectedDeliveryDt)}</td>
            <td>${quotation.remarks || '-'}</td>
          </tr>
        `);
      } catch (error) {
        console.error('⚠️ 견적서 행 생성 오류:', error, quotation);
      }
    });
    
    console.log(`✅ ${list.length}개 견적서 표시 완료`);
  }

  /*──────────────── 견적서 검색 필터 ────────────────*/
  $('#btnQuotationFilter').click(function() {
    const filter = {
      quotationNo: $('#quotationNoFilter').val().trim(),
      customerName: $('#customerNameFilter').val().trim(),
      dateFrom: $('#quotationDateFrom').val(),
      dateTo: $('#quotationDateTo').val()
    };
    loadQuotationList(filter);
  });

  $('#btnQuotationFilterReset').click(function() {
    $('#quotationNoFilter, #customerNameFilter, #quotationDateFrom, #quotationDateTo').val('');
    loadQuotationList(); // 필터 없이 전체 목록 로드
  });

  /*──────────────── 견적서 선택 및 변환 ────────────────*/
  $('#quotationSearchResults').off('click', '.btn-select-quotation').on('click', '.btn-select-quotation', function() {
    console.log('🎯 견적서 선택 버튼 클릭');
    
    try {
      const quotationData = $(this).data('quotation');
      const quotationNo = $(this).data('quotation-no');
      const index = $(this).data('index');
      
      console.log('📄 선택된 견적서 번호:', quotationNo);
      console.log('📄 인덱스:', index);
      
      if (!quotationData) {
        throw new Error('견적서 데이터가 없습니다.');
      }
      
      const quotation = JSON.parse(decodeURIComponent(quotationData));
      console.log('📄 파싱된 견적서 데이터:', quotation);
      
      // 확인 대화상자
      Swal.fire({
        title: '견적서 변환',
        html: `
          <div class="text-start">
            <p><strong>견적서:</strong> ${quotation.quotationNo}</p>
            <p><strong>거래처:</strong> ${quotation.customerName || quotation.customerNm}</p>
            <p><strong>견적일자:</strong> ${formatDate(quotation.quotationDt)}</p>
            <hr>
            <p class="text-muted">이 견적서를 주문서로 변환하시겠습니까?</p>
          </div>
        `,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#1cc88a',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '<i class="fas fa-exchange-alt"></i> 변환',
        cancelButtonText: '<i class="fas fa-times"></i> 취소',
        reverseButtons: true
      }).then((result) => {
        if (result.isConfirmed) {
          convertQuotationToOrder(quotation);
        }
      });
      
    } catch (error) {
      console.error('❌ 견적서 선택 처리 오류:', error);
      Swal.fire('오류', `견적서 데이터 처리 중 오류가 발생했습니다:\n${error.message}`, 'error');
    }
  });

  /*──────────────── 견적서 → 주문서 변환 로직 ────────────────*/
  function convertQuotationToOrder(quotation) {
    console.log('🔄 ==> 견적서 변환 시작 <==');
    console.log('📋 선택된 견적서:', quotation);
    
    // 로딩 표시
    Swal.fire({
      title: '변환 중...',
      text: '견적서 상세 정보를 불러오고 있습니다.',
      allowOutsideClick: false,
      showConfirmButton: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
    
    // 1단계: 견적서 상세 정보 조회
    $.ajax({
      url: `/bsn/quotation/details`,
      method: 'GET',
      data: { quotationNo: quotation.quotationNo },
      dataType: 'json',
      timeout: 15000
    })
    .done(function(details) {
      console.log('📋 견적서 상세 조회 성공:', details);
      
      if (!details || details.length === 0) {
        Swal.fire('경고', '견적서 상세 정보가 없습니다.', 'warning');
        return;
      }
      
      // 2단계: 현재 주문서 확인
      if (!currentOrder || !currentOrder.orderNo) {
        Swal.fire('오류', '먼저 신규 주문서를 생성해주세요.', 'error');
        return;
      }
      
      console.log('📝 현재 주문서:', currentOrder);
      
      try {
        // 3단계: 헤더 정보 매핑 및 업데이트
        updateOrderHeader(quotation);
        
        // 4단계: 상세 정보 매핑 및 업데이트
        updateOrderDetails(details);
        
        // 5단계: 여신 정보 업데이트 (거래처가 있는 경우)
        if (quotation.customerCd) {
          updateCreditInfo(quotation.customerCd);
        } else {
          safeSetRowData(creditGridApi, getInitCreditRows());
        }
        
        // 6단계: 완료 처리
        $('#quotationModal').modal('hide');
        
        Swal.fire({
          title: '변환 완료! 🎉',
          html: `
            <div class="text-center">
              <i class="fas fa-check-circle text-success" style="font-size: 3rem;"></i>
              <p class="mt-3">견적서가 주문서로 성공적으로 변환되었습니다.</p>
              <p class="text-muted">필수 정보를 확인 후 등록해 주세요.</p>
            </div>
          `,
          icon: 'success',
          timer: 3000,
          showConfirmButton: true,
          confirmButtonText: '확인'
        });
        
        console.log('✅ ==> 견적서 변환 완료 <==');
        
      } catch (error) {
        console.error('❌ 변환 처리 중 오류:', error);
        Swal.fire('오류', `변환 처리 중 오류가 발생했습니다:\n${error.message}`, 'error');
      }
      
    })
    .fail(function(xhr, status, error) {
      console.error('❌ 견적서 상세 조회 실패:', {
        status: xhr.status,
        statusText: xhr.statusText,
        responseText: xhr.responseText,
        error: error
      });
      
      Swal.fire({
        title: '오류',
        text: `견적서 상세 정보를 불러올 수 없습니다.\n상태: ${xhr.status} ${error}`,
        icon: 'error'
      });
    });
  }


  /*──────────────── 주문서 헤더 업데이트 함수 (수정됨) ────────────────*/
  function updateOrderHeader(quotation) {
    console.log('📝 헤더 정보 업데이트 시작');

    try {
        // Find the node for the new order row using the now-reliable getRowNodeId
        const rowNode = headerGridApi.getRowNode(currentOrder.orderNo);
        if (!rowNode) {
            // This error should no longer occur with the getRowNodeId fix.
            throw new Error("신규 주문서 행을 찾을 수 없습니다.");
        }

        // Prepare the data to update
        const updatedData = {
            ...rowNode.data, // Keep existing data like orderNo
            customerCd: quotation.customerCd || '',
            customerNm: quotation.customerName || quotation.customerNm || '',
            representativeNm: quotation.representativeNm || '',
            phoneNo: quotation.phone || '',
            salesEmpCd: quotation.salesEmpCd || 'emp-101',
            discountRate: quotation.discountRate || 0,
            paymentTerms: 'Net 30',
            orderWriter: (quotation.sender || quotation.salesEmpCd || 'emp-101').trim()
        };

        // Update the global state variable
        currentOrder = updatedData;

        // Update the grid row data directly using the AG-Grid API
        rowNode.setData(updatedData);

        console.log('✅ 헤더 정보 업데이트 완료:', currentOrder);

    } catch (error) {
        console.error('❌ 헤더 업데이트 실패:', error);
        throw error; // Propagate error to be caught by the caller
    }
  }


  /*──────────────── 주문서 상세 업데이트 함수 ────────────────*/
  function updateOrderDetails(details) {
    console.log('📋 상세 정보 업데이트 시작:', details);
    
    try {
      // 상세 데이터 변환
      const orderDetails = details.map((detail, index) => {
        const lineData = {
          lineNo: index + 1,
          itemCode: detail.itemCode || '',
          itemName: detail.itemName || '',
          spec: detail.spec || '',
          qty: Number(detail.qty) || 1,
          unitPrice: Number(detail.unitPrice) || 0,
          supplyAmount: Number(detail.supplyAmount) || 0,
          taxAmount: Number(detail.taxAmount) || 0,
          totalAmount: Number(detail.totalMoney || detail.totalAmount) || 0,
          remarks: detail.remarks || '',
          outboundDt: null,
          catchDt: null,
          outState: '대기'
        };
        
        console.log(`📋 디테일 ${index + 1}:`, lineData);
        return lineData;
      });
      
      // AG-Grid에 데이터 설정
      safeSetRowData(detailGridApi, orderDetails);
      
      // 합계 계산
      calcTotals();
      
      console.log('✅ 상세 정보 업데이트 완료:', orderDetails.length, '건');
      
    } catch (error) {
      console.error('❌ 상세 정보 업데이트 실패:', error);
      throw error;
    }
  }

  /*──────────────── 여신 정보 업데이트 함수 ────────────────*/
  function updateCreditInfo(customerCd) {
    console.log('💳 여신 정보 업데이트 시작:', customerCd);
    
    $.ajax({
      url: `/bsn/customer/${customerCd}/credit`,
      method: 'GET',
      dataType: 'json',
      timeout: 10000
    })
    .done(function(creditData) {
      console.log('💳 여신 정보 조회 성공:', creditData);
      showCredit(creditData);
    })
    .fail(function(xhr, status, error) {
      console.warn('⚠️ 여신 정보 조회 실패:', error);
      // 실패해도 기본 여신 정보로 설정
      safeSetRowData(creditGridApi, getInitCreditRows());
    });
  }

  /*──────────────── 5. 헤더 inline 편집 저장 ────────────────*/
  function headerCellValueChanged(event){
    const dto = {
      orderNo:      event.data.orderNo,
      orderDt:      event.data.orderDt,
      paymentTerms: event.data.paymentTerms,
      orderWriter:  event.data.orderWriter,
      remarks:      event.data.remarks
    };
    $.ajax({
      url: `/bsn/orders/${encodeURIComponent(dto.orderNo)}`,
      method: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify(dto)
    }).fail(()=>{
      // 실패 시 원래 값 복구
      event.node.setDataValue(event.colDef.field, event.oldValue);
    });
  }

  /*──────────────── 6. 디테일 inline 편집 저장 ────────────────*/
  function detailCellValueChanged(event){
    const d = event.data;
    const dto = {
      orderNo:     d.orderNo,
      lineNo:      d.lineNo,
      itemCode:    d.itemCode,
      qty:         d.qty,
      unitPrice:   d.unitPrice,
      supplyAmount:d.supplyAmount,
      taxAmount:   d.taxAmount,
      totalAmount: d.totalAmount,
      outboundDt:  d.outboundDt,
      catchDt:     d.catchDt,
      outState:    d.outState,
      remarks:     d.remarks
    };
    $.ajax({
      url: `/bsn/orders/${encodeURIComponent(dto.orderNo)}/details/${dto.lineNo}`,
      method: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify(dto)
    }).fail(()=>{
      event.node.setDataValue(event.colDef.field, event.oldValue);
    });
  }

  /*──────────────── 7. 품목 모달 열기 & 처리 ────────────────*/
  function onDetailCellClicked(event){
    if (event.colDef.field === 'itemName') {
      selectedDetailNode = event.node;
      $('#itemSearchInput').val('');
      $('#itemSearchResults').empty();
      $('#itemModal').modal('show');
    }
  }
  $('#itemModal').on('shown.bs.modal', ()=> loadItemList() );
  $('#btnItemSearch').click(()=> loadItemList($('#itemSearchInput').val()) );
  function loadItemList(keyword = '') {
    $.getJSON('/bsn/items',{ name: keyword })
      .done(list => {
        const $tb = $('#itemSearchResults').empty();
        list.forEach(item => {
          const json = encodeURIComponent(JSON.stringify(item));
          $tb.append(`
            <tr>
              <td>
                <button class="btn btn-sm btn-primary btn-select-item"
                        data-item="${json}">선택</button>
              </td>
              <td>${item.itemCode}</td>
              <td>${item.itemName}</td>
              <td>${item.spec}</td>
              <td>${item.unit}</td>
              <td>${Number(item.salePrice||0).toLocaleString()}</td>
            </tr>`);
        });
      })
      .fail(()=> Swal.fire('오류','품목 목록 불러오기 실패','error'));
  }
  $('#itemSearchResults').on('click','.btn-select-item',function(){
    const item = JSON.parse(decodeURIComponent($(this).data('item')));
    const qty = 1;
    const unit = item.salePrice||0;
    const supply = qty * unit;
    const tax = Math.round(supply * (item.taxRate||0)/100);
    selectedDetailNode.setDataValue('itemCode',    item.itemCode);
    selectedDetailNode.setDataValue('itemName',    item.itemName);
    selectedDetailNode.setDataValue('spec',        item.spec);
    selectedDetailNode.setDataValue('unit',        item.unit);
    selectedDetailNode.setDataValue('qty',         qty);
    selectedDetailNode.setDataValue('unitPrice',   unit);
    selectedDetailNode.setDataValue('supplyAmount',supply);
    selectedDetailNode.setDataValue('taxAmount',   tax);
    selectedDetailNode.setDataValue('totalAmount', supply + tax);
    $('#itemModal').modal('hide');
    calcTotals();
  });

  /*──────────────── 8. 거래처 모달 ────────────────*/
  function onHeaderCellClicked(event){
    if (event.colDef.field === 'customerNm') {
      selectedHeaderNode = event.node;
      $('#customerSearchInput').val('');
      $('#customerSearchResults').empty();
      $('#customerModal').modal('show');
    }
  }
  $('#customerModal').on('shown.bs.modal', ()=> loadCustomerList() );
  $('#btnCustomerSearch').click(()=> loadCustomerList($('#customerSearchInput').val()) );
  function loadCustomerList(keyword = '') {
    $.getJSON('/bsn/customers',{ name: keyword })
      .done(data => {
        const $tb = $('#customerSearchResults').empty();
        data.forEach(c => {
          const j = encodeURIComponent(JSON.stringify(c));
          $tb.append(`
            <tr>
              <td>
                <button class="btn btn-sm btn-primary btn-select-cust"
                        data-cust="${j}">선택</button>
              </td>
              <td>${c.customerCd}</td>
              <td>${c.customerNm}</td>
              <td>${c.representativeNm}</td>
              <td>${c.phoneNo}</td>
            </tr>`);
        });
      })
      .fail(()=> Swal.fire('오류','거래처 목록 불러오기 실패','error'));
  }
  $('#customerSearchResults').on('click','.btn-select-cust',function(){
    const c = JSON.parse(decodeURIComponent($(this).data('cust')));
    ['customerCd','customerNm','representativeNm','phoneNo','salesEmpCd','discountRate']
      .forEach(f=> selectedHeaderNode.setDataValue(f, c[f]));
    currentOrder = selectedHeaderNode.data;
    /* 3) ★ 선택 즉시 여신 정보 가져오기 ★ */
    $.getJSON(`/bsn/customer/${c.customerCd}/credit`)
        .done(showCredit)                 // 이미 만들어 둔 showCredit() 재사용
        .fail(() => Swal.fire('오류', '여신 정보를 불러오지 못했습니다.', 'error'));

    $('#customerModal').modal('hide');
  });

  /*──────────────── 9. 저장 버튼 ────────────────*/
  $('#btnSaveHeader').click(function(){
    if (!currentOrder || !currentOrder.orderNo) {
      return Swal.fire('알림','먼저 헤더를 선택 또는 생성해주세요.','info');
    }
    headerGridApi.stopEditing();
    const dto = {
      orderNo:      currentOrder.orderNo,
      orderDt:      currentOrder.orderDt,
      customerCd:   currentOrder.customerCd,
      paymentTerms: currentOrder.paymentTerms,
      orderWriter:  currentOrder.orderWriter,
      remarks:      currentOrder.remarks
    };
    Swal.showLoading();
    $.ajax({
      url: `/bsn/orders/${encodeURIComponent(dto.orderNo)}`,
      method:'PUT',
      contentType:'application/json',
      data: JSON.stringify(dto)
    })
    .done(()=> Swal.fire('완료','헤더가 저장되었습니다.','success'))
    .fail(()=> Swal.fire('실패','헤더 저장 중 오류가 발생했습니다.','error'));
  });

  /*──────────────── 10. 신규 등록(POST) - 완전 강화된 버전 ────────────────*/
  $('#btnSave').click(saveOrder);
  function saveOrder(){
    console.log('=== 주문서 저장 시작 ===');
    
    if (!editMode) {
      return Swal.fire('알림','신규 모드에서만 저장할 수 있습니다.','info');
    }
    
    headerGridApi.stopEditing();
    detailGridApi.stopEditing();
    
    if (!currentOrder) {
      console.error('currentOrder가 없음');
      return Swal.fire('오류','헤더 정보가 없습니다.','error');
    }
    
    console.log('현재 주문서 상태:', currentOrder);
    
    // ★ 필수 필드 검증 강화
    const validationErrors = [];
    
    if (!currentOrder.orderNo || !currentOrder.orderNo.trim()) {
      validationErrors.push('주문번호가 없습니다.');
    }
    
    if (!currentOrder.customerCd || !currentOrder.customerCd.trim()) {
      validationErrors.push('거래처를 선택해 주세요.');
    }
    
    if (!currentOrder.orderDt) {
      validationErrors.push('주문일자를 입력해 주세요.');
    }
    
    if (!currentOrder.orderWriter || !currentOrder.orderWriter.trim()) {
      console.warn('담당자 정보가 없어 기본값으로 설정합니다.');
      currentOrder.orderWriter = 'emp-101'; // 기본 사원코드
    }
    
    if (validationErrors.length > 0) {
      console.error('헤더 검증 실패:', validationErrors);
      return Swal.fire('오류', validationErrors.join('\n'), 'error');
    }
    
    // ★ 디테일 수집 및 검증
    const details = [];
    let detailErrors = [];
    
    detailGridApi.forEachNode((node, index) => {
      const data = node.data;
      console.log(`디테일 ${index + 1}:`, data);
      
      if (data.itemCode && data.itemCode.trim()) {
        // 필수 필드 기본값 설정
        const detail = {
          ...data,
          lineNo: details.length + 1,
          qty: data.qty || 1,
          unitPrice: data.unitPrice || 0,
          supplyAmount: data.supplyAmount || 0,
          taxAmount: data.taxAmount || 0,
          totalAmount: data.totalAmount || 0,
          outState: data.outState || '대기',
          remarks: data.remarks || ''
        };
        
        details.push(detail);
      } else if (data.itemName || data.qty || data.unitPrice) {
        // 품목코드는 없지만 다른 데이터가 있는 경우
        detailErrors.push(`${index + 1}번째 행: 품목코드가 필요합니다.`);
      }
    });
    
    console.log('수집된 디테일:', details);
    console.log('디테일 오류:', detailErrors);
    
    if (details.length === 0) {
      return Swal.fire('안내','품목을 한 개 이상 입력해 주세요.','warning');
    }
    
    if (detailErrors.length > 0) {
      return Swal.fire('오류', detailErrors.join('\n'), 'error');
    }
    
    // ★ 최종 DTO 구성
    const dto = {
      ...currentOrder,
      details: details
    };
    
    // ★ 디버깅: 전송 데이터 로그 출력
    console.log('=== 최종 전송 데이터 ===');
    console.log('DTO 전체:', dto);
    console.log('헤더 필수 필드:', {
      orderNo: dto.orderNo,
      orderDt: dto.orderDt,
      customerCd: dto.customerCd,
      customerNm: dto.customerNm,
      orderWriter: dto.orderWriter,
      paymentTerms: dto.paymentTerms
    });
    console.log('디테일 건수:', details.length);
    details.forEach((detail, index) => {
      console.log(`디테일 ${index + 1}:`, {
        lineNo: detail.lineNo,
        itemCode: detail.itemCode,
        itemName: detail.itemName,
        qty: detail.qty,
        unitPrice: detail.unitPrice
      });
    });
    
    // ★ 저장 요청
    Swal.fire({
      title: '저장 중...',
      text: '주문서를 저장하고 있습니다.',
      allowOutsideClick: false,
      showConfirmButton: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
    
    console.log('AJAX 요청 시작');
    
    $.ajax({
      url: '/bsn/orders',
      method: 'POST',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(dto),
      timeout: 30000 // 30초 타임아웃
    })
    .done(function(response) {
      console.log('=== 저장 응답 성공 ===');
      console.log('응답 전체:', response);
      
      if (response && response.success) {
        console.log('저장 성공:', response.orderNo);
        
        Swal.fire({
          title: '저장 완료!',
          text: `주문서 "${response.orderNo}"가 성공적으로 등록되었습니다.`,
          icon: 'success',
          timer: 3000,
          showConfirmButton: true
        });
        
        // 상태 초기화
        setEditMode(false);
        $('#btnQuotationSearch').prop('disabled', true);
        currentOrder = null;
        
        // 헤더 목록 새로고침
        loadHeaderData();
        
        // 디테일/여신 초기화
        safeSetRowData(detailGridApi, []);
        safeSetRowData(creditGridApi, getInitCreditRows());
        
      } else {
        console.error('저장 실패 응답:', response);
        Swal.fire({
          title: '저장 실패',
          text: response?.message || '서버에서 알 수 없는 오류가 발생했습니다.',
          icon: 'error',
          confirmButtonText: '확인'
        });
      }
    })
    .fail(function(xhr, status, error) {
      console.error('=== 저장 요청 실패 ===');
      console.error('XHR 상태:', xhr.status, xhr.statusText);
      console.error('응답 텍스트:', xhr.responseText);
      console.error('오류 정보:', { status, error });
      
      let errorMessage = '저장 중 오류가 발생했습니다.';
      
      try {
        if (xhr.responseText) {
          const errorData = JSON.parse(xhr.responseText);
          errorMessage = errorData.message || errorMessage;
          
          if (errorData.errorDetail) {
            console.error('상세 오류:', errorData.errorDetail);
          }
        }
      } catch(parseError) {
        console.error('응답 JSON 파싱 실패:', parseError);
        if (xhr.responseText && xhr.responseText.length < 500) {
          errorMessage = xhr.responseText;
        }
      }
      
      Swal.fire({
        title: '저장 실패',
        text: errorMessage,
        icon: 'error',
        footer: `상태 코드: ${xhr.status}`,
        confirmButtonText: '확인'
      });
    });
  }

  /*──────────────── 11. 헤더 클릭 → 디테일+여신 ────────────────*/
  function handleHeaderRowClicked(e){
    // =================================================================
    // ★★★★★★★★★★★★★★★★★★★★★ FIX START ★★★★★★★★★★★★★★★★★★★★★
    // =================================================================
    // 신규 등록(editMode) 중에는 다른 행의 데이터를 불러오지 않도록 수정
    // If in edit mode, do not process the click event to prevent `currentOrder` from being overwritten.
    if (editMode) {
      // Optional: Provide feedback to the user that they are in edit mode.
      if (e.data.orderNo !== currentOrder.orderNo) {
          console.warn('신규 등록 모드입니다. 먼저 현재 주문서를 저장 또는 취소하세요.');
      }
      return;
    }
    // =================================================================
    // ★★★★★★★★★★★★★★★★★★★★★★ FIX END ★★★★★★★★★★★★★★★★★★★★★★
    // =================================================================

    currentOrder = e.data;
    
    // 기존 데이터 조회 모드일 때는 견적서 조회 버튼 비활성화
    $('#btnQuotationSearch').prop('disabled', true);
    
    const { orderNo, customerCd } = e.data;
    const reqDetail = $.getJSON(`/bsn/orders/${orderNo}/details`);
    const reqCredit = $.getJSON(`/bsn/customer/${customerCd}/credit`);
    $.when(reqDetail, reqCredit)
      .done((dRes, cRes)=>{
        safeSetRowData(detailGridApi, dRes[0]);
        showCredit(cRes[0]);
        calcTotals();
      })
      .fail(()=> console.error('데이터 로드 실패'));
  }
  function showCredit(c){
    safeSetRowData(creditGridApi,[{
      creditLimit:     c.creditLimit    || 0,
      remainingCredit: c.remainingCredit|| 0,
      creditUsed:      c.creditUsed     || 0,
      creditStatus:    c.creditStatus   || '',
      creditHoldFlg:   c.creditHoldFlg  || '',
      tradeStopFlg:    c.tradeStopFlg   || ''
    }]);
  }

  /*──────────────── 12. 삭제 ────────────────*/
  $('#btnDelete').click(deleteOrder);
  function deleteOrder(){
    const sel = headerGridApi.getSelectedRows();
    if (!sel.length) {
      return Swal.fire('안내','삭제할 주문서를 선택해 주세요.','warning');
    }
    Swal.fire({
      title: '삭제 확인',
      text: `선택한 ${sel.length}건을 삭제하시겠습니까?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e74a3b',
      cancelButtonColor: '#6c757d',
      confirmButtonText: '삭제',
      cancelButtonText: '취소'
    }).then(r=>{
      if (!r.isConfirmed) return;
      Swal.showLoading();
      const jobs = sel.map(o=>$.ajax({ url:`/bsn/orders/${o.orderNo}`, method:'DELETE' }));
      $.when.apply($, jobs).done(()=>{
        Swal.fire('삭제 완료','삭제되었습니다.','success');
        loadHeaderData();
        safeSetRowData(detailGridApi, []);
        showCredit(getInitCreditObj());
      }).fail(()=> Swal.fire('삭제 실패','서버 오류가 발생했습니다.','error'));
    });
  }

  /*──────────────── 13. 로드 & 계산 ────────────────*/
  function loadHeaderData(){
    $.get('/bsn/sorlist/data')
      .done(list=> safeSetRowData(headerGridApi, list))
      .fail(()=> console.error('헤더 로드 실패'));
  }
  function calcTotals(){
    let supply=0, tax=0, total=0;
    detailGridApi.forEachNode(n=>{
      supply += Number(n.data.supplyAmount||0);
      tax    += Number(n.data.taxAmount||0);
      total  += Number(n.data.totalAmount||0);
    });
    safeSetPinnedBottomRow(detailGridApi,[{
      itemCode:'합계', supplyAmount:supply, taxAmount:tax, totalAmount:total
    }]);
  }

  /*──────────────── 15. 행 추가/삭제 버튼 ────────────────*/
  $('#btnAddRow').click(function() {
    if (!editMode) {
      Swal.fire('알림', '신규 모드에서만 행을 추가할 수 있습니다.', 'info');
      return;
    }
    
    const newRowData = {
      lineNo: detailGridApi.getDisplayedRowCount() + 1,
      itemCode: '',
      itemName: '',
      spec: '',
      qty: 1,
      unitPrice: 0,
      supplyAmount: 0,
      taxAmount: 0,
      totalAmount: 0,
      remarks: '',
      outboundDt: null,
      catchDt: null,
      outState: '대기'
    };
    
    detailGridApi.applyTransaction({ add: [newRowData] });
    calcTotals();
  });

  $('#btnDeleteRow').click(function() {
    if (!editMode) {
      Swal.fire('알림', '신규 모드에서만 행을 삭제할 수 있습니다.', 'info');
      return;
    }
    
    const selectedRows = detailGridApi.getSelectedRows();
    if (selectedRows.length === 0) {
      Swal.fire('알림', '삭제할 행을 선택해 주세요.', 'info');
      return;
    }
    
    Swal.fire({
      title: '행 삭제',
      text: `선택한 ${selectedRows.length}개 행을 삭제하시겠습니까?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#e74a3b',
      cancelButtonColor: '#6c757d',
      confirmButtonText: '삭제',
      cancelButtonText: '취소'
    }).then((result) => {
      if (result.isConfirmed) {
        detailGridApi.applyTransaction({ remove: selectedRows });
        
        // 행 번호 재정렬
        const remainingData = [];
        detailGridApi.forEachNode((node, index) => {
          node.data.lineNo = index + 1;
          remainingData.push(node.data);
        });
        safeSetRowData(detailGridApi, remainingData);
        calcTotals();
        
        Swal.fire('완료', '선택한 행이 삭제되었습니다.', 'success');
      }
    });
  });

  /*──────────────── 16. 기타 유틸리티 ────────────────*/
  function getInitCreditRows(){
    return [{ creditLimit:0, remainingCredit:0, creditUsed:0,
              creditStatus:'', creditHoldFlg:'', tradeStopFlg:'' }];
  }
  function getInitCreditObj(){
    return { creditLimit:0, remainingCredit:0, creditUsed:0,
            creditStatus:'', creditHoldFlg:'', tradeStopFlg:'' };
  }
  function setEditMode(flag){
    editMode = flag;
    detailGridApi.refreshCells({force:true});
    headerGridApi.refreshCells({force:true});
    $('#btnSave,#btnAddRow,#btnDeleteRow').prop('disabled', !flag);
    
    // 편집 모드가 아닐 때는 견적서 조회 버튼 비활성화
    if (!flag) {
      $('#btnQuotationSearch').prop('disabled', true);
    }
  }
  function numFmt(v){ return Number(v||0).toLocaleString()+'원'; }

  // 모달 이벤트 리스너 추가
  $('#quotationModal').on('shown.bs.modal', function() {
    console.log('📋 견적서 모달 열림');
    setTimeout(() => {
      loadQuotationList(); // 모달이 열릴 때 견적서 목록 로드
    }, 100);
  });

  // 견적서 검색 필터 엔터키 이벤트
  $('#quotationNoFilter, #customerNameFilter').off('keypress').on('keypress', function(e) {
    if (e.which === 13) { // 엔터키
      $('#btnQuotationFilter').click();
    }
  });

  // ★ 디버그 버튼 기능
  $('#btnDebug').click(function() {
    Swal.fire({
      title: '디버그 메뉴',
      html: `
        <div class="d-grid gap-2">
          <button class="btn btn-info btn-sm" onclick="checkAuthInfo()">
            <i class="fas fa-user"></i> 인증 정보 확인
          </button>
          <button class="btn btn-warning btn-sm" onclick="checkQuotationData()">
            <i class="fas fa-database"></i> 견적서 데이터 확인
          </button>
          <button class="btn btn-success btn-sm" onclick="testQuotationAPI()">
            <i class="fas fa-api"></i> 견적서 API 테스트
          </button>
          <button class="btn btn-danger btn-sm" onclick="debugQuotationSystem()">
            <i class="fas fa-cog"></i> 시스템 디버그
          </button>
          <button class="btn btn-secondary btn-sm" onclick="clearConsole()">
            <i class="fas fa-broom"></i> 콘솔 초기화
          </button>
        </div>
      `,
      showConfirmButton: false,
      showCancelButton: true,
      cancelButtonText: '닫기',
      width: 400
    });
  });

  // ★ 디버그 함수들
  window.checkAuthInfo = function() {
    console.log('=== 인증 정보 확인 시작 ===');
    $.getJSON('/bsn/debug/auth-info')
      .done(function(data) {
        console.log('인증 정보:', data);
        Swal.fire({
          title: '인증 정보',
          html: `
            <div class="text-start">
              <strong>회사 코드:</strong> ${data.coIdx || 'N/A'}<br>
              <strong>사원 코드:</strong> ${data.empIdx || 'N/A'}<br>
              <strong>사용자 코드:</strong> ${data.userIdx || 'N/A'}<br>
              <strong>마스터 코드:</strong> ${data.mstIdx || 'N/A'}
            </div>
          `,
          icon: 'info'
        });
      })
      .fail(function() {
        Swal.fire('오류', '인증 정보를 가져올 수 없습니다.', 'error');
      });
  };

  window.checkQuotationData = function() {
    console.log('=== 견적서 데이터 확인 시작 ===');
    $.getJSON('/bsn/debug/quotation-raw')
      .done(function(data) {
        console.log('견적서 원시 데이터:', data);
        
        let message = `
          <div class="text-start">
            <strong>회사 코드:</strong> ${data.coIdx || 'N/A'}<br>
            <strong>견적서 건수:</strong> ${data.quotationCount || 0}<br>
        `;
        
        if (data.firstQuotation) {
          message += `
            <hr>
            <strong>첫 번째 견적서:</strong><br>
            - 번호: ${data.firstQuotation.quotationNo || 'N/A'}<br>
            - 거래처: ${data.firstQuotation.customerName || 'N/A'}<br>
            - 거래처코드: ${data.firstQuotation.customerCd || 'N/A'}<br>
          `;
        }
        
        message += '</div>';
        
        Swal.fire({
          title: '견적서 데이터',
          html: message,
          icon: data.success ? 'info' : 'error'
        });
      })
      .fail(function() {
        Swal.fire('오류', '견적서 데이터를 가져올 수 없습니다.', 'error');
      });
  };

  window.testQuotationAPI = function() {
    console.log('=== 견적서 API 테스트 시작 ===');
    
    // 1. 목록 API 테스트
    $.getJSON('/bsn/quotation/list')
      .done(function(data) {
        console.log('견적서 목록 API 결과:', data);
        
        if (data && data.length > 0) {
          // 2. 첫 번째 견적서의 상세 API 테스트
          const firstQuotation = data[0];
          console.log('첫 번째 견적서 상세 API 테스트:', firstQuotation.quotationNo);
          
          $.getJSON(`/bsn/quotation/details?quotationNo=${encodeURIComponent(firstQuotation.quotationNo)}`)
            .done(function(details) {
              console.log('견적서 상세 API 결과:', details);
              
              Swal.fire({
                title: 'API 테스트 성공',
                html: `
                  <div class="text-start">
                    <strong>목록 API:</strong> 성공 (${data.length}건)<br>
                    <strong>상세 API:</strong> 성공 (${details.length}건)<br>
                    <hr>
                    자세한 내용은 콘솔을 확인하세요.
                  </div>
                `,
                icon: 'success'
              });
            })
            .fail(function() {
              Swal.fire('경고', '견적서 목록은 성공했지만 상세 조회에 실패했습니다.', 'warning');
            });
        } else {
          Swal.fire('정보', '견적서 데이터가 없습니다. 먼저 견적서를 등록해주세요.', 'info');
        }
      })
      .fail(function() {
        Swal.fire('오류', '견적서 목록 API 호출에 실패했습니다.', 'error');
      });
  };

  window.debugQuotationSystem = function() {
    console.log('=== 견적서 시스템 디버깅 ===');
    console.log('editMode:', editMode);
    console.log('currentOrder:', currentOrder);
    console.log('quotationList:', quotationList);
    console.log('headerGridApi:', !!headerGridApi);
    console.log('detailGridApi:', !!detailGridApi);
    console.log('creditGridApi:', !!creditGridApi);
    
    const debugInfo = {
      editMode: editMode,
      currentOrder: currentOrder,
      quotationListLength: quotationList?.length || 0,
      hasHeaderGrid: !!headerGridApi,
      hasDetailGrid: !!detailGridApi,
      hasCreditGrid: !!creditGridApi,
      quotationButtonDisabled: $('#btnQuotationSearch').prop('disabled')
    };
    
    Swal.fire({
      title: '시스템 디버그 정보',
      html: `
        <div class="text-start">
          <strong>편집 모드:</strong> ${debugInfo.editMode ? '✅' : '❌'}<br>
          <strong>현재 주문서:</strong> ${debugInfo.currentOrder ? '✅' : '❌'}<br>
          <strong>견적서 목록:</strong> ${debugInfo.quotationListLength}건<br>
          <strong>헤더 그리드:</strong> ${debugInfo.hasHeaderGrid ? '✅' : '❌'}<br>
          <strong>디테일 그리드:</strong> ${debugInfo.hasDetailGrid ? '✅' : '❌'}<br>
          <strong>여신 그리드:</strong> ${debugInfo.hasCreditGrid ? '✅' : '❌'}<br>
          <strong>견적서 버튼:</strong> ${debugInfo.quotationButtonDisabled ? '비활성' : '활성'}<br>
          <hr>
          <small>콘솔에서 상세 정보를 확인하세요.</small>
        </div>
      `,
      icon: 'info'
    });
  };

  window.clearConsole = function() {
    console.clear();
    console.log('콘솔이 초기화되었습니다.');
    Swal.fire('완료', '콘솔이 초기화되었습니다.', 'success');
  };

  console.log('🎉 견적서 변환 기능이 포함된 주문서 관리 JS 초기화 완료');
  console.log('🐛 디버그 기능: 우상단 디버그 버튼을 클릭하세요.');
  console.log('💡 디버깅 명령어:');
  console.log('  - debugQuotationSystem() : 시스템 상태 확인');
  console.log('  - testQuotationAPI() : API 연결 테스트'); 
  console.log('  - checkQuotationData() : 견적서 데이터 확인');

});