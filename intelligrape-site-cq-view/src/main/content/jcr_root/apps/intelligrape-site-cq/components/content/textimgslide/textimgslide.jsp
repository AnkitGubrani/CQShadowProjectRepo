<%--

  Slide component.

  This is a custom slide for adding both image and text. 

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %>
<%@page import="com.day.cq.commons.Doctype,
    com.day.cq.wcm.api.WCMMode,
    com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.Image" %>
<%
%><%
    // TODO add you code here
%> 

<%
 Image image = new Image(resource, "mainimg");
// out.println("TEST=>"+image);
    if (image.hasContent() || WCMMode.fromRequest(request) == WCMMode.EDIT) {
        image.loadStyleData(currentStyle);
        // add design information if not default (i.e. for reference paras)
        if (!currentDesign.equals(resourceDesign)) {
            image.setSuffix(currentDesign.getId());
        }
        
         //drop target css class = dd prefix + name of the drop target in the edit config
        image.addCssClass(DropTarget.CSS_CLASS_PREFIX + "mainimg");
        image.setSelector(".img");
        image.setDoctype(Doctype.fromRequest(request));
        
        String divId = "cq-textimage-jsp-" + resource.getPath();
        String imageHeight = image.get(image.getItemName(Image.PN_HEIGHT));
        // div around image for additional formatting
        %>
        
        <% if (WCMMode.fromRequest(request) != WCMMode.EDIT) { %>
        
        <li class="" style="width: 1170px; float: left; display: block;">
                            <div class="row">
                                <div class="span6">
                                    <h2>${properties.imgtext}</h2>                                    
                                </div>
                                <div class="span6"><% if(image!=null){image.draw(out);}else{out.println("No image added");} %></div>
                            </div>
                        </li>        
        <%
        }else{        
        %>Edit Slide<%       
        }
    
        %><cq:text property="image/jcr:description" placeholder="" tagName="small"/>
        <%
    }
    %>