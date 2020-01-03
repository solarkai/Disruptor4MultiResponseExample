/**  
* Title: SignalSendEvent.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 上午9:31:52  
* @version 1.0  
*/  
package com.kedacom.vline.disruptor.event;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.kedacom.vline.disruptor.pojo.DefaultSignalMessage;
import com.kedacom.vline.disruptor.pojo.SignalResponse;

import lombok.Data;

/**
 * @author zhang.kai
 *
 */
@Data
public class SignalSendEvent {

	DefaultSignalMessage dsm;  //信令请求
	
}
