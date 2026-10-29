/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaohucare.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xiaohucare.framework.core.mp.base.MybatisPlusProperties;
import com.xiaohucare.framework.core.mp.plug.SqlLogInterceptor;

/**
 * mybatisplus 配置
 *
 * 参考 ruoyi-common-mybatis 的 MybatisPlusConfig 实现，
 * 适配 Spring Boot 3.3.5 + MyBatis-Plus 3.5.15 + mybatis-plus-jsqlparser。
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@MapperScan("com.xiaohucare.**.mapper.**")
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusConfig {

	/**
	 * MybatisPlus 核心拦截器链（分页 + 乐观锁）
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 分页插件
		interceptor.addInnerInterceptor(paginationInnerInterceptor());
		// 乐观锁插件
		interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
		return interceptor;
	}

	/**
	 * 分页插件，自动识别数据库类型
	 */
	public PaginationInnerInterceptor paginationInnerInterceptor() {
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		// 分页合理化
		paginationInnerInterceptor.setOverflow(true);
		return paginationInnerInterceptor;
	}

	/**
	 * 乐观锁插件
	 */
	public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	/**
	 * sql 日志
	 * @return SqlLogInterceptor
	 */
	@Bean
	@ConditionalOnProperty(value = "ems.mybatis-plus.sql-log", matchIfMissing = true)
	public SqlLogInterceptor sqlLogInterceptor() {
		return new SqlLogInterceptor();
	}

}


