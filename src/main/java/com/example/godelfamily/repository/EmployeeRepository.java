package com.example.godelfamily.repository;

import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepository {

    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Initialize with 10 sample employees
        save(new Employee(null, "Emil", "Developer", Title.LEAD, "Java"));
        save(new Employee(null, "Pavel", "Developer", Title.SENIOR, "Java"));
        save(new Employee(null, "Sergey", "QA", Title.MIDDLE, "QA"));
        save(new Employee(null, "Anna", "Developer", Title.JUNIOR, "Python"));
        save(new Employee(null, "Maria", "BA", Title.MIDDLE, "BA"));
        save(new Employee(null, "Dmitry", "Developer", Title.SENIOR, "JS"));
        save(new Employee(null, "Olga", "QA", Title.SENIOR, "QA"));
        save(new Employee(null, "Igor", "Developer", Title.MIDDLE, "Java"));
        save(new Employee(null, "Svetlana", "BA", Title.SENIOR, "BA"));
        save(new Employee(null, "Alexey", "Developer", Title.JUNIOR, "Python"));
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idGenerator.getAndIncrement());
        }
        employees.put(employee.getId(), employee);
        return employee;
    }

    public boolean deleteById(Long id) {
        return employees.remove(id) != null;
    }

    public boolean existsById(Long id) {
        return employees.containsKey(id);
    }
}
