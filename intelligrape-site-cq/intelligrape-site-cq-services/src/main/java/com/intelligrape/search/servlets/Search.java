package com.intelligrape.search.servlets;


import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Component(label = "Solr search servlet", enabled = true, immediate = true, metatype = true)
@Service
@Properties({
        @Property(name = "service.description", value = "Solr Descriptors Servlet"),
        @Property(name = "service.vendor", value = "Intelligrape"),
        @Property(name="sling.servlet.paths", value = "/bin/solr/search.html", propertyPrivate = true)
})
public class Search extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(Search.class);

    private static final String PROP_SEARCH_ENGINE_URL = "search.engine.url";

    @Property(name = PROP_SEARCH_ENGINE_URL, description = "Enter the search engine url excluding the collection/core name",
    value = "http://localhost:8983/solr/", propertyPrivate = false)
    private String SEARCH_URL;

    @Activate
    protected void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        SEARCH_URL = properties.get(PROP_SEARCH_ENGINE_URL).toString();

        log.info("Configured Search url, = {}", SEARCH_URL);

    }

    @Modified
    protected  void modified(ComponentContext componentContext){
        log.info("Values have been modified");
        activate(componentContext);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        String query = request.getParameter("query");
        if (query.equals(""))
            query = "*:*";
        String core = request.getParameter("core");
        String maxRows = request.getParameter("maxRows");
        String url = SEARCH_URL + core;

        try {
            PrintWriter out = response.getWriter();
            JSONObject jsonList = new JSONObject();
            jsonList = getSolrResponseAsJson(url, maxRows, core, query);
            out.print(jsonList);
        } catch (Exception e) {
            log.info("Error during quering Data =" + e);
        }

    }

    private JSONObject getSolrResponseAsJson(String url, String maxRows, String core, String query) throws Exception {

        SolrServer solr = new HttpSolrServer(url);
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setFields("id","text_data","title");
        solrQuery.setStart(0);
        solrQuery.setRows(Integer.parseInt(maxRows));

        QueryResponse qp = solr.query(solrQuery);
        SolrDocumentList solrDocumentList = qp.getResults();

        JSONObject jsonList = new JSONObject();
        jsonList = parseResponse(solrDocumentList);
        return jsonList;
    }


    private JSONObject parseResponse(SolrDocumentList solrDocumentList) throws Exception {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> solrDocMap = new HashMap<String, Object>();
        String key = "";
        for (int i = 0; i < solrDocumentList.size(); ++i) {
            for (Map.Entry<String, Object> stringObjectEntry : solrDocumentList.get(i)) {
                key = stringObjectEntry.getKey();
                solrDocMap.put(key, stringObjectEntry.getValue());
            }
            jsonArray.put(solrDocMap);
        }
        jsonObject.put("solrDocs", jsonArray);
        return jsonObject;
    }


}
