package siplaundry.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import siplaundry.entity.ExpenseEntity;
import siplaundry.util.NumberUtil;
import siplaundry.util.ViewUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExpenseReportService {
    private float cellPadding = 5;

    public void generateReportPdf(List<ExpenseEntity> expenses) {
        int expenseTotal = 0;

        String[] headers = new String[]{
            "Pengeluaran", "Tanggal", "Keterangan", "Harga", "Jumlah", "Total"
        };

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("Laporan Pengeluaran - SIP"));
        int result = fileChooser.showSaveDialog(null);

        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savePath = selectedFile.getAbsolutePath() + ".pdf";

            try {
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(savePath));
                document.open();

                Paragraph title = new Paragraph("Laporan Pengeluaran SIP Laundry",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
                title.setSpacingAfter(20);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                PdfPTable table = new PdfPTable(headers.length);
                table.setWidths(new float[] { 2F, 1.5F, 2.5F, 1.5F, 1.3F, 1.5F});

                for(String header: headers) {
                    PdfPCell cell = new PdfPCell(new Paragraph(header,
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

                    cell.setPadding(cellPadding);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                for(ExpenseEntity expense: expenses) {
                    PdfPCell eName = new PdfPCell(new Paragraph("Pengeluaran#" + expense.getExpanse_id()));
                    eName.setPadding(cellPadding);
                    eName.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell eDate = new PdfPCell(new Paragraph(ViewUtil.formatDate(expense.getExpanse_date(), "dd/MM/YYYY")));
                    eDate.setPadding(cellPadding);
                    eDate.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell eNote = new PdfPCell(new Paragraph(expense.getName()));
                    eNote.setPadding(cellPadding);
                    eNote.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell ePrice = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(expense.getSubtotal())));
                    ePrice.setPadding(cellPadding);
                    ePrice.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    PdfPCell eQty = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(expense.getQty())));
                    eQty.setPadding(cellPadding);
                    eQty.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell eAmount = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(expense.getAmount())));
                    eAmount.setPadding(cellPadding);
                    eAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);

                    table.addCell(eName);
                    table.addCell(eDate);
                    table.addCell(eNote);
                    table.addCell(ePrice);
                    table.addCell(eQty);
                    table.addCell(eAmount);

                    expenseTotal += expense.getAmount();
                }

                PdfPCell totalExpense = new PdfPCell(new Paragraph("Total Keseluruhan: "));
                totalExpense.setColspan(4);
                totalExpense.setPadding(cellPadding);

                PdfPCell totalExpenseVal = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(expenseTotal)));
                totalExpenseVal.setPadding(cellPadding);
                totalExpenseVal.setColspan(2);
                totalExpenseVal.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(totalExpense);
                table.addCell(totalExpenseVal);

                document.add(table);
                document.close();
            } catch(Exception e) { e.printStackTrace(); }
        }
    }
}
