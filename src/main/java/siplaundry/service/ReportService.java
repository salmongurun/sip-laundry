package siplaundry.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import siplaundry.data.LaundryStatus;
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
    private float cellPadding = 5;

    public void generateReportPdf(List<TransactionEntity> transactions) {
        int netIncome = 0;
        int income = 0;

        String[] headers = new String[] {
            "Transaksi", "Tanggal", "Status", "Item", "Jumlah", "Subtotal", "Total"
        };

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("Laporan Pendapatan - SIP"));
        int result = fileChooser.showSaveDialog(null);

        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savePath = selectedFile.getAbsolutePath() + ".pdf";

            try {
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(savePath));
                document.open();

                Paragraph title = new Paragraph("Laporan Pendapatan SIP Laundry",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
                title.setSpacingAfter(20);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                PdfPTable table = new PdfPTable(headers.length);
                table.setWidths(new float[] { 2F, 1.5F, 1F, 2F, 1.3F, 1.5F, 1.5F});


                for(String header: headers) {
                    PdfPCell cell = new PdfPCell(new Paragraph(header,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

                    cell.setPadding(cellPadding);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                for(TransactionEntity transaction: transactions) {
                    int i = 0;
                    List<TransactionDetailEntity> details = detailRepo.get(new HashMap<>(){{
                        put("transaction_id", transaction.getid());
                    }});

                    PdfPCell tName = new PdfPCell(new Paragraph("Transaksi#" + transaction.getid()));
                    tName.setRowspan(details.size());
                    tName.setPadding(cellPadding);
                    tName.setVerticalAlignment(Element.ALIGN_CENTER);

                    table.addCell(tName);

                    PdfPCell tDate = new PdfPCell(new Paragraph(ViewUtil.formatDate(transaction.gettransactionDate(), "dd/MM/YYYY")));
                    tDate.setRowspan(details.size());
                    tDate.setPadding(cellPadding);
                    tDate.setVerticalAlignment(Element.ALIGN_CENTER);

                    table.addCell(tDate);

                    PdfPCell tStatus = new PdfPCell(new Paragraph(ViewUtil.toStatusString(transaction.getstatus())));
                    tStatus.setRowspan(details.size());
                    tStatus.setPadding(cellPadding);
                    tStatus.setVerticalAlignment(Element.ALIGN_CENTER);

                    table.addCell(tStatus);

                    for(TransactionDetailEntity detail: details) {
                        PdfPCell tItem = new PdfPCell(new Paragraph(detail.getLaundry().getname()));
                        tItem.setPadding(cellPadding);

                        PdfPCell tJumlah = new PdfPCell(new Paragraph(String.valueOf(detail.getQty())));
                        tJumlah.setPadding(cellPadding);
                        tJumlah.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell tSubtotal = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(detail.getSubtotal())));
                        tSubtotal.setPadding(cellPadding);
                        tSubtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

                        table.addCell(tItem);
                        table.addCell(tJumlah);
                        table.addCell(tSubtotal);

                        if(i < 1) {
                            PdfPCell tTotal = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(transaction.getamount())));
                            tTotal.setPadding(cellPadding);
                            tTotal.setRowspan(details.size());
                            tTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

                            table.addCell(tTotal);
                        }

                        i++;
                    }

                    if(transaction.getstatus() != LaundryStatus.canceled)
                        income += transaction.getamount();

                    if(transaction.getstatus() == LaundryStatus.finish || transaction.getstatus() == LaundryStatus.taken)
                        netIncome += transaction.getamount();
                }

                PdfPCell incomeCell = new PdfPCell(new Paragraph("Total Keseluruhan: "));
                incomeCell.setColspan(5);
                incomeCell.setPadding(cellPadding);

                PdfPCell incomeTotalCell = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(income)));
                incomeTotalCell.setPadding(cellPadding);
                incomeTotalCell.setColspan(2);
                incomeTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                PdfPCell netIncomeCell = new PdfPCell(new Paragraph("Pendapatan Bersih (selesai & diambil): "));
                netIncomeCell.setColspan(5);
                netIncomeCell.setPadding(cellPadding);

                PdfPCell netIncomeTotalCell = new PdfPCell(new Paragraph(NumberUtil.rupiahFormat(netIncome)));
                netIncomeTotalCell.setPadding(cellPadding);
                netIncomeTotalCell.setColspan(2);
                netIncomeTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(incomeCell);
                table.addCell(incomeTotalCell);
                table.addCell(netIncomeCell);
                table.addCell(netIncomeTotalCell);

                document.add(table);
                document.close();

            } catch(Exception e) { e.printStackTrace(); }
        }
    }
}
