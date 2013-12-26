<%@include file="/apps/intelligrape-site-cq/components/page/global.jsp"%>
<%@page import="com.intelligrape.common.util.CommonMethods" %>
<%
     String html=properties.get("text", "");
	html=CommonMethods.parseHtmlToText(html);
%>
<field name="text_data"><%=html%></field>