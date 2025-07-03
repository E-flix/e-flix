package com.eflix.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.service.CompanyService;
import com.eflix.main.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/user")
public class UserRestController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SubscriptionMapper subscriptionService;

    @GetMapping("/me")
    public ResponseEntity<ResResult> me(@AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        ResResult result = null;

        UserDTO dto = userService.findByUserIdx(securityUserDetails.getSecurityUserDTO().getUserIdx());

        if(dto != null) {
            result = ResUtil.makeResult(ResStatus.OK, dto);
        } else {
            result = ResUtil.makeResult("404", "사용자를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PutMapping("/me")
    public ResponseEntity<ResResult> update(@RequestBody UserDTO userDTO) {
        //TODO: process PUT request
        ResResult result = null;

        int affectedRows = userService.updateUser(userDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "사용자 정보를 수정 하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 0703
    @GetMapping("/api/service")
    public ResponseEntity<ResResult> getSubscription() {
        ResResult result = null;

        SubscriptionDTO subscriptionDTO = companyService.findSubscriptionByCoIdx(AuthUtil.getCoIdx());

        if(subscriptionDTO == null) {
            result = ResUtil.makeResult("404", "구독 정보가 없습니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        List<ModuleDTO> moduleList = subscriptionService.findAllModuleBySpiIdx(subscriptionDTO.getSpiIdx());
        if(moduleList == null) {
            result = ResUtil.makeResult("404", "모듈 정보가 없습니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        Map<String, Object> reusltData = new HashMap<>();
        reusltData.put("subscriptionDTO", subscriptionDTO);
        reusltData.put("moduleList", moduleList);

        result = ResUtil.makeResult(ResStatus.OK, reusltData);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
