package com.eflix.bsn.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.bsn.service.QuotationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/** ============================================
  - 작성자   : 이용진
  - 최초작성 : 2025-06-18
  - 설명     : 영업 컨트롤러
============================================  */
@Controller
@RequestMapping("/bsn")
@RequiredArgsConstructor
public class BsnController {

  private final QuotationService quotationService;
  private final OrdersService ordersService;
  private final CreditService creditService;
  private final ObjectMapper    objectMapper;

  //영업 관리 메인 
  @GetMapping()
  public String bsnMain(){
    return "bsn/bsnMain";
  }

  //견적서 조회
  @GetMapping("/qot_list")
  public String quotation_list(Model model) throws JsonProcessingException {
    var list = quotationService.getQuotationList();
    model.addAttribute("quotationList", list);
    return "bsn/quotation_list";
  }

  /** 견적서 상세(품목) 목록 */
  @GetMapping("/quotation/details")
  @ResponseBody
  public List<QuotationDetailDTO> getQuotationDetails(
      @RequestParam("quotationNo") String quotationNo) {
    return quotationService.getQuotationDetails(quotationNo);
  }


  //견적서 입력
  @GetMapping("/qot")
  public String quotation(Model model) {
    // 1) 다음 견적번호
    String nextNo = quotationService.generateNextQuotationNo();
    model.addAttribute("nextQuotationNo", nextNo);

    // 2) 빈 DTO + 한 건의 Detail 초기화
    QuotationDTO dto = new QuotationDTO();
    dto.setQuotationNo(nextNo);
    dto.setDetails(new ArrayList<>());
    dto.getDetails().add(new QuotationDetailDTO());

    model.addAttribute("quotation", dto);
    return "bsn/quotation";
  }

  /** 견적서 등록 처리 */
  @PostMapping("/createQuotation")
  public String createQuotation(@ModelAttribute QuotationDTO quotation) {
    quotationService.createQuotation(quotation);
    return "redirect:/bsn/qot_list";  // 등록 후 목록으로
  }

  //주문서 조회
  @GetMapping("/sorlist")

  public String salesorder_list(Model model) throws JsonProcessingException {
    var list = ordersService.getOrdersList();
    String json = new ObjectMapper().findAndRegisterModules().writeValueAsString(list);

    model.addAttribute("ordersList", json);
    return "bsn/salesorder_list";
  }

    /** 주문서 등록 화면 */
  @GetMapping("/sorder")
  public String salesorder(Model model) {
    // 1) 빈 DTO 세팅
    OrdersDTO order = new OrdersDTO();
    model.addAttribute("order", order);

    // 2) 여신 정보 조회 
    CreditInfoDTO credit = creditService.getCreditByCustomer(order.getCustomerCd());
    model.addAttribute("credit", credit);

    return "bsn/salesorder";
  }

  // 주문서 저장 처리
  @PostMapping("/sorder")
  public String createOrder(@ModelAttribute OrdersDTO order) {
    ordersService.createOrder(order);
    return "redirect:/bsn/sorlist";
  }

  //출고 조회
  @GetMapping("/obound_list")
  public String outbound_list(){
    return "bsn/soutbound_list";
  }

  //출고 의뢰
  @GetMapping("/obound")
  public String outbound(){
    return "bsn/soutbound";
  }
}

