package com.intelligrape.search.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.HashSet;
import java.util.Iterator;


public interface SolrSearchService {
    HashSet<String> getXMLPostingUrls(String currentPageUri,boolean blackListCheck) throws Exception;
    HashSet<String> getValidXMLPostingUrls(Iterator<Resource> resources, String currentPageUri,boolean blackListCheck) throws Exception;
    boolean hasReadAccess(String agentId, String currentPageUri) throws Exception;
    String getXMLData(ResourceResolver resourceResolver, String xmlDataUrl) throws Exception;
    int postXMLDataToSolr(String xml, String xmlPostingUrl) throws Exception;
}
