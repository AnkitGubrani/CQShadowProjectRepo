package com.intelligrape.directorycleanup.impl;

import com.intelligrape.directorycleanup.DirectoryCleanUpService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;

@Component(label = "Directory Cleanup component" ,enabled = true ,immediate = true ,metatype = true)
@Service(DirectoryCleanUpService.class)
public class DirectoryCleanUpServiceImpl implements DirectoryCleanUpService {

    @Reference
    SlingRepository slingRepository;

    Logger logger = LoggerFactory.getLogger(DirectoryCleanUpServiceImpl.class);

    @Override
    public void directoryCleanUp() {
        try {
            Session session = slingRepository.login(new SimpleCredentials("admin", "ankitg90".toCharArray()));
            Node rootNode = session.getRootNode();

            if(rootNode.hasNode("CleanUpNode"))
            {
                Node subNode = rootNode.getNode("CleanUpNode");
                NodeIterator nodeItr = subNode.getNodes();
                while (nodeItr.hasNext())
                {
                    Node deleteNode = nodeItr.nextNode();
                    System.out.println("Removing the node ="+deleteNode);
                    deleteNode.remove();
                }
            }else
            {
                System.out.println("No such node exsists ====");
            }
            session.save();
            session.logout();
        } catch (RepositoryException e) {
            System.out.println("RepositoryException -=>"+e.getLocalizedMessage()+" message "+e.getMessage());
            logger.error("Exception-directoryCleanUp", e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Exception -=>"+e.getLocalizedMessage()+" message "+e.getMessage());
            logger.error("Exception-directoryCleanUp", e.getMessage());
        }
    }
}