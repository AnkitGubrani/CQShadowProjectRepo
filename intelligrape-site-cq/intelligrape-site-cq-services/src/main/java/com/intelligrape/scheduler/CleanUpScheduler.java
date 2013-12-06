package com.intelligrape.scheduler;


import com.intelligrape.directorycleanup.DirectoryCleanUpService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Component(label = "CleanUpScheduler" ,description = "CleanUpScheduler Component" ,immediate = true ,enabled = true
        ,metatype = true )
public class CleanUpScheduler {

    @Property(label = "Cron Expression" ,value = "0 0/2 * 1/1 * ? *" ,
            description = "Enter the cron expression to run the Clean Up service")
    private static final String CRON_EXP = "cron.exp";

    @Property(label = "CleanUp Nodes" , value = {"CleanUpNode","CleanUpNode1"} ,cardinality = Integer.MAX_VALUE ,
            description = "Enter the name of the nodes on which clean up is to be performed")
    private static final String CLEANUP_NODES = "multi.cleanup.nodes";

    @Reference
    Scheduler scheduler;

    @Reference
    DirectoryCleanUpService cleanUpService;

    protected final Logger logger = LoggerFactory.getLogger(CleanUpScheduler.class);

    private String[] cleanUpNodes;

    @Activate
    protected void activate(ComponentContext componentContext)
    {
        logger.debug("Activate method called");
        Dictionary dictionary = componentContext.getProperties();
        //Getting the CRON EXP entered by User
        String cronExp =(String) dictionary.get("cron.exp");

        logger.debug("===============new value =" + CRON_EXP + " read property =" + cronExp);

        //Getting list of all nodes that are to be cleaned up
        cleanUpNodes = (String[])dictionary.get("multi.cleanup.nodes");

        String jobName = "CleanUpJob";
        Map<String ,Serializable> config = new HashMap<String, Serializable>();
        boolean canRunConcurrently = true;

        //Creating a job.
        Runnable cleanUpJob = new Runnable() {
            @Override
            public void run() {
                logger.debug("Strated the service");
                cleanUpService.directoryCleanUp(cleanUpNodes);
            }
        };

        try {
            scheduler.addJob(jobName ,cleanUpJob ,config ,cronExp ,canRunConcurrently);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("CleanUpScheduler EXCEPTION");
            logger.error(e.getMessage());
        }
    }

    @Modified
    protected void update(ComponentContext componentContext)
    {
        //Calling the activate if configuration gets modified.
        logger.debug("Modified the configuration Calling activate");
        activate(componentContext);
    }

    @Deactivate
    public void deactivate(ComponentContext componentContext)
    {
        //Removing a job before deactivating component.
        scheduler.removeJob("CleanUpJob");
        logger.debug("Removing the clean up job");
    }
}
