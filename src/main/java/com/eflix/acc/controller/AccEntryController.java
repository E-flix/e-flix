package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-20
 * - 설명 : 회계 전표관리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-20 (김희정): AccController에서 분리 => 전표관리Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccEntryController {

  //private final EntryService entryService;

  /**
   * 전표 목록 조회
   * 
   * @param model : 뷰에 전달할 데이터 모델
   * @return : 일반전표
   */
  @GetMapping("/en")
  public String entry() {
  //public String entry(Model model) {
    //model.addAttribute("entryList", entryService.getList());
    return "acc/entry";
  }

  /**
   * 매입매출전표 화면 요청 처리
   */
  @GetMapping("/enps")
  public String entryPurchaseSales() {
    return "acc/entryPurchaseSales";
  }
}
