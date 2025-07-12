package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaReqSummaryDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.service.LeaveRequestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/hr/va/mgr")
public class VaMgrController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }
    
    @GetMapping("/summary")
    public ResponseEntity<ResResult> getMethodName(VaSearchDTO vaSearchDTO) {
        ResResult result = null;

        vaSearchDTO.setCoIdx(getCoIdx());

        VaReqSummaryDTO vaReqSummaryDTO = leaveRequestService.findReqSummaryBySearch(vaSearchDTO);

        if(vaReqSummaryDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, vaReqSummaryDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/list")
    public ResponseEntity<ResResult> list(VaSearchDTO vaSearchDTO) {
        ResResult result = null;
        
        vaSearchDTO.setCoIdx(getCoIdx());

        int empCount = leaveRequestService.findCountBySearch(vaSearchDTO);

        vaSearchDTO.setTotalRecord(empCount);
        
        List<VaDTO> list = leaveRequestService.findAllBySearch(vaSearchDTO);

        if(list != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("emps", list);
            searchResult.put("total", empCount);
            searchResult.put("page", vaSearchDTO.getPage());
            searchResult.put("startPage", vaSearchDTO.getStartPage());
            searchResult.put("endPage", vaSearchDTO.getEndPage());
            searchResult.put("lastPage", vaSearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/detail/{leaveReqIdx}")
    public ResponseEntity<ResResult> detail(@PathVariable("leaveReqIdx") String leaveReqIdx) {
        ResResult result = null;

        VaDTO detail = leaveRequestService.findByLeaveReqIdx(leaveReqIdx);

        if(detail != null) {
            result = ResUtil.makeResult(ResStatus.OK, detail);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
