package com.intelligrape.search.impl;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.Page;
import com.intelligrape.search.services.SolrSearchService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.jcr.api.SlingRepository;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

@Component(immediate = true, enabled = true)
@Service(value = SolrSearchService.class)
public class SolrSearchServiceImpl implements SolrSearchService {

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    RequestResponseFactory requestResponseFactory;

    @Reference
    SlingRequestProcessor requestProcessor;

    @Reference
    SlingRepository repo;

    public HashSet<String> getXMLPostingUrls(String currentPageUri, boolean blackListCheck) throws Exception {
        ResourceResolver resourceResolver = null;
        HashMap<String, String> userInfoList = new HashMap<String, String>();
        HashSet<String> xmlPostingUrls = new HashSet<String>();
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Resource resource = resourceResolver.getResource("/etc/search/");
            Iterator<Resource> resources = resourceResolver.listChildren(resource);
            xmlPostingUrls = getValidXMLPostingUrls(resources, currentPageUri, blackListCheck);
        } finally {
            resourceResolver.close();
        }
        return xmlPostingUrls;
    }

    public HashSet<String> getValidXMLPostingUrls(Iterator<Resource> resources, String currentPageUri, boolean blackListCheck) throws Exception {
        Resource tempResource = null;
        String agentId = "", postUrl = "", indexStatus = "", templatePath = "";
        HashSet<String> xmlPostingUrls = new HashSet<String>();
        while (resources.hasNext()) {
            tempResource = resources.next();
            Page tempPage = tempResource.adaptTo(Page.class);
            if (tempPage != null) {
                templatePath = String.valueOf(tempPage.getTemplate().getPath());
                if (templatePath.equals("/apps/search/templates/searchAgent")) {
                    agentId = tempPage.getProperties().get("agentId", "");
                    postUrl = tempPage.getProperties().get("mappingUrl", "");
                    indexStatus = tempPage.getProperties().get("indexStatus", "");
                    if (!agentId.equals("") && !postUrl.equals("") && indexStatus.equals("true")) {
                        if (blackListCheck) {
                            xmlPostingUrls.add(postUrl);
                        } else {
                            if (hasReadAccess(agentId, currentPageUri)) {
                                xmlPostingUrls.add(postUrl);
                            }
                        }
                    }
                }
            }
        }
        return xmlPostingUrls;

    }

    public boolean hasReadAccess(String agentId, String currentPageUri) throws Exception {
        Session adminSession = null;
        Session session = null;
        boolean permission = false;
        try {
            adminSession = repo.loginAdministrative(null);
            session = adminSession.impersonate(new SimpleCredentials(agentId, new char[0]));
            adminSession.logout();
            AccessControlManager acMgr = session.getAccessControlManager();
            permission = session.getAccessControlManager().hasPrivileges(currentPageUri, new Privilege[]{acMgr.privilegeFromName(Privilege.JCR_READ)});
        } finally {
            if (session != null) {
                session.logout();
            }
        }
        return permission;
    }

    public String getXMLData(ResourceResolver resourceResolver, String xmlDataUrl) throws Exception {

        HttpServletRequest request = requestResponseFactory.createRequest("GET", xmlDataUrl);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpServletResponse response;
        response = requestResponseFactory.createResponse(out);
        requestProcessor.processRequest(request, response, resourceResolver);
        return new String(out.toByteArray(), "UTF-8");
    }

    public int postXMLDataToSolr(String xml, String xmlPostingUrl) throws Exception {

        int i = 0;
        PostMethod post = null;
        try {
            HttpClient client = new HttpClient();
            post = new PostMethod(xmlPostingUrl);
            StringRequestEntity body = new StringRequestEntity(xml, "application/xml", "UTF-8");
            post.addParameter("commit", "true");
            post.setRequestEntity(body);
            post.setRequestHeader("Content-length", String.valueOf(body.getContentLength()));
            i = client.executeMethod(post);
        } finally {
            if (post != null)
                post.releaseConnection();
        }
        return i;
    }
}
