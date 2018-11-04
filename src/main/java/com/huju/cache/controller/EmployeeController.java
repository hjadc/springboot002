package com.huju.cache.controller;

import com.huju.cache.entities.Employee;
import com.huju.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huju on 2018/11/4.
 */

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee getEmp(@PathVariable("id") Integer id) {

        Employee employee = employeeService.getEmp(id);
        return employee;
    }
}
