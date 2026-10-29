package com.xiaohucare.app.care.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaohucare.app.care.entity.ResidentContractEntity;
import com.xiaohucare.app.care.entity.ResidentEntity;
import com.xiaohucare.app.care.mapper.ResidentContractMapper;
import com.xiaohucare.app.care.service.ResidentContractService;
import com.xiaohucare.app.care.service.ResidentService;
import com.xiaohucare.app.care.vo.ResidentContractVO;
import com.xiaohucare.app.care.wrapper.ResidentContractWrapper;
import com.xiaohucare.common.exception.ServiceException;
import com.xiaohucare.framework.core.mp.base.BaseServiceImpl;

import cn.hutool.core.util.StrUtil;

/**
 * 合同记录
 *
 * @author xiaohucare
 * @date 2026-10-25
 */
@Service
public class ResidentContractServiceImpl extends BaseServiceImpl<ResidentContractMapper, ResidentContractEntity>
		implements ResidentContractService {

	@Autowired
	private ResidentService residentService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResidentContractVO submitContract(ResidentContractVO contract) {
		if (contract == null) {
			return null;
		}
		Date now = new Date();
		if (contract.getId() == null) {
			// 新增：补合同编号、默认类型与状态
			if (StrUtil.isBlank(contract.getContractNo())) {
				contract.setContractNo(nextContractNo(contract.getResidentId()));
			}
			if (contract.getContractType() == null) {
				contract.setContractType(1);
			}
			if (contract.getStatus() == null) {
				contract.setStatus(0);
			}
			contract.setCreateAt(now);
			contract.setIsDelete(0);
		}
		// 长者快照：前端只传 residentId 时回填姓名与编号
		if (contract.getResidentId() != null) {
			ResidentEntity resident = residentService.getById(contract.getResidentId());
			if (resident != null) {
				if (StrUtil.isBlank(contract.getResidentName())) {
					contract.setResidentName(resident.getName());
				}
				if (StrUtil.isBlank(contract.getResidentNo())) {
					contract.setResidentNo(resident.getResidentNo());
				}
			}
		}
		// 模板快照：前端只传 templateId 时回填协议名称
		if (contract.getTemplateId() != null && StrUtil.isBlank(contract.getTemplateName())) {
			ResidentContractEntity template = getById(contract.getTemplateId());
			if (template != null) {
				contract.setTemplateName(template.getTemplateName());
			}
		}
		if (contract.getSignDate() != null) {
			contract.setSignYear(formatYear(contract.getSignDate()));
		}
		contract.setUpdateAt(now);
		if (!saveOrUpdate(contract)) {
			return null;
		}
		return ResidentContractWrapper.build().entityVO(getById(contract.getId()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean signContract(Long id, Date signDate, String remark, Integer signType) {
		if (id == null) {
			return false;
		}
		ResidentContractEntity contract = getById(id);
		if (contract == null || Integer.valueOf(3).equals(contract.getStatus())) {
			return false;
		}
		Date date = signDate != null ? signDate : new Date();
		contract.setSignDate(date);
		contract.setSignYear(formatYear(date));
		contract.setSignType(signType != null ? signType : 1);
		contract.setStatus(1);
		if (StrUtil.isNotBlank(remark)) {
			contract.setRemark(remark);
		}
		contract.setUpdateAt(new Date());
		return updateById(contract);
	}

	@Override
	public List<ResidentContractVO> listTemplates( String templateName, Integer status) {
		QueryWrapper<ResidentContractEntity> qw = new QueryWrapper<>();
		// 模板行：contract_no 以 TP 开头
		qw.likeRight("contract_no", ResidentContractEntity.NO_PREFIX_TEMPLATE);
		qw.like(StrUtil.isNotBlank(templateName), "template_name", templateName);
		qw.eq(status != null, "status", status);
		// 通用模板在前，同类按新在前
		qw.orderByDesc("id");
		List<ResidentContractEntity> list = list(qw);
		return list.stream().map(ResidentContractWrapper.build()::entityVO).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResidentContractVO submitTemplate(ResidentContractVO template) {
		if (template == null) {
			return null;
		}
		checkTemplateOwnership(template.getId());
		Date now = new Date();
		template.setContractType(ResidentContractEntity.TYPE_TEMPLATE);
		if (template.getId() == null) {
			template.setContractNo(ResidentContractEntity.NO_PREFIX_TEMPLATE + "-" + randomToken());
			if (template.getStatus() == null) {
				template.setStatus(1);
			}
			template.setCreateAt(now);
			template.setIsDelete(0);
		}
		// 模板不挂长者，避免模板行被当成合同统计
		template.setResidentId(null);
		template.setResidentName(null);
		template.setResidentNo(null);
		template.setUpdateAt(now);
		if (!saveOrUpdate(template)) {
			return null;
		}
		return ResidentContractWrapper.build().entityVO(getById(template.getId()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeTemplates(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return false;
		}
		QueryWrapper<ResidentContractEntity> qw = new QueryWrapper<>();
		qw.in("id", new ArrayList<>(ids));
		qw.likeRight("contract_no", ResidentContractEntity.NO_PREFIX_TEMPLATE);
		// 仅删本机构模板：通用模板与其他机构模板均不受影响
		return remove(qw);
	}

	/**
	 * 校验模板是否存在：编辑时确认模板未被删除
	 *
	 * @param id 模板ID，为空表示新增，不校验
	 */
	private void checkTemplateOwnership(Long id) {
		if (id == null) {
			return;
		}
		ResidentContractEntity exist = getById(id);
		if (exist == null) {
			throw new ServiceException("模板不存在或已删除");
		}
	}

	@Override
	public String nextContractNo(Long residentId) {
		// 与历史编号保持一致：HT-{长者ID}-{时间戳}，追加 3 位随机数避免同毫秒重复
		String residentPart = residentId != null ? String.valueOf(residentId) : "0";
		int random = ThreadLocalRandom.current().nextInt(100, 1000);
		return ResidentContractEntity.NO_PREFIX_CONTRACT + "-" + residentPart + "-" + System.currentTimeMillis() + random;
	}

	/** 签署年份 */
	private String formatYear(Date date) {
		return new SimpleDateFormat("yyyy").format(date);
	}

	/** 32 位随机串（去掉横线），用于模板编号 */
	private String randomToken() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
}
