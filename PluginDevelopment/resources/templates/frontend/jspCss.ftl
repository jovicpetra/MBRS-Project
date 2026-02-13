/* Generated CSS for ${app_name} */

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-color: #f5f5f5;
    color: #333;
    line-height: 1.6;
}

.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    background-color: white;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    min-height: 100vh;
}

/* Header Styles */
.app-header {
    text-align: center;
    padding: 40px 0;
    border-bottom: 3px solid #007bff;
    margin-bottom: 30px;
}

.app-header h1 {
    font-size: 2.5em;
    color: #007bff;
    margin-bottom: 10px;
}

.subtitle {
    font-size: 1.2em;
    color: #666;
}

/* Navigation */
.main-nav {
    margin: 30px 0;
}

.main-nav h2 {
    font-size: 1.8em;
    margin-bottom: 20px;
    color: #333;
}

.entity-list {
    list-style: none;
}

.entity-item {
    margin-bottom: 10px;
}

.entity-link {
    display: flex;
    align-items: center;
    padding: 15px 20px;
    background-color: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 5px;
    text-decoration: none;
    color: #333;
    transition: all 0.3s ease;
}

.entity-link:hover {
    background-color: #007bff;
    color: white;
    border-color: #0056b3;
    transform: translateX(5px);
}

.entity-icon {
    font-size: 1.5em;
    margin-right: 15px;
}

.entity-name {
    flex-grow: 1;
    font-size: 1.1em;
    font-weight: 500;
}

.entity-arrow {
    font-size: 1.2em;
}

/* Form Styles */
.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    color: #555;
}

.form-control {
    width: 100%;
    padding: 10px 15px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    font-size: 1em;
    transition: border-color 0.3s ease;
}

.form-control:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
}

.form-checkbox {
    width: auto;
    margin-right: 10px;
}

.error {
    color: #dc3545;
    font-size: 0.9em;
    margin-top: 5px;
    display: block;
}

/* Button Styles */
.btn {
    display: inline-block;
    padding: 10px 20px;
    font-size: 1em;
    font-weight: 500;
    text-align: center;
    text-decoration: none;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
    margin-right: 10px;
}

.btn-primary {
    background-color: #007bff;
    color: white;
}

.btn-primary:hover {
    background-color: #0056b3;
}

.btn-secondary {
    background-color: #6c757d;
    color: white;
}

.btn-secondary:hover {
    background-color: #545b62;
}

.btn-search {
    background-color: #28a745;
    color: white;
}

.btn-search:hover {
    background-color: #218838;
}

.btn-edit {
    background-color: #ffc107;
    color: #333;
}

.btn-edit:hover {
    background-color: #e0a800;
}

.btn-delete {
    background-color: #dc3545;
    color: white;
}

.btn-delete:hover {
    background-color: #c82333;
}

.btn-sm {
    padding: 5px 10px;
    font-size: 0.9em;
}

.form-actions {
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #dee2e6;
}

/* Alert Styles */
.alert {
    padding: 15px 20px;
    margin-bottom: 20px;
    border-radius: 4px;
    font-weight: 500;
}

.alert-success {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.alert-error {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

/* Table Styles */
.data-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
    background-color: white;
}

.data-table thead {
    background-color: #007bff;
    color: white;
}

.data-table th,
.data-table td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #dee2e6;
}

.data-table tbody tr:hover {
    background-color: #f8f9fa;
}

.actions-cell {
    white-space: nowrap;
}

/* Badge Styles */
.badge {
    display: inline-block;
    padding: 4px 8px;
    font-size: 0.85em;
    font-weight: 600;
    border-radius: 3px;
}

.badge-yes {
    background-color: #28a745;
    color: white;
}

.badge-no {
    background-color: #6c757d;
    color: white;
}

/* Search Box */
.search-box {
    margin: 20px 0;
}

.search-box form {
    display: flex;
    gap: 10px;
}

.search-box input {
    flex-grow: 1;
}

/* Actions Section */
.actions {
    margin: 20px 0;
    display: flex;
    gap: 10px;
}

/* No Data Message */
.no-data {
    padding: 40px;
    text-align: center;
    color: #666;
    font-style: italic;
}

/* Footer */
.app-footer {
    margin-top: 50px;
    padding-top: 20px;
    border-top: 1px solid #dee2e6;
    text-align: center;
    color: #666;
    font-size: 0.9em;
}

/* Page Title */
h1 {
    font-size: 2em;
    margin-bottom: 20px;
    color: #333;
}

/* Responsive Design */
@media (max-width: 768px) {
    .container {
        padding: 10px;
    }
    
    .app-header h1 {
        font-size: 2em;
    }
    
    .data-table {
        font-size: 0.9em;
    }
    
    .data-table th,
    .data-table td {
        padding: 8px 10px;
    }
    
    .actions {
        flex-direction: column;
    }
    
    .btn {
        width: 100%;
        margin-bottom: 10px;
    }
}
