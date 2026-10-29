package com.xiaohucare.third.dahua.model.brm.person;

import lombok.Data;

import java.util.List;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-20 19:39
 * @Description: 人员信息分页查询接口请求参数
 */
@Data
public class PersonPageRequest {

    /** 当前页, 默认1 */
    private Integer pageNum=1;
    /** 每页记录数, 默认10 */
    private Integer pageSize=10;
    /** 部门id */
    private Integer departmentId;
    /** 是否包含子部门 */
    private Boolean isContain;
    /** 查询字段，姓名、证件号、手机号、人员编码模糊匹配 */
    private String searchKey;
    /** 证件类型筛选 */
    private List<String> paperTypeList;
    /** 人员类型筛选 */
    private List<Integer> personTypeList;
    /** 民族筛选 */
    private List<Integer> nationList;
    /** 人员状态 */
    private List<Integer> statusList;
    /** 性别 */
    private List<Integer> sexList;
    /** 人员身份 */
    private Long personIdentityId;
    /** 1-指纹 2-红外/9421d人像/人像特征值 无此参数只显示人像照片 选择多项时用逗号连接 例如 1,2 */
    private String biometricType;
    /** 人员ID列表 */
    private List<Long> personIdList;
    /** 人员姓名列表(精确匹配) */
    private List<String> nameList;
    /** 证件号码(精确匹配) */
    private List<String> paperNumberList;

}
