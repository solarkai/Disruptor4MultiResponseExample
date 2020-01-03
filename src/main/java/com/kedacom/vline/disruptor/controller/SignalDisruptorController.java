/**  
* Title: SignalDisruptorController.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月31日 上午9:17:50  
* @version 1.0  
*/
package com.kedacom.vline.disruptor.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kedacom.vline.disruptor.pojo.DefaultSignalMessage;
import com.kedacom.vline.disruptor.pojo.SignalResponse;
import com.kedacom.vline.disruptor.service.SignalDisruptorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zhang.kai
 *
 */
@RestController
@RequestMapping("/vline/disruptor")
@Api(value = "disruptor测试接口")
public class SignalDisruptorController {

	@Autowired
	private SignalDisruptorService sds;

	@RequestMapping(value = "/start", method = RequestMethod.GET)
	@ApiOperation(value = "启动disruptor", notes = "启动disruptor")
	public String startDisruptor() {

		sds.startSignalDisruptor();
		return "success";
	}

	@RequestMapping(value = "/shutdown", method = RequestMethod.GET)
	@ApiOperation(value = "关闭disruptor", notes = "关闭disruptor")
	public String shutdownDisruptor() {

		sds.shutdownSignalDisruptor();
		return "success";
	}

	@RequestMapping(value = "/publishevent", method = RequestMethod.POST)
	@ApiOperation(value = "发布siganl事件", notes = "发布siganl事件")
	public String publish(@RequestBody(required = true) DefaultSignalMessage dsm) {
		if (!sds.isStarted()) {
			return "disruptor not started!";
		} else {
			sds.publishSignal(dsm);
			return "success:" + dsm.getId();
		}
	}

	@RequestMapping(value = "/responsesignal", method = RequestMethod.POST)
	@ApiOperation(value = "siganl响应", notes = "siganl响应")
	public long responseSignal(@RequestBody(required = true) SignalResponse sr) {
		long id = sr.getId();
		CountDownLatch cdl = sds.getCdlMap().get(id);
		if (null != cdl) {
			cdl.countDown();
		}
		List<SignalResponse> respList = sds.getResponseMap().get(id);
		if (null != respList) {
			respList.add(sr);
		}
		return id;
	}

}
