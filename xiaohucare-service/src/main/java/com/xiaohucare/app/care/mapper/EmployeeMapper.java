package com.xiaohucare.app.care.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohucare.app.care.entity.EmployeeEntity;
/**
 * 员工信息表
 * 
 * @author xiaohucare
 * @date 2026-04-14 21:23:43
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {
}
