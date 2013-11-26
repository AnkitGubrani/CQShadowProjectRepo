package com.intelligrape.intelligrapeComponents;

/**
 * Created with IntelliJ IDEA.
 * User: intelligrape
 * Date: 26/11/13
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServlet extends SlingSafeMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(TestServlet.class);

    @Reference
    EventAdmin eventAdmin;

    public TestServlet() {
    }

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        log.info("inside servlet");
        Dictionary properties = new Hashtable();
        properties.put("title", "geetika");
        properties.put("path", "does not exist");
        Event eventA = new Event("com/acme/customEvent/myevent", properties);
        Event eventB = new Event("com/acme/customEvent/myevent1", properties);
        eventAdmin.postEvent(eventA);
        eventAdmin.postEvent(eventB);
        log.info("servlet finished");
    }

    protected void bindEventAdmin(EventAdmin eventadmin) {
        eventAdmin = eventadmin;
    }

    protected void unbindEventAdmin(EventAdmin eventadmin) {
        if (eventAdmin == eventadmin) eventAdmin = null;
    }


}