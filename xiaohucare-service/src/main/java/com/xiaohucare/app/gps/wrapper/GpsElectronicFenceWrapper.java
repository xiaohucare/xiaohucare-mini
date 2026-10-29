
package com.xiaohucare.app.gps.wrapper;
import java.util.ArrayList;
import java.util.List;

import com.xiaohucare.app.gps.entity.GpsElectronicFenceEntity;
import com.xiaohucare.app.gps.vo.GpsCoordinateVo;
import com.xiaohucare.app.gps.vo.GpsElectronicFenceVO;
import com.xiaohucare.common.extra.utils.BeanUtil;
import com.xiaohucare.common.extra.utils.JsonUtil;
import com.xiaohucare.framework.core.mp.base.BaseEntityWrapper;

  /**
 * 电子围栏表 包装类,返回视图层所需的字段
 * @author xiaohucare
 * @date 2026-04-30 10:09:35
 */
public class GpsElectronicFenceWrapper extends BaseEntityWrapper<GpsElectronicFenceEntity, GpsElectronicFenceVO>  {
    public static GpsElectronicFenceWrapper build() {
        return new GpsElectronicFenceWrapper();
    }
	@Override
	public GpsElectronicFenceVO entityVO(GpsElectronicFenceEntity gpsElectronicFence) {
		if(gpsElectronicFence==null) {
			return null;
		}
		GpsElectronicFenceVO gpsElectronicFenceVO = BeanUtil.copy(gpsElectronicFence, GpsElectronicFenceVO.class);
		List<GpsCoordinateVo> polygonPointList=new ArrayList<GpsCoordinateVo>();
			/**
	 * 多边形顶点坐标JSON数组
	 * eg:[{"lng":116.407400,"lat":39.904200},{"lng":116.408500,"lat":39.904500},{"lng":116.409000,"lat":39.903800},{"lng":116.407800,"lat":39.903500}]
	 */
		 String polygonPoints=gpsElectronicFence.getPolygonPoints();
		 if(polygonPoints!=null && !polygonPoints.isEmpty()) {
			try {
				System.out.println("开始解析多边形顶点坐标: " + polygonPoints);
				
				// 使用项目统一的 JsonUtil 工具类解析
				polygonPointList = JsonUtil.getListBean(polygonPoints, GpsCoordinateVo.class);
				
				if(polygonPointList == null) {
					polygonPointList = new ArrayList<>();
					System.out.println("解析结果为空，使用空列表");
				} else {
					System.out.println("多边形顶点坐标解析成功，共 " + polygonPointList.size() + " 个点");
					// 打印第一个点验证
					if(!polygonPointList.isEmpty()) {
						GpsCoordinateVo first = polygonPointList.get(0);
						System.out.println("第一个点: lng=" + first.getLng() + ", lat=" + first.getLat());
					}
				}
			} catch (Exception e) {
				// 解析失败时记录日志并返回空列表
				System.err.println("解析多边形顶点坐标失败: " + e.getMessage());
				e.printStackTrace();
				polygonPointList = new ArrayList<>();
			}
		 }
		gpsElectronicFenceVO.setPolygonPointList(polygonPointList);
		return gpsElectronicFenceVO;
	}

}
