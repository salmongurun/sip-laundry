package siplaundry.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import siplaundry.entity.TransactionDetailEntity;
import siplaundry.entity.TransactionEntity;
import siplaundry.repository.TransactionDetailRepo;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

public class ReportService {
    private TransactionDetailRepo detailRepo = new TransactionDetailRepo();

    public void generatePdf(List<TransactionEntity> transactions) {
        String[] headers = new String[] {
            "Transaksi", "Tanggal", "Status", "Item", "Jumlah", "Subtotal", "Total"
        };



        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);

        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savePath = selectedFile.getAbsolutePath() + ".pdf";

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(savePath));
                document.open();

                Paragraph title = new Paragraph("Laporan SIP Laundry");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                PdfPTable table = new PdfPTable(headers.length);

                for(String header: headers) {
                    PdfPCell cell = new PdfPCell(new Paragraph(header));

                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                for(TransactionEntity transaction: transactions) {
                    PdfPCell tName = new PdfPCell(new Paragraph("Transaksi#" + transaction.getid()));
                    PdfPCell tDate = new PdfPCell(new Paragraph(ViewUtil.formatDate(transaction.gettransactionDate(), "dd/MM/YYYY")));
                    PdfPCell tStatus = new PdfPCell(new Paragraph(transaction.getstatus().toString()));
                    PdfPCell tItem = new PdfPCell(new Paragraph("Anjay"));
                    PdfPCell tJumlah = new PdfPCell(new Paragraph("Anjay"));
                    PdfPCell tSubtotal = new PdfPCell(new Paragraph("Anjay"));
                    PdfPCell tTotal = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(transaction.getamount())));

                    table.addCell(tName);
                    table.addCell(tDate);
                    table.addCell(tStatus);
                    table.addCell(tItem);
                    table.addCell(tJumlah);
                    table.addCell(tSubtotal);
                    table.addCell(tTotal);

                }

                document.add(table);
                document.close();

            } catch(Exception e) { e.printStackTrace(); }
        }
    }
}
