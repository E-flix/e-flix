package com.eflix.acc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.acc.dto.AccountDTO;
import com.eflix.acc.service.AccountService;

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
public class AccAccountController {

  private final AccountService accountService;

  /**
   * 계정과목 목록 조회
   * 
   * @param model : 뷰에 전달할 데이터 모델
   * @return : 계정과목 목록 페이지명
   */
  @GetMapping("/act")
  public String account(Model model) {
    model.addAttribute("accountList", accountService.getList());
    return "acc/account";
  }

  // 계정과목 전체조회
  @ResponseBody
  @GetMapping("/act/list")
  public List<AccountDTO> getAccountList() {
    return accountService.getList();
  }

  // 계정과목 단건조회 (accountCode)
  @ResponseBody
  @GetMapping("/act/{accountCode}")
  public AccountDTO getListByCode(@PathVariable int accountCode) {
    return accountService.getListByCode(accountCode);
  }
}
