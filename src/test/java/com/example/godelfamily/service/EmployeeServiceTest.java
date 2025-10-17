package com.example.godelfamily.service;

import com.example.godelfamily.exception.EmployeeNotFoundException;
import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import com.example.godelfamily.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(1L, "John", "Developer", Title.SENIOR, "Java");
        employee2 = new Employee(2L, "Jane", "QA", Title.MIDDLE, "QA");
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals(employee1, result.get(0));
        assertEquals(employee2, result.get(1));
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById_ExistingEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Employee result = employeeService.getEmployeeById(1L);

        assertEquals(employee1, result);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NonExistingEmployee() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(999L);
        });

        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateEmployee() {
        Employee newEmployee = new Employee(null, "Bob", "BA", Title.JUNIOR, "BA");
        Employee savedEmployee = new Employee(3L, "Bob", "BA", Title.JUNIOR, "BA");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertNull(newEmployee.getId()); // ID should be set to null
        assertEquals(3L, result.getId());
        assertEquals("Bob", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_EnsuresNullId() {
        Employee newEmployee = new Employee(999L, "Bob", "BA", Title.JUNIOR, "BA");
        Employee savedEmployee = new Employee(3L, "Bob", "BA", Title.JUNIOR, "BA");

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.createEmployee(newEmployee);

        assertNull(newEmployee.getId()); // ID should be reset to null
        assertEquals(3L, result.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_ExistingEmployee() {
        Employee updatedEmployee = new Employee(1L, "John Updated", "Developer", Title.LEAD, "Java");

        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertEquals(1L, result.getId());
        assertEquals("John Updated", result.getName());
        assertEquals(Title.LEAD, result.getTitle());
        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_NonExistingEmployee() {
        Employee updatedEmployee = new Employee(999L, "Ghost", "Developer", Title.SENIOR, "Java");

        when(employeeRepository.existsById(999L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(999L, updatedEmployee);
        });

        verify(employeeRepository, times(1)).existsById(999L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee_ExistingEmployee() {
        when(employeeRepository.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            employeeService.deleteEmployee(1L);
        });

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NonExistingEmployee() {
        when(employeeRepository.deleteById(999L)).thenReturn(false);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(999L);
        });

        verify(employeeRepository, times(1)).deleteById(999L);
    }
}

