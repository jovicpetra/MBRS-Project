# JSP Frontend Generation System

## Overview

This MBRS plugin now supports generating a complete JSP-based frontend using FreeMarker templates. The generated frontend uses Spring MVC JSP tags and JSTL for dynamic content rendering.

## Architecture

### FreeMarker Templates (`resources/templates/frontend/`)

#### 1. **jspForm.ftl** - Entity Form Template
Generates create/edit forms for entities with:
- Spring MVC `form:` tags for model binding
- Dynamic field rendering based on property types:
  - **Boolean**: Checkbox input
  - **Numeric** (int, long, Integer, Long): Number input
  - **Date/LocalDate**: Date picker
  - **LocalDateTime/Timestamp**: DateTime picker
  - **String**: Text input
- Reference property handling (ManyToOne, OneToOne) with dropdown selects
- Enum property support with select options
- Client-side and server-side validation error display
- Responsive form layout

#### 2. **jspList.ftl** - Entity List Template
Generates table-based entity lists with:
- JSTL `c:forEach` for iterating entities
- Search functionality
- Dynamic column rendering based on properties
- Formatted display for dates, booleans, and enums
- Action buttons (Edit, Delete) for each row
- "Add New" button for creating entities
- Empty state handling

#### 3. **jspHome.ftl** - Home Page Template
Generates the application home page with:
- Navigation menu listing all entities
- Links to entity list pages
- Clean, card-based layout
- Application branding

#### 4. **style.css.ftl** - CSS Stylesheet
Provides complete styling for:
- Forms and input controls
- Tables and data grids
- Buttons and action links
- Navigation components
- Alert messages
- Responsive design for mobile devices

#### 5. **app.js.ftl** - JavaScript
Client-side functionality:
- Form validation
- Auto-hiding alert messages
- Delete confirmation dialogs
- Search enhancements

## Generator Classes (`src/myplugin/generator/frontend/`)

### 1. **JspFormGenerator**
- Generates form JSP pages for each entity
- Builds context with entity properties, references, and enums
- Output: `{entityName}Form.jsp` (e.g., `customerForm.jsp`)

### 2. **JspListGenerator**
- Generates list/table JSP pages for each entity
- Handles persistent properties and referenced properties
- Output: `{entityName}List.jsp` (e.g., `customerList.jsp`)

### 3. **JspHomePageGenerator**
- Generates a single home page with navigation
- Lists all entities from the model
- Output: `home.jsp`

### 4. **CssGenerator**
- Generates CSS stylesheet from template
- Output: `resources/css/style.css`

### 5. **JsGenerator**
- Generates JavaScript file from template
- Output: `resources/js/app.js`

## Configuration

### Plugin Initialization (`MyPlugin.java`)

Frontend generators are configured in the plugin initialization:

```java
// JSP Form
GeneratorOptions jspFormOptions = new GeneratorOptions(
    outputPath, "jspForm", "templates/frontend", 
    "{0}.jsp", true, "src/main/webapp/WEB-INF/views"
);

// JSP List
GeneratorOptions jspListOptions = new GeneratorOptions(
    outputPath, "jspList", "templates/frontend", 
    "{0}.jsp", true, "src/main/webapp/WEB-INF/views"
);

// JSP Home
GeneratorOptions jspHomeOptions = new GeneratorOptions(
    outputPath, "jspHome", "templates/frontend", 
    "home.jsp", true, "src/main/webapp/WEB-INF/views"
);

// CSS
GeneratorOptions cssOptions = new GeneratorOptions(
    outputPath, "style.css", "templates/frontend", 
    "style.css", true, "src/main/webapp/resources"
);

// JavaScript
GeneratorOptions jsOptions = new GeneratorOptions(
    outputPath, "app.js", "templates/frontend", 
    "app.js", true, "src/main/webapp/resources"
);
```

### Generation Flow (`GenerateAction.java`)

Frontend generation is invoked after backend generation:

```java
// Backend generation (Model, Repository, Service, Controller)
// ...

// Frontend generation
JspFormGenerator jspFormGenerator = new JspFormGenerator(goJspForm);
jspFormGenerator.generate();

JspListGenerator jspListGenerator = new JspListGenerator(goJspList);
jspListGenerator.generate();

JspHomePageGenerator jspHomeGenerator = new JspHomePageGenerator(goJspHome);
jspHomeGenerator.generate();

CssGenerator cssGenerator = new CssGenerator(goCss);
cssGenerator.generate();

JsGenerator jsGenerator = new JsGenerator(goJs);
jsGenerator.generate();
```

