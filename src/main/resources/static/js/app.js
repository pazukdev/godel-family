// API Base URL
const API_URL = 'http://localhost:8080/api/employees';
const AI_URL = 'http://localhost:8080/api/ai/query';

// Bootstrap Modal instance
let employeeModal;
let isEditMode = false;
let currentEmployeeId = null;

// Initialize app on page load
document.addEventListener('DOMContentLoaded', function() {
    employeeModal = new bootstrap.Modal(document.getElementById('employeeModal'));
    loadEmployees();
});

// Load all employees
async function loadEmployees() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error('Failed to fetch employees');

        const employees = await response.json();
        displayEmployees(employees);
    } catch (error) {
        handleError('Error loading employees: ' + error.message);
    }
}

// Display employees in table
function displayEmployees(employees) {
    const tbody = document.getElementById('employeeTableBody');
    const emptyState = document.getElementById('emptyState');

    if (employees.length === 0) {
        tbody.innerHTML = '';
        emptyState.classList.remove('d-none');
        return;
    }

    emptyState.classList.add('d-none');
    tbody.innerHTML = employees.map(emp => `
        <tr>
            <td>${emp.id}</td>
            <td>${emp.name}</td>
            <td>${emp.position}</td>
            <td><span class="badge bg-info">${formatTitle(emp.title)}</span></td>
            <td>${emp.division}</td>
            <td>
                <button class="btn btn-sm btn-warning me-2" onclick="showEditForm(${emp.id})">
                    Edit
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteEmployee(${emp.id})">
                    Delete
                </button>
            </td>
        </tr>
    `).join('');
}

// Format title enum to display format
function formatTitle(title) {
    const titleMap = {
        'JUNIOR': 'Junior',
        'MIDDLE': 'Middle',
        'SENIOR': 'Senior',
        'LEAD': 'Lead'
    };
    return titleMap[title] || title;
}

// Show create form
function showCreateForm() {
    isEditMode = false;
    currentEmployeeId = null;

    document.getElementById('modalTitle').textContent = 'Add New Employee';
    document.getElementById('employeeForm').reset();
    document.getElementById('employeeId').value = '';

    employeeModal.show();
}

// Show edit form
async function showEditForm(id) {
    isEditMode = true;
    currentEmployeeId = id;

    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error('Failed to fetch employee');

        const employee = await response.json();

        document.getElementById('modalTitle').textContent = 'Edit Employee';
        document.getElementById('employeeId').value = employee.id;
        document.getElementById('name').value = employee.name;
        document.getElementById('position').value = employee.position;
        document.getElementById('title').value = employee.title;
        document.getElementById('division').value = employee.division;

        employeeModal.show();
    } catch (error) {
        handleError('Error loading employee: ' + error.message);
    }
}

// Save employee (create or update)
async function saveEmployee() {
    const form = document.getElementById('employeeForm');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const employee = {
        name: document.getElementById('name').value,
        position: document.getElementById('position').value,
        title: document.getElementById('title').value,
        division: document.getElementById('division').value
    };

    try {
        let response;

        if (isEditMode) {
            response = await fetch(`${API_URL}/${currentEmployeeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(employee)
            });
        } else {
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(employee)
            });
        }

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to save employee');
        }

        employeeModal.hide();
        showSuccess(isEditMode ? 'Employee updated successfully!' : 'Employee created successfully!');
        loadEmployees();
    } catch (error) {
        handleError('Error saving employee: ' + error.message);
    }
}

// Delete employee
async function deleteEmployee(id) {
    if (!confirm('Are you sure you want to delete this employee?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to delete employee');
        }

        showSuccess('Employee deleted successfully!');
        loadEmployees();
    } catch (error) {
        handleError('Error deleting employee: ' + error.message);
    }
}

// AI Query function
async function askAI() {
    const input = document.getElementById('aiQueryInput');
    const question = input.value.trim();

    if (!question) {
        handleError('Please enter a question');
        return;
    }

    // Show loading state
    const button = document.getElementById('askButton');
    const buttonText = document.getElementById('askButtonText');
    const buttonSpinner = document.getElementById('askButtonSpinner');

    buttonText.classList.add('d-none');
    buttonSpinner.classList.remove('d-none');
    button.disabled = true;

    try {
        const response = await fetch(AI_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ question: question })
        });

        if (!response.ok) throw new Error('Failed to get AI response');

        const data = await response.json();

        // Display response
        const responseContainer = document.getElementById('aiResponseContainer');
        const responseDiv = document.getElementById('aiResponse');

        responseDiv.innerHTML = data.answer.replace(/\n/g, '<br>');
        responseContainer.classList.remove('d-none');

    } catch (error) {
        handleError('Error getting AI response: ' + error.message);
    } finally {
        // Reset button state
        buttonText.classList.remove('d-none');
        buttonSpinner.classList.add('d-none');
        button.disabled = false;
    }
}

// Show success message
function showSuccess(message) {
    showMessage(message, 'success');
}

// Show error message
function handleError(message) {
    showMessage(message, 'danger');
    console.error(message);
}

// Show message helper
function showMessage(message, type) {
    const container = document.getElementById('messageContainer');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    container.appendChild(alert);

    // Auto-dismiss after 5 seconds
    setTimeout(() => {
        alert.remove();
    }, 5000);
}
