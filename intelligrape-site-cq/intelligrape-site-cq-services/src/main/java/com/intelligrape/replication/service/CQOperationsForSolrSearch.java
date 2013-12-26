package com.intelligrape.replication.service;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.HashSet;
import java.util.Iterator;

public interface CQOperationsForSolrSearch {
    String getXMLData(ResourceResolver resourceResolver, String xmlDataUrl) throws Exception;
    public int postXMLDataToSolr(String xml, String xmlPostingUrl) throws Exception;
}
