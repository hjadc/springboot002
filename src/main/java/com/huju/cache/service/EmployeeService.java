package com.huju.cache.service;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huju on 2018/11/4.
 */

@Slf4j
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    public Employee getEmp(Integer id) {
        log.info("************ 根据id: {} 查询员工 ***********", id);
        Employee empById = employeeMapper.getEmpById(id);

        return empById;
    }
}
