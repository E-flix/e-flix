package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.eflix.acc.dto.EntryMasterDTO;
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
  public List<EntryMasterDTO> getReportList(
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam(required = false, defaultValue = "") String entryType,
      @RequestParam(required = false, defaultValue = "") String transactionType,
      @RequestParam(required = false, defaultValue = "") String electronicType
  ) {
    Map<String, Object> params = new HashMap<>();
    params.put("startDate", startDate);
    params.put("endDate", endDate);
    params.put("entryType", entryType);
    params.put("transactionType", transactionType);
    params.put("electronicType", electronicType);
    return purchaseSalesReportService.getReportList(params);
  }
}
