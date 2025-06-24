package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.purchs.service.InboundService;

import lombok.RequiredArgsConstructor;
/** ============================================
  - 작성자   : 이혁진
  - 최초작성 : 2025-06-23
  - 설명     : 입고 기능 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-23 (이혁진): 입고화면 컨트롤러 완성
============================================  */
@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class inboundController {
  private final InboundService inboundService;
  // 입고조회
  @GetMapping("/ibd")
  public String inbound(Model model) {
    model.addAttribute("ibdList", inboundService.getInbound());
    return "purchs/inbound";
  }
}