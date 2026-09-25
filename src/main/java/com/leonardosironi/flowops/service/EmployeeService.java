package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.dto.CreateEmployeeRequest;
import com.leonardosironi.flowops.dto.EmployeeResponse;
import com.leonardosironi.flowops.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Employee employee = new Employee(
                request.getName(),
                request.getEmail()
        );

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail()
        );
    }
}