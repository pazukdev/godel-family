# Employee CRUD Application - Implementation Plan

## Project Overview
Create a Spring Boot REST API application with JavaScript frontend for managing Employee records using in-memory storage.

---

## 1. Backend Dependencies (pom.xml)
Add the following dependencies to enable Spring Web and validation:

```xml
<!-- Spring Web for REST API -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok (optional but recommended for cleaner code) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 2. Domain Model Layer

### 2.1 Create Enum: `Title.java`
**Location:** `src/main/java/com/example/godelfamily/model/Title.java`

**Purpose:** Define employee title levels

**Requirements:**
- Enum with values: JUNIOR, MIDDLE, SENIOR, LEAD
- Public enum accessible by other packages

---

### 2.2 Create Entity: `Employee.java`
**Location:** `src/main/java/com/example/godelfamily/model/Employee.java`

**Purpose:** Represent Employee entity with all required fields

**Requirements:**
- Fields:
  - `Long id` - unique identifier (auto-generated)
  - `String name` - employee name (not null, not empty)
  - `String position` - role (Developer, QA, BA)
  - `Title title` - enum (Junior, Middle, Senior, Lead)
  - `String division` - team division (Java, Python, JS, QA, BA, etc.)
- Add validation annotations (@NotNull, @NotEmpty where appropriate)
- Include constructors: no-args and all-args
- Include getters and setters
- Override equals() and hashCode() using id field
- Override toString() for debugging

---

## 3. Repository Layer

### 3.1 Create Repository: `EmployeeRepository.java`
**Location:** `src/main/java/com/example/godelfamily/repository/EmployeeRepository.java`

**Purpose:** Data access layer using in-memory storage (ConcurrentHashMap or LinkedHashSet)

**Requirements:**
- Use `@Repository` annotation
- Use `ConcurrentHashMap<Long, Employee>` for thread-safe in-memory storage
- Implement methods:
  - `List<Employee> findAll()` - return all employees
  - `Optional<Employee> findById(Long id)` - find by ID
  - `Employee save(Employee employee)` - create or update
  - `boolean deleteById(Long id)` - delete by ID, return true if deleted
  - `boolean existsById(Long id)` - check existence
- Implement ID auto-generation using `AtomicLong`
- Initialize with 10 sample employees in constructor or @PostConstruct method

**Sample Employees:**
1. Emil - Developer, LEAD, Java
2. Pavel - Developer, SENIOR, Java
3. Sergey - QA, MIDDLE, QA
4. Anna - Developer, JUNIOR, Python
5. Maria - BA, MIDDLE, BA
6. Dmitry - Developer, SENIOR, JS
7. Olga - QA, SENIOR, QA
8. Igor - Developer, MIDDLE, Java
9. Svetlana - BA, SENIOR, BA
10. Alexey - Developer, JUNIOR, Python

---

## 4. Service Layer

### 4.1 Create Service: `EmployeeService.java`
**Location:** `src/main/java/com/example/godelfamily/service/EmployeeService.java`

**Purpose:** Business logic layer between controller and repository

**Requirements:**
- Use `@Service` annotation
- Inject `EmployeeRepository` via constructor
- Implement methods:
  - `List<Employee> getAllEmployees()` - get all employees
  - `Employee getEmployeeById(Long id)` - get by ID, throw exception if not found
  - `Employee createEmployee(Employee employee)` - create new employee
  - `Employee updateEmployee(Long id, Employee employee)` - update existing, throw exception if not found
  - `void deleteEmployee(Long id)` - delete by ID, throw exception if not found
- Add custom exception handling for not found scenarios

---

### 4.2 Create Exception: `EmployeeNotFoundException.java`
**Location:** `src/main/java/com/example/godelfamily/exception/EmployeeNotFoundException.java`

**Purpose:** Custom exception for employee not found scenarios

**Requirements:**
- Extend `RuntimeException`
- Constructor accepting employee ID
- Meaningful error message

---

### 4.3 Create Global Exception Handler: `GlobalExceptionHandler.java`
**Location:** `src/main/java/com/example/godelfamily/exception/GlobalExceptionHandler.java`

**Purpose:** Centralized exception handling for REST API

**Requirements:**
- Use `@RestControllerAdvice` annotation
- Handle `EmployeeNotFoundException` - return 404
- Handle `MethodArgumentNotValidException` - return 400 with validation errors
- Handle generic exceptions - return 500

---

## 5. Controller Layer

### 5.1 Create REST Controller: `EmployeeController.java`
**Location:** `src/main/java/com/example/godelfamily/controller/EmployeeController.java`

**Purpose:** REST API endpoints for Employee CRUD operations

**Requirements:**
- Use `@RestController` annotation
- Use `@RequestMapping("/api/employees")` for base path
- Use `@CrossOrigin` to allow frontend access
- Inject `EmployeeService` via constructor
- Implement endpoints:

  **GET /api/employees**
  - Return all employees
  - HTTP 200 OK
  - Returns: `List<Employee>`

  **GET /api/employees/{id}**
  - Return employee by ID
  - HTTP 200 OK or 404 Not Found
  - Returns: `Employee`

  **POST /api/employees**
  - Create new employee
  - HTTP 201 Created
  - Request body: `Employee` (without ID)
  - Add `@Valid` annotation for validation
  - Returns: created `Employee`

  **PUT /api/employees/{id}**
  - Update existing employee
  - HTTP 200 OK or 404 Not Found
  - Request body: `Employee`
  - Add `@Valid` annotation for validation
  - Returns: updated `Employee`

  **DELETE /api/employees/{id}**
  - Delete employee by ID
  - HTTP 204 No Content or 404 Not Found
  - No response body

---

## 6. Configuration

### 6.1 Update `application.properties`
**Location:** `src/main/resources/application.properties`

**Purpose:** Configure application settings

**Requirements:**
```properties
# Server configuration
server.port=8080

