package com.eflix.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.etc.GradeSearchDTO;
import com.eflix.hr.service.GradeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/hr/grd")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @GetMapping("/list")
    public ResponseEntity<ResResult> list(GradeSearchDTO gradeSearchDTO) {
        ResResult result = null;
        
        gradeSearchDTO.setCoIdx(getCoIdx());
        
        int grdCount = gradeService.findAllGrdCount(gradeSearchDTO);

        gradeSearchDTO.setTotalRecord(grdCount);

        List<GradeDTO> list = gradeService.findAllGrdBySearch(gradeSearchDTO);

        if(list != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("grds", list);
            searchResult.put("total", grdCount);
            searchResult.put("page", gradeSearchDTO.getPage());
            searchResult.put("startPage", gradeSearchDTO.getStartPage());
            searchResult.put("pageSize", gradeSearchDTO.getPageUnit());
            searchResult.put("endPage", gradeSearchDTO.getEndPage());
            searchResult.put("lastPage", gradeSearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/insert")
    public ResponseEntity<ResResult> insert(@RequestBody GradeDTO gradeDTO) {
        ResResult result = null;

        gradeDTO.setCoIdx(getCoIdx());
        int affectedRows = gradeService.insert(gradeDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "직급 등록에 실패했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/detail/{grdIdx}")
    public ResponseEntity<ResResult> get(@PathVariable("grdIdx") String grdIdx) {
        ResResult result = null;

        GradeDTO gradeDTO = gradeService.findByGrdIdx(grdIdx);

        if(gradeDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, gradeDTO);
        } else {
            result = ResUtil.makeResult("404", "직급 삭제에 실패했습니다.", null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @PutMapping("/update/{grdIdx}")
    public ResponseEntity<ResResult> update(@PathVariable("grdIdx") String grdIdx, @RequestBody GradeDTO gradeDTO) {
        ResResult result = null;

        gradeDTO.setGrdIdx(grdIdx);
        System.out.println(gradeDTO.toString());

        int affectedRows = gradeService.updateByGrdIdx(grdIdx);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("404", "직급 삭제에 실패했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{grdIdx}")
    public ResponseEntity<ResResult> delete(@PathVariable("grdIdx") String grdIdx) {
        ResResult result = null;

        int affectedRows = gradeService.deleteByGrdIdx(grdIdx);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("404", "직급 삭제에 실패했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
