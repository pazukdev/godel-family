package com.example.godelfamily.controller;

import com.example.godelfamily.exception.EmployeeNotFoundException;
import com.example.godelfamily.model.Employee;
import com.example.godelfamily.model.Title;
import com.example.godelfamily.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        employee1 = new Employee(1L, "John", "Developer", Title.SENIOR, "Java");
        employee2 = new Employee(2L, "Jane", "QA", Title.MIDDLE, "QA");
        employees = Arrays.asList(employee1, employee2);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("John"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById_ExistingEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employee1);

        mockMvc.perform(get("/api/employees/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.position").value("Developer"))
            .andExpect(jsonPath("$.title").value("SENIOR"))
            .andExpect(jsonPath("$.division").value("Java"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void testGetEmployeeById_NonExistingEmployee() throws Exception {
        when(employeeService.getEmployeeById(999L)).thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/employees/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Employee not found with id: 999"));

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee(null, "Bob", "BA", Title.JUNIOR, "BA");
        Employee savedEmployee = new Employee(3L, "Bob", "BA", Title.JUNIOR, "BA");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Bob"))
            .andExpect(jsonPath("$.position").value("BA"))
            .andExpect(jsonPath("$.title").value("JUNIOR"))
            .andExpect(jsonPath("$.division").value("BA"));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void testCreateEmployee_ValidationError_MissingName() throws Exception {
        Employee invalidEmployee = new Employee(null, "", "Developer", Title.SENIOR, "Java");

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").exists());

        verify(employeeService, never()).createEmployee(any(Employee.class));
    }

    @Test
    void testCreateEmployee_ValidationError_MissingPosition() throws Exception {
        Employee invalidEmployee = new Employee(null, "John", "", Title.SENIOR, "Java");

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.position").exists());

        verify(employeeService, never()).createEmployee(any(Employee.class));
    }

    @Test
    void testCreateEmployee_ValidationError_MissingDivision() throws Exception {
        Employee invalidEmployee = new Employee(null, "John", "Developer", Title.SENIOR, "");

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.division").exists());

        verify(employeeService, never()).createEmployee(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(1L, "John Updated", "Developer", Title.LEAD, "Java");

        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John Updated"))
            .andExpect(jsonPath("$.title").value("LEAD"));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(Employee.class));
    }

    @Test
    void testUpdateEmployee_NonExistingEmployee() throws Exception {
        Employee updatedEmployee = new Employee(999L, "Ghost", "Developer", Title.SENIOR, "Java");

        when(employeeService.updateEmployee(eq(999L), any(Employee.class)))
            .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(put("/api/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Employee not found with id: 999"));

        verify(employeeService, times(1)).updateEmployee(eq(999L), any(Employee.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
            .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void testDeleteEmployee_NonExistingEmployee() throws Exception {
        doThrow(new EmployeeNotFoundException(999L)).when(employeeService).deleteEmployee(999L);

        mockMvc.perform(delete("/api/employees/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Employee not found with id: 999"));

        verify(employeeService, times(1)).deleteEmployee(999L);
    }
}

