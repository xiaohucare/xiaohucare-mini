//package com.xiaohucare.intra.controller.storage;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.xiaohucare.app.ems.storage.DataStorageService;
//import com.xiaohucare.app.ems.storage.entity.HourlyStatistics;
//import com.xiaohucare.app.ems.storage.mapper.HourlyStatisticsMapper;
//import com.xiaohucare.app.ems.storage.model.ProtocolData;
//import com.xiaohucare.framework.core.domain.AjaxResult;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.context.XxlJobHelper;
//
//import jakarta.annotation.Resource;
//
///**
// * 小时统计数据控制器
// */
//@RestController
//@RequestMapping("/storage/hourly-statistics")
//public class HourlyStatisticsController {
//	  private static final Logger logger = LoggerFactory.getLogger(HourlyStatisticsController.class);
//    @Resource
//    private DataStorageService dataStorageService;
//    @Resource
//    private HourlyStatisticsMapper hourlyStatisticsMapper;
//    
//    /**
//     * 手动触发小时统计数据汇总任务
//     */
//    @GetMapping("/trigger")
//    public AjaxResult triggerHourlyStatistics() {
//        try {
//            // 获取所有设备和点位信息
//            List<Map<String, String>> devicesAndAddresses = dataStorageService.getAllDevicesAndAddresses();
//            executeHourlyStatistics(null);
//            return AjaxResult.success("获取到 " + devicesAndAddresses.size() + " 个设备点位组合");
//        } catch (Exception e) {
//            return AjaxResult.error("触发任务失败: " + e.getMessage());
//        }
//    }
//    
//    public ReturnT<String> executeHourlyStatistics(String param) {
//        try {
//            logger.info("开始执行小时统计数据汇总任务");
//            
//            // 计算上一小时的时间范围
//            Instant endTime = Instant.now().truncatedTo(ChronoUnit.HOURS);
//            Instant startTime = endTime.minusSeconds(3600); // 上一小时
//            
//            logger.info("统计时间范围: {} 到 {}", startTime, endTime);
//            
//            // 获取所有设备和点位信息
//            List<Map<String, String>> devicesAndAddresses = 
//                dataStorageService.getAllDevicesAndAddresses();
//            
//            if (devicesAndAddresses.isEmpty()) {
//                logger.warn("未找到任何设备和点位信息");
//                XxlJobHelper.log("未找到任何设备和点位信息");
//                return ReturnT.SUCCESS;
//            }
//            
//            logger.info("找到 {} 个设备点位组合", devicesAndAddresses.size());
//            XxlJobHelper.log("找到 " + devicesAndAddresses.size() + " 个设备点位组合");
//            
//            int totalCount = 0;
//            
//            // 遍历所有设备和点位组合
//            for (Map<String, String> deviceInfo : devicesAndAddresses) {
//                String protocol = deviceInfo.get("protocol");
//                String deviceId = deviceInfo.get("deviceId");
//                String address = deviceInfo.get("address");
//                
//                try {
//                    // 查询InfluxDB中的统计数据
//                    List<ProtocolData.PointStatistics> statsList = 
//                        dataStorageService.queryPointStatistics(protocol, deviceId, address, startTime, endTime);
//                    
//                    if (statsList != null && !statsList.isEmpty()) {
//                        for (ProtocolData.PointStatistics pointStats : statsList) {
//                            // 检查是否已存在相同时间的统计数据
//                            QueryWrapper<HourlyStatistics> queryWrapper = new QueryWrapper<>();
//                            queryWrapper.eq("hour", pointStats.getHour())
//                                       .eq("protocol", pointStats.getProtocol())
//                                       .eq("device_id", pointStats.getDeviceId())
//                                       .eq("address", pointStats.getAddress());
//                            
//                            if (hourlyStatisticsMapper.selectCount(queryWrapper) == 0) {
//                                // 转换为数据库实体并保存
//                                HourlyStatistics hourlyStats = convertToHourlyStatistics(pointStats);
//                                hourlyStatisticsMapper.insert(hourlyStats);
//                                totalCount++;
//                                logger.debug("保存统计数据: {}-{}-{}-{}", 
//                                    pointStats.getHour(), pointStats.getProtocol(), 
//                                    pointStats.getDeviceId(), pointStats.getAddress());
//                            } else {
//                                logger.debug("统计数据已存在，跳过: {}-{}-{}-{}", 
//                                    pointStats.getHour(), pointStats.getProtocol(), 
//                                    pointStats.getDeviceId(), pointStats.getAddress());
//                            }
//                        }
//                    } else {
//                        logger.debug("设备点位无统计数据: {}-{}-{}", protocol, deviceId, address);
//                    }
//                } catch (Exception e) {
//                    logger.error("处理设备点位数据时出错: {}-{}-{}", protocol, deviceId, address, e);
//                    XxlJobHelper.log("处理设备点位数据时出错: " + protocol + "-" + deviceId + "-" + address + ": " + e.getMessage());
//                }
//            }
//            
//            logger.info("小时统计数据汇总任务执行完成，共处理 {} 条记录", totalCount);
//            XxlJobHelper.log("小时统计数据汇总任务执行完成，共处理 " + totalCount + " 条记录");
//            return ReturnT.SUCCESS;
//            
//        } catch (Exception e) {
//            logger.error("执行小时统计数据汇总任务时出错", e);
//            XxlJobHelper.log("执行小时统计数据汇总任务时出错: " + e.getMessage());
//            return ReturnT.FAIL;
//        }
//    }
//    
//    /**
//     * 将ProtocolData.PointStatistics转换为HourlyStatistics
//     */
//    private HourlyStatistics convertToHourlyStatistics(ProtocolData.PointStatistics pointStats) {
//        HourlyStatistics hourlyStats = new HourlyStatistics();
//        hourlyStats.setHour(pointStats.getHour());
//        hourlyStats.setProtocol(pointStats.getProtocol());
//        hourlyStats.setDeviceId(pointStats.getDeviceId());
//        hourlyStats.setAddress(pointStats.getAddress());
//        hourlyStats.setAverageValue(pointStats.getAverageValue());
//        hourlyStats.setSumValue(pointStats.getSumValue());
//        hourlyStats.setMinValue(pointStats.getMinValue());
//        hourlyStats.setMaxValue(pointStats.getMaxValue());
//        hourlyStats.setCount(pointStats.getCount());
//        hourlyStats.setCreateTime(Instant.now());
//        return hourlyStats;
//    }
//    
//    
//    /**
//     * 查询小时统计数据
//     */
//    @GetMapping("/list")
//    public AjaxResult listHourlyStatistics() {
//        try {
//            List<HourlyStatistics> list = hourlyStatisticsMapper.selectList(null);
//            return AjaxResult.success(list);
//        } catch (Exception e) {
//            return AjaxResult.error("查询数据失败: " + e.getMessage());
//        }
//    }
//}