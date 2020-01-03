/**  
* Title: SignalResponse.java
* Description:  
* Copyright: Copyright (c) 2020 
* Company: www.kedacom.com 
* @author zhang.kai  
* @date 2019年12月31日 上午9:56:45  
* @version 1.0  
*/  
package com.kedacom.vline.disruptor.pojo;

import lombok.Data;

/**
 * @author zhang.kai
 *
 */
@Data
public class SignalResponse {
	private long id;
	private String nodeName;
	private String response;
}
