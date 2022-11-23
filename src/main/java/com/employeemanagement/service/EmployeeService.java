package com.employeemanagement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.ExEmployee;
import com.employeemanagement.exception.BusinessException;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.ExEmpRepo;
import com.employeemanagement.util.EmployeeUtil;

@Service
public class EmployeeService implements EmployeeServiceInterface {

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ExEmpRepo exEmpRepo;
	@Qualifier("exEmpRepo")


	// ---------------------ADD
	// EMPLOYEE-----------------------------------------------------

	@Override

	public Employee addEmployee(Employee employee) {

		Employee savedEmployee = employee;

		if (!(employeeRepository.findByPhone(savedEmployee.getPhone()) == null)) {
			logger.error(" EmployeeService/addEmployee : Employee PHONE NUMBER Already EXsists  ");
			throw new BusinessException("Phone number already Exists",
					"Entered Phone Number already exsists in DataBase");
		}
		if (!(employeeRepository.findByEmail(savedEmployee.getEmail()) == null)) {
			logger.error(" EmployeeService/addEmployee : Employee Email Already EXsists  ");
			throw new BusinessException("Email ID already Exists", "Entered Email Id already exsists in DataBase");
		}

		if ((employeeRepository.findByPhone(savedEmployee.getPhone()) == null
				&& employeeRepository.findByEmail(savedEmployee.getEmail()) == null)) {
			try {
				savedEmployee = employeeRepository.save(employee);
				savedEmployee.setESTUATE_ID("EST-" + savedEmployee.getEmpId());
				savedEmployee.setEmail(employee.getEmail().toLowerCase());
				savedEmployee = employeeRepository.save(employee);
				return savedEmployee;
			} catch (IllegalArgumentException e) {
				logger.warn(" EmployeeService : IllegalArgumentException handled inside addEmployee Method  ");
				throw new BusinessException("EmployeeService-addEmployee-2",
						"Not Valid Name, Please Enter Valid Name " + e.getMessage());
			} catch (Exception e) {
				logger.warn(" EmployeeService : Exception handled inside addEmployee Method  ");
				throw new BusinessException("EmployeeService-addEmployee-3",
						"Something went wrong in service layer " + e.getMessage());
			}
		} else {
			throw new BusinessException("EmployeeService-addEmployee-1;",
					"Email Id or Mobile Already Exsists in DataBase ");
		}

	}
	// ---------------------VIEW ALL 
	// EMPLOYEE-----------------------------------------------------
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> empoyeeList = null;


		try {
			empoyeeList = employeeRepository.findAll();
			logger.info("EmployeeService : getAllEmployees : Getting all the employee details ");
		} catch (Exception e) {
			logger.warn(" EmployeeService : Exception handled inside getAllEmployees Method  ");
			throw new BusinessException("EmployeeService-getAllEmployees-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (empoyeeList.isEmpty()) {
			logger.error(" EmployeeService : Employee List Empty  ");
			throw new BusinessException("EmployeeService-getAllEmployees-1",
					" List is Empty, Add Some Data in Register Page... ");
		}
		return empoyeeList;
	}

