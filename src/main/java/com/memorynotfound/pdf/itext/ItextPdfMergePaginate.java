package com.memorynotfound.pdf.itext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopy.PageStamp;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class ItextPdfMergePaginate {

 public static void main(String[] args) {
  List<InputStream> pdfs = new ArrayList<InputStream>();
  try (FileInputStream fis1 = new FileInputStream("D:/pdf/pdf6.pdf");
    FileInputStream fis2 = new FileInputStream("D:/pdf/pdf2.pdf");
    FileInputStream fis3 = new FileInputStream("D:/pdf/pdf3.pdf");) {
   pdfs.add(fis1);
   pdfs.add(fis2);
   pdfs.add(fis3);
   OutputStream output = new FileOutputStream("D:/pdf/merge.pdf");
   mergeAndPaginate(pdfs, output);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public static void mergeAndPaginate(List<InputStream> streamOfPDFFiles, OutputStream outputStream) {
  Document document = new Document(PageSize.LETTER);

  try {

   // Open PdfCopy for to copy the merged document into outputStream
   PdfCopy copy = new PdfCopy(document, outputStream);

   document.open();

   // We will use the list of readers to get total page count and
   // process individual pages later
   List<PdfReader> readers = new ArrayList<PdfReader>();
   int totalPageCount = 0;
   for (InputStream file : streamOfPDFFiles) {

    // Create Reader for each input stream and add to reader list
    PdfReader reader = new PdfReader(file);
    readers.add(reader);

    totalPageCount += reader.getNumberOfPages();
   }

   // Number of pages processed so far.
   int currentPageNumber = 0;
   for (PdfReader reader : readers) {

    int pageCount = reader.getNumberOfPages();

    // Page Number starts with 1.
    for (int i = 1; i <= pageCount; i++) {
     int pageNumber = currentPageNumber + i;

     // Process one page at a time
     PdfImportedPage page = copy.getImportedPage(reader, i);
     PageStamp stamp = copy.createPageStamp(page);

     // Create chunk of text with page information and set font settings
     Chunk chunk = new Chunk(String.format("Page %d of %d", pageNumber, totalPageCount));
     Font font = new Font();
     font.setFamily(Font.FontFamily.HELVETICA.name());
     font.setSize(8);
     font.setStyle(Font.FontStyle.ITALIC.getValue());
     chunk.setFont(font);

     // Write the text into page (represented by stamp object
     ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_RIGHT, new Phrase(chunk), 580, 10, 0);
     stamp.alterContents();

     // Add page after page info is written.
     copy.addPage(page);
    }
    currentPageNumber += pageCount;
   }
   outputStream.flush();
  } catch (Exception e) {
   e.printStackTrace();
  } finally {
   if (document.isOpen())
    document.close();
   try {
    if (outputStream != null)
     outputStream.close();
   } catch (IOException ioe) {
    ioe.printStackTrace();
   }
  }
 }
}