# Application name
spring.application.name=godel-family

# Logging
logging.level.com.example.godelfamily=DEBUG
```

---

## 7. Frontend

### 7.1 Create HTML Page: `index.html`
**Location:** `src/main/resources/static/index.html`

**Purpose:** Single-page application for Employee management

**Requirements:**
- Modern, clean UI design
- Bootstrap CSS for styling (CDN)
- Sections:
  - Header with application title
  - Employee list table with columns: ID, Name, Position, Title, Division, Actions
  - Action buttons: Edit, Delete for each row
  - "Add New Employee" button
  - Modal form for Create/Update operations
- Table features:
  - Display all employees in a responsive table
  - Each row has Edit and Delete buttons
  - Empty state message when no employees exist

---

### 7.2 Create JavaScript: `app.js`
**Location:** `src/main/resources/static/js/app.js`

**Purpose:** Frontend logic for CRUD operations

**Requirements:**
- Use vanilla JavaScript or fetch API
- Base API URL: `http://localhost:8080/api/employees`
- Functions:

  **loadEmployees()**
  - Fetch all employees (GET /api/employees)
  - Populate table with data
  - Call on page load and after each operation

  **showCreateForm()**
  - Show modal/form for creating new employee
  - Clear all form fields
  - Set form mode to "create"

  **showEditForm(id)**
  - Fetch employee by ID (GET /api/employees/{id})
  - Populate form with employee data
  - Show modal/form
  - Set form mode to "edit"

  **createEmployee()**
  - Collect form data
  - Send POST request to /api/employees
  - Close form on success
  - Reload employee list
  - Show success message

  **updateEmployee(id)**
  - Collect form data
  - Send PUT request to /api/employees/{id}
  - Close form on success
  - Reload employee list
  - Show success message

  **deleteEmployee(id)**
  - Show confirmation dialog
  - Send DELETE request to /api/employees/{id}
  - Reload employee list on success
  - Show success message

  **handleError(error)**
  - Display error messages to user
  - Log errors to console

