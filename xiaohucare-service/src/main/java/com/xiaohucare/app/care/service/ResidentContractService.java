package com.xiaohucare.app.care.service;

import java.util.Date;
import java.util.List;

import com.xiaohucare.app.care.entity.ResidentContractEntity;
import com.xiaohucare.app.care.vo.ResidentContractVO;
import com.xiaohucare.framework.core.mp.base.BaseService;

/**
 * 合同记录
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
public interface ResidentContractService extends BaseService<ResidentContractEntity> {

	/**
	 * 保存合同：新增时生成合同编号，并按 residentId / templateId 回填长者与模板快照
	 *
	 * @param contract 合同数据
	 * @return 保存后的合同
	 */
	ResidentContractVO submitContract(ResidentContractVO contract);

	/**
	 * 签署合同：置为已签署并记录签署日期、签署年份
	 *
	 * @param id        合同ID
	 * @param signDate  签署日期，为空取当前时间
	 * @param remark    签署备注
	 * @param signType  签署类型，为空取 1
	 * @return 是否成功
	 */
	boolean signContract(Long id, Date signDate, String remark, Integer signType);

	/**
	 * 模版列表
	 *
	 * @param templateName 模版名称，模糊匹配
	 * @param status       状态：1 启用 0 停用
	 * @return 模版列表
	 */
	List<ResidentContractVO> listTemplates( String templateName, Integer status);

	/**
	 * 保存模版：新增时生成 TP- 编号并固定 contract_type=100，
	 * 修改时校验模板是否存在
	 *
	 * @param template 模版数据
	 * @return 保存后的模版
	 */
	ResidentContractVO submitTemplate(ResidentContractVO template);

	/**
	 * 删除模版（逻辑删除），仅作用于 TP- 行
	 *
	 * @param ids     模版ID集合
	 * @return 是否成功
	 */
	boolean removeTemplates(List<Long> ids);

	/**
	 * 生成合同编号：HT-{residentId}-{时间戳}
	 *
	 * @param residentId 长者ID，可为空
	 * @return 合同编号
	 */
	String nextContractNo(Long residentId);
}
