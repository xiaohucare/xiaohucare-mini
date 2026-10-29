package com.xiaohucare.app.care.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohucare.app.care.entity.ResidentFamilyMemberEntity;
import com.xiaohucare.app.care.service.ResidentFamilyMemberService;
import com.xiaohucare.app.care.vo.ResidentFamilyMemberVO;
import com.xiaohucare.app.care.wrapper.ResidentFamilyMemberWrapper;
import com.xiaohucare.framework.core.domain.R;
import com.xiaohucare.framework.core.domain.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
/**
 * 长者家属表
 * @author xiaohucare
 * @date 2026-10-21
 */
@RestController
@RequestMapping("intra/resident/residentfamilymember")
public class ResidentFamilyMemberController extends BaseController{
    @Autowired
    private ResidentFamilyMemberService residentFamilyMemberService;
    /**
     * 按长者ID查询家属列表
     */
    @Operation(summary = "按长者ID查询家属列表")
    @GetMapping("/listByResidentId")
    @PreAuthorize("@ss.hasPermi('resident:resident:list')")
    public R<List<ResidentFamilyMemberVO>> listByResidentId(@RequestParam("residentId") Long residentId){
        List<ResidentFamilyMemberEntity> list = residentFamilyMemberService.listByResidentId(residentId);
        return R.data(ResidentFamilyMemberWrapper.build().listVO(list));
    }
    /**
     * 保存&更新
     */
    @Operation(summary = "保存&更新")
    @PostMapping("/submit")
    @PreAuthorize("@ss.hasPermi('resident:resident:submit')")
    public R<ResidentFamilyMemberVO> submit(@Valid @RequestBody ResidentFamilyMemberVO familyMember){
   		if(familyMember.getId()==null) {
            familyMember.setCreateAt(new Date());
            familyMember.setIsDelete(0);
        }
        familyMember.setUpdateAt(new Date());
    	if(residentFamilyMemberService.saveOrUpdate(familyMember)) {
    		 return R.ok(familyMember);
    	 }
    	return R.error("数据错误");
    }
    /**
     * 删除
     */
    @Operation(summary = "删除")
    @RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.DELETE})
    @PreAuthorize("@ss.hasPermi('resident:resident:delete')")
    public R<Boolean> delete(@RequestBody Long[] ids){
        return R.ok(residentFamilyMemberService.removeByIds(Arrays.asList(ids)));
    }

}
