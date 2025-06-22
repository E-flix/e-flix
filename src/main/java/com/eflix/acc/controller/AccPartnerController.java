package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eflix.acc.dto.PartnerDetailDTO;
import com.eflix.acc.service.PartnerService;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 거래처관리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 거래처관리Controller 생성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccPartnerController {

  private final PartnerService partnerService;

  /**
   * 거래처관리 화면 요청 처리
   */
  @GetMapping("/pt")
  public String partner(Model model) {
    model.addAttribute("partnerList", partnerService.getList());
    return "acc/partner";
  }

  @ResponseBody
  @GetMapping("/pt/{partnerCode}")
  public PartnerDetailDTO getListByCode(@PathVariable int partnerCode) {
    return partnerService.getListByCode(partnerCode);
  }
}
