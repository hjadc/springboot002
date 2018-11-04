package com.huju.cache.dao;

import com.huju.cache.entities.Employee;
import org.apache.ibatis.annotations.*;

/**
 * Created by huju on 2018/11/4.
 */
public interface EmployeeMapper {

    @Select("select * from employee where id = #{id}")
    public Employee getEmpById(Integer id);

    @Update("update employee set lastName = #{lastName},email=#{email},gender=#{gender},d_id=#{dId} where id = #{id}")
    public int updateEmpById(Employee employee);

    @Delete("delete from employee where id  = #{id}")
    public int deleteEmpById(Integer id);

    @Insert("insert into employee(lastName,email,gender,d_id) values (#{lastName},#{email},#{gender},#{dId},#{id})")
    public int insertEmp(Employee employee);
}
