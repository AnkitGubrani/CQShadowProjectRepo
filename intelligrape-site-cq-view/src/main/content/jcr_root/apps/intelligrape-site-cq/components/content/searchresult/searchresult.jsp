
<%@page import="com.day.cq.search.result.Hit"%>
<%@page import="com.day.cq.search.result.SearchResult"%>
<%@page import="com.day.cq.search.Query"%>
<%@page import="com.day.cq.search.PredicateGroup"%>
<%@page import="com.intelligrape.intelligrapeComponents.SearchWrapper"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%><%--

  Search Result component.

  This is result page 

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%
    // TODO add you code here
    SearchWrapper searchWrapper = sling.getService(SearchWrapper.class);
    String searchText = (String)request.getParameter("searchtext");
    String searchInPath = (String)request.getParameter("searchinpath");
    
    SearchResult result = searchWrapper.getResults(searchText ,searchInPath ,slingRequest);
    if(result.getHits().size() > 0)
    {
    	for(Hit hit : result.getHits())
        {
            out.println("<a href="+hit.getPath()+".html>"+hit.getTitle()+"</a><br>");
        }
    }else
    {
    	%>
    	   <br><h2>No match found !</h2>
    	<%
    }
%>