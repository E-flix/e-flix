package com.eflix.bsn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.bsn.service.QuotationService;

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

  //영업 관리 메인 
  @GetMapping()
  public String bsnMain(){
    return "bsn/bsnMain";
  }

  //견적서 조회
  @GetMapping("/qot_list")
  public String quotation_list(Model model){
    model.addAttribute("quotationList", quotationService.getQuotationList());
    return "bsn/quotation_list";
  }

  //견적서 등록
  @GetMapping("/qot")
  public String quotation(){
    return "bsn/quotation";
  }

  //주문서 조회
  @GetMapping("/sorlist")
  public String salesorder_list(){
    return "bsn/salesorder_list";
  }

  //주문서 등록
  @GetMapping("/sorder")
  public String salesorder(){
    return "bsn/salesorder";
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

