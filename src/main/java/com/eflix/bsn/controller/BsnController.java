package com.eflix.bsn.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.CustomerService;
import com.eflix.bsn.service.ItemService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.bsn.service.QuotationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** ============================================
 *  영업(BSN) 모듈 컨트롤러
 * ============================================ */
@Slf4j
@Controller
@RequestMapping("/bsn")
@RequiredArgsConstructor
public class BsnController {

    /* ────────── 의존 서비스 ────────── */
    private final QuotationService quotationService;
    private final OrdersService    ordersService;
    private final CreditService    creditService;
    private final CustomerService  customerService;
    private final ItemService      itemService;
    private final ObjectMapper     objectMapper;

    /*──────────── 1. 메인 ────────────*/
    @GetMapping
    public String bsnMain() { return "bsn/bsnMain"; }

    /*──────────────────────────────
     * 2. 견적서 영역
     *──────────────────────────────*/
    @GetMapping("/qot_list")
    public String quotationList(Model model) {
        try {
            model.addAttribute("quotationList", quotationService.getQuotationList());
        } catch (Exception e) {
            log.error("견적서 조회 오류", e);
            model.addAttribute("quotationList", new ArrayList<>());
        }
        return "bsn/quotation_list";
    }

    @GetMapping("/quotation/details")
    @ResponseBody
    public List<QuotationDetailDTO> quotationDetails(@RequestParam String quotationNo) {
        try {
            return quotationService.getQuotationDetails(quotationNo);
        } catch (Exception e) {
            log.error("견적서 상세 조회 오류: {}", quotationNo, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/qot")
    public String quotationForm(Model model) {
        try {
            String nextNo = quotationService.generateNextQuotationNo();
            QuotationDTO dto = new QuotationDTO();
            dto.setQuotationNo(nextNo);
            dto.setDetails(List.of(new QuotationDetailDTO()));
            model.addAttribute("nextQuotationNo", nextNo);
            model.addAttribute("quotation", dto);
            return "bsn/quotation";
        } catch (Exception e) {
            log.error("견적서 입력 화면 오류", e);
            return "redirect:/bsn";
        }
    }

    @PostMapping("/createQuotation")
    public String createQuotation(@ModelAttribute QuotationDTO quotation) {
        try {
            quotationService.createQuotation(quotation);
            return "redirect:/bsn/qot_list";
        } catch (Exception e) {
            log.error("견적서 저장 오류", e);
            return "redirect:/bsn/qot";
        }
    }

    /*──────────────────────────────
     * 3. 주문서 영역
     *──────────────────────────────*/
    @GetMapping("/order_management")
    public String orderManagement() { return "bsn/salesorder_management"; }

    /* ───── REST: 헤더 목록 ───── */
    /** ✅ 헤더 그리드(신규) */
    @GetMapping("/orders")
    @ResponseBody
    public List<OrdersDTO> ordersList() {
        try { return ordersService.getOrdersList(); }
        catch (Exception e) {
            log.error("주문 목록 조회 오류", e);
            return new ArrayList<>();
        }
    }
    /** ✅ 헤더 그리드(legacy 경로) */
    @GetMapping("/sorlist/data")
    @ResponseBody
    public List<OrdersDTO> ordersListLegacy() {
        return ordersList();   // 동일 서비스 호출
    }

    /* ───── REST: 단건 헤더 ───── */
    @GetMapping("/orders/{orderNo}")
    @ResponseBody
    public OrdersDTO orderHeader(@PathVariable String orderNo) {
        try { return ordersService.getOrder(orderNo); }
        catch (Exception e) {
            log.error("주문 헤더 조회 오류: {}", orderNo, e);
            return new OrdersDTO();
        }
    }

    /* ───── REST: 디테일 목록 ───── */
    @GetMapping("/orders/{orderNo}/details")
    @ResponseBody
    public List<OrdersDetailDTO> orderDetails(@PathVariable String orderNo) {
        try { return ordersService.getOrderDetails(orderNo); }
        catch (Exception e) {
            log.error("주문 디테일 조회 오류: {}", orderNo, e);
            return new ArrayList<>();
        }
    }

    /* ───── 번호 채번 ───── */
    @GetMapping("/orders/nextOrderNo")
    @ResponseBody
    public String nextOrderNo() {
        try { return ordersService.generateNextOrderNo(); }
        catch (Exception e) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "SO-" + today + "-0001";
        }
    }

    /* ───── 저장 / 삭제 ───── */
    @PostMapping("/orders")
    @ResponseBody
    public Map<String,Object> saveOrder(@RequestBody OrdersDTO dto) {
        Map<String,Object> res = new HashMap<>();
        try {
            res.put("orderNo", ordersService.saveOrder(dto));
            res.put("success", true);
        } catch (Exception e) {
            log.error("주문 저장 오류", e);
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    @DeleteMapping("/orders/{orderNo}")
    @ResponseBody
    public Map<String,Object> deleteOrder(@PathVariable String orderNo) {
        Map<String,Object> res = new HashMap<>();
        try {
            ordersService.deleteOrder(orderNo);
            res.put("success", true);
        } catch (Exception e) {
            log.error("주문 삭제 오류: {}", orderNo, e);
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    /*──────────────────────────────
     * 4. 거래처 / 품목 / 여신
     *──────────────────────────────*/
    @GetMapping("/customer/info")
    @ResponseBody
    public CustomerDTO customerInfo(@RequestParam String customerCd){
        try { return customerService.getCustomerInfo(customerCd); }
        catch(Exception e){ log.error("거래처 정보 오류: {}", customerCd, e); return new CustomerDTO(); }
    }

    @GetMapping("/customer/detail")
    @ResponseBody
    public Map<String,Object> customerWithCredit(@RequestParam String customerCd){
        try { return customerService.getCustomerWithCredit(customerCd); }
        catch(Exception e){ log.error("거래처+여신 오류: {}", customerCd, e); return new HashMap<>(); }
    }

    // BsnController.java
    @GetMapping("/customer/{customerCd}/credit")
    @ResponseBody
    public CreditInfoDTO creditInfo(@PathVariable String customerCd){
        return creditService.getCreditInfo(customerCd);
    }

    @GetMapping("/customer/search")
    @ResponseBody
    public List<CustomerDTO> customerSearch(@RequestParam String keyword){
        try {
            return keyword!=null && keyword.trim().length()>=2
                  ? customerService.searchCustomers(keyword.trim())
                  : new ArrayList<>();
        } catch(Exception e){
            log.error("거래처 검색 오류: {}", keyword, e); return new ArrayList<>();
        }
    }

    @GetMapping("/item/info")
    @ResponseBody
    public BsnItemDTO itemInfo(@RequestParam String code){
        try { return Optional.ofNullable(itemService.getItemByCode(code)).orElseGet(BsnItemDTO::new); }
        catch(Exception e){ log.error("품목 정보 오류: {}", code, e); return new BsnItemDTO(); }
    }

    @GetMapping("/item/search")
    @ResponseBody
    public List<BsnItemDTO> itemSearch(@RequestParam String keyword){
        try {
            return keyword!=null && keyword.trim().length()>=2
                   ? itemService.searchItemsByName(keyword.trim())
                   : new ArrayList<>();
        } catch(Exception e){
            log.error("품목 검색 오류: {}", keyword, e); return new ArrayList<>();
        }
    }

    /*──────────────────────────────
     * 5. 출고 뷰(임시)
     *──────────────────────────────*/
    @GetMapping("/obound_list")
    public String outboundList(){ return "bsn/soutbound_list"; }

    @GetMapping("/obound")
    public String outboundForm(){ return "bsn/soutbound"; }
}
