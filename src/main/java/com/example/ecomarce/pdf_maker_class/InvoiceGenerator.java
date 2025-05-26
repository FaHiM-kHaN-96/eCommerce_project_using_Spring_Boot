package com.example.ecomarce.pdf_maker_class;


import com.example.ecomarce.entity.OrderTableEN;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceGenerator {

    public static byte[] generateSellerInvoice(List<OrderTableEN> orders) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Background watermark
        PdfContentByte canvas = writer.getDirectContentUnder();
        Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 60, Font.BOLD, new BaseColor(200, 200, 200));

        // Blue header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(59, 130, 246);

        // Group orders by invoice_id
        Map<String, List<OrderTableEN>> ordersByInvoice = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getInvoice_id() != null ? order.getInvoice_id() : "UNKNOWN"));

        // Process each invoice group
        int index = 0;
        for (Map.Entry<String, List<OrderTableEN>> entry : ordersByInvoice.entrySet()) {
            String invoiceId = entry.getKey();
            List<OrderTableEN> orderItems = entry.getValue();
            OrderTableEN firstOrder = orderItems.get(0); // Use first order for shared details

            // Add watermark
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Seller Copy", watermarkFont),
                    297.5f, 421, 45);

            // Blue header
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("E-Commerce", headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setPadding(10);
            cell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(cell);
            document.add(headerTable);

            // Invoice ID
            document.add(new Paragraph("Seller Invoice: " + invoiceId));
            document.add(Chunk.NEWLINE);

            // Customer Details
            document.add(new Paragraph("Customer Details:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph("Name: " + (firstOrder.getUser() != null && firstOrder.getUser().getFullname() != null ?
                    firstOrder.getUser().getFullname() : "N/A")));
            document.add(new Paragraph("Email: " + (firstOrder.getUser() != null && firstOrder.getUser().getUsername() != null ?
                    firstOrder.getUser().getUsername() : "N/A")));
            document.add(new Paragraph("Phone: " + (firstOrder.getUser() != null && firstOrder.getUser().getPhone() != null ?
                    firstOrder.getUser().getPhone() : "N/A")));
            document.add(new Paragraph("Address: " + (firstOrder.getUser() != null && firstOrder.getUser().getAddress() != null ?
                    firstOrder.getUser().getAddress() : "N/A")));

            document.add(Chunk.NEWLINE);

            // Order Details
           // document.add(new Paragraph("Order Details:", new Font("Order Details:", Font.BOLD)));
            document.add(new Paragraph("Order Date: " + (firstOrder.getOrder_date() != null ? firstOrder.getOrder_date() : "N/A")));
            document.add(new Paragraph("Status: " + capitalize(firstOrder.getOrder_status() != null ? firstOrder.getOrder_status() : "")));
            document.add(new Paragraph("Payment Status: " + capitalize(firstOrder.getOrder_payment_status() != null ?
                    firstOrder.getOrder_payment_status() : "")));
            document.add(new Paragraph("Payment Method: " + capitalize(firstOrder.getOrder_payment_method() != null ?
                    firstOrder.getOrder_payment_method() : "")));

            document.add(Chunk.NEWLINE);

            // Order Items Table
            PdfPTable itemTable = new PdfPTable(new float[]{3, 2, 1, 2});
            itemTable.setWidthPercentage(100);
            itemTable.addCell("Product");
            itemTable.addCell("Price");
            itemTable.addCell("Quantity");
            itemTable.addCell("Subtotal");

            double totalSubtotal = 0.0;
            for (OrderTableEN item : orderItems) {
                itemTable.addCell(item.getOrder_product_name() != null ? item.getOrder_product_name() : "N/A");
                float price = item.getOrder_selling_price();
                int quantity = item.getOrder_quantity();
                float subtotal = item.getOrder_subtotal();
                totalSubtotal += subtotal;

                itemTable.addCell(String.format("%.2f BDT", price));
                itemTable.addCell(String.valueOf(quantity));
                itemTable.addCell(String.format("%.2f BDT", subtotal));
            }

            // If no items (unlikely due to grouping, but for safety)
            if (orderItems.isEmpty()) {
                itemTable.addCell("No items");
                itemTable.addCell("");
                itemTable.addCell("");
                itemTable.addCell("");
            }
            document.add(itemTable);

            // Total Subtotal
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Total Subtotal: " + String.format("%.2f BDT", totalSubtotal),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            document.add(Chunk.NEWLINE);

            // Barcode
            String barcodeValue = "INV-" + invoiceId + "-SELLER";
            Image barcodeImage = Image.getInstance(generateBarcode(barcodeValue));
            barcodeImage.scaleToFit(200, 50);
            document.add(barcodeImage);

            // QR Code
            String qrValue = "https://example.com/order/" + invoiceId;
            Image qrImage = Image.getInstance(generateQRCode(qrValue, 100, 100));
            qrImage.setAbsolutePosition(400, document.getPageSize().getHeight() - 200);
            document.add(qrImage);

            // New page for next invoice (except last)
            if (index < ordersByInvoice.size() - 1) {
                document.newPage();
            }
            index++;
        }

        document.close();
        return baos.toByteArray();
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