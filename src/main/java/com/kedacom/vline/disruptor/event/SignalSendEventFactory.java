/**  
* Title: SignalSendEventFactory.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 下午2:16:04  
* @version 1.0  
*/  
package com.kedacom.vline.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * @author zhang.kai
 *
 */
public class SignalSendEventFactory implements EventFactory<SignalSendEvent>{

	@Override
	public SignalSendEvent newInstance() {
		
		return new SignalSendEvent();
	}
	
	
	

}
