/**  
* Title: DefaultMessageSignal.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月30日 上午9:33:25  
* @version 1.0  
*/  
package com.kedacom.vline.disruptor.pojo;

import lombok.Data;

/**
 * @author zhang.kai
 *
 */
@Data
public class DefaultSignalMessage {
	
	private long id;
	private String head;
	private String body;

}
