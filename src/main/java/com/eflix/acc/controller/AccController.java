package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import lombok.RequiredArgsConstructor;
/** ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-18
  - 설명     : 회계 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-18 (김희정): 회계 메인 화면 및 각 기능별 화면 요청 처리 메소드 추가
  - 2025-06-19 (김희정): 계정과목관리 화면 Controller 분리
============================================  */
@Controller
@RequestMapping("/acc")
public class AccController {

    /**
     * 회계 메인 화면 요청 처리
     */
    @GetMapping()
    public String accMain() {
        return "acc/accMain";
    }

    /**
     * 재무상태표 화면 요청 처리
     */
    @GetMapping("/bs")
    public String balanceSheet() {
        return "acc/BalanceSheet";
    }

    /**
     * 현금영수증관리 화면 요청 처리
     */
    @GetMapping("/cr")
    public String cashReceipt() {
        return "acc/cashReceipt";
    }

    /**
     * 거래처원장 화면 요청 처리
     */
    @GetMapping("/cl")
    public String clientsLedger() {
        return "acc/clientsLedger";
    }

    /**
     * 원가명세서 화면 요청 처리
     */
    @GetMapping("/cs")
    public String costStatement() {
        return "acc/costStatement";
    }

    /**
     * 전자세금계산서관리 화면 요청 처리
     */
    @GetMapping("/eti")
    public String electronicTaxInvoice() {
        return "acc/electronicTaxInvoice";
    }

    /**
     * 일반전표 화면 요청 처리
     */
    @GetMapping("/en")
    public String entry() {
        return "acc/entry";
    }

    /**
     * 자동전표처리 화면 요청 처리
     */
    @GetMapping("/ena")
    public String entryAuto() {
        return "acc/entryAuto";
    }

    /**
     * 매입매출전표 화면 요청 처리
     */
    @GetMapping("/enps")
    public String entryPurchaseSales() {
        return "acc/entryPurchaseSales";
    }

    /**
     * 고정자산관리 화면 요청 처리
     */
    @GetMapping("/fa")
    public String fixedAsset() {
        return "acc/fixedAsset";
    }

    /**
     * 손익계산서 화면 요청 처리
     */
    @GetMapping("/is")
    public String incomeStatement() {
        return "acc/incomeStatement";
    }

    /**
     * 다중거래처 모달창 화면 요청 처리
     */
    @GetMapping("/mtm")
    public String multipleTransactionModal() {
        return "acc/multipleTransactionModal";
    }

    /**
     * 거래처관리 화면 요청 처리
     */
    @GetMapping("/pt")
    public String partner() {
        return "acc/partner";
    }

    /**
     * 매입매출장 화면 요청 처리
     */
    @GetMapping("/psr")
    public String purchaseSalesReport() {
        return "acc/purchaseSalesReport";
    }
}
