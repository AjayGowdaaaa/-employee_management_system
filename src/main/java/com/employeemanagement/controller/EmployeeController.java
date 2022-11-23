package com.employeemanagement.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.hibernate.engine.jdbc.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.ExEmployee;
import com.employeemanagement.entity.FileResponse;
import com.employeemanagement.exception.BusinessException;
import com.employeemanagement.exception.ControllerException;
import com.employeemanagement.service.EmployeeServiceInterface;
import com.employeemanagement.util.PdfViewById;
import com.employeemanagement.util.PdfViewTable;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/")
public class EmployeeController {


	@Autowired
	private EmployeeServiceInterface employeeServiceInterface;

	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	/*
	 * Through Register page we can add employee details and store  in Data Base
	 * It is mapped to add employee method in employee service interface
	 * It will Register a new Employee to DataBase
	 */
	@PostMapping("/register")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
		try {
			Employee savedEmployee = employeeServiceInterface.addEmployee(employee);
			logger.info("Controller Class/addEmployee method called	:	Registering new Employee Details "+employee);
			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
		} catch (BusinessException e) {
			logger.warn(" Controller class : BusinessException occured and handled in addEmployee Method  ");
			ControllerException ce = new ControllerException(e.getErrorCode(), e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn(" Controller class : Exception handled inside add Employee Method  ");
			ControllerException ce = new ControllerException("EmployeeController-addEmployee",
					"Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Fetching All employees details present in DataBase 
	 * It is mapped togetAll Employees method in employee service interface
	 * It will fetch a All Employees detail present in Data Base
	 */
	@GetMapping("/allEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> listOfEmployees = employeeServiceInterface.getAllEmployees();
		logger.info(" Controller class/getAllEmployees Method called 	:	Displaying all the Employee Details");
		return new ResponseEntity<List<Employee>>(listOfEmployees, HttpStatus.ACCEPTED);
	}

	/*
	 * Fetching All EX employees details present in DataBase 
	 * It is mapped togetAll EX Employees method in employee service interface
	 * It will fetch a All EX Employees detail present in Data Base
	 */
	@GetMapping("/allExEmployees")
	public ResponseEntity<List<ExEmployee>> getAllExEmployees() {
		List<ExEmployee> listOfEmployees = employeeServiceInterface.getAllExEmployees();
		logger.info(" Controller class/getAllExEmployees Method called 	:	Displaying all the Ex Employee Details");
		return new ResponseEntity<List<ExEmployee>>(listOfEmployees, HttpStatus.ACCEPTED);
	}
	
	/*
	 * Dumping all the Archived Data to CSV File
	 */
	@GetMapping("/exportToCSv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String fileName="Archivelist" +currentDateTime +".csv";
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename= "+fileName;
		response.setHeader(headerKey, headerValue);
		List<ExEmployee> listOfEmployees = employeeServiceInterface.getAllExEmployees();
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"Employee ID","Estuate ID","First Name","Last Name","DOB","E-mail","Phone Number","PHOTO"};
		String[] nameMapping= {"empId","ESTUATE_ID","firstName","lastName","dateOfBirth","email","phone","photo"};
		csvWriter.writeHeader(csvHeader);
		for(ExEmployee exemp :listOfEmployees) {
			csvWriter.write(exemp,nameMapping);
		}
		logger.info("Controller class/exportToCSV : Exporting Archived List to CSV File");
		csvWriter.close();
	}

	/*
	 * Fetching one employee details by Using Id 
	 * It is mapped to getEmployeeById method in employee service interface 
	 * It will fetch a Particular Employee details by ID
	 */
	@GetMapping("/employeeById/{empId}")
	public ResponseEntity<?> getEmployeeById(@PathVariable("empId") Long empId) {
		try {
			logger.info("Controller class/employeeById Method called 	:	Displaying the Employee Details of Employee ID ---->	"+ empId);						
			Employee employeeObtained = employeeServiceInterface.getEmployeeById(empId);
			return new ResponseEntity<Employee>(employeeObtained, HttpStatus.ACCEPTED);
		} catch (BusinessException e) {
			logger.warn(" Controller class/getEmployeeById method called : BusinessException handled inside getEmployeeById Method ");
			ControllerException ce = new ControllerException(e.getErrorCode(), e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn(" Controller class/getEmployeeById method called : BusinessException handled inside getEmployeeById Method ");
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeById","Something went wrong on Controller");					
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Fetching one employee details by Using Id, if employee is present it will give Employee Object else it will throw exception
	 * It is mapped to update method in employee service interface 
	 * It will fetch a Particular Employee details by ID and
	 * UPDATE the employee details
	 */

	@PutMapping("/update/{empId}")
	public ResponseEntity<?> updateEmployee(@Valid @PathVariable long empId, @RequestBody Employee employee) {
		try {
			logger.info(" Controller Class/updateEmployee called  : Updating Employee of empId----------->	 " + empId);
			Employee savedEmployee = employeeServiceInterface.updateEmployee(empId, employee);
			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
		} catch (BusinessException e) {			
			logger.warn(" Controller class/updateEmployee method called : BusinessException handled inside updateEmployee Method ");
			ControllerException ce = new ControllerException(e.getErrorCode(), e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {	
			logger.warn(" Controller class/updateEmployee method called : BusinessException handled inside updateEmployee Method ");
			ControllerException ce = new ControllerException("Employeecontroller.Update.1",
					"Employee ID is not present in Database, please Enter valid Employee ID");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Fetching one employee details by Using Id and Resigning from service 
	 * and Adding that employee to ExEmployees DataBase
	 */
	@DeleteMapping("/resign/{empId}")
	public ResponseEntity<?> resignEmployeeById(@PathVariable("empId") Long empId) {
		try {
			employeeServiceInterface.resignEmployeeById(empId);
			logger.info(" Controller Class/resign EmployeeById called : resign employee of  empId---------->	 " + empId);
			return new ResponseEntity<String>("Employee Resigned Successfully " ,HttpStatus.ACCEPTED);
		} catch (BusinessException e) {	
			logger.warn(" Controller class/resignEmployeeById method called : BusinessException handled inside resignEmployeeById Method ");
			ControllerException ce = new ControllerException(e.getErrorCode(), e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn(" Controller class/resign EmployeeById method called : BusinessException handled inside resignEmployeeById Method ");
			ControllerException ce = new ControllerException("EmployeeController-resignEmployeeById","Something went wrong on Controller");			
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}
	/*
	 * Fetching one employee details from Ex Employee DataBase by Using Id and Rejoining to  service 
	 * and Adding that employee to Employees DataBase with new ID
	 */
	@PostMapping("/rejoin/{empId}")
	public ResponseEntity<?> rejoinEmployeeById(@PathVariable("empId") Long empId) {
		try {
			logger.info(" Controller Class/RejoiningEmployeeById called : Rejoining employee of  empId---------->	 " + empId);
			employeeServiceInterface.rejoinEmployeeById(empId);
			return new ResponseEntity<String>("Employee Re-Joined Successfully " ,HttpStatus.ACCEPTED);
		} catch (BusinessException e) {	
			logger.warn(" Controller class/rejoinEmployeeById method called : BusinessException handled inside rejoinEmployeeById Method ");
			ControllerException ce = new ControllerException(e.getErrorCode(), e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn(" Controller class/rejoinEmployeeById method called : BusinessException handled inside rejoinEmployeeById Method ");
			ControllerException ce = new ControllerException("EmployeeController-rejoinEmployeeById","Something went wrong on Controller");			
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Adding Profile Picture to existing Employee
	 */
	@Value("${project.images}")
	private String path;
	@PostMapping("/setProfilePicture/{empId}")
	public ResponseEntity<?> setProfilePicture(@RequestParam("empId") Long empId, MultipartFile file) {
		try {
			String fileName = this.employeeServiceInterface.setProfilePicture(path, file, empId);
			logger.info("Controller class/setProfilePicture method called : Photo updated for empID----->	 " + empId);
			return new ResponseEntity<>(new FileResponse(fileName, "Image Uploaded"), HttpStatus.CREATED);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-Upload  FAILED TO UPLOAD IMAGE ",
					"Employee ID NOT FOUND");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Displaying Profile picture of an Employee By Using ID
	 */

	@GetMapping(value = "/getProfilePicture/{empId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void getProfilePicture(@PathVariable("empId") Long empId, HttpServletResponse response) throws IOException {
		logger.info("Controller class/getProfilePicture method called : Photo displaying for empID----->	 " + empId);
		InputStream resource = this.employeeServiceInterface.getProfilePicture(path, empId);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

	/*
	 * Printing All the employee details in PDF
	 */
	@GetMapping("/pdfDownload")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		logger.info("Controller class/exportToPDF called  : Downloading all the Employee details in PDF ");
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		@SuppressWarnings("unused")
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=employeeView.pdf";
		response.setHeader(headerKey, headerValue);
		List<Employee> empList = employeeServiceInterface.getAllEmployees();
		PdfViewTable exporter = new PdfViewTable(empList);
		exporter.employeePdfDownload(response);

	}

	/*
	 * Printing One employee details in PDF by using ID
	 */

	@RequestMapping(path = "/employee/{empId}", method = RequestMethod.GET)
	public void getEmployeePdfById(@PathVariable("empId") Long empId, HttpServletResponse response) throws IOException {
		logger.info("Controller class/getEmployeePdfById called  : Downloading Employee details in PDF of empID------->"
				+ empId);
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=employeeView.pdf";
		response.setHeader(headerKey, headerValue);
		Optional<Employee> optionalEmployee = employeeServiceInterface.findById(empId);
		PdfViewById exporter = new PdfViewById(Collections.singletonList(optionalEmployee));
		exporter.employeePdfDownloadById(empId, response);
	}
}
