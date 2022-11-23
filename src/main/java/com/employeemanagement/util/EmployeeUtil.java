package com.employeemanagement.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeUtil {
	
	public static String generateFileName() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String fileName="Archivelist" +currentDateTime +".csv";
		return fileName;
	}


}
