package com.employeemanagement.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.entity.ExEmployee;

public interface EmployeeServiceInterface {

	String setProfilePicture(String path, MultipartFile file,Long empId);

	InputStream getProfilePicture(String path, Long empId);
	
	Employee addEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	List<ExEmployee> getAllExEmployees();
	
	Employee getEmployeeById(Long empId);

	void resignEmployeeById(Long empId);

	Employee updateEmployee(Long empId, Employee employee);

	Employee getEmployeeByPhone(Long phone);

	Employee getEmployeeByEmail(String email);
	
	Optional<Employee> findById(Long id);

	void rejoinEmployeeById(Long empId);

	
}
