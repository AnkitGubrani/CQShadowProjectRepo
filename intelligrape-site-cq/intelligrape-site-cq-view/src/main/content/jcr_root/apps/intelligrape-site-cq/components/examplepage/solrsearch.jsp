<%@page import="com.day.cq.wcm.api.WCMMode"%>
<%
    WCMMode mode=WCMMode.fromRequest(request);
	if(mode==WCMMode.EDIT || mode ==WCMMode.DESIGN)
		WCMMode.DISABLED.toRequest(request);
	String url = request.getRequestURL().toString();
	url=url.replace("solrsearch.html","html");
%>
<%@include file="/apps/intelligrape-site-cq/components/page/global.jsp"%>
<add>
    <doc>
		<field name="id"><%=url%></field>
		<cq:include path="par" resourceType="foundation/components/parsys"/>
    </doc>
</add>