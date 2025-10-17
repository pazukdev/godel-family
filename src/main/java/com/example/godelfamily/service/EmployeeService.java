package com.example.godelfamily.service;

import com.example.godelfamily.exception.EmployeeNotFoundException;
import com.example.godelfamily.model.Employee;
import com.example.godelfamily.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(null); // Ensure new employee gets a new ID
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employee.setId(id);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }
}
