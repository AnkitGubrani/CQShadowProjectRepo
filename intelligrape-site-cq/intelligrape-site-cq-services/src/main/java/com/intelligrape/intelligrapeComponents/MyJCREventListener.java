package com.intelligrape.intelligrapeComponents;

/**
 * Created with IntelliJ IDEA.
 * User: intelligrape
 * Date: 26/11/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */


import javax.jcr.*;
import javax.jcr.observation.*;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJCREventListener implements EventListener {
    private Session adminSession;
    private static final Logger log = LoggerFactory.getLogger(MyJCREventListener.class);

    @Reference
    SlingRepository repository;
    public MyJCREventListener() {
    }

    public void activate(ComponentContext context) throws Exception {
        try {
            log.info("in activate method");
            adminSession = repository.loginAdministrative(null);
            adminSession.getWorkspace().getObservationManager().addEventListener(this, 3, "/content/intelligrapetestWebsite", true, null, null, false);
        } catch (RepositoryException e) {
            throw new Exception(new StringBuilder().append("Exception occured").append(e).toString());
        }
    }

    public void deactivate() {
        log.info("in deActivate method");
        if (adminSession != null) adminSession.logout();
    }

    public void onEvent(EventIterator eventIterator) {
        try {
            for (; eventIterator.hasNext(); log.info((new StringBuilder()).append("Something has been changed").append(eventIterator.nextEvent().getPath()).toString()))
                ;
        } catch (RepositoryException e) {
            log.info((new StringBuilder()).append("Exception occured while handling event").append(e).toString());
        }
    }


}

