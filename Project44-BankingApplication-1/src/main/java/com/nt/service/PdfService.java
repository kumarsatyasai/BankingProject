
package com.nt.service;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.nt.entity.Transaction; 
@Service
public class PdfService {

    public ByteArrayOutputStream generateTransactionPdf(List<Transaction> transactions, String accountNumber) {
    	  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          try {
              PdfWriter writer = new PdfWriter(outputStream);
              PdfDocument pdfDocument = new PdfDocument(writer);
              Document document = new Document(pdfDocument);

              // Set a title with a larger font size and bold
              PdfFont font = PdfFontFactory.createFont();
              // Set a title with a larger font size and bold
              Paragraph title = new Paragraph("Bank Statement for Account: " + accountNumber)
                      .setFont(font)
                      .setFontSize(20)
                      .setTextAlignment(TextAlignment.CENTER);
              document.add(title);
              document.add(new Paragraph(" ")); // Add some space

              // Create a colorful table
              Table table = new Table(3);
              table.setWidth(UnitValue.createPercentValue(100)); // Set table width to 100%
              
              Color backgroundColor = new DeviceRgb(236, 193, 156); 
              Color backgroundColor1 = new DeviceRgb(239, 157, 16); 
              Color backgroundColor2 = new DeviceRgb(107, 123, 140); 

              // Header cells with background color
              Cell headerCell1 = new Cell().add(new Paragraph("DATE").setFont(font)).setBackgroundColor(backgroundColor).setTextAlignment(TextAlignment.CENTER);
              Cell headerCell2 = new Cell().add(new Paragraph("AMOUNT").setFont(font)).setBackgroundColor(backgroundColor).setTextAlignment(TextAlignment.CENTER);
              Cell headerCell3 = new Cell().add(new Paragraph("TRANSACTION TYPE").setFont(font)).setBackgroundColor(backgroundColor).setTextAlignment(TextAlignment.CENTER);

              table.addHeaderCell(headerCell1);
              table.addHeaderCell(headerCell2);
              table.addHeaderCell(headerCell3);

              // Add transaction data with alternating row colors
              for (int i = 0; i < transactions.size(); i++) {
                  Transaction transaction = transactions.get(i);
                  Cell dateCell = new Cell().add(new Paragraph(transaction.getCreatedAt().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER));
                  Cell amountCell = new Cell().add(new Paragraph(transaction.getAmount().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER));
                  Cell transaTypeCell = new Cell().add(new Paragraph(transaction.getTransactionType().toString()).setFont(font).setTextAlignment(TextAlignment.CENTER));

                  // Alternate row colors
                  if (i % 2 == 0) {
                      dateCell.setBackgroundColor(backgroundColor1);
                      amountCell.setBackgroundColor(backgroundColor1);
                      transaTypeCell.setBackgroundColor(backgroundColor1);
                  }
                  else {
                	  dateCell.setBackgroundColor(backgroundColor2);
                      amountCell.setBackgroundColor(backgroundColor2);
                      transaTypeCell.setBackgroundColor(backgroundColor2);
                	  
                  }
                 

                  table.addCell(dateCell);
                  table.addCell(amountCell);
                  table.addCell(transaTypeCell);
              }

              document.add(table);
              document.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return outputStream;
    }
}