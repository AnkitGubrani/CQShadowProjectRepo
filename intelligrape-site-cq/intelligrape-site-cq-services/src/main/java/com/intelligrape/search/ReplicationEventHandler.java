package com.intelligrape.search;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.Page;
import com.intelligrape.search.services.SolrSearchService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;


@Component(immediate = true, enabled = true, metatype = true)
@Service
@Properties({
        @Property(name = "event.topics", value = ReplicationAction.EVENT_TOPIC, propertyPrivate = true)

})
public class ReplicationEventHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(ReplicationEventHandler.class);

    private static final String BLACK_LIST_FOR_MOVE_OPERATION = "black.list.uri.for.page.move.operation";

    @Property(name = BLACK_LIST_FOR_MOVE_OPERATION, value = {"/content/catalogs", "/content/campaigns", "/content/dam"},
            description = "default blacklist urls are \" /content/catalogs,/content/campaigns,/content/dam \"",
            label = BLACK_LIST_FOR_MOVE_OPERATION, cardinality = Integer.MAX_VALUE
    )
    private String[] BLACK_LIST_URLS;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    SolrSearchService solrSearchService;

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
    public void handleEvent(Event event) {

        ReplicationAction action = ReplicationAction.fromEvent(event);
        ResourceResolver resourceResolver = null;
        HashSet<String> xmlPostingUrls = new HashSet<String>();
        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            String pageUri = action.getPath();
            Page page = getSpecificPage(resourceResolver, pageUri);
            if (page != null) {
                xmlPostingUrls = solrSearchService.getXMLPostingUrls(pageUri, false);
                if (xmlPostingUrls.isEmpty()) {
                    log.info(" \n Solr User Agents doesn't Have Read Permission for {} \n ", pageUri);
                } else {
                    if (action.getType().equals(ReplicationActionType.ACTIVATE)) {
                        pageActivationEvent(resourceResolver, pageUri, xmlPostingUrls);
                    }
                    if (action.getType().equals(ReplicationActionType.DEACTIVATE)) {
                        pageDeactivationEvent(pageUri, xmlPostingUrls);
                    }
                    if (action.getType().equals(ReplicationActionType.DELETE)) {
                        pageDeactivationEvent(pageUri, xmlPostingUrls);
                    }
                }
            } else {
                boolean status = isPageUriStartsWithBlackListPaths(pageUri);
                if (!status) {
                    xmlPostingUrls = solrSearchService.getXMLPostingUrls(pageUri, true);
                    if (xmlPostingUrls.isEmpty()) {
                        log.info(" \n Solr User Agents doesn't Have Read Permission for {} \n ", pageUri);
                    } else {
                        log.info("\n Move Operation Performed on page \n");
                        if (action.getType().equals(ReplicationActionType.DELETE))
                            pageDeactivationEvent(pageUri, xmlPostingUrls);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Error in Replication Event Handler is {}", e.getMessage());
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }

    private Page getSpecificPage(ResourceResolver resourceResolver, String pageUri) {
        Resource resource = resourceResolver.resolve(pageUri);
        return resource.adaptTo(Page.class);

    }

    private void pageActivationEvent(ResourceResolver resourceResolver, String pageUri, HashSet<String> xmlPostingUrls) throws Exception {

        String xmlDataUrl = pageUri + ".solrsearch.html";
        String xmlData = solrSearchService.getXMLData(resourceResolver, xmlDataUrl);
        int status = 0;
        if (!xmlData.equals("")) {
            String xmlPostingUrl = "";
            Iterator<String> iterator = xmlPostingUrls.iterator();
            log.info(" \n Status for Page URI {} are \n ", pageUri);
            while (iterator.hasNext()) {
                xmlPostingUrl = iterator.next();
                xmlPostingUrl = xmlPostingUrl + "/update/";
                status = solrSearchService.postXMLDataToSolr(xmlData, xmlPostingUrl);
                log.info(" \n Update Status for Solr Posting Url {} = {} \n ", xmlPostingUrl, status);
            }
        } else
            log.info("Nothing To Update as status is {}", status);
    }

    private void pageDeactivationEvent(String pageUri, HashSet<String> xmlPostingUrls) throws Exception {

        Iterator<String> iterator = xmlPostingUrls.iterator();
        log.info(" \n Deactivating the Page with URI {} \n ", pageUri);
        String xmlPostingUrl = "";
        SolrServer solr = null;
        while (iterator.hasNext()) {
            xmlPostingUrl = iterator.next();
            solr = new HttpSolrServer(xmlPostingUrl);
            solr.deleteById(pageUri);
            solr.commit();
            log.info(" \n Update Status for Solr Posting Url {} = {} \n ", xmlPostingUrl, "Deleted");
        }

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
