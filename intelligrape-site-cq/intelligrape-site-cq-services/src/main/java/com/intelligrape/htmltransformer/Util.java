package com.intelligrape.htmltransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Util {

    Logger logger = LoggerFactory.getLogger(Util.class);
    public String fileReader(String filePath)
    {
        StringBuffer fileContent = new StringBuffer();
       try{
           BufferedReader reader = new BufferedReader(new FileReader(filePath));
           String line = new String();
            while ((line =reader.readLine())!=null)
            {
                fileContent.append(line);
            }
       }catch(Exception e)
       {
           logger.error("exception =>"+e.getMessage());
       }
        return fileContent.toString();
    }

    public void htmlTags(String htmlCode)
    {
        int bodyStart = htmlCode.indexOf("<body");

        int bodyEnd = htmlCode.indexOf(">",bodyStart);

        //This is line adds the cqcanvas div readable for design importer
        htmlCode = htmlCode.substring(0,bodyEnd+1) +"<div id=\"cqcanvas\">"
                +htmlCode.substring(bodyEnd+1 ,htmlCode.length());


        htmlCode = htmlCode.replaceAll("<h1" ,"<h1 data-cq-component=\"test_TITLE\"");
        htmlCode = htmlCode.replaceAll("<h2" ,"<h2 data-cq-component=\"test_TITLE\"");
        htmlCode = htmlCode.replaceAll("<h3" ,"<h3 data-cq-component=\"test_TITLE\"");
        htmlCode = htmlCode.replaceAll("<h4" ,"<h4 data-cq-component=\"test_TITLE\"");
        htmlCode = htmlCode.replaceAll("<h5" ,"<h5 data-cq-component=\"test_TITLE\"");
        htmlCode = htmlCode.replaceAll("<h6" ,"<h6 data-cq-component=\"test_TITLE\"");

        htmlCode = htmlCode.replaceAll("<img","<img data-cq-component=\"test_IMAGE\"");

        htmlCode = htmlCode.replaceAll("<p","<p data-cq-component=\"test_TEXT\"");

        htmlCode = htmlCode.replaceAll("<a","<a data-cq-component=\"clickThroughLink\"");
        //closing the cqcanvas div
        htmlCode = htmlCode.substring(0,htmlCode.indexOf("</body>"))+"</div>"
                +htmlCode.substring(htmlCode.indexOf("</body>"),htmlCode.length());

        System.out.println(" bodyStart "+bodyStart+" bodyEnd "+bodyEnd+" =="+htmlCode);

        File outputFile = new File("/home/ankit/Desktop/output.html");
        FileWriter fw;
        BufferedWriter bw;
        try
        {
        if(!outputFile.exists())
        {
               outputFile.createNewFile();
        }
            fw = new FileWriter(outputFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(htmlCode);
            bw.close();
            fw.close();
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }

    }

}
