package com.intelligrape.directorycleanup;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.ComponentContext;

@SlingServlet(methods = {"GET"} ,
        paths = {"/bin/cleanup/test.html"}
)
public class TestCleanUpServlet extends SlingSafeMethodsServlet {

    @Reference
    DirectoryCleanUpService cleanUpService;

    @Activate
    protected void activate(ComponentContext componentContext)
    {

    }

    @Override
    protected void doGet(SlingHttpServletRequest request ,SlingHttpServletResponse response)
    {
        cleanUpService.directoryCleanUp();
    }
}
