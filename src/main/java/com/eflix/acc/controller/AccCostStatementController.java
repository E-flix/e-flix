package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 원가명세서 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 원가명세서Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccCostStatementController {

  // private final Service Service;

  /**
   * 원가명세서 화면 요청 처리
   */
  @GetMapping("/cs")
  public String costStatement() {
    return "acc/costStatement";
  }
}
