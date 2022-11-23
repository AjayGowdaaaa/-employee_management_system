package com.employeemanagement.util;

import com.employeemanagement.entity.Employee;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PdfViewById {

    private List<Optional<Employee>> employeeList;


    public PdfViewById(List<Optional<Employee>> employeeList) {
        this.employeeList = employeeList;
    }


    private void empDataHeader(PdfPTable table) {

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.RED);

        List<Employee> empList = employeeList.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (Employee emp : empList) {
            PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Emp. Id : ", font));
            table.addCell(pdfPCell1);
            table.addCell(String.valueOf(emp.getEmpId()));
            PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("ESTUATE Id : ", font));
            table.addCell(pdfPCell2);
            table.addCell(emp.getESTUATE_ID());
            PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Name : ", font));
            table.addCell(pdfPCell3);
            table.addCell(emp.getFirstName()+" "+emp.getLastName());
            PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Email : ", font));
            table.addCell(pdfPCell4);
            table.addCell(emp.getEmail());
            PdfPCell pdfPCell5 = new PdfPCell(new Paragraph("DoB : ", font));
            table.addCell(pdfPCell5);
            table.addCell(emp.getDateOfBirth());
            PdfPCell pdfPCell6 = new PdfPCell(new Paragraph("Phone No. : ", font));
            table.addCell(pdfPCell6);
            table.addCell(String.valueOf(emp.getPhone()));


        }


    }

    @SuppressWarnings("unused")
	public void employeePdfDownloadById(Long empId, HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        List<Employee> empList = employeeList.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Paragraph p = new Paragraph("Employee Data", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph p1 = new Paragraph("\n", font);
        Paragraph p2 = new Paragraph("\n", font);

        document.add(p);
        document.add(p1);
        document.add(p2);

        PdfPTable table = new PdfPTable(2);

        empDataHeader(table);

        document.add(table);

        document.close();

    }
}