package com.eflix.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.main.service.MasterService;

@RestController
@RequestMapping("/master")
public class MasterController {

    @Autowired
    private MasterService masterService;
    
    // 0714
    @GetMapping("/exist")
    public ResponseEntity<ResResult> existMstId(@RequestParam("mstId") String mstId) {
        ResResult result = null;

        int existMstId = masterService.existMstId(mstId);

        if(existMstId > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "이미 존재하는 아이디 입니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
