<%--

  Home page component.

  This is the home page component for intelligrape website

--%>
<%
%>
<%
%><%@page session="false"%>
<%
%>
<%
    // TODO add you code here
%>

<%@include file="../../global.jspx"%>
<%@ page import="java.util.Iterator,
        com.day.text.Text,
        com.day.cq.wcm.api.PageFilter, com.day.cq.wcm.api.Page" %>


<section  class="clearfix">
<div id="header-inner" class="container">
<div class="row">

<div class="span4 logo"><cq:include path="logoCust"
    resourceType="foundation/components/logo" /></div>

<div class="span8">
<div id="smoothmenu" class="ddsmoothmenu">
<ul>

<%

    // get starting point of navigation
    Page navRootPage = currentPage.getAbsoluteParent(1);
    if (navRootPage == null && currentPage != null) {
    navRootPage = currentPage;
    }
    if (navRootPage != null) {
        Iterator<Page> children = navRootPage.listChildren(new PageFilter(request));
        while (children.hasNext()) {
            Page child = children.next();
            %><li><a href="<%= child.getPath() %>.html"><%=child.getNavigationTitle() %></a><% 
              Iterator<Page> childPages = child.listChildren(new PageFilter(request));%>
              <ul><%
                while (childPages.hasNext()) {
                   Page childPage = childPages.next();
                   %> <li><a href="<%= childPage.getPath() %>.html"><%
                   String navTitle=childPage.getNavigationTitle();
                    if(navTitle!=null){%><%=navTitle %>
                    <% }
                   else{ %> <%= childPage.getPageTitle() %>
                   <% }%> </a> </li><%
                   }
                   %>
                  </ul>        
           
            </li><%
        }
    }
%>
</ul> 
</div>
</div>
</div>
</div>
<div id="header-btm">
<div class="container">
<div class="row">
<div class="span12">
<p><cq:include path="contactUs"
    resourceType="foundation/components/image" />Contact Us
    <cq:include path="search" resourceType="/libs/foundation/components/search"/>
</p>
</div>
</div>
</div>
</div>
</section>
