package com.eflix.mgr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.mgr.dto.etc.AccDTO;
import com.eflix.mgr.dto.etc.BsnDTO;
import com.eflix.mgr.dto.etc.HrDTO;
import com.eflix.mgr.dto.etc.PurchsDTO;
import com.eflix.mgr.dto.etc.SummaryDTO;
import com.eflix.mgr.service.SummaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/erp/dashboard")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @GetMapping("/summary")
    public ResponseEntity<ResResult> summary() {
        ResResult result = null;

        SummaryDTO summaryDTO = summaryService.getSummary();

        if (summaryDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, summaryDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/hr")
    public ResponseEntity<ResResult> hr() {
        ResResult result = null;

        HrDTO hrDTO = summaryService.getHr();

        if (hrDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, hrDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/acc")
    public ResponseEntity<ResResult> acc() {
        ResResult result = null;

        AccDTO accDTO = summaryService.getAcc();

        if (accDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, accDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/purchs")
    public ResponseEntity<ResResult> purchs() {
        ResResult result = null;

        PurchsDTO purchsDTO = summaryService.getPurchs();

        if (purchsDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, purchsDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/bsn")
    public ResponseEntity<ResResult> bsn() {
        ResResult result = null;

        BsnDTO bsnDTO = summaryService.getBsn();

        if (bsnDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, bsnDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
