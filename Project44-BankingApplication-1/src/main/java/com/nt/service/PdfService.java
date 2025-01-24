package com.nt.service;



import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.nt.entity.Transaction;
import com.itextpdf.layout.element.Table;



@Service
public class PdfService {

    public ByteArrayOutputStream generateTransactionPdf(List<Transaction> transactions, String accountNumber) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Bank Statement for Account: " + accountNumber));
            document.add(new Paragraph(" ")); // Add some space

            Table table = new Table(3);
            table.addHeaderCell("Date");
            table.addHeaderCell("Amount");
            table.addHeaderCell("Transaction Type");

            for (Transaction transaction : transactions) {
                table.addCell(transaction.getCreatedAt().toString());
                table.addCell(transaction.getAmount().toString());
                table.addCell(transaction.getTransactionType());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}