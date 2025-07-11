package com.eflix.mgr.dto;

import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.main.dto.MasterDTO;

import lombok.Data;

@Data
public class UserMgrDTO {
    private MasterDTO masterDTO;
    private EmployeeDTO employeeDTO;
}
