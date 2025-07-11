package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-19
 * - 설명 : 회계 계정과목관리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-19 (김희정): AccController에서 분리 => 계정과목Controller 생성
 * - 2025-06-23 (김희정): 단건 조회(accountCode) 작성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccTrialBalanceController {

  @GetMapping("/trbal")
  public String trialBalance() {
    return "acc/trialBalance";
  }
}
