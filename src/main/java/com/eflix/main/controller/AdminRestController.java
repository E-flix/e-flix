package com.eflix.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.etc.CompanySearchDTO;
import com.eflix.main.dto.etc.UserSearchDTO;
import com.eflix.main.service.CompanyService;
import com.eflix.main.service.InquiryService;
import com.eflix.main.service.UserService;

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

    @GetMapping("/api/users")
    public ResponseEntity<ResResult> users(@ModelAttribute UserSearchDTO userSearchDTO) {

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

    @GetMapping("/api/companies")
    public ResponseEntity<ResResult> companies(@ModelAttribute CompanySearchDTO companySearchDTO) {
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

    @GetMapping("/api/inquirys")
    public ResponseEntity<ResResult> getMethodName(@RequestParam String param) {
        ResResult result = null;

        int inquiryCount = 0;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
