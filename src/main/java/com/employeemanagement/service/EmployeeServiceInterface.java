package com.employeemanagement.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.employeemanagement.entity.Employee;

public interface EmployeeServiceInterface {

	String setProfilePicture(String path, MultipartFile file,Long empId);

	InputStream getProfilePicture(String path, Long empId);
	
	Employee addEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	List<Employee> getAllActiveEmployees();
	
	List<Employee> getAllInActiveEmployees();

	Employee getEmployeeById(Long empId);

	Employee resign(Long empId);
	
	Employee rejoin(Long empId);
	
	Employee deleteEmployeeById(Long empId);

	Employee updateEmployee(Long empId, Employee employee);

	Employee getEmployeeByPhone(Long phone);

	Employee getEmployeeByEmail(String email);
	
	Optional<Employee> findById(Long id);

	
}
