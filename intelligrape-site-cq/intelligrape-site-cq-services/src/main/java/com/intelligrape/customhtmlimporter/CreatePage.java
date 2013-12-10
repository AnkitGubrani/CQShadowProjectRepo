package com.intelligrape.customhtmlimporter;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;

public class CreatePage {

    private static Logger logger = LoggerFactory.getLogger(CreatePage.class);

    public boolean createTargetPage(SlingHttpServletRequest request, String targetTemplate ,String targetPagePath
                                    ,String pageName , String pageTitle,PageManagerFactory managerFactory)
    {
        boolean isPageCreated = false;
        try{
            PageManager pageManager = managerFactory.getPageManager(request.getResourceResolver());
            Page targetPage = pageManager.getPage(targetPagePath);

            if(targetPage!=null && (pageManager.getPage(targetPagePath+"/"+pageName))==null)
            {
                Page createdPage = pageManager.create(targetPagePath ,pageName ,targetTemplate ,pageTitle);
                if(createdPage !=null)
                isPageCreated = true;
            }else
            {
                //logger.info("cannot create this page as it already exsists");
            }
        }catch (Exception e)
        {
            logger.error(e.getLocalizedMessage()+e.getMessage()+e.toString());
        }
        return isPageCreated;
    }
}
