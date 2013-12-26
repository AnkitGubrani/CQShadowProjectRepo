package com.intelligrape.replication.impl;

import com.day.cq.replication.*;
import com.day.cq.wcm.api.Page;
import com.intelligrape.replication.service.CQOperationsForSolrSearch;
import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Dictionary;
import java.util.Map;

@Component(immediate = true, enabled = true, metatype = true)
@Service(ContentBuilder.class)
@Property(name = "name", value = "SolrReplication", propertyPrivate = true)

public class CustomContentBuilderImpl implements ContentBuilder {

    public static final String name = "SolrReplication";
    public static final String title = "Solr Replication Content Builder";
    private static final String BLACK_LIST_FOR_MOVE_OPERATION = "black.list.uri.for.page.move.operation";

    @Property(name = BLACK_LIST_FOR_MOVE_OPERATION, value = {"/content/catalogs", "/content/campaigns", "/content/dam"},
            description = "default blacklist urls are \" /content/catalogs,/content/campaigns,/content/dam \"",
            label = BLACK_LIST_FOR_MOVE_OPERATION, cardinality = Integer.MAX_VALUE)
    private String[] BLACK_LIST_URLS;

    private static final Logger log = LoggerFactory.getLogger(CustomContentBuilderImpl.class);

    @Reference
    CQOperationsForSolrSearch cqOperationsForSolrSearch;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Activate
    protected void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        BLACK_LIST_URLS = (String[]) properties.get(BLACK_LIST_FOR_MOVE_OPERATION);
    }

    @Modified
    protected void modified(ComponentContext componentContext) {
        activate(componentContext);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ReplicationContent create(Session session, ReplicationAction action, ReplicationContentFactory replicationContentFactory, Map<String, Object> stringObjectMap) throws ReplicationException {
        return null;
    }

    @Override
    public ReplicationContent create(Session session, ReplicationAction action, ReplicationContentFactory factory) throws ReplicationException {

        AgentConfig config = action.getConfig();
        String solrUri = config.getTransportURI();
        ResourceResolver resourceResolver = null;
        String currentPageUri = action.getPath();
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            Page page = getSpecificPage(resourceResolver, currentPageUri);
            if (page != null) {
                return pageModificationEvents(resourceResolver, currentPageUri, solrUri, action.getType(), factory);
            } else
                return pageMoveEvent(currentPageUri, solrUri, action.getType(), factory);
        } catch (Exception e) {
            log.info("cause of error =>" + e.getMessage());
        } finally {
            if (resourceResolver != null)
                resourceResolver.close();
        }

        return blankData(factory);
    }

    private Page getSpecificPage(ResourceResolver resourceResolver, String pageUri) {

        Resource resource = resourceResolver.resolve(pageUri);
        return resource.adaptTo(Page.class);
    }

    private ReplicationContent pageModificationEvents(ResourceResolver resourceResolver, String currentPageUri, String solrUri, ReplicationActionType operation, ReplicationContentFactory factory) throws Exception {

        if (!solrUri.equals("")) {
            if (operation.equals(ReplicationActionType.ACTIVATE)) {
                return pageActivationEvent(resourceResolver, currentPageUri, factory);
            } else if (operation.equals(ReplicationActionType.DEACTIVATE)) {
                return pageDeactivationEvent(currentPageUri, solrUri, factory);
            } else if (operation.equals(ReplicationActionType.DELETE)) {
                return pageDeactivationEvent(currentPageUri, solrUri, factory);
            }
        }
        return blankData(factory);
    }

    private ReplicationContent pageActivationEvent(ResourceResolver resourceResolver, String currentPageUri, ReplicationContentFactory factory) throws Exception {

        currentPageUri = currentPageUri + ".solrsearch.html";
        String xmlData = cqOperationsForSolrSearch.getXMLData(resourceResolver, currentPageUri);
        log.info("\n ========xml Data for page URI {} = \n {} \n", currentPageUri, xmlData);
        xmlData="";
        return (!xmlData.equals("")) ? create(factory, xmlData) : blankData(factory);
    }

    private ReplicationContent pageDeactivationEvent(String currentPageUri, String solrUri, ReplicationContentFactory factory) throws Exception {

        log.info(" \n Deactivating the Page with URI = {} \n for solrUri = {}", currentPageUri, solrUri);
        String xmlData = "<delete><id>" + currentPageUri + "</id></delete>";
        return create(factory, xmlData);
    }

    private ReplicationContent create(ReplicationContentFactory factory, String xmlData) throws Exception {

        File tempFile = null;
        BufferedWriter out = null;
        try {
            tempFile = File.createTempFile("xmlData", ".tmp");
            out = new BufferedWriter(new FileWriter(tempFile));
            out.write(xmlData);
            out.close();
            return factory.create("application/xml", tempFile, true);
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
                if (tempFile != null)
                    tempFile.delete();
            }
        }
    }

    private ReplicationContent blankData(ReplicationContentFactory factory) {
        try {
            String xmlData = "<add><doc><field name=\"id\">_blank_</field></doc></add>";
            log.info(xmlData);
            return create(factory, xmlData);
        } catch (Exception e) {
            log.info("Error in Replication Handler blank data method " + e.getMessage());
        }
        return ReplicationContent.VOID;
    }

    private ReplicationContent pageMoveEvent(String currentPageUri, String solrUri, ReplicationActionType operation, ReplicationContentFactory factory) throws Exception {

//        boolean flag = isPageUriStartsWithBlackListPaths(currentPageUri);
//        if (!flag) {
//            if (solrUri.equals("")) {
//                log.info(" \n Solr User Agents doesn't Have Read Permission for {} \n ", currentPageUri);
//            } else {
//                log.info("\n Move Operation Performed on page \n");
//                if (operation.equals(ReplicationActionType.DELETE))
//                    return pageDeactivationEvent(currentPageUri, solrUri, factory);
//            }
//        }
        return pageDeactivationEvent("_blank_", solrUri, factory);
    }

    private boolean isPageUriStartsWithBlackListPaths(String pageUri) {

        boolean status = false;
        for (String blackListUrl : BLACK_LIST_URLS) {
            if (pageUri.startsWith(blackListUrl))
                status = true;
            break;
        }
        return status;
    }

}
