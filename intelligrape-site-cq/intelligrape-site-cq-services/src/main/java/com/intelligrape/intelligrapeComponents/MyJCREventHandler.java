package com.intelligrape.intelligrapeComponents;

/**
 * Created with IntelliJ IDEA.
 * User: intelligrape
 * Date: 26/11/13
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */

import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJCREventHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(MyJCREventHandler.class);


    public MyJCREventHandler() {
    }

    public void activate(ComponentContext context) {
        System.out.println("inside event handler activate");
        String filter = (String) (String) context.getProperties().get("event.filter");
        System.out.println((new StringBuilder()).append("filter is").append(filter).toString());
    }


    @Override
    public void handleEvent(Event event) {
        //To change body of implemented methods use File | Settings | File Templates.
        log.info("in my customised event handled");
        log.info("something has been added");
        String reportPath = (String) event.getProperty("path");
        log.info((new StringBuilder()).append("path is").append(reportPath).toString());
        System.out.println("in my customised event handled");
        System.out.println("something has been added");
        System.out.println((new StringBuilder()).append("path is").append(reportPath).toString());
    }
}

