package com.employeemanagement.entity;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
@Entity
public class ExEmployees {
	@Id	
	private Long empId;
	private String ESTUATE_ID;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String email;
	private Long phone;
//------------------------------------------------------------------------
	@Lob
	private  byte[] photo;
	private String photoName;
	private String photoPath;
	
	

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getESTUATE_ID() {
		return ESTUATE_ID;
	}

	public void setESTUATE_ID(String eSTUATE_ID) {
		ESTUATE_ID = eSTUATE_ID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", ESTUATE_ID=" + ESTUATE_ID + ", firstName=" + firstName + ", lastName="
				+ lastName + ", dateOfBirth=" + dateOfBirth + ", email=" + email + ", phone=" + phone + ", photo="
				+ Arrays.toString(photo) + ", photoName=" + photoName + ", photoPath=" + photoPath + "]";
	}

	
}
