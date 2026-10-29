package com.xiaohucare.third.dahua.model.brm.person;

import com.dahuatech.icc.oauth.http.IccResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-20 19:39
 * @Description: 人员信息分页查询接口返回参数
 */
@Data
public class PersonPageResponse extends IccResponse {
    /** 分页数据 */
    private PageVO data;

    @Data
    public static class PageVO{
        /** 当前页码 */
        private Integer currentPage;
        /** 总页数 */
        private Integer totalPage;
        /** 每页条数 */
        private Integer pageSize;
        /** 送条数 */
        private Integer totalRows;
        /** 人员数据列表 */
        private List<PersonInfoVO> pageData;

        @Data
        public static class PersonInfoVO{
            /** 人员id */
            private Long id;
            /** 人员姓名 */
            private String name;
            /** 人员编码 */
            private String code;
            /** 证件类型 */
            private Integer paperType;
            /** 证件号码 */
            private String paperNumber;
            /** 证件地址 */
            private String paperAddress;
            /** 部门ID */
            private Long departmentId;
            /** 部门名称 */
            private String departmentName;
            /** 电话 */
            private String phone;
            /** 电子邮箱 */
            private String email;
            /** 年龄 */
            private Integer age;
            /** 生日 */
            private Date birthday;
            /** 性别 */
            private String sex;
            /** 国家 */
            private String country;
            /** 民族 */
            private Integer nation;
            /** 民族名称 */
            private String nationName;
            /** 人员自编号 */
            private String selfCode;
            /** 人员状态 */
            private Integer status;
            /** 组织编码 */
            private String ownerCode;
            /** 权限有效期开始日期，日期格式：yyyy-MM-dd HH:mm:ss,5.0.15版本支持 */
            private String validStartTime;
            /** 权限有效期结束日期，日期格式：yyyy-MM-dd HH:mm:ss,5.0.15版本支持 */
            private String validEndTime;
            /** 所属部门列表,一人多部门场景使用：若departmentId、departmentList同时传值，以departmentId传参为准，该参数无效 */
            private List<Department> departmentList;
            /** 生物特征数据:人脸头像、人脸特征、指纹特征 */
            private List<PersonAddRequest.PersonBioSignature> personBiosignatures;
            /** 扩展属性值 */
            private PersonAddRequest.FieldExt fieldExt;

            @Data
            public static class Department{
                /** 部门ID */
                private Long departmentId;
                /** 部门类型 */
                private Long departmentType;
            }

            @Data
            public static class PersonBioSignature{
                /** 生物特征主键 */
                private Integer id;
                /** 生物特征类型 */
                private Integer type;
                /** 生物特征存储路径 */
                private Integer index;
                /** 同种生物特征数据索引 */
                private String path;
                /** 生物特征数据 */
                private String data;
            }

            @Data
            public static class FieldExt{
                /** 业务类型 */
                private String businessType;
                /** 自定义字段信息 key:value格式的数据 */
                private Map useFieldNames;
            }
        }
    }
}
