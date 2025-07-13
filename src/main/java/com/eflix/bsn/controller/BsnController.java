package com.eflix.bsn.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.CustomerService;
import com.eflix.bsn.service.ItemService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.bsn.service.QuotationService;
import com.eflix.bsn.service.SOutboundService;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.common.security.dto.SecurityEmpDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** ============================================
 * 영업(BSN) 모듈 컨트롤러
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
    private final SOutboundService sOutboundService;
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

    @GetMapping("/quotation/list")
    @ResponseBody
    public List<QuotationDTO> quotationListJson() {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 목록 JSON 조회 시작 - 회사: {}", coIdx);
            List<QuotationDTO> quotations = quotationService.getQuotationList();
            log.info("견적서 목록 조회 완료 - 회사: {}, 건수: {}", coIdx, quotations.size());
            if (!quotations.isEmpty()) {
                QuotationDTO first = quotations.get(0);
                log.info("첫 번째 견적서 샘플: quotationNo={}, customerName={}, customerCd={}", 
                        first.getQuotationNo(), first.getCustomerName(), first.getCustomerCd());
            }
            return quotations;
        } catch (Exception e) {
            log.error("견적서 목록 JSON 조회 오류 - 회사: {}", coIdx, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/quotation/details")
    @ResponseBody
    public List<QuotationDetailDTO> quotationDetails(@RequestParam String quotationNo) {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 상세 조회 시작 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            List<QuotationDetailDTO> details = quotationService.getQuotationDetails(quotationNo);
            log.info("견적서 상세 조회 완료 - 회사: {}, 견적서: {}, 상세건수: {}", 
                    coIdx, quotationNo, details.size());
            if (!details.isEmpty()) {
                QuotationDetailDTO first = details.get(0);
                log.info("첫 번째 상세 샘플: itemCode={}, itemName={}, qty={}, unitPrice={}", 
                        first.getItemCode(), first.getItemName(), first.getQty(), first.getUnitPrice());
            }
            return details;
        } catch (Exception e) {
            log.error("견적서 상세 조회 오류 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/quotation/search")
    @ResponseBody
    public List<QuotationDTO> searchQuotations(
        @RequestParam(required = false) String quotationNo,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String dateFrom,
        @RequestParam(required = false) String dateTo
    ) {
        try {
            List<QuotationDTO> allQuotations = quotationService.getQuotationList();
            return allQuotations.stream()
                .filter(q -> quotationNo == null || quotationNo.trim().isEmpty() 
                    || (q.getQuotationNo() != null && q.getQuotationNo().contains(quotationNo.trim())))
                .filter(q -> customerName == null || customerName.trim().isEmpty() 
                    || (q.getCustomerName() != null && q.getCustomerName().contains(customerName.trim())))
                .filter(q -> {
                    if (dateFrom == null || dateFrom.trim().isEmpty()) return true;
                    if (q.getQuotationDt() == null) return false;
                    try {
                        String qDateStr = q.getQuotationDt().toString().substring(0, 10);
                        return qDateStr.compareTo(dateFrom) >= 0;
                    } catch (Exception e) { return false; }
                })
                .filter(q -> {
                    if (dateTo == null || dateTo.trim().isEmpty()) return true;
                    if (q.getQuotationDt() == null) return false;
                    try {
                        String qDateStr = q.getQuotationDt().toString().substring(0, 10);
                        return qDateStr.compareTo(dateTo) <= 0;
                    } catch (Exception e) { return false; }
                })
                .toList();
        } catch (Exception e) {
            log.error("견적서 검색 오류", e);
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
            
            // 1. AuthUtil을 사용하여 로그인한 사원 정보 DTO를 가져옵니다.
            SecurityEmpDTO loginEmployee = AuthUtil.getLoginEmployeeDTO();
            
            // 2. 사원 정보가 있을 경우, QuotationDTO의 sender 필드에 직접 이름을 설정합니다.
            if (loginEmployee != null && loginEmployee.getEmpName() != null) {
                dto.setSender(loginEmployee.getEmpName());
            } else {
                // 로그인하지 않았거나 사원 정보가 없는 경우 기본값을 설정합니다.
                dto.setSender("담당자"); 
            }

            model.addAttribute("quotation", dto); // sender 필드가 채워진 DTO를 모델에 추가

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

    /**
     * 휴지통 견적서 목록 조회
     */
    @GetMapping("/quotation/trash")
    @ResponseBody
    public List<QuotationDTO> getTrashQuotations() {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("휴지통 견적서 목록 조회 - 회사: {}", coIdx);
            List<QuotationDTO> archivedList = quotationService.getArchivedQuotationList();
            log.info("휴지통 견적서 조회 완료 - 회사: {}, 건수: {}", coIdx, archivedList.size());
            return archivedList;
        } catch (Exception e) {
            log.error("휴지통 견적서 목록 조회 오류 - 회사: {}", coIdx, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 견적서 휴지통으로 이동
     */
    @PostMapping("/quotation/{quotationNo}/trash")
    @ResponseBody
    public Map<String, Object> moveQuotationToTrash(@PathVariable String quotationNo) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("견적서 휴지통 이동 요청 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            
            boolean success = quotationService.moveToTrash(quotationNo);
            
            if (success) {
                response.put("success", true);
                response.put("message", "견적서가 휴지통으로 이동되었습니다.");
                log.info("견적서 휴지통 이동 완료 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            } else {
                response.put("success", false);
                response.put("message", "견적서를 휴지통으로 이동할 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("견적서 휴지통 이동 실패 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            response.put("success", false);
            response.put("message", "휴지통 이동 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    /**
     * 견적서 복원
     */
    @PostMapping("/quotation/{quotationNo}/restore")
    @ResponseBody
    public Map<String, Object> restoreQuotation(@PathVariable String quotationNo) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("견적서 복원 요청 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            
            boolean success = quotationService.restoreQuotation(quotationNo);
            
            if (success) {
                response.put("success", true);
                response.put("message", "견적서가 복원되었습니다.");
                log.info("견적서 복원 완료 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            } else {
                response.put("success", false);
                response.put("message", "견적서를 복원할 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("견적서 복원 실패 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            response.put("success", false);
            response.put("message", "복원 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    /**
     * 견적서 완전 삭제
     */
    @DeleteMapping("/quotation/{quotationNo}/permanent")
    @ResponseBody
    public Map<String, Object> deleteQuotationPermanently(@PathVariable String quotationNo) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("견적서 완전 삭제 요청 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            
            boolean success = quotationService.deleteQuotationPermanently(quotationNo);
            
            if (success) {
                response.put("success", true);
                response.put("message", "견적서가 완전히 삭제되었습니다.");
                log.info("견적서 완전 삭제 완료 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            } else {
                response.put("success", false);
                response.put("message", "견적서를 삭제할 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("견적서 완전 삭제 실패 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    /**
     * 만료된 견적서 자동 아카이브 실행 (수동 실행용)
     */
    @PostMapping("/quotation/archive-expired")
    @ResponseBody
    public Map<String, Object> archiveExpiredQuotations() {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("수동 자동 아카이브 실행 - 회사: {}", coIdx);
            
            int archivedCount = quotationService.archiveExpiredQuotations();
            
            response.put("success", true);
            response.put("archivedCount", archivedCount);
            response.put("message", archivedCount + "건의 견적서가 휴지통으로 이동되었습니다.");
            
            log.info("수동 자동 아카이브 완료 - 회사: {}, 처리 건수: {}", coIdx, archivedCount);
        } catch (Exception e) {
            log.error("수동 자동 아카이브 실패 - 회사: {}", coIdx, e);
            response.put("success", false);
            response.put("message", "자동 아카이브 실행 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    /**
     * 휴지통 통계 정보 조회
     */
    @GetMapping("/quotation/trash/statistics")
    @ResponseBody
    public Map<String, Object> getTrashStatistics() {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("휴지통 통계 조회 - 회사: {}", coIdx);
            return quotationService.getTrashStatistics();
        } catch (Exception e) {
            log.error("휴지통 통계 조회 실패 - 회사: {}", coIdx, e);
            Map<String, Object> emptyStats = new HashMap<>();
            emptyStats.put("totalCount", 0);
            emptyStats.put("thisMonthCount", 0);
            return emptyStats;
        }
    }

    /*──────────────────────────────
     * 3. 주문서 영역
     *──────────────────────────────*/
    @GetMapping("/order_management")
    public String orderManagement() { return "bsn/salesorder_management"; }

    @GetMapping("/orders")
    @ResponseBody
    public List<OrdersDTO> ordersList() {
        try { return ordersService.getOrdersList(); }
        catch (Exception e) {
            log.error("주문 목록 조회 오류", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/sorlist/data")
    @ResponseBody
    public List<OrdersDTO> ordersListLegacy() {
        return ordersList();
    }

    @GetMapping("/orders/{orderNo}")
    @ResponseBody
    public OrdersDTO orderHeader(@PathVariable String orderNo) {
        try { return ordersService.getOrder(orderNo); }
        catch (Exception e) {
            log.error("주문 헤더 조회 오류: {}", orderNo, e);
            return new OrdersDTO();
        }
    }

    @GetMapping("/orders/{orderNo}/details")
    @ResponseBody
    public List<OrdersDetailDTO> orderDetails(@PathVariable String orderNo) {
        try { return ordersService.getOrderDetails(orderNo); }
        catch (Exception e) {
            log.error("주문 디테일 조회 오류: {}", orderNo, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/orders/nextOrderNo")
    @ResponseBody
    public String nextOrderNo() {
        try { return ordersService.generateNextOrderNo(); }
        catch (Exception e) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "SO-" + today + "-0001";
        }
    }

    @PostMapping("/orders")
    @ResponseBody
    public Map<String,Object> saveOrder(@RequestBody OrdersDTO dto) {
        Map<String,Object> res = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        try {
            log.info("주문서 저장 요청 - 회사: {}, 사원: {}, 주문번호: {}", coIdx, empIdx, dto.getOrderNo());
            log.info("요청 헤더: customerCd={}, orderWriter={}, orderDt={}", dto.getCustomerCd(), dto.getOrderWriter(), dto.getOrderDt());
            log.info("요청 디테일: {} 건", dto.getDetails() != null ? dto.getDetails().size() : 0);
            String orderNo = ordersService.saveOrder(dto);
            res.put("orderNo", orderNo);
            res.put("success", true);
            res.put("message", "주문서가 성공적으로 저장되었습니다.");
            log.info("주문서 저장 성공 - 회사: {}, 주문번호: {}", coIdx, orderNo);
        } catch (IllegalArgumentException e) {
            log.warn("주문서 저장 실패 (유효성 검증) - 회사: {}, 오류: {}", coIdx, e.getMessage());
            res.put("success", false);
            res.put("message", "입력 오류: " + e.getMessage());
            res.put("errorType", "VALIDATION_ERROR");
        } catch (RuntimeException e) {
            log.error("주문서 저장 실패 (비즈니스 로직) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "저장 오류: " + e.getMessage());
            res.put("errorType", "BUSINESS_ERROR");
        } catch (Exception e) {
            log.error("주문서 저장 실패 (시스템 오류) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "시스템 오류가 발생했습니다. 관리자에게 문의하세요.");
            res.put("errorType", "SYSTEM_ERROR");
            res.put("errorDetail", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return res;
    }

    @DeleteMapping("/orders/{orderNo}")
    @ResponseBody
    public Map<String,Object> deleteOrder(@PathVariable String orderNo) {
        Map<String,Object> res = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("주문서 삭제 요청 - 회사: {}, 주문번호: {}", coIdx, orderNo);
            ordersService.deleteOrder(orderNo);
            res.put("success", true);
            res.put("message", "주문서가 성공적으로 삭제되었습니다.");
            log.info("주문서 삭제 성공 - 회사: {}, 주문번호: {}", coIdx, orderNo);
        } catch (Exception e) {
            log.error("주문서 삭제 실패 - 회사: {}, 주문번호: {}, 오류: {}", coIdx, orderNo, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return res;
    }

    /*──────────────────────────────
     * 4. 거래처 / 품목 / 여신 (멀티테넌트 지원)
     *──────────────────────────────*/
    @GetMapping("/customer/info")
    @ResponseBody
    public CustomerDTO customerInfo(@RequestParam String customerCd){
        try { 
            return customerService.getCustomerInfo(customerCd); 
        }
        catch(Exception e){ 
            log.error("거래처 정보 오류 - 회사: {}, 거래처: {}", AuthUtil.getCoIdx(), customerCd, e); 
            return new CustomerDTO(); 
        }
    }

    @GetMapping("/customer/list")
    @ResponseBody
    public List<CustomerDTO> customerList(@RequestParam(required = false) String customerName) {
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("거래처 목록 조회 - 회사: {}, 검색어: {}", coIdx, customerName);
            if (customerName != null && !customerName.trim().isEmpty()) {
                return customerService.searchCustomersByName(customerName.trim());
            } else {
                return customerService.findAll();
            }
        } catch (Exception e) {
            log.error("거래처 목록 조회 오류 - 회사: {}, 검색어: {}", AuthUtil.getCoIdx(), customerName, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/customer/detail")
    @ResponseBody
    public Map<String,Object> customerWithCredit(@RequestParam String customerCd){
        try { 
            String coIdx = AuthUtil.getCoIdx();
            log.info("거래처+여신 조회 - 회사: {}, 거래처: {}", coIdx, customerCd);
            return customerService.getCustomerWithCredit(customerCd); 
        }
        catch(Exception e){ 
            log.error("거래처+여신 오류 - 회사: {}, 거래처: {}", AuthUtil.getCoIdx(), customerCd, e); 
            return new HashMap<>(); 
        }
    }

    @GetMapping("/customer/{customerCd}/credit")
    @ResponseBody
    public CreditInfoDTO creditInfo(@PathVariable String customerCd){
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("여신 정보 조회 - 회사: {}, 거래처: {}", coIdx, customerCd);
            return creditService.getCreditInfo(customerCd);
        } catch(Exception e) {
            log.error("여신 정보 오류 - 회사: {}, 거래처: {}", AuthUtil.getCoIdx(), customerCd, e);
            CreditInfoDTO defaultCredit = new CreditInfoDTO();
            defaultCredit.setCustomerCd(customerCd);
            defaultCredit.setCreditLimit(java.math.BigDecimal.ZERO);
            defaultCredit.setCreditUsed(java.math.BigDecimal.ZERO);
            defaultCredit.setCreditStatus("정상");
            return defaultCredit;
        }
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

    @GetMapping("/customers/search")
    @ResponseBody
    public List<CustomerDTO> customers(@RequestParam(required = false) String name) {
        return customerList(name);
    }

    @GetMapping("/item/info")
    @ResponseBody
    public BsnItemDTO itemInfo(@RequestParam String code){
        try { return Optional.ofNullable(itemService.getItemByCode(code)).orElseGet(BsnItemDTO::new); }
        catch(Exception e){ log.error("품목 정보 오류: {}", code, e); return new BsnItemDTO(); }
    }

    @GetMapping("/item/list")
    @ResponseBody
    public List<BsnItemDTO> itemList(@RequestParam(required = false) String itemName) {
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("품목 목록 조회 - 회사: {}, 검색어: {}", coIdx, itemName);
            if (itemName != null && !itemName.trim().isEmpty()) {
                return itemService.searchItemsByName(itemName.trim());
            } else {
                return itemService.getAllItems();
            }
        } catch (Exception e) {
            log.error("품목 목록 조회 오류 - 회사: {}, 검색어: {}", AuthUtil.getCoIdx(), itemName, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/items")
    @ResponseBody
    public List<BsnItemDTO> items(@RequestParam(required = false) String name) {
        return itemList(name);
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
     * 5. 디버깅 및 테스트용 API
     *──────────────────────────────*/
    @GetMapping("/debug/auth-info")
    @ResponseBody
    public Map<String,Object> getAuthInfo(){
        Map<String,Object> info = new HashMap<>();
        try {
            info.put("coIdx", AuthUtil.getCoIdx());
            info.put("empIdx", AuthUtil.getEmpIdx());
            info.put("userIdx", AuthUtil.getUserIdx());
            info.put("mstIdx", AuthUtil.getMstIdx());
            info.put("success", true);
            log.info("인증 정보 조회: {}", info);
        } catch (Exception e) {
            log.error("인증 정보 조회 실패", e);
            info.put("success", false);
            info.put("error", e.getMessage());
        }
        return info;
    }

    @GetMapping("/debug/quotation-raw")
    @ResponseBody
    public Map<String,Object> getQuotationRawData(){
        Map<String,Object> result = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 원시 데이터 조회 시작 - 회사: {}", coIdx);
            List<QuotationDTO> quotations = quotationService.getQuotationList();
            result.put("coIdx", coIdx);
            result.put("quotationCount", quotations.size());
            result.put("quotations", quotations);
            result.put("success", true);
            if (!quotations.isEmpty()) {
                result.put("firstQuotation", quotations.get(0));
            }
            log.info("견적서 원시 데이터 조회 완료 - 건수: {}", quotations.size());
        } catch (Exception e) {
            log.error("견적서 원시 데이터 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("coIdx", coIdx);
        }
        return result;
    }

    @GetMapping("/debug/quotation-detail-raw")
    @ResponseBody
    public Map<String,Object> getQuotationDetailRawData(@RequestParam String quotationNo){
        Map<String,Object> result = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 상세 원시 데이터 조회 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            List<QuotationDetailDTO> details = quotationService.getQuotationDetails(quotationNo);
            result.put("coIdx", coIdx);
            result.put("quotationNo", quotationNo);
            result.put("detailCount", details.size());
            result.put("details", details);
            result.put("success", true);
            log.info("견적서 상세 원시 데이터 조회 완료 - 건수: {}", details.size());
        } catch (Exception e) {
            log.error("견적서 상세 원시 데이터 조회 실패 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("coIdx", coIdx);
            result.put("quotationNo", quotationNo);
        }
        return result;
    }

    /*──────────────────────────────
     * 6. 출고 뷰 및 관리 영역
     *──────────────────────────────*/
    @GetMapping("/obound_list")
    public String outboundList(){ return "bsn/soutbound_list"; }

    /**
     * 출고 관리 페이지 뷰
     * GET /bsn/soutbound
     * ★★★ 오류 수정: 중복된 메소드를 모두 제거하고 이 하나만 남깁니다. ★★★
     */
    @GetMapping("/soutbound")
    public String soutboundPage(Model model) {
        model.addAttribute("outbound", new SalesOutboundDTO());
        return "bsn/soutbound";
    }

    @GetMapping("/soutbounds")
    @ResponseBody
    public List<SalesOutboundDTO> getOutboundList() {
        return sOutboundService.getOutboundList();
    }

    @GetMapping("/soutbounds/{outboundNo}/details")
    @ResponseBody
    public List<SoutboundDetailDTO> getOutboundDetails(@PathVariable String outboundNo) {
        return sOutboundService.getOutboundDetails(outboundNo);
    }

    @PostMapping("/soutbounds")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOutbound(@RequestBody SalesOutboundDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String outboundNo = sOutboundService.createOutbound(dto);
            response.put("success", true);
            response.put("outboundNo", outboundNo);
            response.put("message", "출하 의뢰서가 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("출고 등록 실패", e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/soutbounds/create-from-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOutboundFromOrder(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String orderNo = payload.get("orderNo");
        try {
            if (!StringUtils.hasText(orderNo)) {
                throw new IllegalArgumentException("주문 번호가 필요합니다.");
            }
            String outboundNo = sOutboundService.createOutboundFromOrder(orderNo);
            response.put("success", true);
            response.put("outboundNo", outboundNo);
            response.put("message", "주문서 기반 출하 의뢰서가 성공적으로 생성되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("주문서 기반 출고 생성 실패: {}", orderNo, e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/soutbounds/{outboundNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOutbound(
        @PathVariable String outboundNo,
        @RequestBody SalesOutboundDTO dto
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            dto.setOutboundNo(outboundNo);
            sOutboundService.updateOutbound(dto);
            response.put("success", true);
            response.put("message", "출하 의뢰서가 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("출고 수정 실패: {}", outboundNo, e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/soutbounds/{outboundNo}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteOutbound(@PathVariable String outboundNo) {
        Map<String, Object> response = new HashMap<>();
        try {
            sOutboundService.deleteOutbound(outboundNo);
            response.put("success", true);
            response.put("message", "출하 의뢰서가 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("출고 삭제 실패: {}", outboundNo, e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/soutbounds/stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getOutboundStats() {
        try {
            List<Map<String, Object>> stats = sOutboundService.getOutboundStatusStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("출고 통계 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /*──────────────────────────────
     * 7. 대시보드 및 리포트
     *──────────────────────────────*/
    @GetMapping("/dashboard")
    public String salesDashboard(Model model) {
        String coIdx = AuthUtil.getCoIdx();
        log.info("영업 대시보드 접근 - 회사: {}", coIdx);
        return "bsn/SalesDashboard";
    }

    @GetMapping("/analytics/customer")
    public String customerAnalytics() {
        return "bsn/analytics/customer";
    }

    @GetMapping("/analytics/product")
    public String productAnalytics() {
        return "bsn/analytics/product";
    }

    @GetMapping("/analytics/employee")
    public String employeeAnalytics() {
        return "bsn/analytics/employee";
    }

    @GetMapping("/analytics/trend")
    public String trendAnalytics() {
        return "bsn/analytics/trend";
    }

    @GetMapping("/reports/daily")
    public String dailyReport() {
        return "bsn/reports/daily";
    }

    @GetMapping("/reports/monthly") 
    public String monthlyReport() {
        return "bsn/reports/monthly";
    }

    @GetMapping("/reports/annual")
    public String annualReport() {
        return "bsn/reports/annual";
    }

    @GetMapping("/reports/custom")
    public String customReport() {
        return "bsn/reports/custom";
    }

    @GetMapping("/customer_search")
    public String customerSearch(@RequestParam String keyword, Model model) {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("거래처 검색 - 회사: {}, 키워드: {}", coIdx, keyword);
            List<CustomerDTO> customers = customerService.searchCustomers(keyword);
            model.addAttribute("customers", customers);
            model.addAttribute("keyword", keyword);
            return "bsn/customer_search_results";
        } catch (Exception e) {
            log.error("거래처 검색 실패 - 회사: {}, 키워드: {}", coIdx, keyword, e);
            model.addAttribute("customers", new ArrayList<>());
            model.addAttribute("keyword", keyword);
            model.addAttribute("error", "검색 중 오류가 발생했습니다.");
            return "bsn/customer_search_results";
        }
    }

    @GetMapping("/reports/today")
    public String todayReport(Model model) {
        String coIdx = AuthUtil.getCoIdx();
        try {
            model.addAttribute("reportDate", LocalDate.now());
            return "bsn/reports/today_popup";
        } catch (Exception e) {
            log.error("오늘 실적 리포트 조회 실패 - 회사: {}", coIdx, e);
            return "bsn/reports/today_popup";
        }
    }
}
