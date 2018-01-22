package com.memorynotfound.pdf.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergePdf {

    

    public static void main(String... args) throws IOException, DocumentException {
    	List<PdfReader> files = new ArrayList<PdfReader>(Arrays.asList(
        		new PdfReader("D:/pdf/pdf1.pdf"),
        		new PdfReader("D:/pdf/pdf2.pdf"),
        		new PdfReader("D:/pdf/pdf3.pdf"),
        		new PdfReader("D:/pdf/pdf5.pdf")
        ));
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream("D:/pdf/merge-pdf-result.pdf"));

        document.open();
        for (PdfReader reader : files){
           // PdfReader reader = new PdfReader(file);
            copy.addDocument(reader);
            copy.freeReader(reader);
            reader.close();
        }
        document.close();
    }
}
