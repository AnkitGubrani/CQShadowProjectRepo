package com.intelligrape.customhtmlimporter;


import com.day.cq.wcm.api.PageManagerFactory;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.ParserDelegator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SlingServlet(methods = {"POST","GET"} ,
                paths = {"/htmlimporter/upload"},
                generateComponent = false)
@Component(description = "My custom impoter servlet" ,enabled = true ,immediate = true ,metatype = true)
public class CustomHtmlImporterServlet extends SlingAllMethodsServlet{

    private static final Logger logger = LoggerFactory.getLogger(CustomHtmlImporterServlet.class);

    @Reference
    PageManagerFactory pageManagerFactory ;

    @Override
    protected void doPost(SlingHttpServletRequest request ,SlingHttpServletResponse response)
    {
        logger.info("Post method called");
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
                InputStream inputStream = request.getRequestParameter("htmlFile").getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                ParserDelegator pd = new ParserDelegator();
                pd.parse(reader ,new HtmlParser(request ,targetPagePath+"/"+pageName) ,false);
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
        System.out.println("Get method called of HTML Servlet");
    }
}
