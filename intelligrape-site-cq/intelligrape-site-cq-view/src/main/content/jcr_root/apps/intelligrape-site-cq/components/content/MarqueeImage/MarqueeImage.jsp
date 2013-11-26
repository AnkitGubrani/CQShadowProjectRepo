
    <%--

  Marquee Image component.

  This component is used to display carousel images and marquee

--%><%@ page
    import="com.day.cq.wcm.api.WCMMode,com.day.cq.wcm.api.components.DropTarget,
    com.day.cq.wcm.foundation.List,java.util.Iterator,
    com.day.cq.wcm.foundation.Image,org.apache.sling.api.resource.ValueMap"%>
<%%><%@include file="../../global.jspx"%>
<%%><%@page session="false"%>
<%%>
<%
    // TODO add you code here
%>
<%
    WCMMode mode = WCMMode.fromRequest(request);

    if (mode == WCMMode.EDIT || mode == WCMMode.DESIGN) {
%>
<cq:include path="title" resourceType="foundation/components/title" />
<br />
<cq:include path="tpar" resourceType="foundation/components/parsys" />
<%
    } else {
        Resource titleComp = resource.getChild("title");
        ValueMap titleCompValueMap = titleComp.adaptTo(ValueMap.class);
        String action = properties.get("actionType", "marq");
        System.out.println("action is"+action);
        Resource parRes = resource.getChild("tpar");
        System.out.println("pars is"+parRes);
           if (parRes != null) {
               Iterator itr = parRes.listChildren();
                  if (action.equalsIgnoreCase("marq")) {
                        %>
                        <div class="section_area clients">
                            <div class="container">
                                <div class="row">
                                    <div class="span12">
                                        <h2><% out.println(titleCompValueMap.get("jcr:title")); %></h2>
                                        <div id="myMarquee" class="clients-list">
                                            <ul class="clearfix bxslider1">
                                                <%
                                                    while (itr.hasNext()) {
                                                        Resource clientImages = (Resource) itr.next();
                                                        System.out.println("image is"+clientImages);
                                                        // ValueMap clientImageslideValueMap = clientImages.adaptTo(ValueMap.class);
                                                        Image thumbimg = new Image(clientImages);
                                                %>
                                                <li>
                                                    <%thumbimg.draw(out);%>
                                                </li>
                                                <%
                                                }  %>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
        <%
            }
                else if (action.equalsIgnoreCase("imageCar")) {
                    %>
                  <div class="span6 case-studies">
                        <ul class="bxslider clearfix">
                            <%
                                while (itr.hasNext()) {
                                    Resource carouselImages = (Resource) itr.next();
                                    System.out.println("image is"+carouselImages);
                                    // ValueMap clientImageslideValueMap = clientImages.adaptTo(ValueMap.class);
                                    Image thumbimg = new Image(carouselImages);
                            %>
                            <li>
                                <%thumbimg.draw(out);%>
                            </li>
                            <%
                            }  %>
                          </ul>
                        </div>
                      <div class="divider"></div>
                        
        <%
        }
    }
}
%>