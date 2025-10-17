package com.example.godelfamily.config;

import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import com.example.godelfamily.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    public void init() {
        // Initialize with 10 sample employees
        employeeRepository.save(new Employee(null, "Emil", "Developer", Title.LEAD, "Java"));
        employeeRepository.save(new Employee(null, "Pavel", "Developer", Title.SENIOR, "Java"));
        employeeRepository.save(new Employee(null, "Sergey", "QA", Title.MIDDLE, "QA"));
        employeeRepository.save(new Employee(null, "Anna", "Developer", Title.JUNIOR, "Python"));
        employeeRepository.save(new Employee(null, "Maria", "BA", Title.MIDDLE, "BA"));
        employeeRepository.save(new Employee(null, "Dmitry", "Developer", Title.SENIOR, "JS"));
        employeeRepository.save(new Employee(null, "Olga", "QA", Title.SENIOR, "QA"));
        employeeRepository.save(new Employee(null, "Igor", "Developer", Title.MIDDLE, "Java"));
        employeeRepository.save(new Employee(null, "Svetlana", "BA", Title.SENIOR, "BA"));
        employeeRepository.save(new Employee(null, "Alexey", "Developer", Title.JUNIOR, "Python"));
    }
}

