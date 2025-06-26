package com.eflix.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.erp.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/erp/user")
public class UserRestController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResResult> me(@AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        ResResult result = null;

        UserDTO dto = userService.findByUserIdx(securityUserDetails.getUserDTO().getUserIdx());

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
}
