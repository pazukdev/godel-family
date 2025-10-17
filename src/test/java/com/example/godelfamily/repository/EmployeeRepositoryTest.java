package com.example.godelfamily.repository;

import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void testFindAll_ReturnsAllEmployees() {
        // Save some test data
        repository.save(new Employee(null, "Emil", "Developer", Title.LEAD, "Java"));
        repository.save(new Employee(null, "Pavel", "Developer", Title.SENIOR, "Java"));

        List<Employee> employees = repository.findAll();

        assertNotNull(employees);
        assertEquals(2, employees.size());
    }

    @Test
    void testFindById_ExistingEmployee() {
        Employee saved = repository.save(new Employee(null, "Emil", "Developer", Title.LEAD, "Java"));

        Optional<Employee> employee = repository.findById(saved.getId());

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
        Employee existing = repository.save(new Employee(null, "Original Name", "Developer", Title.JUNIOR, "Java"));
        existing.setName("Updated Name");

        Employee updated = repository.save(existing);

        assertEquals(existing.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteById_ExistingEmployee() {
        Employee saved = repository.save(new Employee(null, "Test", "Developer", Title.JUNIOR, "Java"));
        Long id = saved.getId();
        assertTrue(repository.existsById(id));

        repository.deleteById(id);

        assertFalse(repository.existsById(id));
    }

    @Test
    void testDeleteById_NonExistingEmployee() {
        // JPA's deleteById doesn't throw exception if not found, it just does nothing
        assertDoesNotThrow(() -> repository.deleteById(999L));
    }

    @Test
    void testExistsById_ExistingEmployee() {
        Employee saved = repository.save(new Employee(null, "Test", "Developer", Title.JUNIOR, "Java"));
        assertTrue(repository.existsById(saved.getId()));
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
