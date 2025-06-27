package com.eflix.acc.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.service.ClientsLedgerService;
import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-23
 * - 설명 : 거래처원장 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-23 (김희정): AccController에서 분리 => 거래처원장Controller 생성
 * - 2025-06-26 (김희정): 잔액 조건 전체 조회 작성
 * - 2025-06-27 (김희정): 내용 조건 전체 조회 작성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccClientsLedgerController {

  private final ClientsLedgerService clientsLedgerService;

  /**
   * 거래처원장 화면 요청 처리
   */
  @GetMapping("/cl")
  public String clientsLedger() {
    return "acc/clientsLedger";
  }

  // 잔액 조건 전체 조회
  @ResponseBody
  @GetMapping("/cl/list")
  public List<EntryMasterDTO> getList( @RequestParam String startDate,
                                       @RequestParam String endDate,
                                       @RequestParam(required = false) String accountCode,
                                       @RequestParam(required = false) String partnerCode) {
    return clientsLedgerService.getList(startDate, endDate, accountCode, partnerCode);
  }

  // 내용 조건 전체 조회
  @ResponseBody
  @GetMapping("/cl/listDetail")
  public List<EntryMasterDTO> getListDetail( @RequestParam String startDate,
                                             @RequestParam String endDate,
                                             @RequestParam String accountCode,
                                             @RequestParam String partnerCode) {
    return clientsLedgerService.getListDetail(startDate, endDate, accountCode, partnerCode);
  }
}
