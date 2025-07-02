package com.eflix.common.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.eflix.acc.controller.AccAccountController;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.common.security.mapper.SecurityMapper;
import com.eflix.main.service.UserService;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityMapper securityMapper;

    LoginController(AccAccountController accAccountController) {
        this.accAccountController = accAccountController;
    }
    
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ResResult> postMethodName(@RequestBody UserDTO userDTO, RedirectAttributes rttr) {
        //TODO: process POST request

        ResResult result;
        
        userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));

        int affectedRow = userService.insertUser(userDTO);

        if(affectedRow > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "회원가입에 실패했습니다.", null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/erp/login")
    public String erpLogin() {
        return "pages/account/login";
    }
    

    // @PostMapping("/erp/login")
    // @ResponseBody
    // public ResponseEntity<ResResult> erpLogin(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
    //     ResResult result = null;

    //     String username = loginData.get("username");
    //     String password = loginData.get("password");
    //     String coIdx = loginData.get("co_idx");
    //     String masterChecked = loginData.get("masterCheck");

    //     UsernamePasswordAuthenticationToken token =
    //             new UsernamePasswordAuthenticationToken(username, password);

    //     try {
    //         request.setAttribute("co_idx", coIdx);
    //         request.setAttribute("masterChecked", masterChecked);

    //         Authentication authResult = authenticationManager.authenticate(token);
    //         SecurityContextHolder.getContext().setAuthentication(authResult);

    //         // Principal 객체 확인
    //         Object principal = authResult.getPrincipal();
    //         log.info("Principal: {}", principal);

    //         // 권한 확인
    //         Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
    //         log.info("권한 목록: {}", authorities);
    //         log.info("인증 여부: {}", authResult.isAuthenticated());
    //         log.info("전체 Authentication: {}", authResult);

    //         SecurityUserDetails userDetails = (SecurityUserDetails) authResult.getPrincipal();

    //         log.info("로그인된 사용자 ID: {}", userDetails.getUsername());
    //         log.info("권한 목록: {}", userDetails.getAuthorities());
    //         log.info("마스터 여부: {}", userDetails.getSecurityMasterDTO() != null);
    //         log.info("사원 여부: {}", userDetails.getSecurityEmpDTO() != null);
    //         log.info("메인 사용자 여부: {}", userDetails.getUserDTO() != null);
    //         log.info("전체 정보: {}", userDetails);

    //         Map<String, Object> response = new HashMap<>();
    //         response.put("username", userDetails.getUsername());
    //         response.put("role", userDetails.getAuthorities());
    //         response.put("redirectUrl", userDetails.getSecurityMasterDTO() != null ? "/erp" :
    //                                     userDetails.getSecurityEmpDTO() != null ? "/erp" : "/");

    //         result = ResUtil.makeResult(ResStatus.OK, response);

    //         return new ResponseEntity<>(result, HttpStatus.OK);

    //     } catch (BadCredentialsException e) {
    //         result = ResUtil.makeResult("401", "아이디 또는 비밀번호 오류", e.getMessage());
    //         return new ResponseEntity<>(result, HttpStatus.OK);
    //     } catch (Exception e) {
    //         result = ResUtil.makeResult("500", "서버 오류", e.getMessage());
    //         return new ResponseEntity<>(result, HttpStatus.OK);
    //     }
    // }

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
