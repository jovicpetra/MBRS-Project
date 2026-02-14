<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${entityName} List - ${app_name}</title>
    <link rel="stylesheet" href="${r"${pageContext.request.contextPath}"}/resources/css/style.css">
    <script src="${r"${pageContext.request.contextPath}"}/resources/js/app.js"></script>
    <style>
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
        }
        .modal.active {
            display: block;
        }
        .modal-content {
            background-color: #fefefe;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            border-radius: 8px;
            width: 90%;
            max-width: 600px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 10px;
        }
        .modal-header h2 {
            margin: 0;
            font-size: 1.5em;
        }
        .modal-close {
            font-size: 28px;
            font-weight: bold;
            color: #aaa;
            cursor: pointer;
            border: none;
            background: none;
            padding: 0;
        }
        .modal-close:hover {
            color: #000;
        }
        .modal-body {
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        .modal-footer {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }
        .modal-footer button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-save {
            background-color: #4CAF50;
            color: white;
        }
        .btn-save:hover {
            background-color: #45a049;
        }
        .btn-cancel {
            background-color: #f44336;
            color: white;
        }
        .btn-cancel:hover {
            background-color: #da190b;
        }
        .search-box {
            margin: 20px 0;
        }
        .search-box form {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .search-box input[type="text"] {
            flex: 1;
            max-width: 400px;
        }
        .search-box button {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${entityName} List</h1>
        
        <c:if test="${r"${not empty message}"}">
            <div class="alert alert-success">
                ${r"${"}message${r"}"}
            </div>
        </c:if>
        
        <div class="actions">
            <a href="${r"${pageContext.request.contextPath}"}/${entityNameLower}Form.jsp" class="btn btn-primary">Add New ${entityName}</a>
            <a href="${r"${pageContext.request.contextPath}"}/home.jsp" class="btn btn-secondary">Back to Home</a>
        </div>
        
        <div class="search-box">
            <form id="search-form" onsubmit="return false;">
                <input type="text" id="search-input" name="search" placeholder="Search..." class="form-control" />
                <button type="button" class="btn btn-search" onclick="performSearch()">Search</button>
                <button type="button" class="btn btn-secondary" onclick="clearSearch()">Clear</button>
            </form>
        </div>
        
        <p class="no-data" id="no-data" style="display:none;">No ${entityNameLower}s found.</p>
        <table class="data-table" id="entity-table" data-entity="${entityNameLower}">
            <thead>
                <tr>
<#list persistentProperties as property>
    <#if property.name != "id">
        <#assign isEnum = false />
        <#if enums??>
        <#list enums as enum>
            <#if enum.name == property.name>
                <#assign isEnum = true />
            </#if>
        </#list>
        </#if>
        <#if !isEnum>
                            <th>${property.name?cap_first}</th>
        </#if>
    </#if>
</#list>
<#if enums??>
<#list enums as enum>
                            <th>${enum.name?cap_first}</th>
</#list>
</#if>
<#list referencedProperties as refProp>
    <#if refProp.connectionType.name() == "MANY_TO_ONE" || refProp.connectionType.name() == "ONE_TO_ONE">
                            <th>${refProp.name?cap_first}</th>
    </#if>
</#list>
                            <th>Actions</th>
                        </tr>
            </thead>
            <tbody id="entity-table-body">
                <c:forEach var="item" items="${r"${"}${entityNameLower}List${r"}"}">
                    <tr>
<#list persistentProperties as property>
    <#if property.name != "id">
        <#assign isEnum = false />
        <#if enums??>
        <#list enums as enum>
            <#if enum.name == property.name>
                <#assign isEnum = true />
            </#if>
        </#list>
        </#if>
        <#if !isEnum>
        <#if property.type == "boolean">
                                <td>
                                    <c:choose>
                                        <c:when test="${r"${"}item.${property.name}${r"}"}">
                                            <span class="badge badge-yes">Yes</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-no">No</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
        <#elseif property.type == "Date" || property.type == "LocalDate">
                                <td><fmt:formatDate value="${r"${"}item.${property.name}${r"}"}" pattern="yyyy-MM-dd" /></td>
        <#elseif property.type == "LocalDateTime" || property.type == "Timestamp">
                                <td><fmt:formatDate value="${r"${"}item.${property.name}${r"}"}" pattern="yyyy-MM-dd HH:mm" /></td>
        <#else>
                                <td>${r"${"}item.${property.name}${r"}"}</td>
        </#if>
        </#if>
    </#if>
</#list>
<#if enums??>
<#list enums as enum>
                                <td>${r"${"}item.${enum.name}${r"}"}</td>
</#list>
</#if>
<#list referencedProperties as refProp>
    <#if refProp.connectionType.name() == "MANY_TO_ONE" || refProp.connectionType.name() == "ONE_TO_ONE">
                                <td>${r"${"}item.${refProp.name}.name${r"}"}</td>
    </#if>
</#list>
                                <td class="actions-cell">
                                                <a href="${r"${pageContext.request.contextPath}"}/${entityNameLower}Form.jsp?id=${r"${"}item.id${r"}"}" class="btn btn-sm btn-edit">Edit</a>
                                                <a href="${r"${pageContext.request.contextPath}"}/${entityNameLower}Delete/${r"${"}item.id${r"}"}" 
                                                    class="btn btn-sm btn-delete" 
                                                    onclick="return confirm('Are you sure you want to delete this ${entityNameLower}?');">Delete</a>
                                </td>
                            </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <div id="editModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Edit Record</h2>
                <button class="modal-close" onclick="closeEditModal()">&times;</button>
            </div>
            <div class="modal-body" id="modalFormContainer">
            </div>
            <div class="modal-footer">
                <button class="btn btn-cancel" onclick="closeEditModal()">Cancel</button>
                <button class="btn btn-save" onclick="submitEditForm()">Save</button>
            </div>
        </div>
    </div>
    
    <script>
        var entity = '${entityNameLower}';
        var baseUrl = '${r"${pageContext.request.contextPath}"}';
        
        // Helper functions
        function getValue(item, path) {
            var parts = path.split('.');
            var cur = item;
            for (var i = 0; i < parts.length; i++) {
                if (cur == null) {
                    return '';
                }
                cur = cur[parts[i]];
            }
            return cur == null ? '' : cur;
        }

        function formatValue(type, value, isRef) {
            if (value == null) {
                return '';
            }
            if (type === 'boolean') {
                return value ? 'Yes' : 'No';
            }
            if (isRef && typeof value === 'object') {
                // For referenced entities, get meaningful display text
                return getEntityDisplayText(value);
            }
            return value;
        }
        
        function getEntityDisplayText(item) {
            if (!item) {
                return '';
            }
            // Try firstName + lastName (for user, client, etc.)
            if (item.firstName && item.lastName) {
                return item.firstName + ' ' + item.lastName;
            }
            // Try firstName alone
            if (item.firstName) {
                return item.firstName;
            }
            // Try lastName alone
            if (item.lastName) {
                return item.lastName;
            }
            // Try name field
            if (item.name) {
                return item.name;
            }
            // Try title field
            if (item.title) {
                return item.title;
            }
            // Try description field
            if (item.description) {
                return item.description;
            }
            // Try email field
            if (item.email) {
                return item.email;
            }
            // Try code field
            if (item.code) {
                return item.code;
            }
            // Fallback - don't show ID, just a placeholder
            return '[Item ' + (item.id || '?') + ']';
        }
        
        function openEditModal(item, columns) {
            var modal = document.getElementById('editModal');
            var formContainer = document.getElementById('modalFormContainer');
            formContainer.innerHTML = '';
            
            // Store current item for submission
            window.currentEditItem = item;
            window.currentEditColumns = columns;
            
            console.log('Opening edit modal for item:', item);
            console.log('Columns:', columns);
            
            // Get unique reference entities
            var refEntitiesNeeded = {};
            columns.forEach(function(col) {
                console.log('Processing column:', col.key, 'isRef:', col.isRef, 'refEntity:', col.refEntity);
                if (col.isRef && col.refEntity) {
                    if (!refEntitiesNeeded[col.refEntity]) {
                        refEntitiesNeeded[col.refEntity] = col;
                    }
                }
            });
            
            var refEntityList = Object.keys(refEntitiesNeeded);
            console.log('Referenced entities needed:', refEntityList);
            
            // If no referenced entities, render form immediately
            if (refEntityList.length === 0) {
                console.log('No referenced entities, rendering form immediately');
                renderEditForm(item, columns, {});
                modal.classList.add('active');
                return;
            }
            
            // Fetch all referenced entities data
            console.log('Fetching data for referenced entities:', refEntityList);
            var fetchPromises = refEntityList.map(function(refEntity) {
                var fetchUrl = baseUrl + '/api/' + refEntity;
                console.log('Fetching from:', fetchUrl);
                return fetch(fetchUrl)
                    .then(function(resp) {
                        console.log('Response for ' + refEntity + ':', resp.status);
                        if (!resp.ok) throw new Error('Failed to load ' + refEntity + ': ' + resp.status);
                        return resp.json();
                    })
                    .then(function(data) {
                        console.log('Data received for ' + refEntity + ':', data);
                        return { entity: refEntity, data: data || [] };
                    })
                    .catch(function(e) {
                        console.error('Error loading ' + refEntity + ':', e);
                        return { entity: refEntity, data: [] };
                    });
            });
            
            Promise.all(fetchPromises).then(function(refData) {
                console.log('All reference data loaded:', refData);
                var refDataMap = {};
                refData.forEach(function(rd) {
                    refDataMap[rd.entity] = rd.data;
                });
                
                console.log('Reference data map:', refDataMap);
                renderEditForm(item, columns, refDataMap);
                modal.classList.add('active');
            }).catch(function(err) {
                console.error('Error loading referenced entities:', err);
                alert('Error loading form data: ' + err.message);
            });
        }
        
        function renderEditForm(item, columns, refDataMap) {
            var formContainer = document.getElementById('modalFormContainer');
            formContainer.innerHTML = '';
            
            console.log('Rendering edit form with:', { item: item, columns: columns, refDataMap: refDataMap });
            
            columns.forEach(function(col) {
                var key = col.key;
                
                console.log('Rendering field:', key, 'isRef:', col.isRef, 'refEntity:', col.refEntity);
                
                // Skip ID field
                if (key === 'id') {
                    console.log('Skipping ID field');
                    return;
                }
                
                var formGroup = document.createElement('div');
                formGroup.className = 'form-group';
                
                var label = document.createElement('label');
                label.htmlFor = 'field_' + key;
                label.textContent = key.charAt(0).toUpperCase() + key.slice(1);
                formGroup.appendChild(label);
                
                if (col.isRef && col.refEntity) {
                    console.log('Creating dropdown for ref entity:', col.refEntity);
                    
                    // Create dropdown for referenced entity
                    var select = document.createElement('select');
                    select.name = key;
                    select.className = 'form-control';
                    select.setAttribute('data-ref', 'true');
                    
                    var emptyOption = document.createElement('option');
                    emptyOption.value = '';
                    emptyOption.textContent = '-- Select ' + key.charAt(0).toUpperCase() + key.slice(1) + ' --';
                    select.appendChild(emptyOption);
                    
                    var refItems = refDataMap[col.refEntity] || [];
                    console.log('Reference items for ' + col.refEntity + ':', refItems);
                    
                    // DTOs use "propertyId" format instead of nested objects
                    var currentRefId = item[key + 'Id'] || (item[key] ? item[key].id : null);
                    console.log('Current ref ID for field ' + key + ':', currentRefId);
                    
                    if (!Array.isArray(refItems)) {
                        console.warn('Reference items is not an array:', refItems);
                        refItems = [];
                    }
                    
                    refItems.forEach(function(refItem) {
                        console.log('Adding option:', refItem.id);
                        var option = document.createElement('option');
                        option.value = refItem.id;
                        option.textContent = getEntityDisplayText(refItem);
                        if (currentRefId && refItem.id == currentRefId) {
                            option.selected = true;
                        }
                        select.appendChild(option);
                    });
                    
                    console.log('Dropdown created with ' + refItems.length + ' options');
                    formGroup.appendChild(select);
                } else {
                    var input = document.createElement('input');
                    input.type = 'text';
                    input.name = key;
                    input.className = 'form-control';
                    
                    var value = item[key];
                    
                    if (col.type === 'boolean') {
                        input.type = 'checkbox';
                        input.checked = value === true || value === 'true';
                    } else if (col.type === 'Date' || col.type === 'LocalDate') {
                        input.type = 'date';
                        if (value) {
                            input.value = value;
                        } else {
                            // Pre-fill with today's date
                            var today = new Date();
                            input.value = today.getFullYear() + '-' + 
                                         String(today.getMonth() + 1).padStart(2, '0') + '-' + 
                                         String(today.getDate()).padStart(2, '0');
                        }
                    } else if (col.type === 'LocalDateTime' || col.type === 'Timestamp') {
                        input.type = 'datetime-local';
                        if (value) {
                            input.value = value.replace(' ', 'T');
                        } else {
                            // Pre-fill with today's date and current time
                            var now = new Date();
                            input.value = now.getFullYear() + '-' + 
                                         String(now.getMonth() + 1).padStart(2, '0') + '-' + 
                                         String(now.getDate()).padStart(2, '0') + 'T' +
                                         String(now.getHours()).padStart(2, '0') + ':' +
                                         String(now.getMinutes()).padStart(2, '0');
                        }
                    } else if (col.type === 'int' || col.type === 'long' || col.type === 'Integer' || col.type === 'Long') {
                        input.type = 'number';
                        input.value = value || '';
                    } else {
                        input.type = 'text';
                        input.value = value || '';
                    }
                    
                    formGroup.appendChild(input);
                }
                
                formContainer.appendChild(formGroup);
            });
            
            console.log('Form rendering complete');
        }
        
        function closeEditModal() {
            var modal = document.getElementById('editModal');
            modal.classList.remove('active');
            window.currentEditItem = null;
            window.currentEditColumns = null;
        }
        
        function submitEditForm() {
            var formContainer = document.getElementById('modalFormContainer');
            var inputs = formContainer.querySelectorAll('input');
            var selects = formContainer.querySelectorAll('select');
            var data = {};
            
            // Always include the ID from the current item
            data.id = window.currentEditItem.id;
            
            inputs.forEach(function(input) {
                var key = input.name;
                if (input.type === 'checkbox') {
                    data[key] = input.checked;
                } else {
                    data[key] = input.value;
                }
            });
            
            // Handle select dropdowns for referenced entities
            selects.forEach(function(select) {
                var key = select.name;
                var value = select.value;
                
                if (value && value !== '') {
                    // For referenced entities, send just the ID with "Id" suffix
                    data[key + 'Id'] = parseInt(value);
                } else {
                    data[key + 'Id'] = null;
                }
            });
            
            fetch(baseUrl + '/api/' + entity + '/' + window.currentEditItem.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(function(resp) {
                if (!resp.ok) {
                    throw new Error('Update failed: ' + resp.status);
                }
                alert('Record updated successfully!');
                closeEditModal();
                location.reload();
            })
            .catch(function(err) {
                alert('Error updating record: ' + err.message);
            });
        }
        
        // Close modal when clicking outside
        window.onclick = function(event) {
            var modal = document.getElementById('editModal');
            if (event.target === modal) {
                closeEditModal();
            }
        };
        
        (function() {
            var table = document.getElementById('entity-table');
            var tbody = document.getElementById('entity-table-body');
            var noData = document.getElementById('no-data');
            var allItems = []; // Store all items for filtering

            console.log('Entity:', entity);
            console.log('BaseURL:', baseUrl);
            console.log('API URL will be:', baseUrl + '/api/' + entity);

            if (!entity || !tbody) {
                console.error('Missing entity or tbody');
                return;
            }

            var columns = [
<#list persistentProperties as property>
    <#if property.name != "id">
        <#assign isEnum = false />
        <#if enums??>
        <#list enums as enum>
            <#if enum.name == property.name>
                <#assign isEnum = true />
            </#if>
        </#list>
        </#if>
        <#if !isEnum>
                { key: '${property.name}', type: '${property.type}', isRef: false },
        </#if>
    </#if>
</#list>
<#if enums??>
<#list enums as enum>
                { key: '${enum.name}', type: 'enum', isRef: false },
</#list>
</#if>
<#list referencedProperties as refProp>
    <#if refProp.connectionType.name() == "MANY_TO_ONE" || refProp.connectionType.name() == "ONE_TO_ONE">
                { key: '${refProp.name}', type: 'ref', isRef: true, refEntity: '${refProp.type?uncap_first}', refType: '${refProp.type}' },
    </#if>
</#list>
            ];

            function renderRows(items) {
                tbody.innerHTML = '';
                if (!items || !items.length) {
                    if (table) {
                        table.style.display = 'none';
                    }
                    if (noData) {
                        noData.style.display = 'block';
                    }
                    return;
                }

                if (table) {
                    table.style.display = '';
                }
                if (noData) {
                    noData.style.display = 'none';
                }

                items.forEach(function(item) {
                    var tr = document.createElement('tr');

                    columns.forEach(function(col) {
                        var td = document.createElement('td');
                        var value = getValue(item, col.key);
                        td.textContent = formatValue(col.type, value, col.isRef);
                        tr.appendChild(td);
                    });

                    var actionsTd = document.createElement('td');
                    actionsTd.className = 'actions-cell';
                    var editBtn = document.createElement('button');
                    editBtn.type = 'button';
                    editBtn.className = 'btn btn-sm btn-edit';
                    editBtn.textContent = 'Edit';
                    editBtn.onclick = function() {
                        openEditModal(item, columns);
                    };
                    actionsTd.appendChild(editBtn);
                    var deleteLink = document.createElement('a');
                    deleteLink.href = baseUrl + '/' + entity + 'Delete/' + (item.id || '');
                    deleteLink.className = 'btn btn-sm btn-delete';
                    deleteLink.textContent = 'Delete';
                    deleteLink.onclick = function() { return confirm('Are you sure you want to delete this ' + entity + '?'); };
                    actionsTd.appendChild(deleteLink);
                    tr.appendChild(actionsTd);

                    tbody.appendChild(tr);
                });
            }

            fetch(baseUrl + '/api/' + entity)
                .then(function(resp) {
                    if (!resp.ok) {
                        throw new Error('List load failed: ' + resp.status);
                    }
                    return resp.json();
                })
                .then(function(items) {
                    allItems = items || [];
                    renderRows(allItems);
                })
                .catch(function(err) {
                    console.error('Error loading data:', err);
                });
            
            // Make search functions available globally
            window.performSearch = function() {
                var searchInput = document.getElementById('search-input');
                var searchTerm = searchInput.value.toLowerCase().trim();
                
                if (!searchTerm) {
                    renderRows(allItems);
                    return;
                }
                
                var filtered = allItems.filter(function(item) {
                    // Search across all visible columns
                    return columns.some(function(col) {
                        var value = getValue(item, col.key);
                        if (value === null || value === undefined) {
                            return false;
                        }
                        // Format the value for display (handles referenced entities)
                        var displayValue = formatValue(col.type, value, col.isRef);
                        var strValue = String(displayValue).toLowerCase();
                        return strValue.indexOf(searchTerm) !== -1;
                    });
                });
                
                renderRows(filtered);
            };
            
            window.clearSearch = function() {
                var searchInput = document.getElementById('search-input');
                searchInput.value = '';
                renderRows(allItems);
            };
            
            // Enable search on Enter key
            var searchInput = document.getElementById('search-input');
            if (searchInput) {
                searchInput.addEventListener('keyup', function(event) {
                    if (event.key === 'Enter') {
                        window.performSearch();
                    }
                });
            }
        })();
    </script>
</body>
</html>
