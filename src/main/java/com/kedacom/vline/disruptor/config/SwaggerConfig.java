/**  
* Title: SwaggerConfig.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月31日 上午10:19:14  
* @version 1.0  
*/
package com.kedacom.vline.disruptor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zhang.kai
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket swaggerSpringMvcPlugin() {

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("disruptor测试API").description("disruptor测试API").version("2.2.0").build();
	}

}
