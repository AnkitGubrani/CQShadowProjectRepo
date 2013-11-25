<%--

  Search Component component.

  This is the search component

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%><%
    // TODO add you code here
%>
<div style="float: right;">
<form action="/content/MyLiveCopy/SearchResult.html" method="get">
<input type="text" name = "searchtext" style="width: 200px;"/>
<input type="hidden" value = "${properties.searchin}" name = "searchinpath" />
<input type="submit" value="Search" /> 
</form>
</div>
<br><br>
