package com.intelligrape.customhtmlimporter;


import com.day.cq.wcm.api.PageManagerFactory;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.ParserDelegator;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;

@SlingServlet(methods = {"POST","GET"} ,
                paths = {"/htmlimporter/upload"},
                generateComponent = false)
@Component(description = "My custom impoter servlet" ,enabled = true ,immediate = true ,metatype = true)
public class CustomHtmlImporterServlet extends SlingAllMethodsServlet{

    @Reference
    PageManagerFactory pageManagerFactory ;

    private final Logger logger = LoggerFactory.getLogger(CustomHtmlImporterServlet.class);

    @Property(label = "Components mapping" ,value = {"text:foundation/components/text"
            ,"title:foundation/components/title" ,"clickthroughlink:mcm/components/cta-clickthroughlink" ,
            "image:foundation/components/image" ,"graphicallink:mcm/components/cta-graphicallink"}
            ,cardinality = Integer.MAX_VALUE ,
            description = "Enter values for component mapping in format = <label>:<resourceType> [use text ," +
                    " title ,clickthroughlink ,image ,graphicallink keywords to define components]")
    private static final String COMPONENT_MAPPING = "component.mapping";

    String[] componentMapping;

    @Activate
    protected void activate(ComponentContext componentContext)
    {
        componentMapping = (String[])componentContext.getProperties().get("component.mapping");
    }

    @Override
    protected void doPost(SlingHttpServletRequest request ,SlingHttpServletResponse response)
    {
        logger.info("Post method called NEW TEST");
        try
        {
            String targetTemplate = request.getRequestParameter("targetTemplate").toString();
            String targetPagePath = request.getRequestParameter("targetPage").toString();
            String pageName = request.getRequestParameter("pageName").toString();
            String pageTitle = request.getRequestParameter("pageTitle").toString();

            boolean isPageCreated = new CreatePage().createTargetPage(request ,targetTemplate ,targetPagePath ,pageName
                                                  ,pageTitle ,pageManagerFactory);

            //logger.info("Is page created -=>"+isPageCreated);
            if(isPageCreated)
            {
                //logger.info("new logs page created successfully");
                InputStream inputStream = request.getRequestParameter("htmlFile").getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                ChangedHtmlParser changedHtmlParser = new ChangedHtmlParser(request ,targetPagePath+"/"+pageName ,componentMapping);
                changedHtmlParser.parseAndCreateNodes(inputStream);

//                ParserDelegator pd = new ParserDelegator();
//                pd.parse(reader ,new NewHtmlParser(request ,targetPagePath+"/"+pageName ,componentMapping) ,false);
            }

            response.sendRedirect(targetPagePath+"/"+pageName+".html");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Override
    protected void doGet(SlingHttpServletRequest request ,SlingHttpServletResponse response)
    {
        logger.error("Get method called of HTML Servlet");
    }
}
