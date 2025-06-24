package com.eflix.erp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.exception.SubscriptionException;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.erp.dto.SubscriptionDTO;
import com.eflix.erp.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/erp")
public class SubscriptionRestController {
    
    @Value("${file.path}")
    private String path;

    @Autowired
    private SubscriptionService service;

    @PostMapping("/contract")
    public ResponseEntity<ResResult> postMethodName(@RequestBody Map<String,String> data) {
        //TODO: process POST request
        ResResult result;

        String contractHTML = data.get("contractHTML");
        String contractName = data.get("contractName");

        // 경로 생성
        File directory = new File(path + "/contract");
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 파일 경로 설정 (파일명에 .html 확장자 포함)
        File htmlFile = new File(directory, contractName + ".html");

        try (FileWriter writer = new FileWriter(htmlFile)) {
            writer.write(contractHTML);

            log.info("Saved HTML to: " + htmlFile.getAbsolutePath());

            result = ResUtil.makeResult(ResStatus.OK, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = ResUtil.makeResult("400", "파일을 저장하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/subscription")
    public ResponseEntity<ResResult> postMethodName(@RequestBody SubscriptionDTO subscriptionDTO) {
        //TODO: process POST request
        ResResult result;
        try {
            service.insertSubscriptionInfo(subscriptionDTO);
            result = ResUtil.makeResult(ResStatus.OK, null);
        } catch (SubscriptionException e) {
            result = ResUtil.makeResult("400", e.getMessage(), null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
