package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 전자세금계산서관리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 전자세금계산서관리Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class ElectronicTaxInvoiceController {

  // private final Service Service;

  /**
   * 전자세금계산서관리 화면 요청 처리
   */
  @GetMapping("/eti")
  public String electronicTaxInvoice() {
    return "acc/electronicTaxInvoice";
  }

  /**
   * 다중거래처 모달창 화면 요청 처리
   */
  @GetMapping("/mtm")
  public String multipleTransactionModal() {
    return "acc/multipleTransactionModal";
  }
}
