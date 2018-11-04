package com.huju.cache.service;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by huju on 2018/11/4.
 */

@Slf4j
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存,以后再要相同的数据,直接从缓存中获取,不用调用方法
     *
     * 几个属性：
     *      cacheNames/value：指定缓存组件的名字;将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存；
     *
     *      key：缓存数据使用的key；可以用它来指定。默认是使用方法参数的值  1-方法的返回值
     *              编写SpEL； #i d;参数id的值   #a0  #p0  #root.args[0]
     *              getEmp[2]
     *
     *      keyGenerator：key的生成器；可以自己指定key的生成器的组件id
     *              key/keyGenerator：二选一使用;
     *
     *
     *      cacheManager：指定缓存管理器；或者cacheResolver指定获取解析器
     *
     *      condition：指定符合条件的情况下才缓存；
     *              ,condition = "#id>0"
     *          condition = "#a0>1"：第一个参数的值》1的时候才进行缓存
     *
     *      unless:否定缓存；当unless指定的条件为true，方法的返回值就不会被缓存；可以获取到结果进行判断
     *              unless = "#result == null"
     *              unless = "#a0==2":如果第一个参数的值是2，结果不缓存；
     *      sync：是否使用异步模式
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = {"emp"},key = "#a0"/*keyGenerator = "myKeyGenerator",condition = "#a0>1"*/)
    public Employee getEmp(Integer id) {
        log.info("************ 根据id: {} 查询员工 ***********", id);
        Employee empById = employeeMapper.getEmpById(id);

        return empById;
    }

    /**
     * 更新缓存
     * @param employee
     * @return
     */
    @CachePut(value = "emp",key = "#result.id")
    public Employee updateEmp(Employee employee) {
        int i = employeeMapper.updateEmpById(employee);
        log.info("************** 成功更新 {} 条数据 *************", i);
        return employee;
    }

    /**
     * @CacheEvict：缓存清除
     *  key：指定要清除的数据
     *  allEntries = true：指定清除这个缓存中所有的数据
     *  beforeInvocation = false：缓存的清除是否在方法之前执行
     *      默认代表缓存清除操作是在方法执行之后执行;如果出现异常缓存就不会清除
     *
     *  beforeInvocation = true：
     *      代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     *
     * @param id
     */
    @CacheEvict(value = "emp",key = "#a0")
    public void deleteEmp(Integer id) {
        log.info("************ 删除员工,ID: {} ***********", id);
    }
}
