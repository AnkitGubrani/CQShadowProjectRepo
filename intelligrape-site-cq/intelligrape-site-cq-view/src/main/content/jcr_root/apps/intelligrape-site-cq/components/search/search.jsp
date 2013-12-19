<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>

<%
    String core=properties.get("solrCore","");
    String maxRows=properties.get("maxRows","10");
    if(Integer.parseInt(maxRows)<0){
        maxRows="10";
    }


%>
<html>
    <head>
        <script type="text/javascript">

            function openMe(url){
                window.open(url);
                window.target('_new');
            }

            function getHttp(){
                var xmlhttp;
                if (window.XMLHttpRequest){
                    xmlhttp=new XMLHttpRequest();
                }else{
                    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                }
                return xmlhttp;
            }


            function submitMe(){
                if(f1.query.value==""){
                    return false;
                }


                try{
                    var xmlhttp = getHttp();
                    xmlhttp.onreadystatechange=function(){
                        if (xmlhttp.readyState==4 && xmlhttp.status==200){
                            var data= xmlhttp.responseText;
                            try{
                                var obj = JSON.parse("["+xmlhttp.responseText+"]");
                                var data="<table width='70%' cellspacing='0' style=\"border:1px solid red;\">"+
                                        "<tr style=\"background-color:#aaccaa;color:white;\">"+
                                        "<th width='100%' style=\"text-align:left\">Search Result links</th>"+
                                        "</tr>";
                                var objList =obj[0].solrDocs;


                                for(var i=0;i<objList.length;++i){
                                    data+="<tr>"+
                                            "<td><a href='javascript:openMe(\""+objList[i].id+"\");'>"+objList[i].id+"</a></td>"+
                                            "</tr>";

                                }
                                data+="</table>"

                            }catch(e){}
                            document.getElementById("searchResult").innerHTML=data;
                        }
                    }
                    queryString ="query="+f1.query.value+"&core="+f1.core.value+"&maxRows="+f1.maxRows.value;
                    xmlhttp.open("GET","/bin/solr/search.html?"+queryString,true);
                    xmlhttp.send();

                }catch(e){alert(e);}
            }
        </script>
    </head>
    <body>
        <form name="f1">
            <input type="text" name="query" value="" />
            <input type="button" value="Submit" onclick="submitMe();"/>
            <input type="hidden" name="core" value="<%=core%>" />
            <input type="hidden" name="maxRows" value="<%=maxRows%>" />
        </form>
    <div id="searchResult"></div>
    </body>
</html>