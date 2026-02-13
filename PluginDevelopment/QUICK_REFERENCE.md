# JSP Frontend Generation - Quick Reference

## File Structure

### Templates (resources/templates/frontend/)
- `jspForm.ftl` - Entity create/edit forms
- `jspList.ftl` - Entity list/table pages  
- `jspHome.ftl` - Application home page
- `style.css.ftl` - CSS stylesheet
- `app.js.ftl` - JavaScript functionality

### Generators (src/myplugin/generator/frontend/)
- `JspFormGenerator.java` - Generates forms
- `JspListGenerator.java` - Generates list pages
- `JspHomePageGenerator.java` - Generates home page
- `CssGenerator.java` - Generates CSS
- `JsGenerator.java` - Generates JavaScript

## Quick Start: Customizing Generated UI

### Modify Form Fields
Edit **jspForm.ftl** around line 25-50 where property types are checked:

```ftl
<#if property.type.name == "boolean">
    <form:checkbox path="${property.name}" cssClass="form-checkbox" />
<#elseif property.type.name == "int" || property.type.name == "long">
    <form:input path="${property.name}" type="number" cssClass="form-control" />
...
```

### Modify Table Columns
Edit **jspList.ftl** around line 40-80 for column rendering:

```ftl
<#list properties as property>
    <#if property.persistent>
        <th>${property.name?cap_first}</th>
    </#if>
</#list>
```

### Change Styling
Edit **style.css.ftl** - fully customizable CSS:

```css
.btn-primary {
    background-color: #007bff;  /* Change button color */
    color: white;
}
```

### Add JavaScript Behavior
Edit **app.js.ftl** to add client-side functionality:

```javascript
document.addEventListener('DOMContentLoaded', function() {
    // Your custom JavaScript here
});
```

## Common Customizations

### Add New Field Type
In **jspForm.ftl**, add new condition:

```ftl
<#elseif property.type.name == "YourType">
    <form:input path="${property.name}" type="text" cssClass="custom-input" />
```

### Add Computed Field to Context
In **JspFormGenerator.java**, add to context:

```java
context.put("timestamp", System.currentTimeMillis());
```

Then use in **jspForm.ftl**:
```ftl
<div>Generated at: ${timestamp}</div>
```

### Change Output Path  
In **MyPlugin.java**, modify GeneratorOptions:

```java
GeneratorOptions jspFormOptions = new GeneratorOptions(
    outputPath, "jspForm", "templates/frontend", 
    "{0}.jsp", true, "your/custom/path"  // <-- Change here
);
```

### Skip Certain Properties
In template, add filter condition:

```ftl
<#list properties as property>
    <#if property.persistent && property.name != "password">
        <!-- Render field -->
    </#if>
</#list>
```

## FreeMarker Template Syntax Quick Reference

### Variables
```ftl
${variableName}                    <-- Output variable
${variableName?cap_first}          <-- Capitalize first letter
${variableName?lower_case}         <-- Convert to lowercase
${variableName?upper_case}         <-- Convert to uppercase
```

### Conditionals
```ftl
<#if condition>
    ...
<#elseif otherCondition>
    ...
<#else>
    ...
</#if>
```

### Loops
```ftl
<#list items as item>
    ${item.name}
</#list>
```

### Raw Output (for JSP EL)
```ftl
${r"${jspVariable}"}              <-- Outputs ${jspVariable} literally
```

## Context Variables Reference

### All Templates
- `app_name` - Project name

### jspForm.ftl
- `entityName` - "Customer"
- `entityNameLower` - "customer"
- `pageTitle` - "Customer Form"
- `properties` - All properties
- `persistentProperties` - DB fields only
- `referencedProperties` - Relationships
- `enums` - Enum properties with values

### jspList.ftl
- `entityName` - "Customer"
- `entityNameLower` - "customer"
- `properties` - All properties
- `persistentProperties` - DB fields only
- `referencedProperties` - Relationships

### jspHome.ftl
- `classes` - All entity classes
- `currentYear` - Current year

## Testing Your Changes

1. Edit `.ftl` template
2. Run "Generate" action from MagicDraw
3. Check generated `.jsp` files in output folder
4. Build and deploy Spring Boot application
5. Test in browser

## Common Issues

### Property not showing:
- Check if property is persistent: `<#if property.persistent>`
- Verify property type name matches condition

### Wrong input type:
- Add/modify type check in jspForm.ftl
- Ensure FMProperty.getType().getName() returns expected value

### CSS not applied:
- Check CSS path in JSP: `${pageContext.request.contextPath}/resources/css/style.css`
- Verify Spring MVC resource handler configuration

### Reference dropdown empty:
- Ensure controller populates `{reference}List` model attribute
- Example: `model.addAttribute("categoryList", categoryService.findAll());`

## Extension Points

### Add New Page Type
1. Create `newPage.ftl` template
2. Create `NewPageGenerator.java` extending BasicGenerator
3. Add options in MyPlugin.init()
4. Call in GenerateAction.actionPerformed()

### Add Pagination
Modify jspList.ftl to include pagination controls and update controller

### Add Search Filters
Extend search-box div in jspList.ftl with additional filter fields

### Add Validation
Use Spring `form:errors` tag (already included) and add validator in backend

## Learn More

See **JSP_FRONTEND_README.md** for complete documentation.
