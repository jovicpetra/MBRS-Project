<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${r"${"}pageTitle${r"}"} - ${app_name}</title>
    <link rel="stylesheet" href="${r"${pageContext.request.contextPath}"}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>${r"${"}pageTitle${r"}"}</h1>
        
        <c:if test="${r"${not empty error}"}">
            <div class="alert alert-error">
                ${r"${"}error${r"}"}
            </div>
        </c:if>
        
        <form id="entity-form" data-entity="${entityNameLower}" method="POST">
            <input type="hidden" name="id" id="id" value="${r"${"}${entityNameLower}.id${r"}"}" />
            
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
            <div class="form-group">
                <label for="${property.name}">${property.name?cap_first}:</label>
        <#if property.type == "boolean">
                <input type="checkbox" name="${property.name}" id="${property.name}" class="form-checkbox" value="${r"${"}${entityNameLower}.${property.name}${r"}"}" ${r"${"}${entityNameLower}.${property.name} ? 'checked' : ''${r"}"} />
        <#elseif property.type == "int" || property.type == "long" || property.type == "Integer" || property.type == "Long">
                <input type="number" name="${property.name}" id="${property.name}" class="form-control" value="${r"${"}${entityNameLower}.${property.name}${r"}"}" />
        <#elseif property.type == "Date" || property.type == "LocalDate">
                <input type="date" name="${property.name}" id="${property.name}" class="form-control" value="${r"${"}${entityNameLower}.${property.name}${r"}"}" />
        <#elseif property.type == "LocalDateTime" || property.type == "Timestamp">
                <input type="datetime-local" name="${property.name}" id="${property.name}" class="form-control" value="${r"${"}${entityNameLower}.${property.name}${r"}"}" />
        <#else>
                <input type="text" name="${property.name}" id="${property.name}" class="form-control" value="${r"${"}${entityNameLower}.${property.name}${r"}"}" />
        </#if>
            </div>
        </#if>
    </#if>
</#list>
<#list referencedProperties as refProp>
    <#if refProp.connectionType.name() == "MANY_TO_ONE" || refProp.connectionType.name() == "ONE_TO_ONE">
            <div class="form-group">
                <label for="${refProp.name}">${refProp.name?cap_first}:</label>
                <select id="${refProp.name}" name="${refProp.name}" class="form-control" data-ref-entity="${refProp.type?uncap_first}" data-ref-field="${refProp.name}">
                    <option value="">-- Select ${refProp.name?cap_first} --</option>
                </select>
            </div>
    </#if>
</#list>
<#if enums??>
<#list enums as enum>
            <div class="form-group">
                <label for="${enum.name}">${enum.name?cap_first}:</label>
                <select name="${enum.name}" id="${enum.name}" class="form-control">
                    <option value="">-- Select ${enum.name?cap_first} --</option>
    <#list enum.literals as literal>
                    <option value="${literal}" ${r"${"}${entityNameLower}.${enum.name} == '${literal}' ? 'selected' : ''${r"}"}>${literal}</option>
    </#list>
                </select>
            </div>
