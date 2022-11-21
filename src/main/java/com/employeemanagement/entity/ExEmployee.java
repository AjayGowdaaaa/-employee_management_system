package com.employeemanagement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@Entity
public class ExEmployee {

	
	Employee emp;
	@Id
	private Long empId= emp.getEmpId();
	private String ESTUATE_ID=emp.getESTUATE_ID();
	@NotNull
	private String firstName= emp.getFirstName();
	private String lastName= emp.getLastName();
	private String dateOfBirth= emp.getDateOfBirth();
	@Email 
	private String email= emp.getEmail();
	private Long phone= emp.getPhone();
	//private boolean active= emp
//------------------------------------------------------------------------
	@Lob
	private  byte[] photo= emp.getPhoto();
	private String photoName= emp.getPhotoName();
	private String photoPath= emp.getPhotoPath();
	
}
