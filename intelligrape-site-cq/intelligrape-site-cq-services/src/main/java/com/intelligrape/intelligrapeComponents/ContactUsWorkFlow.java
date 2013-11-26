package com.intelligrape.intelligrapeComponents;


/**
 * Created with IntelliJ IDEA.
 * User: intelligrape
 * Date: 26/11/13
 * Time: 10:05 PM
 * To change this template use File | Settings | File Templates.
 */



import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

public class ContactUsWorkFlow implements WorkflowProcess{

    public ContactUsWorkFlow() {
    }

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        System.out.println("workflow executed");
    }

}