## Output Structure

Generated files follow Spring MVC conventions:

```
src/main/webapp/
├── WEB-INF/
│   └── views/
│       ├── home.jsp
│       ├── customerForm.jsp
│       ├── customerList.jsp
│       ├── appointmentForm.jsp
│       ├── appointmentList.jsp
│       └── ...
└── resources/
    ├── css/
    │   └── style.css
    └── js/
        └── app.js
```

## Extending the Frontend

### To Modify UI Output:

**DO:** Edit FreeMarker templates (`.ftl` files) in `resources/templates/frontend/`

**DON'T:** Edit generated `.jsp` files directly (they will be overwritten)

### To Add More Data to Templates:

1. Extend the generator class (e.g., `JspFormGenerator`)
2. Add data to the `context` map in the `generate()` method
3. Use the new data in the FreeMarker template

Example:
```java
// In JspFormGenerator.java
context.put("additionalData", someValue);
```

```html
<!-- In jspForm.ftl -->
<p>${additionalData}</p>
```

### To Add New Templates:

1. Create new `.ftl` template in `resources/templates/frontend/`
2. Create corresponding generator class extending `BasicGenerator`
3. Add generator options in `MyPlugin.java`
4. Invoke generator in `GenerateAction.java`

## FMModel Extensions

### Added to FMType:
- `isEnumeration()`: Checks if a type is an enumeration

### Used from FMClass:
- `getProperties()`: All properties
- `getPersistentProperties()`: Database-mapped properties
- `getReferencedProperties()`: Relationship properties

### Used from FMModel:
- `getClasses()`: All entity classes
- `getEnumerations()`: All enum types

## Template Context Variables

### Common Variables (all templates):
- `app_name`: Application name from ProjectOptions
- `entityName`: Entity class name (e.g., "Customer")
- `entityNameLower`: Lowercase entity name (e.g., "customer")

### Form Template Variables:
- `pageTitle`: Form page title
- `properties`: List of all properties
- `persistentProperties`: Database properties
- `referencedProperties`: Relationship properties
- `enums`: Enum properties with their values

### List Template Variables:
- `properties`: List of all properties
- `persistentProperties`: Database properties
- `referencedProperties`: Relationship properties

### Home Template Variables:
- `classes`: List of all entity classes
- `currentYear`: Current year for footer

## Property Type Handling

The templates intelligently render different input types:

| Property Type | HTML Input | Notes |
|--------------|------------|-------|
| boolean | checkbox | Uses `form:checkbox` |
| int, long, Integer, Long | number | Uses `type="number"` |
| Date, LocalDate | date | Uses `type="date"` |
| LocalDateTime, Timestamp | datetime-local | Uses `type="datetime-local"` |
| String | text | Default text input |
| Enum | select | Dropdown with enum values |
| Entity reference | select | Dropdown with entity instances |

## Spring MVC Integration

Generated JSPs expect Spring MVC controllers with these endpoints:

- `GET /{entity}/list` - Display entity list
- `GET /{entity}/create` - Display create form
- `GET /{entity}/edit/{id}` - Display edit form
- `POST /{entity}/save` - Save entity
- `GET /{entity}/delete/{id}` - Delete entity

Model attributes expected:
- `{entity}List`: List of entities (for list page)
- `{entity}`: Single entity instance (for form page)
- `{reference}List`: Lists of referenced entities (for dropdowns)

## Best Practices

1. **Keep Templates Generic**: Use FreeMarker directives to handle variations rather than creating template-specific code
2. **Maintain Generation Flow**: Always regenerate from model, don't manually edit generated files
3. **Extend via Context**: Add data to the generator context rather than hardcoding in templates
4. **Use Standard Tags**: Stick to Spring MVC `form:` tags and JSTL for consistency
5. **Test with Different Models**: Ensure templates work with various property types and relationships

## Troubleshooting

### Template Not Found
- Check that template directory is correctly set in GeneratorOptions
- Verify `.ftl` file exists in `resources/templates/frontend/`

### Missing Property Values
- Ensure property is added to context map in generator
- Check FreeMarker variable name matches context key

### Incorrect Field Rendering
- Review property type detection logic in template
- Check FMProperty.getType() returns expected type name

### Generation Errors
- Check console output for FreeMarker template errors
- Verify FMModel contains expected classes and properties
- Ensure output directory permissions allow file creation
