<%--

  TestPage component.

  This is actual page

--%><%
%><%@include file="/apps/intelligrapeComponents/components/page/global.jsp"%><%
%><%@page session="false" %><%
%><%
    // TODO add you code here
%>


<%@page import="com.day.cq.wcm.foundation.Search"%>
<%@page import="com.day.cq.search.SimpleSearch"%><html>
<cq:include script="head.jsp"/>
<body>
<h3>Test Page For Intelligrape components</h3>

<cq:include path="par" resourceType="/libs/foundation/components/parsys" />
<cq:include script="footer.jsp"/>
</body>
</html>
