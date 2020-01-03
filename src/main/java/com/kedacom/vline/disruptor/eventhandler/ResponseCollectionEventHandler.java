/**  
* Title: ResponseCollectionEventHandler.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 下午2:00:29  
* @version 1.0  
*/
package com.kedacom.vline.disruptor.eventhandler;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kedacom.vline.disruptor.event.SignalSendEvent;
import com.kedacom.vline.disruptor.pojo.SignalResponse;
import com.kedacom.vline.disruptor.service.SignalDisruptorService;
import com.lmax.disruptor.EventHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang.kai
 *
 */
@Slf4j
@Component
public class ResponseCollectionEventHandler implements EventHandler<SignalSendEvent> {

	//阻塞时间，这里会实现测试效果，设置地比较大
	private final long SIGNAL_TIMEOUT = 1000 * 1000;

	@Autowired
	SignalDisruptorService sds;

	@Override
	public void onEvent(SignalSendEvent event, long sequence, boolean endOfBatch) throws Exception {

		long id = event.getDsm().getId();
		// 阻塞等待所有节点对信令的响应
		CountDownLatch latch = sds.getCdlMap().get(id);
		if (null != latch) {
			log.info("start to wait response by id:{}",id);
			latch.await(SIGNAL_TIMEOUT, TimeUnit.MILLISECONDS);
			// 阻塞结束，处理所有响应
			List<SignalResponse> respList = sds.getResponseMap().get(id);
			handleRespList(respList);
			// 清除资源
			sds.getCdlMap().remove(event.getDsm().getId());
			sds.getResponseMap().remove(event.getDsm().getId());
		} else {
			log.error("event not set latch:{}", event);
		}
	}

	/**
	 * 处理所有响应结果
	 * 
	 * @param respList
	 */
	private void handleRespList(List<SignalResponse> respList) {
		log.info("2. get response list:{}", respList);
	}

}
