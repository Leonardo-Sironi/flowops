
package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.domain.EmployeeRole;
import com.leonardosironi.flowops.dto.CreateEmployeeRequest;
import com.leonardosironi.flowops.dto.EmployeeResponse;
import com.leonardosironi.flowops.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        String passwordHash = passwordEncoder.encode(request.getPassword());

        Employee employee = new Employee(
                request.getName(),
                request.getEmail(),
                passwordHash,
                EmployeeRole.EMPLOYEE
        );

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail()
        );
    }
}
