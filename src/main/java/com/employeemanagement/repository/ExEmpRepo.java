package com.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employeemanagement.entity.ExEmployees;

@Repository
public interface ExEmpRepo extends JpaRepository<ExEmployees, Long> {

}
