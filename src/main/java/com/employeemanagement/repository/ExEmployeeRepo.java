package com.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.employeemanagement.entity.Employee;

public interface ExEmployeeRepo extends JpaRepository<Employee, Long> {
	

}
