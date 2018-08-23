package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.ServiceInstanceBinding;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *  
 * @author whalsrn0710@bluedigm.com
 *
 */
@Service
public class WebIdeServiceInstanceBindingService implements ServiceInstanceBindingService {

	private static final Logger logger = LoggerFactory.getLogger(WebIdeServiceInstanceBindingService.class);
	@Autowired
	private WebIdeAdminService webIdeAdminService;
	
	
	@Autowired
	public WebIdeServiceInstanceBindingService(WebIdeAdminService webIdeAdminService) {
		this.webIdeAdminService = webIdeAdminService;
	}
	
	@Override
	public ServiceInstanceBinding createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException {
		logger.debug("Web-Ide ServiceInstanceBindingService CLASS createServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported.");

		throw new ServiceBrokerException("Not Supported");
	}


	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
			throws ServiceBrokerException {
		logger.debug("Web-Ide ServiceInstanceBindingService CLASS deleteServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported");

		throw new ServiceBrokerException("Not Supported");
	}

}
