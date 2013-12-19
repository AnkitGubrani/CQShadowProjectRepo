<%@include file="/apps/intelligrape-site-cq/components/page/global.jsp"%>
<%@page import="com.intelligrape.common.util.CommonMethods" %>
<%
    String html=properties.get("jcr:description", "");
	String title=properties.get("jcr:title", "");
	html=CommonMethods.parseHtmlToText(html);
	title=CommonMethods.parseHtmlToText(title);
%>
<field name="title"><%=title%></field>
<field name="description"><%=html%></field>