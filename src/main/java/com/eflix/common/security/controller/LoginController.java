package com.eflix.common.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.eflix.acc.controller.AccAccountController;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.main.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


/**
 * <p>
 * ERP 관리자 기능을 제공하는 컨트롤러입니다.
 * 회사 내부 관리자(Admin) 전용으로, 시스템 설정 및 사용자 관리, 조직 구성 관리,
 * 직원 계정 관리 등의 화면을 제공합니다.
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li></li>
 * </ul>
 *
 * @author 
 * @since 2025.06.20
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 *   <li>2025-06-24: 회원가입 로직 추가 (복성민)</li>
 * </ul>
 */

@Slf4j
@Controller
public class LoginController {

    private final AccAccountController accAccountController;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    LoginController(AccAccountController accAccountController) {
        this.accAccountController = accAccountController;
    }
    
    @PostMapping("/erp/signup")
    @ResponseBody
    public ResponseEntity<ResResult> postMethodName(@RequestBody UserDTO userDTO, RedirectAttributes rttr) {
        //TODO: process POST request

        ResResult result;

        System.out.println(userDTO.toString());
        
        userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));

        int affectedRow = userService.insertUser(userDTO);

        if(affectedRow > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "회원가입에 실패했습니다.", null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    // @PostMapping("path")
    // public String postMethodName(@RequestParam String company, @RequestParam String user_id, @RequestParam String user_pw, HttpServletRequest request) {
    //     //TODO: process POST request

    //     request.getS
        
    //     return entity;
    // }
    

    @GetMapping("/login")
    public String login() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "pages/account/login";
    }

    // @GetMapping("/logout")
    // public String erpLogout() {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    //     if(auth != null) {
    //         new SecurityContext
    //     }

    //     return "redirect:/erp";
    // }

    // @GetMapping("/erp/logout")
    // public String logout() {
    //     return "redirect:/";
    // }
}
