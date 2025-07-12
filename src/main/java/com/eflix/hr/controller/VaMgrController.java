package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.BulkDTO;
import com.eflix.hr.dto.etc.LeaveRequestAppDTO;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaReqSummaryDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.service.LeaveRequestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/hr/va/mgr")
public class VaMgrController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    private String getEmpIdx() {
        return AuthUtil.getEmpIdx();
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

    @PutMapping("/approval/{leaveReqIdx}")
    public ResponseEntity<ResResult> putMethodName(@PathVariable("leaveReqIdx") String leaveReqIdx, @RequestBody LeaveRequestAppDTO leaveRequestAppDTO) {
        ResResult result = null;

        leaveRequestAppDTO.setApproverIdx(getEmpIdx());

        int affectedRows = leaveRequestService.insertReqApprover(leaveRequestAppDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "데이터를 반영하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @PostMapping("/bulk")
    public ResponseEntity<ResResult> accept(@RequestBody BulkDTO bulkDTO) {
        ResResult result = null;

        List<LeaveRequestAppDTO> list = new ArrayList<>();

        for(String idx: bulkDTO.getLeaveReqIdxs()) {
            LeaveRequestAppDTO leaveRequestAppDTO = new LeaveRequestAppDTO();
            leaveRequestAppDTO.setLeaveReqIdx(idx);
            leaveRequestAppDTO.setAbReason(bulkDTO.getAbReason());
            leaveRequestAppDTO.setApproverIdx(getEmpIdx());
            leaveRequestAppDTO.setStatus(bulkDTO.getType());
            list.add(leaveRequestAppDTO);
        }

        int affectedRows = leaveRequestService.insertReqApprover(list);
        
        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "데이터를 반영하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
