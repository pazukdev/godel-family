package com.example.godelfamily.repository;

import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryTest {

    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EmployeeRepository();
        repository.init(); // Initialize with 10 sample employees
    }

    @Test
    void testInit_LoadsSampleData() {
        List<Employee> employees = repository.findAll();

        assertEquals(10, employees.size());
    }

    @Test
    void testFindAll_ReturnsAllEmployees() {
        List<Employee> employees = repository.findAll();

        assertNotNull(employees);
        assertEquals(10, employees.size());
    }

    @Test
    void testFindById_ExistingEmployee() {
        Optional<Employee> employee = repository.findById(1L);

        assertTrue(employee.isPresent());
        assertEquals("Emil", employee.get().getName());
        assertEquals("Developer", employee.get().getPosition());
        assertEquals(Title.LEAD, employee.get().getTitle());
        assertEquals("Java", employee.get().getDivision());
    }

    @Test
    void testFindById_NonExistingEmployee() {
        Optional<Employee> employee = repository.findById(999L);

        assertFalse(employee.isPresent());
    }

    @Test
    void testSave_NewEmployee() {
        Employee newEmployee = new Employee(null, "Test User", "Developer", Title.JUNIOR, "Python");

        Employee saved = repository.save(newEmployee);

        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getName());
        assertTrue(repository.existsById(saved.getId()));
    }

    @Test
    void testSave_ExistingEmployee() {
        Employee existing = repository.findById(1L).get();
        existing.setName("Updated Name");

        Employee updated = repository.save(existing);

        assertEquals(1L, updated.getId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteById_ExistingEmployee() {
        assertTrue(repository.existsById(1L));

        boolean deleted = repository.deleteById(1L);

        assertTrue(deleted);
        assertFalse(repository.existsById(1L));
    }

    @Test
    void testDeleteById_NonExistingEmployee() {
        boolean deleted = repository.deleteById(999L);

        assertFalse(deleted);
    }

    @Test
    void testExistsById_ExistingEmployee() {
        assertTrue(repository.existsById(1L));
    }

    @Test
    void testExistsById_NonExistingEmployee() {
        assertFalse(repository.existsById(999L));
    }

    @Test
    void testAutoIncrementId() {
        Employee emp1 = new Employee(null, "User1", "Developer", Title.JUNIOR, "Java");
        Employee emp2 = new Employee(null, "User2", "QA", Title.MIDDLE, "QA");

        Employee saved1 = repository.save(emp1);
        Employee saved2 = repository.save(emp2);

        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());
        assertTrue(saved2.getId() > saved1.getId());
    }
}