</#list>
</#if>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save</button>
                <a href="${r"${pageContext.request.contextPath}"}/${entityNameLower}List.jsp" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
    <script>
        (function() {
            var form = document.getElementById('entity-form');
            if (!form) {
                return;
            }

            var entity = form.getAttribute('data-entity');
            var baseUrl = '${r"${pageContext.request.contextPath}"}';
            var urlParams = new URLSearchParams(window.location.search);
            var entityId = urlParams.get('id');

            console.log('Form loaded for entity:', entity, 'with ID:', entityId);

            // Find all reference dropdowns
            var refSelects = form.querySelectorAll('select[data-ref-entity]');
            console.log('Found ' + refSelects.length + ' reference selects');

            // Fetch data for all referenced entities
            var refEntities = {};
            var fetchPromises = [];

            refSelects.forEach(function(select) {
                var refEntity = select.getAttribute('data-ref-entity');
                if (!refEntities[refEntity]) {
                    refEntities[refEntity] = [];
                    var fetchUrl = baseUrl + '/api/' + refEntity;
                    console.log('Will fetch from:', fetchUrl);
                    fetchPromises.push(
                        fetch(fetchUrl)
                            .then(function(resp) {
                                console.log('Response for ' + refEntity + ':', resp.status);
                                if (!resp.ok) throw new Error('Failed to load ' + refEntity);
                                return resp.json();
                            })
                            .then(function(data) {
                                console.log('Data for ' + refEntity + ':', data);
                                refEntities[refEntity] = data || [];
                                return { entity: refEntity, data: data || [] };
                            })
                            .catch(function(e) {
                                console.error('Error loading ' + refEntity + ':', e);
                                refEntities[refEntity] = [];
                                return { entity: refEntity, data: [] };
                            })
                    );
                }
            });

            // Populate reference dropdowns once data is loaded
            Promise.all(fetchPromises).then(function() {
                console.log('All reference entities loaded:', refEntities);
                refSelects.forEach(function(select) {
                    var refEntity = select.getAttribute('data-ref-entity');
                    var data = refEntities[refEntity] || [];
                    console.log('Populating ' + refEntity + ' dropdown with', data.length, 'items');

                    data.forEach(function(item) {
                        var option = document.createElement('option');
                        option.value = item.id;
                        // Display meaningful information without showing ID
                        option.textContent = getEntityDisplayText(item);
                        select.appendChild(option);
                    });
                });

                // After populating dropdowns, load entity data if editing
                if (entityId && entityId !== '') {
                    loadEntityData(entityId);
                } else {
                    // For new records, pre-fill date fields with today's date
                    preFillDates();
                }
            }).catch(function(err) {
                console.error('Error loading reference data:', err);
            });

            function getEntityDisplayText(item) {
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
                return '[Item ' + item.id + ']';
            }

            function preFillDates() {
                var today = new Date();
                var todayStr = today.getFullYear() + '-' +
                               String(today.getMonth() + 1).padStart(2, '0') + '-' +
                               String(today.getDate()).padStart(2, '0');

                form.querySelectorAll('input[type="date"]').forEach(function(input) {
                    if (!input.value) {
                        input.value = todayStr;
                    }
                });

                var now = new Date();
                var nowStr = now.getFullYear() + '-' +
                            String(now.getMonth() + 1).padStart(2, '0') + '-' +
                            String(now.getDate()).padStart(2, '0') + 'T' +
                            String(now.getHours()).padStart(2, '0') + ':' +
                            String(now.getMinutes()).padStart(2, '0');

                form.querySelectorAll('input[type="datetime-local"]').forEach(function(input) {
                    if (!input.value) {
                        input.value = nowStr;
                    }
                });
            }

            function loadEntityData(id) {
                fetch(baseUrl + '/api/' + entity + '/' + id)
                    .then(function(resp) {
                        if (!resp.ok) {
                            throw new Error('Load failed: ' + resp.status);
                        }
                        return resp.json();
                    })
                    .then(function(data) {
                        console.log('Loaded entity data:', data);
                        form.elements['id'].value = data.id;

                        // Set regular form fields
                        Object.keys(data).forEach(function(key) {
                            var input = form.elements[key];
                            if (input) {
                                if (input.type === 'checkbox') {
                                    input.checked = data[key];
                                } else {
                                    input.value = data[key];
                                }
                            }
                        });

                        // Set referenced entity dropdowns
                        refSelects.forEach(function(select) {
                            var refField = select.getAttribute('data-ref-field');
                            if (data[refField] && data[refField].id) {
                                select.value = data[refField].id;
                                console.log('Set ' + refField + ' to', data[refField].id);
                            }
                        });
                    })
                    .catch(function(err) {
                        console.error('Error loading entity:', err);
                    });
            }

            function setNested(obj, path, value) {
                var parts = path.split('.');
                var cur = obj;
                for (var i = 0; i < parts.length - 1; i++) {
                    if (!cur[parts[i]]) {
                        cur[parts[i]] = {};
                    }
                    cur = cur[parts[i]];
                }
                cur[parts[parts.length - 1]] = value;
            }

            form.addEventListener('submit', function(e) {
                e.preventDefault();
                var entity = form.getAttribute('data-entity');
                var formData = new FormData(form);
                var payload = {};

                formData.forEach(function(value, key) {
                    if (value === '') {
                        return;
                    }

                    // Check if this is a reference field
                    var select = form.querySelector('select[name="' + key + '"]');
                    if (select && select.getAttribute('data-ref-entity')) {
                        // For referenced entities, send just the ID with "Id" suffix
                        if (value) {
                            payload[key + 'Id'] = parseInt(value);
                        }
                    } else {
                        payload[key] = value;
                    }
                });

                var id = payload.id ? parseInt(payload.id) : null;
                var method = id ? 'PUT' : 'POST';
                var url = '${r"${pageContext.request.contextPath}"}/api/' + entity + (id ? '/' + id : '');

                console.log('Submitting form:', { entity: entity, id: id, method: method, url: url, payload: payload });

                fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                }).then(function(resp) {
                    if (!resp.ok) {
                        return resp.text().then(function(text) {
                            throw new Error('Save failed: ' + resp.status + '\n' + text);
                        });
                    }
                    window.location.href = '${r"${pageContext.request.contextPath}"}/' + entity + 'List.jsp';
                }).catch(function(err) {
                    alert(err.message);
                });
            });
        })();
    </script>
</body>
</html>
