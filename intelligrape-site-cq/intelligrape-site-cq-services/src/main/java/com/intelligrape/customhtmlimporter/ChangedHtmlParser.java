package com.intelligrape.customhtmlimporter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ChangedHtmlParser
{
    SlingHttpServletRequest request;
    Node canavsNode;
    String[] componentMapping;
    String pagePath;

    Logger logger = LoggerFactory.getLogger(ChangedHtmlParser.class);

    public ChangedHtmlParser(SlingHttpServletRequest request ,String pagePath ,String[] componentMapping)
    {
        this.request = request;
        this.pagePath = pagePath;
        this.componentMapping = componentMapping;
    }

    public void parseAndCreateNodes(InputStream inputStream)
    {
        try{
            String componentName = "";
            Integer textComponentSuffix = 0;
            Integer imageComponentSuffix = 0;
            Integer textLinkComponentSuffix = 0;
            Integer otherComponentSuffix = 0;
            Map componentProperties = new HashMap();

            Document doc = Jsoup.parse(inputStream, "UTF-8" ,"");

            Elements idElements = doc.body().getElementsByAttribute("id");
            for(Element element : idElements)
            {
                if(element.tag().toString().equalsIgnoreCase("div")
                        && element.attr("id").equalsIgnoreCase("cqcanvas"))
                {
                    logger.info("DIV cqCanvas COMPONENT FOUND ");
                    canavsNode = new CreateNodes().createCanvasNode(request , pagePath);
                }
            }

            Elements elements = doc.body().getElementsByAttribute("data-cq-component");
            for(Element el : elements)
            {
                if((el.attr("data-cq-component").equalsIgnoreCase("text"))
                        && (el.tag().toString().equalsIgnoreCase("div")))
                {
                    componentName = "text";
                    componentProperties = new HashMap();
                    for(String compo : componentMapping)
                    {
                        if(el.attr("data-cq-component").equalsIgnoreCase(compo.split("!")[0]))
                        {
                            String[] splitedProps = compo.split("!");
                            if(splitedProps.length > 2)
                            {
                                if(splitedProps[0].equalsIgnoreCase("text"))
                                {
                                    componentProperties.put("sling:resourceType",compo.split("!")[1]);
                                }
                                if(!splitedProps[2].equalsIgnoreCase(""))
                                {
                                    componentProperties.put(splitedProps[2] ,el.html().replaceAll("\n",""));
                                }
                            }
                        }
                    }
                    componentProperties.put("textIsRich",Boolean.valueOf(true));
                    componentName = componentName+"_"+textComponentSuffix;
                    new CreateNodes().createComponent(request ,canavsNode ,componentProperties ,componentName);
                    textComponentSuffix++;
                }else if((el.attr("data-cq-component").equalsIgnoreCase("clickThroughLink"))
                        && (el.tag().toString().equalsIgnoreCase("div")))
                {
                    componentName = "textlink";
                    componentProperties = new HashMap();
                    for(String components : componentMapping)
                    {
                        if(components.split("!")[0].equalsIgnoreCase("clickThroughLink"))
                        {
                            componentProperties.put("sling:resourceType",components.split("!")[1]);
                        }
                    }
                    componentProperties.put("href" ,el.child(0).attr("href"));
                    componentProperties.put("label" ,el.child(0).text());
                    componentName = componentName+"_"+textLinkComponentSuffix;
                    new CreateNodes().createComponent(request ,canavsNode ,componentProperties ,componentName);
                    textLinkComponentSuffix++;
                }else if((el.attr("data-cq-component").equalsIgnoreCase("image"))
                        && (el.tag().toString().equalsIgnoreCase("div")))
                {
                    componentName = "image";
                    componentProperties = new HashMap();
                    for(String components : componentMapping)
                    {
                        if(components.split("!")[0].equalsIgnoreCase("image"))
                        {
                            componentProperties.put("sling:resourceType",components.split("!")[1]);
                        }
                    }
                    for(Attribute attribute : el.child(0).attributes())
                    {
                        componentProperties.put(attribute.getKey() ,attribute.getValue());
                    }
                    componentName = componentName+"_"+imageComponentSuffix;
                    new CreateNodes().createImageComponent(request ,canavsNode ,componentProperties ,componentName ,pagePath);
                    imageComponentSuffix++;
                }else if((el.attr("data-cq-component").equalsIgnoreCase("clickThroughGraphicalLink"))
                        && (el.tag().toString().equalsIgnoreCase("div")))
                {
                    componentName = "graphicallink";
                    componentProperties = new HashMap();
                    for(String components : componentMapping)
                    {
                        if(components.split("!")[0].equalsIgnoreCase("graphicallink"))
                        {
                            componentProperties.put("sling:resourceType",components.split("!")[1]);
                        }
                    }
                    for(Attribute attribute : el.child(0).attributes())
                    {
                        if(attribute.getKey().equalsIgnoreCase("href"))
                        {componentProperties.put("targetURL" ,attribute.getValue());}
                        else
                        {componentProperties.put(attribute.getKey() ,attribute.getValue());}
                    }
                    for(Attribute attribute : el.child(0).child(0).attributes())
                    {
                        componentProperties.put(attribute.getKey() ,attribute.getValue());
                    }
                    componentName = componentName+"_"+imageComponentSuffix;
                    new CreateNodes().createImageComponent(request ,canavsNode ,componentProperties ,componentName ,pagePath);
                    imageComponentSuffix++;
                }
                else
                {
                    for(String components : componentMapping)
                    {
                        if((el.attr("data-cq-component").equalsIgnoreCase(components.split("!")[0]))
                                && (el.tag().toString().equalsIgnoreCase("div")))
                        {
                            componentName = components.split("!")[0];
                            componentProperties = new HashMap();
                            componentProperties.put("sling:resourceType",components.split("!")[1]);

                            String[] splitedProps = components.split("!");
                            if(splitedProps.length > 2)
                            {
                                if(!splitedProps[2].equalsIgnoreCase(""))
                                {
                                    componentProperties.put(splitedProps[2],el.html());
                                }
                            }
                            break;
                        }
                    }
                    componentName = componentName+"_"+otherComponentSuffix;
                    new CreateNodes().createComponent(request ,canavsNode ,componentProperties ,componentName );
                    otherComponentSuffix++;
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage()+e.getLocalizedMessage()+e.fillInStackTrace());
        }
    }
}