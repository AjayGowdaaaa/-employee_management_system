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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.exception.BusinessException;
import com.employeemanagement.repository.EmployeeRepository;

@Service
public class EmployeeService implements EmployeeServiceInterface {

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	// ---------------------ADD
	// EMPLOYEE-----------------------------------------------------

	@Override

	public Employee addEmployee(Employee employee) {
		Employee savedEmployee = new Employee();
		savedEmployee = employee;

		if (!(employeeRepository.findByPhone(savedEmployee.getPhone()) == null)) {

			logger.error(" EmployeeService/addEmployee : Employee PHONE NUMBER Already EXsists  ");
			throw new BusinessException("AddEmployee Duplicate Phone",
					"Entered Phone Number already exsists in DataBase");
		}
		if (!(employeeRepository.findByEmail(savedEmployee.getEmail()) == null)) {
			logger.error(" EmployeeService/addEmployee : Employee Email Already EXsists  ");
			throw new BusinessException("AddEmployee Duplicate Email", "Entered Email Id already exsists in DataBase");
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

	// ---------------------UPDATE
	// EMPLOYEE-----------------------------------------------------
	@Override
	public Employee updateEmployee(Long empId, Employee employee) {

		if (employeeRepository.findById(empId).get().equals(null)) {
			logger.error(" EmployeeService : Employee ID null  ");
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
		logger.info("EmployeeService : updateEmployee : Working Successfully " + empId);
		employeeRepository.save(existingEmployee);

		return existingEmployee;
	}

	// ---------------------VIEW ALL
	// EMPLOYEE-----------------------------------------------------
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> empoyeeList = null;
		try {
			logger.info("EmployeeService : getAllEmployees : Working Successfully ");
			empoyeeList = employeeRepository.findAll();
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

	// ---------------------DELETE 1 EMPLOYEE BY ID
	// -----------------------------------------------------
	@Override
	public Employee deleteEmployeeById(Long empId) {
		if (!employeeRepository.existsById(empId)) {
			logger.error(" EmployeeService : Employee ID Not Present  " + empId);
			throw new BusinessException("EmployeeService-deleteEmployeeById-1",
					" Employee ID Not found in DataBase, Please enter valid ID");
		}
		Employee deletedEmp = employeeRepository.getById(empId);
		try {
			employeeRepository.deleteById(empId);
			logger.info("Inside the method: deleteEmployeeById,Employee Id is sucessfully deleted" + empId);
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-3",
					"Something went wrong in service layer " + e.getMessage());
		}
		return deletedEmp;
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
		return employeeRepository.findById(id);
	}

	// ---------------------UPDATE PHOTO
	// -----------------------------------------------------
	@Override
	public String uploadPhoto(String path, MultipartFile file, Long empId) {

		Employee emp = employeeRepository.getById(empId);

		if (emp == null) {
			new BusinessException("EMployee Id not found ", "Failed to ge employee Details");
		}
		// File name
		String fileName = emp.getFirstName();// + "_" + emp.getLastName() + "_" + file.getContentType();
		// Full path
		String filePath = path + fileName;
		// Setting path
		emp.setPhotoPath(filePath);
		// Setting photo name
		emp.setPhotoName(fileName);

		// setting photo
		try {
			logger.info("EmployeeService : Uploading Photo : Working Successfully ");
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
		return fileName;
	}

	// --------------------VIEW
	// IMAGE-----------------------------------------------------
	@Override

	public InputStream getResource(String path, Long empId) {
		Employee emp = employeeRepository.getById(empId);
		if (!(employeeRepository.existsById(empId))) {
			new BusinessException("Employee Id Not Found", "Please Enter Valid Id");
		}

		String fullPath = path + File.separator + emp.getPhotoName();

		InputStream is=null;
		try {
			// DataBase logic to return inputstream
			logger.info("EmployeeService : View Photo : Working Successfully ");
			 is = new FileInputStream(fullPath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;

	}

}