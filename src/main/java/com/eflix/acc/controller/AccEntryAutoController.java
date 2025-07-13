package com.eflix.acc.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eflix.acc.dto.EntryAutoAllDTO;
import com.eflix.acc.dto.EntryDetailDTO;
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
    model.addAttribute("code0A", commonService.getCommon("0A"));
    model.addAttribute("code0H", commonService.getCommon("0H"));
    model.addAttribute("code0G", commonService.getCommon("0G"));
    return "acc/entryAuto";
  }

  // 자동전표 전체(차변/대변/기간 등 프론트에서 JS로 분기)
  @GetMapping("/ena/all")
  @ResponseBody
  public List<EntryAutoAllDTO> getAutoEntryAll() {
    EntryAutoAllDTO param = new EntryAutoAllDTO();
    return entryAutoService.selectAutoEntryAll(param);
  }

  /**
   * 급여기준 자동전표 생성
   */
  @PostMapping("/ena/auto-salary-entry")
  @ResponseBody
  public String createAutoSalaryEntry() {
    entryAutoService.createSalaryEntryMasters();
    return "자동전표 생성 완료";
  }

  @PostMapping("/ena/addDetail")
  @ResponseBody
  public int insertBatch(@RequestBody List<EntryDetailDTO> entryDetailList) {
    return entryAutoService.insertBatch(entryDetailList);
  }
}
