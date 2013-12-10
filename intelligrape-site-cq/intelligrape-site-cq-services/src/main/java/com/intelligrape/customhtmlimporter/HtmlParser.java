package com.intelligrape.customhtmlimporter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser extends HTMLEditorKit.ParserCallback{

    Integer titleComponentSuffix = 0;
    Integer textComponentSuffix = 0;
    Integer textLinkComponentSuffix = 0;
    Integer imageComponentSuffix = 0;

    StringBuffer htmlBuffer = new StringBuffer();
    String componentName ;
    String titleType;
    String pagePath ;
    String linkUrl ;

    boolean isReadChar = false;
    List openedTags = new ArrayList();

    SlingHttpServletRequest request;
    Node canavsNode ;

    private static final Logger logger = LoggerFactory.getLogger(HtmlParser.class);

    public HtmlParser(SlingHttpServletRequest request ,String pagePath)
    {
        this.request = request;
        this.pagePath = pagePath;
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {

        if(t.toString().matches("(div)") && a.containsAttribute(HTML.Attribute.ID ,"cqcanvas"))
        {
            //handling  div id = "cqcanvas"
            //===> Create a node in CONTENT with name canvas
            logger.info("DIV cqCanvas COMPONENT FOUND");
            canavsNode = new CreateNodes().createCanvasNode(request , pagePath);
        }
//=============================================TITLE COMPONENT STarts HERE=====================================
        else if(t.toString().matches("[hH][1-6]") && a.containsAttribute("data-cq-component","title"))
        {
            //handling head tag for titleType
            componentName = "title";
            openedTags.add(t.toString().toLowerCase());
            this.titleType = t.toString().toLowerCase();
            isReadChar = true;
        }
//=============================================TITLE COMPONENT ENDS HERE======================================
//=============================================TEXT COMPONENT STarts HERE=====================================
        else if(t.toString().matches("[pP]") && a.containsAttribute("data-cq-component","text"))
        {
            //handling Text Component
            componentName = "text";
            openedTags.add(t.toString().toLowerCase());
            isReadChar = true;
        }
//=============================================TEXT COMPONENT ENDS HERE=======================================
//=============================================clickThroughLink COMPONENT STarts HERE===========================
        else if(t.toString().matches("[aA]") && a.containsAttribute("data-cq-component","clickThroughLink"))
        {
            //handling clickThroughLink component
            openedTags.add(t.toString().toLowerCase());
            componentName = "textlink";
            linkUrl = a.getAttribute(HTML.Attribute.HREF).toString();
            isReadChar = true;
        }
//=============================================clickThroughLink COMPONENT ENDS HERE=======================================
    }

    public void handleText(char[] data, int pos) {
        if(isReadChar)
        {
            //reading the data only when to read
            htmlBuffer = new StringBuffer();
            for(char ch : data)
            {htmlBuffer.append(ch);}
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        //Title Component
        if(t.toString().matches("[hH][1-6]") && openedTags.contains(t.toString().toLowerCase()))
        {
            openedTags.remove(t.toString().toLowerCase());
            Map base = new HashMap();
            base.put("jcr:title", this.htmlBuffer.toString());
            base.put("type", this.titleType);
            base.put("sling:resourceType","foundation/components/title");

            if(titleComponentSuffix != 0)
            {
                componentName = componentName.substring(0,(componentName.indexOf("e")+1))+"_"+titleComponentSuffix;
            }
            titleComponentSuffix++;
            new CreateNodes().createComponent(request ,canavsNode ,base ,componentName);
        }
        //Text Component
        if(t.toString().matches("[pP]") && openedTags.contains(t.toString().toLowerCase()))
        {
            //Creating a node under canvas node
            openedTags.remove(t.toString().toLowerCase());
            Map base = new HashMap();
            base.put("text", this.htmlBuffer.toString());
            base.put("textIsRich",Boolean.valueOf(true));
            base.put("sling:resourceType","foundation/components/text");

            if(textComponentSuffix != 0)
            {
                componentName = componentName.substring(0,(componentName.lastIndexOf("t")+1))+"_"+textComponentSuffix;
            }
            new CreateNodes().createComponent(request ,canavsNode ,base ,componentName);
            textComponentSuffix++;
        }
        //clickThroughLink Component
        if(t.toString().matches("[aA]") && openedTags.contains(t.toString().toLowerCase()))
        {
            //Creating a node under canvas node
            openedTags.remove(t.toString().toLowerCase());
            Map base = new HashMap();
            base.put("label", this.htmlBuffer.toString());
            base.put("href", linkUrl);
            base.put("sling:resourceType","mcm/components/cta-clickthroughlink");

            if(textLinkComponentSuffix != 0)
            {
                componentName = componentName.substring(0,(componentName.lastIndexOf("k")+1))+"_"+textLinkComponentSuffix;
            }
            new CreateNodes().createComponent(request ,canavsNode ,base ,componentName );
            textLinkComponentSuffix++;
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {

        if(t.toString().matches("(img)") && a.containsAttribute("data-cq-component","image"))
        {
            //handling Simple Image component
            componentName = "image";
            Map propertiesMap = new HashMap();
            a.removeAttribute("data-cq-component");

            Enumeration attrKey = a.getAttributeNames();
            while (attrKey.hasMoreElements())
            {
                Object key = attrKey.nextElement();
                Object value = a.getAttribute(key);
                propertiesMap.put(key.toString(),value.toString());
            }
            propertiesMap.put("sling:resourceType" ,"foundation/components/image");

            if(imageComponentSuffix != 0)
            {
                componentName = componentName.substring(0,(componentName.lastIndexOf("e")+1))+"_"+imageComponentSuffix;
            }

            new CreateNodes().createImageComponent(request ,canavsNode ,propertiesMap ,componentName ,pagePath);
            imageComponentSuffix++;
        }
    }
}
