package com.eflix.mgr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.mgr.dto.UserMgrDTO;
import com.eflix.mgr.service.UserMgrService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@RestController
@RequestMapping("/mgr")
public class UserMgrRestController {

    @Autowired
    private UserMgrService userMgrService;
    
    @GetMapping("/api/users")
    public ResponseEntity<ResResult> getUser(@RequestParam("type") String type) {
        ResResult result = null;

        List<UserMgrDTO> userMgrDTOs = userMgrService.findAllUser();

        if(userMgrDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, userMgrDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/api/user")
    public ResponseEntity<ResResult> addUser(@RequestBody UserMgrDTO userMgrDTO) {
        ResResult result = null;

        int affectedRow = userMgrService.insertUser(userMgrDTO);

        if(affectedRow > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "등록에 실패했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/user")
    public ResponseEntity<ResResult> modifyUser(@RequestBody UserMgrDTO userMgrDTO) {
        ResResult result = null;

        int affectedRow = userMgrService.updateUser(userMgrDTO);

        if(affectedRow > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "수정에 실패했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
