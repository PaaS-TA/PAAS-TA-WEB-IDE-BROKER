package org.paasta.servicebroker.apiplatform.Exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;

public class DeliveryServiceException extends ServiceBrokerException {

    private static final long serialVersionUID = 8667141725171626000L;

    public DeliveryServiceException(String message) {
        super(message);
    }

    public DeliveryServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}