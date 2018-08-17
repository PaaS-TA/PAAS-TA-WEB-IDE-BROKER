package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.openpaas.servicebroker.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mingu on 2017-06-01.
 */
@Service
public class WebIdeCatalogService implements CatalogService {

    private Catalog catalog;
    private Map<String,ServiceDefinition> serviceDefs = new HashMap<String,ServiceDefinition>();

    @Autowired
    public WebIdeCatalogService(Catalog catalog) {
        this.catalog = catalog;
        initializeMap();
    }

    private void initializeMap() {
        for (ServiceDefinition def: catalog.getServiceDefinitions()) {
            serviceDefs.put(def.getId(), def);
        }
    }

    @Override
    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public ServiceDefinition getServiceDefinition(String serviceId) {
        return serviceDefs.get(serviceId);
    }

}