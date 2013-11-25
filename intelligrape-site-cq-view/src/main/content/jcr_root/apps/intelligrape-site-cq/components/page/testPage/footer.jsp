
<%@page import="com.day.cq.wcm.api.WCMMode"%><script type="text/javascript">eval(mod_pagespeed_h4laXkNDh5);</script>
<script type="text/javascript">eval(mod_pagespeed_ZlDuYbeKr_);</script>
<script type="text/javascript">eval(mod_pagespeed_wLINTlHgKQ);</script>
<script type="text/javascript">eval(mod_pagespeed_MfaxLFyQoU);</script>
<script type="text/javascript">eval(mod_pagespeed_kjybWuPdgd);</script>
<% if(WCMMode.fromRequest(request)!=WCMMode.EDIT)
	{ %>      
<script type="text/javascript">$(document).ready(function(){if($(window).width()<959){$('#slider').flexslider({directionNav:true,controlNav:false});}
else{$('#thumb-slider').flexslider({animation:"slide",animationLoop:false,slideshow:true,directionNav:false,controlNav:false,itemWidth:180,itemMargin:0,asNavFor:'#slider'});$('#slider').flexslider({animation:"slide",controlNav:false,directionNav:false,animationLoop:false,slideshow:true,sync:"#thumb-slider"});}});</script>
<% } else {%>
<script type="text/javascript">document.getElementById('#slider').style.width="100";</script>
     
<% }%>