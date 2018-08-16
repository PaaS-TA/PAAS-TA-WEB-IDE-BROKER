package org.paasta.servicebroker.webide.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;


/**
 * Web-Ide 서비스 관련 에러 Exception 클래스
 *
 * @author sjchoi
 * @since 2018.08.14
 */
public class WebIdeServiceException extends ServiceBrokerException {

	private static final long serialVersionUID = 8667141725171626000L;

	public WebIdeServiceException(String message){
		super(message);
	}

	public WebIdeServiceException(String message,Throwable cause){
		super(message,cause);
	}

	public WebIdeServiceException(Throwable cause){
		super(cause);
	}
	
}
