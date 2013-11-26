<%--

  Home page component.

  This is the home page component for intelligrape website

--%><%
%><%
%><%@page session="false" %><%
%><%
    // TODO add you code here
%>
<footer id="footer" class="clearfix">
    <div id="footer-inner" class="container">
        <div class="row">
            <div class="span3 footer-logo"><a href="/about-us.html"><img src="http://www.intelligrape.com/images/xlogo-footer.png.pagespeed.ic.g16U5cyieG.png" alt="logo"></a><br>
               
               <a href="http://aws.amazon.com/"><img src="http://www.intelligrape.com/images/xaws-partner.png.pagespeed.ic.4eDaSD-eRZ.png" alt="aws"></a></div>
            <div class="span3">
                <h4>IntelliGrape Newsletter</h4>

                <p>Subscribe to our newsletter to receive periodic updates about the latest trends in technologies, design and development.</p>

                <form id="subform" name="subform" onSubmit="return subscribe()">
                    <fieldset>
                        <p>
                            <input name="subscribe_email" id="subscribe_email" class="required email" type="text" placeholder="Email here"/>
                            <input name="ok" type="submit" value="OK" class="sub-submit"/>
                        </p>

                        <div id="subresult"></div>
                    </fieldset>
                </form>
            </div>
            <div class="span3">
                <h4>Flickr Stream</h4>

                <div class="flickr">
                    <script type="text/javascript" src="http://www.flickr.com/badge_code_v2.gne?count=6&amp;display=latest&amp;size=s&amp;layout=x&amp;source=user&amp;user=97685591%40N06"></script>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="span3 address">
                <h4>Contact Us</h4>
                <address>
                    IntelliGrape Software (P) Ltd<br/>
                    SDF L-6, NSEZ,<br/>
                    Noida Phase 2,<br/>
                    India<br/>
                    Phone : +91-120-6493668<br/>
                    Fax : +91-120-4207689
                </address>
                <ul class="social">
                    <li><a href="http://www.linkedin.com/company/246617" target="_blank"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAAUVBMVEX////h4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eE7BuAyAAAAGnRSTlMAAwkMEicqLTA2PFpgZm+Bh73Ayc/V8PP5/MTDGVkAAABcSURBVDjL7c7LEkAwDEDReBct6ln3/z/Uzko67MxwV1mcSSLy96x8wqcx0AE2BpqdYKI3alfdfadsL1cBIoBZAJ/pYAWg10EY3AizDqxIAZsOUpEE0ME5vxF8uwOGHwtAn1mhsAAAAABJRU5ErkJggg==" alt="icon"></a></li>
                    <li><a href="https://www.facebook.com/intelligrape.software" target="_blank"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgBAMAAACBVGfHAAAAIVBMVEX////h4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eEHPmgKAAAACnRSTlMARW91irHP2978xvB3hgAAADFJREFUKM9jYBhcgClrFaqA1So0gSp0gVkL0cxYtQCNv2rVCnSBpegCS/CbMVACAw0AXE8ZMykmnhUAAAAASUVORK5CYII=" alt="icon"></a></li>
                </ul>
            </div>
        </div>
    </div>
    <img src="http://www.intelligrape.com/images/xfooter-crytal.png.pagespeed.ic.kgdDyIHaMU.png"></footer>
    
    
    <script type="text/javascript">
    function followUs() {
        window.open(
                "https://twitter.com/intent/follow?screen_name=IntelliGrape",
                "Twitter", "height=350");
    }
    $('.bxslider').bxSlider( {
        mode : 'fade',
        auto : true,
        autoStart : true,
        captions : false
    });
    jQuery('.bxslider1').bxSlider( {
        minSlides : 2,
        maxSlides : 10,
        slideMargin : 20,
        ticker : true,
        tickerHover : true,
        speed : 18000
    });
    try {
        var _gaq = _gaq || [];
        _gaq.push( [ '_setAccount', 'UA-2116872-2' ]);
        _gaq.push( [ '_trackPageview' ]);
    } catch (err) {
    }
    (function() {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl'
                : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>
