package com.eflix.hr.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eflix.acc.controller.AccAccountController;
import com.eflix.bsn.controller.OrdersController;
import com.eflix.bsn.service.impl.OrdersServiceImpl;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.purchs.controller.OutboundController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/hr/dept")
public class DeptRestController {

    private final OutboundController outboundController;

    private final AccAccountController accAccountController;

    private final OrdersController ordersController;

    private final OrdersServiceImpl ordersServiceImpl;

    @Autowired
    private DepartmentService departmentService;

    DeptRestController(OrdersServiceImpl ordersServiceImpl, OrdersController ordersController, AccAccountController accAccountController, OutboundController outboundController) {
        this.ordersServiceImpl = ordersServiceImpl;
        this.ordersController = ordersController;
        this.accAccountController = accAccountController;
        this.outboundController = outboundController;
    }

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }
    
    @GetMapping("/list/up")
    public ResponseEntity<ResResult> getUpList() {
        ResResult result = null;

        List<DepartmentDTO> list = departmentService.findUpAllByCoIdx(getCoIdx());

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", "");
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/down")
    public ResponseEntity<ResResult> getDownList(@RequestParam("deptUpIdx") String deptUpIdx) {
        ResResult result = null;
        
        List<DepartmentDTO> list = departmentService.findDownAllByCoIdx(getCoIdx(), deptUpIdx);

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", "");
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/all")
    public List<DepartmentDTO> getUpList(@RequestParam("coIdx") String coIdx) {
        return departmentService.findAllDepts(coIdx);
    }
    
    // 0714
    @GetMapping("/list")
    public ResponseEntity<ResResult> list() {
        ResResult result = null;

        List<DepartmentDTO> list = departmentService.findAllDepartmentWithEmpCountByCoIdx(getCoIdx());

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", "null");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResResult> insert(@RequestBody DepartmentDTO departmentDTO) {
        ResResult result = null;

        departmentDTO.setCoIdx(getCoIdx());
        int affectedRows = departmentService.insert(departmentDTO);
        
        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "부서를 등록하는 과정 중 오류가 발생했습니다.", "null");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update/{deptIdx}")
    public ResponseEntity<ResResult> update(@PathVariable String deptIdx, @RequestBody DepartmentDTO departmentDTO) {
        ResResult result = null;

        departmentDTO.setCoIdx(deptIdx);
        departmentDTO.setDeptIdx(deptIdx);
        
        int affectedRows = departmentService.update(departmentDTO);
        
        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "부서를 수정하는 과정 중 오류가 발생했습니다.", "null");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<ResResult> delete(@RequestBody List<String> deptIdxList) {
        ResResult result = null;

        int affectedRows = departmentService.deleteDepartments(deptIdxList);

        if(affectedRows == deptIdxList.size()) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else if (affectedRows > 0 && affectedRows < deptIdxList.size()) {
            result = ResUtil.makeResult("400", "일부 부서를 삭제하던 중 오류가 발생했습니다.", null);
        } else if (affectedRows <= 0) {
            result = ResUtil.makeResult("400", "부서를 삭제하는 과정 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
