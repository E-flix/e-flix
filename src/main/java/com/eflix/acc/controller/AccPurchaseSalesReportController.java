package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import com.eflix.acc.dto.PurchaseSalesReportDTO;
import com.eflix.acc.service.PurchaseSalesReportService;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 매입매출장 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 매입매출장Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccPurchaseSalesReportController {

  private final PurchaseSalesReportService purchaseSalesReportService;

  /**
   * 매입매출장 화면 요청 처리
   */
  @GetMapping("/psr")
  public String purchaseSalesReport() {
    return "acc/purchaseSalesReport";
  }

  /**
   * 매입매출장 목록 조회 (AJAX)
   */
  @ResponseBody
  @GetMapping("/psr/list")
  public List<PurchaseSalesReportDTO> getReportList(
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam(required = false, defaultValue = "전체") String type,
      @RequestParam(required = false, defaultValue = "전체") String taxType,
      @RequestParam(required = false, defaultValue = "전체") String electronicType
  ) {
    return purchaseSalesReportService.getReportList(startDate, endDate, type, taxType, electronicType);
  }
}
