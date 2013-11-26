
    <%--

  Teaser  component.

  This component is to be used to display the Client Reviews

--%>
<%%><%@include file="../../global.jspx"%>
<%%><%@page session="false"%>
<%%>
<%
    // TODO add you code here
%>

<div class="container row-general">
    <div class="row">
        <div class="span4">
            <div class="clearfix"></div>
            <div class="box-styled">
                <div class="box-head clearfix">
<!--                    <h4 style="margin-top: 10px;">BRIAN ANDERSON <span>Founder&Managing Partner at Media Mash</span></h4>-->
<!---->
<!--                    <div class="photo"><img src="http://www.intelligrape.com/images/testimonials/xbrian.png.pagespeed.ic._xXWCaQ0Mp.png" alt="img"></div>-->

                <cq:include path="tpar" resourceType="../components/content/textimage" />
                </div>
                <div class="box-body review"> 
                <cq:include path="clientReviews" resourceType="foundation/components/text" />
                </div>
            </div>
        </div>
        
        </div></div>