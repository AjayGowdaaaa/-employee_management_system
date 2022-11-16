package com.employeemanagement.util;

import com.employeemanagement.entity.Employee;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
//import com.master.EmployeeDatabase.entity.Employee;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PdfViewTable {

	private List<Employee> employeeList;

	public PdfViewTable(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

//    public static void tableHeader(PdfPTable table){
//        PdfPCell cell=new PdfPCell();
//        cell.setBackgroundColor(Color.cyan);
//        cell.setPadding(5);
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.WHITE);
//        cell.setPhrase(new Phrase("Emp. Id : ",font));
//        table.addCell(cell);
//        PdfPCell cell1=new PdfPCell();
//        cell1.setPhrase(new Phrase("Name : ",font));
//        table.addCell(cell1);
//        PdfPCell cell2=new PdfPCell();
//        cell2.setPhrase((new Phrase("DoB : ",font)));
//        table.addCell(cell2);
//        PdfPCell cell3=new PdfPCell();
//        cell.setPhrase((new Phrase("Email : ",font)));
//        table.addCell(cell3);
//        PdfPCell cell4=new PdfPCell();
//        cell4.setPhrase((new Phrase("Ph. No. : ",font)));
//        table.addCell(cell4);
//    }

	public void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Emp. Id  ", font));

		table.addCell(cell);
		
		cell.setPhrase(new Phrase("ESTUATE Id  ", font));

		table.addCell(cell);


		cell.setPhrase(new Phrase("Name ", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("DoB", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone No.", font));
		table.addCell(cell);
	}

	private void writeTableData(PdfPTable table) {
		for (Employee emp : employeeList) {
			if (emp.getEmpId() != null) {
				table.addCell(String.valueOf(emp.getEmpId()));
			} else {
				table.addCell("null");
			}
			if (emp.getFirstName() != null) {
				table.addCell(emp.getESTUATE_ID());
			} else {
				table.addCell("null");
			}
			if (emp.getFirstName() != null) {
				table.addCell((emp.getFirstName() + " " + emp.getLastName()));
			} else {
				table.addCell("null");
			}
			if (emp.getEmail() != null) {
				table.addCell(emp.getEmail());
			} else {
				table.addCell("null");
			}

			if (emp.getDateOfBirth() != null) {
				table.addCell(String.valueOf(emp.getDateOfBirth()));
			} else {
				table.addCell("null");
			}

			if (emp.getPhone() != null) {
				table.addCell(String.valueOf(emp.getPhone()));
			} else {
				table.addCell("null");
			}

		}
	}

	public void employeePdfDownload(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Employee List", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f,1.5f,3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}

}
