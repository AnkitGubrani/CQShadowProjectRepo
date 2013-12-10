package com.intelligrape.customhtmlimporter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CreateNodes {

    private static final Logger logger = LoggerFactory.getLogger(CreateNodes.class);

    public Node createCanvasNode(SlingHttpServletRequest request ,String pagePath)
    {
        //Method to create CANVAS Node for parsys
        Node canvasNode = null;
        try{
            Resource resource = request.getResourceResolver().getResource(pagePath);
            //logger.info("Res ="+resource.getName());
            Resource childRes = resource.getChild("jcr:content");
            //logger.info("Child res=>>" + childRes.getName() + childRes.getPath());
            Node childResNode = childRes.adaptTo(Node.class);
            if(childResNode.hasNode("canvas"))
            {
                childResNode.getNode("canvas").remove();
            }
            canvasNode = childResNode.addNode("canvas");
            canvasNode.setProperty("sling:resourceType" , "foundation/components/parsys");

            Session session = request.getResourceResolver().adaptTo(Session.class);
            session.save();
        }
        catch (Exception e)
        {
            logger.error("error=>"+e.getMessage()+" localized messgae=>"+e.getLocalizedMessage()+" exception="+e.toString());
        }
        return canvasNode;
    }

    public void createComponent(SlingHttpServletRequest request,Node canvasNode,Map propertiesMap,
                                        String nodeName)
    {
        //Method to create components other than image component
        try{
            if(canvasNode != null)
            {
                Node componentNode = canvasNode.addNode(nodeName);
                Iterator mapItr = propertiesMap.entrySet().iterator();
                while (mapItr.hasNext())
                {
                    Map.Entry entry = (Map.Entry)mapItr.next();
                    componentNode.setProperty(entry.getKey().toString() ,entry.getValue().toString());
                }
                Session session = request.getResourceResolver().adaptTo(Session.class);
                session.save();
            }
        }catch (Exception e)
        {
            logger.error("error=>"+e.getMessage()+" localized messgae=>"+e.getLocalizedMessage()+" exception="+e.toString());
        }
    }

    public void createImageComponent(SlingHttpServletRequest request,Node canvasNode,Map propertiesMap,
                                        String nodeName ,String pathPage)
    {
        try{
            if(canvasNode != null)
            {
                Session session = request.getResourceResolver().adaptTo(Session.class);
                Node imageNode = canvasNode.addNode(nodeName);
                Iterator itr = propertiesMap.entrySet().iterator();
                while (itr.hasNext())
                {
                    Map.Entry entry = (Map.Entry) itr.next();
                    if(entry.getKey().toString().equalsIgnoreCase("src"))
                    {
                        File file = new File(entry.getValue().toString());
                        if(file.exists())
                        {
                            Node fileNode = imageNode.addNode("file" , "nt:file");
                            Node jcrNode = fileNode.addNode(Node.JCR_CONTENT ,"nt:resource");
                            FileInputStream fin = new FileInputStream(file);
                            jcrNode.setProperty("jcr:data",fin);
                            jcrNode.setProperty("jcr:mimeType" ,"image/jpeg");
                        }else
                        {
                            imageNode.setProperty("fileReference" ,entry.getValue().toString());
                        }
                    }else if(entry.getKey().toString().equalsIgnoreCase("title"))
                    {
                        imageNode.setProperty("jcr:title" ,entry.getValue().toString());
                    }
                    else
                    {
                        imageNode.setProperty(entry.getKey().toString() ,entry.getValue().toString());
                    }
                }
                session.save();
            }
        }catch (Exception e)
        {
            logger.error("error=>"+e.getMessage()+" localized messgae=>"+e.getLocalizedMessage()+" exception="+e.toString());
            e.printStackTrace();
        }
    }
}