package com.xiaohucare.third.dahua.model.brm.person;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * program:java-sdk-demo
 *
 * @author xiaohucare
 * @Date:2024-03-19 14:37
 * @Description: 人员添加请求参数
 */
@Data
public class PersonAddRequest {
  /** 子系统编码,默认evo-thirdParty */
  private String service="evo-thirdParty";
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
  /** 人员类型,默认0 */
  private Integer personType=0;
  /** 有效刷卡次数 仅来宾用户有效 */
  private int availableTimes;
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
  /** 组织编码 */
  private String ownerCode;
  /** 权限有效期开始日期，日期格式：yyyy-MM-dd HH:mm:ss,5.0.15版本支持 */
  private String validStartTime;
  /** 权限有效期结束日期，日期格式：yyyy-MM-dd HH:mm:ss,5.0.15版本支持 */
  private String validEndTime;
  /** 人员密码密文 */
  private String password;
  /** 私钥key值,根据获取RSA加密公钥接口返回的私钥key值字段key，配合password字段使用 */
  private String passwordKey;
  /** 生物特征数据:人脸头像、人脸特征、指纹特征 */
  private List<PersonBioSignature> personBiosignatures;
  /** 扩展属性值 */
  private FieldExt fieldExt;

  @Data
  public static class PersonBioSignature{
    /** 生物特征类型 */
    private Integer type;
    /** 同种生物特征数据索引 人像头像:index=1 主头像 */
    private Integer index;
    /** 生物特征存储路径 如图片地址 */
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
