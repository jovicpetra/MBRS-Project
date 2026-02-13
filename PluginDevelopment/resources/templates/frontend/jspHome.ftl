<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${app_name} - Home</title>
    <link rel="stylesheet" href="${r"${pageContext.request.contextPath}"}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <header class="app-header">
            <h1>${app_name}</h1>
        </header>
        
        <nav class="main-nav">
            <ul class="entity-list">
<#list classes as class>
                <li class="entity-item">
                    <a href="${r"${pageContext.request.contextPath}"}/${class.name?lower_case}List.jsp" class="entity-link">
                        <span class="entity-icon">✨</span>
                        <span class="entity-name">${class.name}</span>
                        <span class="entity-arrow">→</span>
                    </a>
                </li>
</#list>
            </ul>
        </nav>
        
        <footer class="app-footer">
       
        </footer>
    </div>
</body>
</html>
