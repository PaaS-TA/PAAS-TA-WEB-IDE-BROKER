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

//import org.openpaas.servicebroker.model.ServiceInstance;

/**
 *  
 * @author whalsrn0710@bluedigm.com
 *
 */
@Service
public class WebIdeServiceInstanceBindingService implements ServiceInstanceBindingService {

	private static final Logger logger = LoggerFactory.getLogger(WebIdeServiceInstanceBindingService.class);
	@Autowired
	private WebIdeAdminService deliveryPipelineAdminService;
	
	
	@Autowired
	public WebIdeServiceInstanceBindingService(WebIdeAdminService deliveryPipelineAdminService) {
		this.deliveryPipelineAdminService = deliveryPipelineAdminService;
	}
	
	@Override
	public ServiceInstanceBinding createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException {
//
		logger.debug("DeliveryPipelineServiceInstanceBindingService CLASS createServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported.");
//		ServiceInstanceBinding binding = deliveryPipelineAdminService.findBindById(request.getBindingId());
//		if (binding != null) {
//			throw new ServiceInstanceBindingExistsException(binding);
//		}
//		ServiceInstance instance = deliveryPipelineAdminService.findById(request.getServiceInstanceId());
//
//		String database = instance.getServiceInstanceId();
//		String username = request.getBindingId();
//		// TODO Password Generator
//		String password = "password";
//
//		if (deliveryPipelineAdminService.isExistsUser(username)) {
//			// ensure the instance is empty
//			deliveryPipelineAdminService.deleteUser(database, username);
//		}
//
//
//		deliveryPipelineAdminService.createUser(database, username, password);
//
//		Map<String,Object> credentials = new HashMap<String,Object>();
//		credentials.put("uri", deliveryPipelineAdminService.getConnectionString(database, username, password));
//		credentials.put("hostname", deliveryPipelineAdminService.getConnectionString(database, username, password));
//
//		binding = new ServiceInstanceBinding(request.getBindingId(), instance.getServiceInstanceId(), credentials, null, request.getAppGuid());
//		deliveryPipelineAdminService.saveBind(binding);
//

		throw new ServiceBrokerException("Not Supported");

//		return binding;
	}

//	protected ServiceInstanceBinding getServiceInstanceBinding(String id) {
//		return deliveryPipelineAdminService.findBindById(id);
//	}

	@Override
	public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
			throws ServiceBrokerException {
		logger.debug("DeliveryPipelineServiceInstanceBindingService CLASS deleteServiceInstanceBinding");
		logger.debug("ServiceInstanceBinding not supported");

//		String bindingId = request.getBindingId();
//		ServiceInstanceBinding binding = getServiceInstanceBinding(bindingId);
//		if (binding!= null) {
//			deliveryPipelineAdminService.deleteUser(binding.getServiceInstanceId(), bindingId);
//			deliveryPipelineAdminService.deleteBind(bindingId);
//		}

		throw new ServiceBrokerException("Not Supported");

//		return binding;
	}

}
