<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Text-Image component

  Combines the text and the image component

--%><%@ page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image" %><%
%><%@include file="../../global.jspx"%><%
    Image image = new Image(resource, "image");

    if (image.hasContent() || WCMMode.fromRequest(request) == WCMMode.EDIT) {
        image.loadStyleData(currentStyle);
        // add design information if not default (i.e. for reference paras)
        if (!currentDesign.equals(resourceDesign)) {
            image.setSuffix(currentDesign.getId());
        }
        //drop target css class = dd prefix + name of the drop target in the edit config
        image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "image");
        image.setSelector(".img");
        image.setDoctype(Doctype.fromRequest(request));

        String divId = "cq-textimage-jsp-" + resource.getPath();
        String imageHeight = image.get(image.getItemName(Image.PN_HEIGHT));
        // changing the code to align the text to teh left
        %>
        <h4 style="margin-top: 10px;color: #353535">
        <cq:text property="image/jcr:description" placeholder="" tagName="small"/>
        <span><cq:text property="text" tagClass="text"/></span></h4><%
        
        %> 
        
        <%// div around image for additional formatting
        %><div class="photo" id="<%= divId %>"><%
        %><% image.draw(out); %><br></div>
        <%@include file="/libs/foundation/components/image/tracking-js.jsp"%>
        <%
    }
    %><div class="clear"></div>