- Form validation:
  - All fields are required
  - Position dropdown: Developer, QA, BA
  - Title dropdown: Junior, Middle, Senior, Lead
  - Division: free text input

- Event listeners:
  - DOMContentLoaded: initialize app and load employees
  - Form submit: create or update employee
  - Delete button: confirm and delete
  - Cancel button: close form

---

### 7.3 Create CSS: `style.css`
**Location:** `src/main/resources/static/css/style.css`

**Purpose:** Custom styling for the application

**Requirements:**
- Clean, modern design
- Responsive layout
- Styled table with hover effects
- Button styling (primary, danger, success)
- Modal/form styling
- Success/error message styling
- Mobile-friendly design

---

## 8. Testing

### 8.1 Update Test: `GodelFamilyApplicationTests.java`
**Location:** `src/test/java/com/example/godelfamily/GodelFamilyApplicationTests.java`

**Purpose:** Ensure application context loads successfully

**Requirements:**
- Keep existing context load test
- Optionally add basic integration tests for REST endpoints

---

## 9. Implementation Order

Follow this sequence for smooth development:

1. **Update pom.xml** - Add required dependencies
2. **Create Title enum** - Simple enum for employee titles
3. **Create Employee model** - Domain entity
4. **Create custom exceptions** - EmployeeNotFoundException and GlobalExceptionHandler
5. **Create EmployeeRepository** - Data access with sample data
6. **Create EmployeeService** - Business logic layer
7. **Create EmployeeController** - REST API endpoints
8. **Update application.properties** - Configuration
9. **Test backend** - Use Postman or curl to verify all endpoints
10. **Create HTML structure** - index.html with UI layout
11. **Create JavaScript logic** - app.js with CRUD operations
12. **Create CSS styling** - style.css for visual appeal
13. **Test full application** - Verify all CRUD operations work end-to-end

---

## 10. Testing Checklist

After implementation, verify:

- ✅ Application starts without errors
- ✅ GET /api/employees returns 10 sample employees
- ✅ GET /api/employees/{id} returns specific employee
- ✅ POST /api/employees creates new employee
- ✅ PUT /api/employees/{id} updates existing employee
- ✅ DELETE /api/employees/{id} removes employee
- ✅ Frontend loads and displays all employees
- ✅ Create new employee from UI works
- ✅ Edit employee from UI works
- ✅ Delete employee from UI works (with confirmation)
- ✅ Form validation works
- ✅ Error messages display correctly
- ✅ UI is responsive and user-friendly

---

## 11. Best Practices Applied

- **Separation of Concerns:** Controller → Service → Repository layers
- **RESTful Design:** Proper HTTP methods and status codes
- **Exception Handling:** Centralized with meaningful messages
- **Validation:** Bean validation for input data
- **Thread Safety:** ConcurrentHashMap for in-memory storage
- **Clean Code:** Meaningful names, single responsibility
- **User Experience:** Confirmation dialogs, success/error messages
- **CORS Configuration:** Enable frontend-backend communication
- **Responsive Design:** Mobile-friendly UI

---

## 12. Future Enhancements (Optional)

- Add pagination for employee list
- Add search/filter functionality
- Add sorting by different columns
- Persist data to actual database (H2, PostgreSQL)
- Add authentication and authorization
- Add unit and integration tests
- Add Docker containerization
- Add API documentation (Swagger/OpenAPI)

---

## Notes

- The application uses in-memory storage, so data resets on restart
- CORS is enabled for local development
- All IDs are auto-generated
- The frontend uses modern JavaScript (ES6+)
- No external JavaScript frameworks required (vanilla JS)
- Bootstrap CDN is used for quick, professional styling

---

**Start with this plan and implement step by step. Each component is independent and can be developed incrementally. Good luck! 🚀**
