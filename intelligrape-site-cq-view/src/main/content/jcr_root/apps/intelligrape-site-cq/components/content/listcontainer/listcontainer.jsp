<%--

  List Container component.

  This is the container for the list component

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %>
<%@page import="com.day.cq.wcm.api.WCMMode"%><div class="row-colortoned">
<%
    // TODO add you code here
    if(WCMMode.fromRequest(request)==WCMMode.EDIT){
    	%>
    	List container
    	<%
    }
%>
    <div class="container">
        <div class="row">
            <div class="span12">
                <ul class="list-3col features-centered">
                    <cq:include path="par" resourceType="foundation/components/parsys" />
                </ul>
            </div>
        </div>
    </div>
</div>