	// ---------------------VIEW ALL Ex
	// EMPLOYEE-----------------------------------------------------
	@Override
	public List<ExEmployee> getAllExEmployees() {
		List<ExEmployee> empoyeeList = null;
		try {
			empoyeeList = exEmpRepo.findAll();
			logger.info("EmployeeService : getAllEmployees : Getting all the employee details ");
		} catch (Exception e) {
			logger.warn(" EmployeeService : Exception handled inside getAllEmployees Method  ");
			throw new BusinessException("EmployeeService-getAllEmployees-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (empoyeeList.isEmpty()) {
			logger.error(" EmployeeService : Employee List Empty  ");
			throw new BusinessException("EmployeeService-getAllEmployees-1",
					" List is Empty, Add Some Data in Register Page... ");
		}
		return empoyeeList;
	}


	// ---------------------UPDATE
	// EMPLOYEE-----------------------------------------------------
	@Override
	public Employee updateEmployee(Long empId, Employee employee) {

		if (employeeRepository.findById(empId).get().equals(null)) {
			logger.error(" EmployeeService : Employee ID is not valid  ");
			throw new BusinessException("EmployeeService-updateEmployee-1",
					"Entered null value , Please Enter Valid ID");
		}

		Employee existingEmployee = employeeRepository.findById(empId).orElseThrow(
				() -> new BusinessException("Employee ID is not present in Database", "Please Enter valid ID"));

		existingEmployee.setFirstName(employee.getFirstName());
		existingEmployee.setLastName(employee.getLastName());
		existingEmployee.setDateOfBirth(employee.getDateOfBirth());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setPhone(employee.getPhone());

		try {
			employeeRepository.save(existingEmployee);
			logger.info("EmployeeService : updateEmployee : updating employee details of employee id --->  " + empId);
		} catch (BusinessException e) {
			logger.warn(" EmployeeService : Exception handled inside update Method  ");
			throw new BusinessException("EmployeeService-getAllEmployees-2",
					"Something went wrong in service layer while updating employee details " + e.getMessage());
		}
		return existingEmployee;
	}



	// ---------------------VIEW 1 EMPLOYEE BY
	// ID-----------------------------------------------------
	@Override
	public Employee getEmployeeById(Long empId) {
		if (empId.equals(null)) {
			logger.error(" EmployeeService : Employee ID null  ");
			throw new BusinessException("EmployeeService-getEmployeeById-1",
					"You Entered a null value, please Enter Any int Value");
		}
		try {
			logger.info("EmployeeService : getEmployeeById : Working Successfully " + empId);
			return employeeRepository.findById(empId).get();
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside getEmployeeById Method  ");
			throw new BusinessException("EmployeeService-getEmployeeById-BE-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside getEmployeeById Method  ");
			throw new BusinessException("EmployeeService-getEmployeeById-BE-3",
					"Something went wrong in service layer " + e.getMessage());
		}

	}

	// ---------------------Resign 1 EMPLOYEE BY ID
	// -----------------------------------------------------
	@Override
	public void resignEmployeeById(Long empId) {
		if (!employeeRepository.existsById(empId)) {
			logger.error(" EmployeeService : Employee ID Not Present  " + empId);
			throw new BusinessException("EmployeeService-deleteEmployeeById-1",
					" Employee ID Not found in DataBase, Please enter valid ID");
		}

		Employee deletingEmployee = employeeRepository.findById(empId).get();
		ExEmployee emp = new ExEmployee();
		try {
			emp.setEmpId(deletingEmployee.getEmpId());
			emp.setESTUATE_ID(deletingEmployee.getESTUATE_ID());
			emp.setFirstName(deletingEmployee.getFirstName());
			emp.setLastName(deletingEmployee.getLastName());
			emp.setDateOfBirth(deletingEmployee.getDateOfBirth());
			emp.setEmail(deletingEmployee.getEmail());
			emp.setPhone(deletingEmployee.getPhone());
			emp.setPhoto(deletingEmployee.getPhoto());
			emp.setPhotoName(deletingEmployee.getPhotoName());
			emp.setPhotoPath(deletingEmployee.getPhotoPath());

			exEmpRepo.save(emp);
			employeeRepository.delete(deletingEmployee);

			logger.info("Inside the resign  method: resign ,Employee Id is sucessfully resign " + empId);
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-3",
					"Something went wrong in service layer " + e.getMessage());
		}

	}
	// ---------------------Rejoin 1 EMPLOYEE BY ID
	@Override
	public void rejoinEmployeeById(Long empId) {
		if (!exEmpRepo.existsById(empId)) {
			logger.error(" EmployeeService : Employee ID Not Present  " + empId);
			throw new BusinessException("EmployeeService-deleteEmployeeById-1",
					" Employee ID Not found in DataBase, Please enter valid ID");
		}
		ExEmployee rejoiningEmp = exEmpRepo.findById(empId).get();
		Employee emp = new Employee();
		try {

			emp.setESTUATE_ID(rejoiningEmp.getESTUATE_ID());
			emp.setFirstName(rejoiningEmp.getFirstName());
			emp.setLastName(rejoiningEmp.getLastName());
			emp.setDateOfBirth(rejoiningEmp.getDateOfBirth());
			emp.setEmail(rejoiningEmp.getEmail());
			emp.setPhone(rejoiningEmp.getPhone());
			emp.setPhoto(rejoiningEmp.getPhoto());
			emp.setPhotoName(rejoiningEmp.getPhotoName());
			emp.setPhotoPath(rejoiningEmp.getPhotoPath());
			employeeRepository.save(emp);
			emp.setESTUATE_ID("EST-" + emp.getEmpId());
			employeeRepository.save(emp);
			exEmpRepo.delete(rejoiningEmp);
			logger.info("Inside the resign  method: resign ,Employee Id is sucessfully resign " + empId);
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-3",
					"Something went wrong in service layer " + e.getMessage());
		}

	}
	// ---------------------VIEW 1 EMPLOYEE BY
	// PHONE-----------------------------------------------------
	@Override
	public Employee getEmployeeByPhone(Long phone) {
		Employee empoyeeListByPhone = null;
		try {
			logger.info("EmployeeService : getEmployeeByPhone : Working Successfully ");
			empoyeeListByPhone = employeeRepository.findByPhone(phone);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByPhone-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (employeeRepository.existsById(empoyeeListByPhone.getEmpId())) {
			throw new BusinessException("EmployeeService-getEmployeeByPhone-1",
					"Requested Data not found in List , Please enter some data in Register Page  ");
		}
		return empoyeeListByPhone;

	}

	// ---------------------VIEW 1 EMPLOYEE BY
	// EMAIL-----------------------------------------------------
	@Override
	public Employee getEmployeeByEmail(String email) {
		Employee empoyeeListByEmail = null;
		try {
			logger.info("EmployeeService : getEmployeeByEmail : Working Successfully ");
			empoyeeListByEmail = employeeRepository.findByEmail(email);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByEmail-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (employeeRepository.existsById(empoyeeListByEmail.getEmpId())) {
			throw new BusinessException("EmployeeService-getEmployeeByEmail-1",
					"Requested Data not found in List , Please enter some data in Register Page  ");
		}
		return empoyeeListByEmail;
	}

	// --------------------FIND BY ID
	// -----------------------------------------------------
	@Override
	public Optional<Employee> findById(Long id) {
		Employee emp=	employeeRepository.findById(id).get();
		if (emp==null) {
			throw new BusinessException("Find by Id ", "Employee ID not found in Database");
		}
		return employeeRepository.findById(id);
	}

	// ---------------------UPDATE PHOTO
	// -----------------------------------------------------
	int count=0;
	@Override
	public String setProfilePicture(String path, MultipartFile file, Long empId) {

		Employee emp = employeeRepository.findById(empId).get();;

		if (emp == null) {
			new BusinessException("Employee Id not found ", "Failed to setProfilePicture ");
		}
		
		// File name
		String fileName = emp.getFirstName()+count++;
		// Full path
		String filePath = path + fileName;
		// Setting path
		emp.setPhotoPath(filePath);
		// Setting photo name
		emp.setPhotoName(fileName);

		// setting photo
		try {

			emp.setPhoto(file.getBytes());
		} catch (IOException e1) {
			new BusinessException("Failed to add photo to Database ", "Error occured in Upload photo , setPhoto ");
			e1.printStackTrace();
		}
		// Create folder
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		// file copy
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath));
		} catch (IOException e) {
			logger.warn(" EmployeeService : IOException handled inside UploadPhoto Method  ");
			new BusinessException("Something went wrong", "Failed to copy");
		}
		employeeRepository.save(emp);
		logger.info("EmployeeService : Uploading Photo : Working Successfully ");
		return fileName;
	}

	// --------------------VIEW
	// IMAGE-----------------------------------------------------
	@Override

	public InputStream getProfilePicture(String path, Long empId) {
		Employee emp = employeeRepository.findById(empId).get();
		if (!(employeeRepository.existsById(empId))) {
			new BusinessException("Employee Id Not Found", "Please Enter Valid Id");
		}
		String fullPath;
		if (path== null) {
			new BusinessException("Failed to getting Images", "Image not found/");
		}
		fullPath	= path + File.separator + emp.getPhotoName();
		InputStream inputStream=null;
		try {
			// DataBase logic to return inputstream
			logger.info("EmployeeService : View Photo : Displaying IMAGE ");
			inputStream = new FileInputStream(fullPath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		String fileName = EmployeeUtil.generateFileName();
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename= "+fileName;
		response.setHeader(headerKey, headerValue);
		List<ExEmployee> listOfEmployees = getAllExEmployees();
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader= {"ID","Estuate ID","First Name","Last Name","DOB","E-mail","Phone Number","PHOTO"};
		String[] nameMapping= {"empId","ESTUATE_ID","firstName","lastName","dateOfBirth","email","phone","photo"};
		csvWriter.writeHeader(csvHeader);
		for(ExEmployee exemp :listOfEmployees) {
			csvWriter.write(exemp,nameMapping);
		}
		csvWriter.close();
	}

}
