/**  
* Title: Send2NodeEventHandler.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 下午1:49:26  
* @version 1.0  
*/  
package com.kedacom.vline.disruptor.eventhandler;

import com.kedacom.vline.disruptor.event.SignalSendEvent;
import com.kedacom.vline.disruptor.pojo.DefaultSignalMessage;
import com.lmax.disruptor.EventHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang.kai
 *
 */
@Slf4j
public class Send2NodeEventHandler implements EventHandler<SignalSendEvent>{
	
	@Getter @Setter
	private String nodeName; 

	@Override
	public void onEvent(SignalSendEvent event, long sequence, boolean endOfBatch) throws Exception {
		//发送信令请求到各节点
		sendSignal2Node(event.getDsm(),nodeName);
		
	}

	/**
	 * @param nodeName2
	 */
	private void sendSignal2Node(DefaultSignalMessage dsm, String nodeName) {
		
		log.info("1.send signal {} to node:{} ",dsm,nodeName);
		
	}
	
	

}
