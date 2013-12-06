package com.intelligrape.directorycleanup.impl;

import com.intelligrape.directorycleanup.DirectoryCleanUpService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.net.URLEncoder;

@Component(label = "Directory Cleanup component" ,enabled = true ,immediate = true ,metatype = true)
@Service(DirectoryCleanUpService.class)
public class DirectoryCleanUpServiceImpl implements DirectoryCleanUpService {

    @Reference
    SlingRepository slingRepository;

    Logger logger = LoggerFactory.getLogger(DirectoryCleanUpServiceImpl.class);

    @Override
    public void directoryCleanUp(String[] cleanUpNodes) {
        try {
            logger.debug("CleanUp Method");
            Session session = slingRepository.login(new SimpleCredentials("admin", "ankitg90".toCharArray()));
            Node rootNode = session.getRootNode();

            //This loop iterates through all the nodes entered by USER
            for(int index = 0; index < cleanUpNodes.length;index++)
            {
                String cleanUpNode = cleanUpNodes[index];
                if(rootNode.hasNode(cleanUpNode))
                {
                    Node subNode = rootNode.getNode(cleanUpNode);
                    logger.debug("sub node=>"+subNode.getName());

                    NodeIterator nodeItr = subNode.getNodes();
                    //Variable i for deleting 2 Nodes at a time.
                    int i =0 ;
                    while (nodeItr.hasNext())
                    {
                        Node deleteNode = nodeItr.nextNode();
                        logger.debug("Removing the node ="+deleteNode);
                        deleteNode.remove();
                        i++;
                        if(i==2)
                        {
                            break;
                        }
                    }
                }else
                {
                    //If given node does'nt exsists
                    logger.debug("No such node exsists ====");
                }
            }
            session.save();
            session.logout();
        } catch (RepositoryException e) {
            logger.error("Exception-directoryCleanUp", e.getMessage());
        }
        catch (Exception e) {
            logger.error("Exception-directoryCleanUp", e.getMessage());
        }
    }
}