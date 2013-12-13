<%@include file="/libs/foundation/global.jsp"%>
<body>
    <div style="margin-left:auto;margin-right:auto;width:70%;background-color:#00ffff;font-weight: 500;font-style: italic">
        <div>
            <h2>Custom HTML Reader</h2>
            <form name="htmlReaderFrom" action="/htmlimporter/upload" method="POST" enctype="multipart/form-data">
                Select Html File to be read :<input name="htmlFile" type="file" style="float: right" />
                </br></br>
                Select Target Template :
                <select id="selectTemplate" name="targetTemplate" style="width:230px;float: right;">
                    <option value=""></option>
                </select>
                </br></br>
                Enter Target Page :<input type="text" name="targetPage" value="/content/" style="float: right" />
                </br></br>
                Enter name of the page to be created : <input type="text" name="pageName" style="float: right" />
                </br></br>
                Enter Title for the page : <input name="pageTitle" type="text" style="float: right" />
                </br></br>
                <div style="margin-left:auto;margin-right:auto;width:20%;">
                    <input type="button" value="Upload" id="uploadBtn" />
                </div>
            </form>
        </div>
        </br><div id="error" style="color: red;margin-left:auto;margin-right:auto;width:45%;"></div>
    </div>
<script type="text/javascript">
    function upload()
    {
        if(($('[name=htmlFile]').val()==""))
        {
            $('#error').text("!!!!!!Please Select a file to be read!!!!!!");
        }else if(($('[name=htmlFile]').val().indexOf(".html")==-1) || ($('[name=htmlFile]').val().indexOf(".htm")==-1))
        {
            $('#error').text("!!!!!!Please Select a HTML only!!!!!!");
        }else if(($('[name=targetTemplate]').val()==""))
        {
            $('#error').text("!!!!!!Please Select a target template!!!!!!");
        }else if(($('[name=targetPage]').val()=="/content/") || ($('[name=targetPage]').val()==""))
        {
            $('#error').text("!!!!!!Please Enter the Target Page!!!!!!");
        }else if(($('[name=pageName]').val()==""))
        {
            $('#error').text("!!!!!!Please Enter the Page Name!!!!!!");
        }else if(($('[name=pageTitle]').val()==""))
        {
            $('#error').text("!!!!!!Please Enter the Page Title!!!!!!");
        }else
        {
            $('#error').text("");
            $('[name=htmlReaderFrom]').submit();
        }
    }
    $("#uploadBtn").on("click" ,upload);
</script>
</body>