package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.service.LeaveRequestService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/hr/va/mgr")
public class VaMgrController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }
    
    @GetMapping("/get")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/list")
    public ResponseEntity<ResResult> list(VaSearchDTO vaSearchDTO) {
        ResResult result = null;
        
        vaSearchDTO.setCoIdx(getCoIdx());
        
        List<VaDTO> list = leaveRequestService.findAllBySearch(vaSearchDTO);

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
