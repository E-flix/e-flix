package com.eflix.hr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.eflix.common.security.dto.EmployeeDTO;

@Mapper
public interface EmployeeMapper {
    public EmployeeDTO findByEmpEmailAndCompany(String empEmail, String coIdx);
}
