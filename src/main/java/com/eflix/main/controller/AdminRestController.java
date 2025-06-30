package com.eflix.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.dto.AnswerDTO;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.InquiryDTO;
import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.SubscriptionBillDTO;
import com.eflix.main.dto.etc.CompanySearchDTO;
import com.eflix.main.dto.etc.CompanySubscriptionDTO;
import com.eflix.main.dto.etc.InquirySearchDTO;
import com.eflix.main.dto.etc.SubscriptionInfoDTO;
import com.eflix.main.dto.etc.UserSearchDTO;
import com.eflix.main.service.CompanyService;
import com.eflix.main.service.InquiryService;
import com.eflix.main.service.SubscriptionService;
import com.eflix.main.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


// 최초 생성 6 27

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private InquiryService inquiryService;

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * 회사 상세 조회
     * GET /api/companies/{coIdx}
     */
    // @GetMapping("/api/company/{coIdx}")
    // public ResponseEntity<ResResult> getCompanyDetail(@PathVariable("coIdx") String coIdx) {
    //     ResResult result = null;

    //     CompanyDTO companyDTO = companyService.findByCoIdx(coIdx);

    //     if(companyDTO != null) {
    //         result = ResUtil.makeResult(ResStatus.OK, companyDTO);
    //     } else {
    //         result = ResUtil.makeResult("404", "회사 정보를 불러 오던 중 오류가 발생했습니다.", null);
    //     }

    //     return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    /**
     * 회사 + 구독 조회
     * GET /api/companies/{coIdx}
     */
    @GetMapping("/api/company-subscription")
    public ResponseEntity<ResResult> getCompanySbuscription() {
        ResResult result = null;

        List<CompanySubscriptionDTO> companySubscriptionDTOs = companyService.findAllCompanyWithSubscription();

        if(companySubscriptionDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, companySubscriptionDTOs);
        } else {
            result = ResUtil.makeResult("404", "회사 정보를 불러 오던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 구독 싱세 조회
     * GET /api/subscription
     */
    @GetMapping("/api/subscription")
    public ResponseEntity<ResResult> getSubscription(@RequestParam("coIdx") String coIdx) {
        ResResult result = null;

        List<SubscriptionInfoDTO> subscriptionDTOs = subscriptionService.findAllSubscriptionByCoIdx(coIdx);

        if(subscriptionDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, subscriptionDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 영수증 조회
     * GET /api/subscription-bill
     */
    @GetMapping("/api/subscription-bill")
    public ResponseEntity<ResResult> getSubscriptionBill(@RequestParam("spiIdx") String spiIdx) {
        ResResult result = null;

        List<SubscriptionBillDTO> billDTOs = subscriptionService.findAllSubscriptionBillByCoIdx(spiIdx);

        if(billDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, billDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 유저 목록 조회
     * GET /api/users
     */
    @GetMapping("/api/users")
    public ResponseEntity<ResResult> getUsers(@ModelAttribute UserSearchDTO userSearchDTO) {

        ResResult result = null;

        int userCount = userService.findAllUsersCount(userSearchDTO);

        userSearchDTO.setTotalRecord(userCount);

        List<UserDTO> userDTO = userService.findAllUsers(userSearchDTO);

        if(userDTO != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("users", userDTO);
            searchResult.put("total", userCount);
            searchResult.put("page", userSearchDTO.getPage());
            searchResult.put("startPage", userSearchDTO.getStartPage());
            searchResult.put("endPage", userSearchDTO.getEndPage());
            searchResult.put("lastPage", userSearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 회사 목록 조회
     * GET /api/companies
     */
    @GetMapping("/api/companies")
    public ResponseEntity<ResResult> getCompanies(@ModelAttribute CompanySearchDTO companySearchDTO) {
        ResResult result = null;

        int companyCount = companyService.findAllCompanyCount(companySearchDTO);

        companySearchDTO.setTotalRecord(companyCount);

        List<CompanyDTO> companyDTO = companyService.findAllCompany(companySearchDTO);

        if(companyDTO != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("companies", companyDTO);
            searchResult.put("total", companyCount);
            searchResult.put(("page"), companySearchDTO.getPage());
            searchResult.put("startPage", companySearchDTO.getStartPage());
            searchResult.put("endPage", companySearchDTO.getEndPage());
            searchResult.put("lastPage", companySearchDTO.getLast());

            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 문의 목록 조회
     * GET /api/inquirys
     */
    @GetMapping("/api/inquirys")
    public ResponseEntity<ResResult> getInquiry(@ModelAttribute InquirySearchDTO inquirySearchDTO) {
        ResResult result = null;

        int inquiryCount = inquiryService.findAllInquiryCount(inquirySearchDTO);

        inquirySearchDTO.setTotalRecord(inquiryCount);

        List<InquiryDTO> inquiryDTO = inquiryService.findAllInquiry(inquirySearchDTO);

        if(inquiryDTO != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("inquirys", inquiryDTO);
            searchResult.put("total", inquiryCount);
            searchResult.put(("page"), inquirySearchDTO.getPage());
            searchResult.put("startPage", inquirySearchDTO.getStartPage());
            searchResult.put("endPage", inquirySearchDTO.getEndPage());
            searchResult.put("lastPage", inquirySearchDTO.getLast());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 문의 상세 조회
     * GET /api/inquiry-detail
     */
    @GetMapping("/api/inquiry-detail")
    public ResponseEntity<ResResult> getInquiryDetail(@RequestParam String qstIdx) {
        ResResult result = null;

        QuestionDTO questionDTO = inquiryService.findQstByIdx(qstIdx);
        AnswerDTO answerDTO = inquiryService.findAnsByQstIdx(qstIdx);

        if(questionDTO != null) {
            Map<String, Object> inquiryData = new HashMap<>();

            inquiryData.put("question", questionDTO);
            inquiryData.put("answer", answerDTO);

            result = ResUtil.makeResult(ResStatus.OK, inquiryData);
        } else {
            result = ResUtil.makeResult("404", "존재하지 않는 문의 입니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 문의 답변 등록
     * GET /api/companies
     */
    @PostMapping("/api/inquiry-answer")
    public ResponseEntity<ResResult> postAnswer(@RequestBody AnswerDTO answerDTO, @AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        ResResult result = null;

        answerDTO.setEmpIdx("emp-101");

        int affectedRows = inquiryService.insertAns(answerDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "답변 등록 중 오류가 발생했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
