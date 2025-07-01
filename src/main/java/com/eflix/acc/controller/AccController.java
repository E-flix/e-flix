package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-18
 * - 설명 : 회계 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-18 (김희정): 회계 메인 화면 및 각 기능별 화면 요청 처리 메소드 추가
 * - 2025-06-19 (김희정): 계정과목관리 화면 Controller 분리
 * - 2025-06-20 (김희정): 일반전표 화면/ 매입매출전표 화면 Controller 분리
 * - 2025-06-23 (김희정): Main제외 남은 ACC 화면 Controller 분리
 * ============================================
 */
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
}
