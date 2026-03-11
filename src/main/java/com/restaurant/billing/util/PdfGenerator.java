package com.restaurant.billing.util;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.restaurant.billing.entity.Order;
import com.restaurant.billing.entity.OrderItem;

public class PdfGenerator {
	public static byte[] generate(Order order) {
		try {
			Document document = new Document(PageSize.A4, 30, 30, 40, 30);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, out);

			document.open();

			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
			Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
			Font normal = new Font(Font.HELVETICA, 12);

			/*--------Header---------*/

			Paragraph title = new Paragraph("HOTEL GURUKRUPA", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			Paragraph address = new Paragraph(
					"Address : Line 1, Near Nashik Phata, Nashik\nGST : 22AABCD0023A654\nPhone:8786543210\n\n", normal);
			address.setAlignment(Element.ALIGN_CENTER);
			document.add(address);

			/*--------Order Info---------*/
			PdfPTable infoTable = new PdfPTable(2);
			infoTable.setWidthPercentage(100);

			infoTable.addCell(cell("Order ID:", bold));
			infoTable.addCell(cell(String.valueOf(order.getId()), normal));

			infoTable.addCell(cell("Date:", bold));
			infoTable.addCell(cell(order.getCreatedAt() != null
			        ? order.getCreatedAt().toString()
			                : "N/A", normal
					));

			infoTable.addCell(cell("Status:", bold));
			infoTable.addCell(cell(order.getStatus(), normal));

			document.add(infoTable);
			document.add(new Paragraph("\n"));
			
            /* ---------- ITEMS TABLE ---------- */

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4,2,2,2});

            table.addCell(header("Item"));
            table.addCell(header("Price"));
            table.addCell(header("Qty"));
            table.addCell(header("Total"));

            double subtotal = 0;

            for (OrderItem item : order.getItems()) {

                double price = item.getMenuItem().getPrice();
                double total = item.getPrice();
                subtotal += total;

                table.addCell(cell(item.getMenuItem().getName(), normal));
                table.addCell(cell("₹ " + price, normal));
                table.addCell(cell(String.valueOf(item.getQuantity()), normal));
                table.addCell(cell("₹ " + total, normal));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            /* ---------- CALCULATIONS ---------- */

            double tax = subtotal * 0.05;
            double discount = subtotal > 500 ? subtotal * 0.10 : 0;
            double grandTotal = subtotal + tax - discount;

            PdfPTable calc = new PdfPTable(2);
            calc.setWidthPercentage(40);
            calc.setHorizontalAlignment(Element.ALIGN_RIGHT);

            calc.addCell(cell("Subtotal", bold));
            calc.addCell(cell("₹ " + subtotal, normal));

            calc.addCell(cell("Tax (5%)", bold));
            calc.addCell(cell("₹ " + tax, normal));

            calc.addCell(cell("Discount", bold));
            calc.addCell(cell("₹ " + discount, normal));

            calc.addCell(cell("Grand Total", bold));
            calc.addCell(cell("₹ " + grandTotal, bold));

            document.add(calc);
            document.add(new Paragraph("\n"));

            /* ---------- FOOTER ---------- */

            Paragraph footer = new Paragraph(
                    "Thank you for dining with us!\nVisit Again 😊",
                    bold);
            footer.setAlignment(Element.ALIGN_CENTER);

            document.add(footer);

            document.close();
            return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	/* ---------- CELL HELPERS ---------- */

	private static PdfPCell cell(String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(5);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}

	private static PdfPCell header(String text) {
		Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, bold));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(6);
		return cell;
	}
}
