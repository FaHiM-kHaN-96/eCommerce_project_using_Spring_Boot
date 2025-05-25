package com.example.ecomarce.pdf_maker_class;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import java.util.Map;

public class InvoiceGenerator {

    public static void generateSellerInvoice(Map<String, Object> order) {
        try {
            Document document = new Document();
            String fileName = "seller-invoice-" + order.get("inv_number") + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Background watermark
            PdfContentByte canvas = writer.getDirectContentUnder();
            Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 60, Font.ITALIC, new BaseColor(200, 200, 200));
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Seller Copy", watermarkFont),
                    297.5f, 421, 45);

            // Blue header
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("Ahmad Shop", new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, BaseColor.WHITE)));
            cell.setBackgroundColor(new BaseColor(59, 130, 246));
            cell.setPadding(10);
            cell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(cell);
            document.add(headerTable);

            document.add(new Paragraph("Seller Invoice: " + order.get("inv_number")));
            document.add(Chunk.NEWLINE);

            // Customer Details
            document.add(new Paragraph("Customer Details:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph("Name: " + order.get("customer_name")));
            document.add(new Paragraph("Email: " + order.get("email")));
            document.add(new Paragraph("Phone: " + order.get("phone")));
            document.add(new Paragraph("Address: " + order.get("address")));

            document.add(Chunk.NEWLINE);

            // Order Details
            document.add(new Paragraph("Order Details:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph("Order ID: " + order.get("order_id")));
            document.add(new Paragraph("Date: " + order.get("order_date")));
            document.add(new Paragraph("Total: " + order.get("total") + " BDT"));
            document.add(new Paragraph("Status: " + capitalize(order.get("status").toString())));
            document.add(new Paragraph("Payment Status: " + capitalize(order.get("payment_status").toString())));
            document.add(new Paragraph("Payment Method: " + capitalize(order.get("payment_method").toString())));
            document.add(new Paragraph("Transaction ID: " + order.get("transaction_id")));

            document.add(Chunk.NEWLINE);

            // Order Items Table
            PdfPTable itemTable = new PdfPTable(new float[]{3, 2, 1, 2});
            itemTable.setWidthPercentage(100);
            itemTable.addCell("Product");
            itemTable.addCell("Price");
            itemTable.addCell("Quantity");
            itemTable.addCell("Subtotal");

            @SuppressWarnings("unchecked")
            var items = (Iterable<Map<String, Object>>) order.get("items");
            for (Map<String, Object> item : items) {
                itemTable.addCell(item.get("name").toString());
                double price = Double.parseDouble(item.get("price").toString());
                int quantity = Integer.parseInt(item.get("quantity").toString());
                itemTable.addCell(String.format("%.2f BDT", price));
                itemTable.addCell(String.valueOf(quantity));
                itemTable.addCell(String.format("%.2f BDT", price * quantity));
            }
            document.add(itemTable);

            document.add(Chunk.NEWLINE);

            // Barcode
            String barcodeValue = "INV-" + order.get("order_id") + "-SELLER";
            Image barcodeImage = Image.getInstance(generateBarcode(barcodeValue));
            barcodeImage.scaleToFit(200, 50);
            document.add(barcodeImage);

            // QR Code
            String qrValue = "https://example.com/order/" + order.get("order_id");
            Image qrImage = Image.getInstance(generateQRCode(qrValue, 100, 100));
            qrImage.setAbsolutePosition(400, document.getPageSize().getHeight() - 200);
            document.add(qrImage);

            document.close();
            System.out.println("PDF generated: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateBarcode(String data) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, 300, 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", baos);
        return baos.toByteArray();
    }

    private static byte[] generateQRCode(String data, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", baos);
        return baos.toByteArray();
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
