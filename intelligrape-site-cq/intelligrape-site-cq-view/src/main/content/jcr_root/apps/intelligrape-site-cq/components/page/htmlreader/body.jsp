<%@include file="/libs/foundation/global.jsp"%>
<body>
    <div align="center">
        <h2>Custom HTML Reader</h2>
        <form action="/htmlimporter/upload" method="POST" enctype="multipart/form-data">
            <input name="htmlFile" type="file" />
            <br><br>
            <input type="submit" value="Upload" />
        </form>
    </div>
</body>