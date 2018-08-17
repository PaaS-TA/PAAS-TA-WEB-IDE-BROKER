package org.paasta.servicebroker.webide.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;


/**
 * Exception thrown when issues with the underlying mongo service occur.
 * NOTE: com.mongodb.MongoException is a runtime exception and therefore we 
 * want to have to handle the issue.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class WebIdeServiceException extends ServiceBrokerException {

	private static final long serialVersionUID = 8667141725171626000L;

	public WebIdeServiceException(String message) {
		super(message);
	}
	public WebIdeServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
