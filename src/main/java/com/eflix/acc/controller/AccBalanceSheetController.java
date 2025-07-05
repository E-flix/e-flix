package com.eflix.acc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.acc.dto.BalanceSheetDTO;
import com.eflix.acc.service.BalanceSheetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 재무상태표 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 재무상태표Controller 생성
 * ============================================
 */
@Slf4j
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccBalanceSheetController {

    private final BalanceSheetService balanceSheetService;

  /**
   * 재무상태표 화면 요청 처리
   */
  @GetMapping("/bs")
  public String balanceSheet() {
    return "acc/BalanceSheet";
  }

  /**
   * 재무상태표 데이터 조회 (AJAX) - URL 매핑 수정
   */
  @GetMapping("/bs/data")
  @ResponseBody
  public Map<String, Object> getBalanceSheetData(
      @RequestParam(required = false) String year,
      @RequestParam(required = false) String month,
      @RequestParam(required = false) String coIdx) {
    
    Map<String, Object> result = new HashMap<>();
    
    try {
      log.info("재무상태표 데이터 조회 요청 - year: {}, month: {}, coIdx: {}", year, month, coIdx);
      
      // 파라미터 설정 (ServiceImpl에서 자동으로 처리됨)
      Map<String, Object> params = new HashMap<>();
      params.put("year", year);
      params.put("month", month);
      params.put("coIdx", coIdx);
      
      // 서비스 호출
      List<BalanceSheetDTO> dbData = balanceSheetService.getBalanceSheetByYear(params);
      List<Map<String, Object>> gridData = balanceSheetService.convertToGridFormat(dbData);
      
      result.put("success", true);
      result.put("data", gridData);
      result.put("message", "데이터 조회 성공");
        
    } catch (Exception e) {
      log.error("재무상태표 데이터 조회 중 오류 발생", e);
      result.put("success", false);
      result.put("data", null);
      result.put("message", "데이터 조회 실패: " + e.getMessage());
    }
    
    return result;
  }
}
