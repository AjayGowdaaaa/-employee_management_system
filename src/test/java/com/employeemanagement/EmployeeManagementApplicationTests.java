package com.employeemanagement;

import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.employeemanagement.entity.Employee;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.service.EmployeeService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class EmployeeManagementSystemApplicationTests {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeService employeeservice;

	@Test
	@Order(1)
	@Rollback(value = false)
	public void saveEmployeeTest() {
		Employee employee=new Employee();
		employee.setEmpId(16L);
		employee.setFirstName("sanjay");
		employee.setLastName("kumar");
		employee.setEmail("sanjay1998@gmail.com");
		employee.setDateOfBirth("1998-09-06");
		employee.setPhone(8974001549L);
		employeeservice.addEmployee(employee); 
		assertNotNull(employeeRepository.findById(15L).get());


	}
	@Test
	@Order(2)
	public void getemplopyee() {
		Employee employee= employeeservice. getEmployeeById(15L);
		Assertions.assertThat(employee.getEmpId()).isEqualTo(15L);

	}
	@Test
	@Order(3)
	public void getemplopyeebyList() {
		List< Employee> employee= employeeservice.getAllEmployees();
		Assertions.assertThat(employee.size()).isGreaterThan(0);}

	@Test
	@Order(4)
	@Rollback(value = false)
	public void updateEmployeeTest() {
		Employee employee=new Employee();
		employee.setEmpId(15L);
		employee.setFirstName("Raviteja");
		employee.setLastName("yasana");
		employee.setEmail("Raviteja1998@gmail.com");
		employee.setDateOfBirth("1998-09-06");
		employee.setPhone(8977001540L);
		employeeservice.updateEmployee(15L, employee);
		assertNotNull(employeeRepository.findById(15L).get());

	}
	@Test
	@Order(5)
	@Rollback(value = false)
	public void deleteEmplopyeeTest() throws Exception{
		Employee employee= employeeRepository.findById(15L).get();
		employeeservice.deleteEmployeeById(15L);
		Assertions.assertThat(employee.getEmpId()).isGreaterThan(0) ;       
	}
}

