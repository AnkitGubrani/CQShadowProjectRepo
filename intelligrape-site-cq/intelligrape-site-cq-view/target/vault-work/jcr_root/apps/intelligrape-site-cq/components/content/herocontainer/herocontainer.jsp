<%--
  HeroContainer component.

  This is hero container
--%>
<%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%>
<%@page import="java.util.Iterator"%>
<%@page import="com.day.cq.wcm.foundation.Image"%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<div id="slider-bg">
    <div class="container clearfix">
        <div class="row content-top">
            <div class="span12">
                <div id="slider" class="flexslider">
                <div class="flex-viewport" style="overflow: hidden; position: relative;">
                <ul style="width: 1200%; transition: 0s; -webkit-transition: 0s; -webkit-transform: translate3d(-2340px, 0, 0);" class="slides">
                        <!-- Parsys is being added here as Li components -->
                        <cq:include path="par" resourceType="foundation/components/parsys" />
                        
                    </ul></div></div>
            </div>
            <div class="span12">
                <div id="thumb-slider" class="flexslider">
                    
                <div style="overflow: hidden; position: relative;" class="flex-viewport"></div>
                <div class="flex-viewport" style="overflow: hidden; position: relative; ">
                <ul style="width: 1200%; transition: 0s; -webkit-transition: 0s; -webkit-transform: translate3d(0px, 0, 0);" class="slides">
                    
                    <%       
                    Resource parRes = resource.getChild("par"); 
                        if(parRes!=null){
                       Iterator itr = parRes.listChildren();                       
                       while(itr.hasNext())
                       {
                           Resource textImageSlideRes = (Resource) itr.next();
                           ValueMap textImageSlideValueMap = textImageSlideRes.adaptTo(ValueMap.class);
                           Image thumbimg = new Image(textImageSlideRes,"thumbimg");
                           %>
                            <li style="width: 180px; float: left; display: block;" class="">
                            <div class="thumb-text clearfix">
                            <% thumbimg.draw(out); %>
                            <p><% out.println(textImageSlideValueMap.get("thumbtext")); %></p>
                            </div>
                            </li>                           
                           <%
                        }
                       }
                   %>                    
                    </ul></div></div>
            </div>
        </div>
     </div>
  </div>

   