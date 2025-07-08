package com.eflix.acc.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.eflix.acc.service.EntryAutoService;
import com.eflix.common.code.service.CommonService;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 자동전표처리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 자동전표처리Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccEntryAutoController {

  private final EntryAutoService entryAutoService;
  private final CommonService commonService;

  /**
   * 자동전표처리 화면 요청 처리
   */
  @GetMapping("/ena")
  public String entryAuto(Model model) {
    model.addAttribute("code0A", commonService.getCommon("0A")); // 여부
    model.addAttribute("code0H", commonService.getCommon("0H")); // 과세유형
    model.addAttribute("code0G", commonService.getCommon("0G")); // 차변대변
    return "acc/entryAuto";
  }

  /**
   * 자동전표처리 상세 목록 조회
   * 
   * @return List<Map<String, Object>> - 자동전표처리 상세 목록
   */
  @GetMapping("/list")
  public List<Map<String, Object>> getDetailList() {
    return entryAutoService.getDetailList();
  }
}
