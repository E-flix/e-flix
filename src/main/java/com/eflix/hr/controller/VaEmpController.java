package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.AttendanceRequestDTO;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaSummaryDTO;
import com.eflix.hr.service.LeaveRequestService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/hr/va/emp")
public class VaEmpController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Value("${upload.path}")
    private String path;

    private String getEmpIdx() {
        return AuthUtil.getEmpIdx();
    }

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @PostMapping("/req")
    public ResponseEntity<ResResult> postMethodName(
        @RequestPart("reqData") VaDTO vaDTO,
        @RequestPart(value = "reqFile", required = false) MultipartFile reqFile) throws IllegalStateException, IOException {
        ResResult result = null;

        if (reqFile != null) {
            String uploadDir = path + "/hr/attd/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(uploadDir + reqFile.getOriginalFilename());
            reqFile.transferTo(dest);

            vaDTO.setAttFile(reqFile.getOriginalFilename());
        }

        vaDTO.setCoIdx(getCoIdx());
        vaDTO.setEmpIdx(getEmpIdx());

        int affectedRows = leaveRequestService.insert(vaDTO);

        if (affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "등록 과정에서 오류가 발생했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<ResResult> summary() {
        ResResult result = null;

        VaSummaryDTO summaryDTO = leaveRequestService.findSummaryByEmpIdxWithCoIdx(getEmpIdx(), getCoIdx());

        if(summaryDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, summaryDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/record")
    public ResponseEntity<ResResult> record() {
        ResResult result = null;

        List<VaDTO> list = leaveRequestService.findAllByEmpIdxWithCoIdx(getEmpIdx(), getCoIdx());

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
