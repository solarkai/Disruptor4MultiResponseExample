/**  
* Title: SignalDisruptorService.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 上午9:29:38  
* @version 1.0  
*/
package com.kedacom.vline.disruptor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedacom.vline.disruptor.event.SignalSendEvent;
import com.kedacom.vline.disruptor.event.SignalSendEventFactory;
import com.kedacom.vline.disruptor.eventhandler.ResponseCollectionEventHandler;
import com.kedacom.vline.disruptor.eventhandler.Send2NodeEventHandler;
import com.kedacom.vline.disruptor.pojo.DefaultSignalMessage;
import com.kedacom.vline.disruptor.pojo.SignalResponse;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import lombok.Getter;

/**
 * @author zhang.kai
 *
 */
@Service
public class SignalDisruptorService {

	@Autowired
	ResponseCollectionEventHandler rceh;

	private final List<String> nodeNameList = Arrays.asList("node1", "node2", "node3");

	@Getter
	// key为消息的唯一标识，value为本条信令对应的信号量
	private final ConcurrentHashMap<Long, CountDownLatch> cdlMap = new ConcurrentHashMap<Long, CountDownLatch>();

	@Getter
	// key为消息的唯一标识，value为本条信令对应的回应列表(来自多网)
	private final ConcurrentHashMap<Long, List<SignalResponse>> responseMap = new ConcurrentHashMap<Long, List<SignalResponse>>();

	private final int bufferSize = 1024 * 1024;

	private final SignalSendEventFactory sseFactory = new SignalSendEventFactory();

	private final Disruptor<SignalSendEvent> disruptor = new Disruptor<SignalSendEvent>(sseFactory, bufferSize,
			DaemonThreadFactory.INSTANCE);

	private boolean isDisruptorStarted = false;

	/**
	 * 启动disruptor
	 */
	public void startSignalDisruptor() {
		if (!isDisruptorStarted) {
			Send2NodeEventHandler[] send2NodeEventHandlerArray = new Send2NodeEventHandler[nodeNameList.size()];
			for (int i = 0; i < nodeNameList.size(); i++) {
				String nodeName = nodeNameList.get(i);
				Send2NodeEventHandler handler = new Send2NodeEventHandler();
				handler.setNodeName(nodeName);
				send2NodeEventHandlerArray[i] = handler;
			}

			// 设置消费者依赖图
			disruptor.handleEventsWith(send2NodeEventHandlerArray).then(rceh);
			disruptor.start();
			isDisruptorStarted = true;
		}
	}

	/**
	 * 关闭disruptor
	 */
	public void shutdownSignalDisruptor() {
		if (isDisruptorStarted) {
			disruptor.shutdown();
			isDisruptorStarted = false;
		}
	}

	/**
	 * 发布信令事件
	 * 
	 * @param dsm
	 * @return
	 */
	public long publishSignal(DefaultSignalMessage dsm) {
		RingBuffer<SignalSendEvent> rb = disruptor.getRingBuffer();
		long sequence = rb.next();
		try {
			SignalSendEvent event = rb.get(sequence);
			event.setDsm(dsm);

			// 放入回调函数可访问的map
			this.cdlMap.put(dsm.getId(), new CountDownLatch(nodeNameList.size()));
			this.responseMap.put(dsm.getId(), new ArrayList<SignalResponse>());
		} finally {
			rb.publish(sequence);
		}
		return sequence;
	}

	/**
	 * 判断disruptor是否启动
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return this.isDisruptorStarted;
	}

}
