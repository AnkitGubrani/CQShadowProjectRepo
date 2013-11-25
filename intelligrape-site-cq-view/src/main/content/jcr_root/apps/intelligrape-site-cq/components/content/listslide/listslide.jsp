<%--

  List Slide component.

  This is slide of List container

--%><%
%><%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@page import="com.day.cq.wcm.foundation.Image"%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<% String linkResourcePath =  properties.get("linkpath","No Link Path"); 
   Resource linkResource = resourceResolver.getResource(linkResourcePath);
   ValueMap pageContentValMap = null;
   Image logoImg = logoImg = new Image(resource,"logoimg");
   if(linkResource!=null)
   {
	   Resource pageContent = linkResource.getChild("jcr:content");
	   pageContentValMap = pageContent.adaptTo(ValueMap.class);
	   
   }   
   if(WCMMode.fromRequest(request)==WCMMode.EDIT)
   {%>
        List Slide
   <%	   
   }else
   { %>
		<li class="span3">
	        <div class="image"><a href="<%out.println(linkResourcePath+".html");%>"><% if(logoImg!=null){logoImg.draw(out);}else{out.println("No image added");} %></a></div>
	            <h4><a href="<%out.println(linkResourcePath+".html");%>"><% if(pageContentValMap!=null){out.println(pageContentValMap.get("pageTitle"));} %></a></h4>
	                <div class="divider-arrow"></div>
	            <% if(pageContentValMap!=null){out.println(pageContentValMap.get("jcr:description"));}else{out.println("No link added");} %>
	    </li>
	<% }
%>