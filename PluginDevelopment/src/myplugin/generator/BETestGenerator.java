package myplugin.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMMethod;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.fmmodel.FMParameter;
import myplugin.generator.fmmodel.PersistentProperty;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generates backend test artifacts for each entity:
 * - Service tests (JUnit 5 + Mockito)
 * - Controller tests (MockMvc + @WebMvcTest)
 * - Repository tests (@DataJpaTest)
 * Also generates one-time backend test configuration files.
 */
public class BETestGenerator extends BasicGenerator {

	public BETestGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public BETestGenerator(GeneratorOptions generatorOptions, Configuration configuration) {
		super(generatorOptions, configuration);
	}

	public void generateAllTests(List<FMClass> entities, String testOutputPath) {
		if (entities == null || entities.isEmpty()) {
			return;
		}

		String previousOutputPath = getOutputPath();
		String previousTemplateName = getTemplateName();
		String previousFilePackage = getFilePackage();

		String basePackage = extractBasePackage(previousFilePackage);
		String baseJavaPackage = normalizeToJavaPackage(basePackage);
		Map<String, String> enumDefaults = buildEnumDefaults(baseJavaPackage);

		try {
			setOutputPath(testOutputPath);
			generateByTemplate("ServiceTest", entities, basePackage + "/services", baseJavaPackage + ".services", enumDefaults);
			generateByTemplate("ControllerTest", entities, basePackage + "/controllers", baseJavaPackage + ".controllers", enumDefaults);
			generateByTemplate("RepositoryTest", entities, basePackage + "/repositories", baseJavaPackage + ".repositories", enumDefaults);
			generateBackendTestConfigs(previousOutputPath, baseJavaPackage);
		} finally {
			setOutputPath(previousOutputPath);
			setTemplateName(previousTemplateName);
			setFilePackage(previousFilePackage);
		}
	}

	private void generateByTemplate(String templateName, List<FMClass> entities, String filePackagePath,
			String testPackageName, Map<String, String> enumDefaults) {
		setTemplateName(templateName);
		setFilePackage(filePackagePath);

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}

		for (int i = 0; i < entities.size(); i++) {
			FMClass entity = entities.get(i);
			Map<String, String> idField = buildIdField(entity.getPersistentProperties(), enumDefaults);
			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();

			try {
				out = getWriter(entity.getName(), getFilePackage());
				if (out != null) {
					context.clear();
					context.put("class", entity);
					context.put("entity", entity.getEntity());
					context.put("persistentProperties", entity.getPersistentProperties());
					context.put("referencedProperties", entity.getReferencedProperties());
					context.put("entityName", entity.getName());
					context.put("entityNameLower", lowerCaseFirst(entity.getName()));
					context.put("fields", buildFields(entity.getPersistentProperties(), enumDefaults));
					context.put("customQueryMethods", buildCustomQueryMethods(entity.getMethods(), enumDefaults));
					context.put("packageName", testPackageName);
					context.put("app_name", extractBaseJavaPackage(testPackageName));
					context.put("basePackage", extractBaseJavaPackage(testPackageName));
					context.put("modelPackage", extractBaseJavaPackage(testPackageName) + ".models");
					context.put("dtoPackage", extractBaseJavaPackage(testPackageName) + ".models.dto");
					context.put("servicePackage", extractBaseJavaPackage(testPackageName) + ".services");
					context.put("repositoryPackage", extractBaseJavaPackage(testPackageName) + ".repositories");
					context.put("controllerPackage", extractBaseJavaPackage(testPackageName) + ".controllers");
					context.put("idFieldName", idField.get("name"));
					context.put("idFieldType", idField.get("type"));
					context.put("idFieldSampleValue", idField.get("sampleValue"));
					context.put("idFieldAccessor", idField.get("accessor"));
					getTemplate().process(context, out);
					out.flush();
				}
			} catch (TemplateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	private List<Map<String, String>> buildFields(List<PersistentProperty> persistentProperties,
			Map<String, String> enumDefaults) {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		if (persistentProperties == null) {
			return fields;
		}

		for (int i = 0; i < persistentProperties.size(); i++) {
			PersistentProperty property = persistentProperties.get(i);
			Map<String, String> field = new HashMap<String, String>();
			field.put("name", property.getName());
			field.put("type", property.getType());
			field.put("sampleValue", sampleValueForType(property.getType(), enumDefaults));
			field.put("nullable", String.valueOf(property.isNullable()));
			field.put("isId", String.valueOf(property.getIsId()));
			fields.add(field);
		}
		return fields;
	}

	private Map<String, String> buildIdField(List<PersistentProperty> persistentProperties,
			Map<String, String> enumDefaults) {
		Map<String, String> idField = new HashMap<String, String>();
		idField.put("name", "id");
		idField.put("type", "Integer");
		idField.put("sampleValue", "1");
		idField.put("accessor", "Id");

		if (persistentProperties == null || persistentProperties.isEmpty()) {
			return idField;
		}

		for (int i = 0; i < persistentProperties.size(); i++) {
			PersistentProperty property = persistentProperties.get(i);
			if (property.getIsId() || "id".equalsIgnoreCase(property.getName())) {
				idField.put("name", property.getName());
				idField.put("type", property.getType());
				idField.put("sampleValue", sampleValueForType(property.getType(), enumDefaults));
				idField.put("accessor", capitalize(property.getName()));
				return idField;
			}
		}

		PersistentProperty firstProperty = persistentProperties.get(0);
		idField.put("name", firstProperty.getName());
		idField.put("type", firstProperty.getType());
		idField.put("sampleValue", sampleValueForType(firstProperty.getType(), enumDefaults));
		idField.put("accessor", capitalize(firstProperty.getName()));
		return idField;
	}

	private Map<String, String> buildEnumDefaults(String baseJavaPackage) {
		Map<String, String> enumDefaults = new HashMap<String, String>();
		List<FMEnumeration> enumerations = FMModel.getInstance().getEnumerations();
		for (int i = 0; i < enumerations.size(); i++) {
			FMEnumeration enumeration = enumerations.get(i);
			if (enumeration.getValuesCount() > 0) {
				enumDefaults.put(enumeration.getName(), baseJavaPackage + ".enums." + enumeration.getName() + "." + enumeration.getValueAt(0));
			}
		}
		return enumDefaults;
	}

	private List<Map<String, Object>> buildCustomQueryMethods(List<FMMethod> methods, Map<String, String> enumDefaults) {
		List<Map<String, Object>> customMethods = new ArrayList<Map<String, Object>>();
		if (methods == null) {
			return customMethods;
		}

		for (int i = 0; i < methods.size(); i++) {
			FMMethod method = methods.get(i);
			if (method == null || method.getName() == null) {
				continue;
			}
			String methodName = method.getName();
			if (!(methodName.startsWith("findBy") || methodName.startsWith("countBy") || methodName.startsWith("existsBy"))) {
				continue;
			}

			Map<String, Object> methodData = new HashMap<String, Object>();
			methodData.put("name", methodName);

			List<Map<String, String>> params = new ArrayList<Map<String, String>>();
			List<FMParameter> methodParameters = method.getParameters();
			if (methodParameters != null) {
				for (int j = 0; j < methodParameters.size(); j++) {
					FMParameter parameter = methodParameters.get(j);
					String typeName = "Object";
					if (parameter != null && parameter.getType() != null && parameter.getType().getName() != null) {
						typeName = parameter.getType().getName();
					}
					Map<String, String> param = new HashMap<String, String>();
					param.put("type", typeName);
					param.put("value", sampleValueForType(typeName, enumDefaults));
					params.add(param);
				}
			}

			methodData.put("params", params);
			customMethods.add(methodData);
		}

		return customMethods;
	}

	private void generateBackendTestConfigs(String projectOutputPath, String packageName) {
		try {
			writePomIfNeeded(projectOutputPath, packageName);
			writeApplicationTestProperties(projectOutputPath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void writePomIfNeeded(String projectOutputPath, String packageName) throws IOException {
		File pomFile = new File(projectOutputPath + File.separator + "pom.xml");
		if (pomFile.exists() && !isOverwrite()) {
			return;
		}

		if (!pomFile.getParentFile().exists() && !pomFile.getParentFile().mkdirs()) {
			throw new IOException("An error occurred during output folder creation " + pomFile.getParent());
		}

		String content = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
				+ "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
				+ "    <modelVersion>4.0.0</modelVersion>\n"
				+ "    <groupId>" + packageName + "</groupId>\n"
				+ "    <artifactId>generated-app</artifactId>\n"
				+ "    <version>1.0.0-SNAPSHOT</version>\n"
				+ "\n"
				+ "    <properties>\n"
				+ "        <java.version>17</java.version>\n"
				+ "        <maven.compiler.source>${java.version}</maven.compiler.source>\n"
				+ "        <maven.compiler.target>${java.version}</maven.compiler.target>\n"
				+ "        <junit.jupiter.version>5.10.2</junit.jupiter.version>\n"
				+ "        <mockito.version>5.12.0</mockito.version>\n"
				+ "        <spring.boot.version>3.2.5</spring.boot.version>\n"
				+ "    </properties>\n"
				+ "\n"
				+ "    <dependencies>\n"
				+ "        <dependency>\n"
				+ "            <groupId>org.springframework.boot</groupId>\n"
				+ "            <artifactId>spring-boot-starter-test</artifactId>\n"
				+ "            <version>${spring.boot.version}</version>\n"
				+ "            <scope>test</scope>\n"
				+ "        </dependency>\n"
				+ "        <dependency>\n"
				+ "            <groupId>org.junit.jupiter</groupId>\n"
				+ "            <artifactId>junit-jupiter</artifactId>\n"
				+ "            <version>${junit.jupiter.version}</version>\n"
				+ "            <scope>test</scope>\n"
				+ "        </dependency>\n"
				+ "        <dependency>\n"
				+ "            <groupId>org.mockito</groupId>\n"
				+ "            <artifactId>mockito-core</artifactId>\n"
				+ "            <version>${mockito.version}</version>\n"
				+ "            <scope>test</scope>\n"
				+ "        </dependency>\n"
				+ "        <dependency>\n"
				+ "            <groupId>com.h2database</groupId>\n"
				+ "            <artifactId>h2</artifactId>\n"
				+ "            <scope>test</scope>\n"
				+ "        </dependency>\n"
				+ "    </dependencies>\n"
				+ "\n"
				+ "    <build>\n"
				+ "        <plugins>\n"
				+ "            <plugin>\n"
				+ "                <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                <artifactId>maven-compiler-plugin</artifactId>\n"
				+ "                <version>3.11.0</version>\n"
				+ "                <configuration>\n"
				+ "                    <source>${java.version}</source>\n"
				+ "                    <target>${java.version}</target>\n"
				+ "                </configuration>\n"
				+ "            </plugin>\n"
				+ "            <plugin>\n"
				+ "                <groupId>org.apache.maven.plugins</groupId>\n"
				+ "                <artifactId>maven-surefire-plugin</artifactId>\n"
				+ "                <version>3.2.5</version>\n"
				+ "                <configuration>\n"
				+ "                    <failIfNoTests>true</failIfNoTests>\n"
				+ "                    <useModulePath>false</useModulePath>\n"
				+ "                </configuration>\n"
				+ "            </plugin>\n"
				+ "        </plugins>\n"
				+ "    </build>\n"
				+ "</project>\n";

		Writer writer = new OutputStreamWriter(new FileOutputStream(pomFile));
		try {
			writer.write(content);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	private void writeApplicationTestProperties(String projectOutputPath) throws IOException {
		File propertiesFile = new File(projectOutputPath + File.separator + "src" + File.separator
				+ "test" + File.separator + "resources" + File.separator + "application-test.properties");

		if (propertiesFile.exists() && !isOverwrite()) {
			return;
		}

		if (!propertiesFile.getParentFile().exists() && !propertiesFile.getParentFile().mkdirs()) {
			throw new IOException("An error occurred during output folder creation " + propertiesFile.getParent());
		}

		String content = "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1\n"
				+ "spring.datasource.driverClassName=org.h2.Driver\n"
				+ "spring.datasource.username=sa\n"
				+ "spring.datasource.password=\n"
				+ "spring.jpa.hibernate.ddl-auto=create-drop\n"
				+ "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect\n";

		Writer writer = new OutputStreamWriter(new FileOutputStream(propertiesFile));
		try {
			writer.write(content);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	private String extractBasePackage(String filePackagePath) {
		if (filePackagePath == null || filePackagePath.isEmpty()) {
			return "BeautySalon";
		}

		String normalized = filePackagePath.replace("\\", "/");
		String packagePath = stripSourceRoot(normalized);

		if (packagePath.endsWith("/services")) {
			packagePath = packagePath.substring(0, packagePath.length() - "/services".length());
		} else if (packagePath.endsWith("/repositories")) {
			packagePath = packagePath.substring(0, packagePath.length() - "/repositories".length());
		} else if (packagePath.endsWith("/controllers")) {
			packagePath = packagePath.substring(0, packagePath.length() - "/controllers".length());
		} else if (packagePath.endsWith("/models")) {
			packagePath = packagePath.substring(0, packagePath.length() - "/models".length());
		}

		if (packagePath.isEmpty()) {
			return "BeautySalon";
		}

		return packagePath;
	}

	private String stripSourceRoot(String path) {
		String[] markers = new String[] { "src/main/java/", "src/test/java/" };
		for (int i = 0; i < markers.length; i++) {
			int markerIndex = path.indexOf(markers[i]);
			if (markerIndex >= 0) {
				String stripped = path.substring(markerIndex + markers[i].length());
				return stripped.isEmpty() ? "BeautySalon" : stripped;
			}
		}
		return path;
	}

	private String normalizeToJavaPackage(String packagePath) {
		if (packagePath == null || packagePath.isEmpty()) {
			return "BeautySalon";
		}
		return packagePath.replace('/', '.').replace('\\', '.');
	}

	private String lowerCaseFirst(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		return Character.toLowerCase(value.charAt(0)) + value.substring(1);
	}

	private String capitalize(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}

	private String extractBaseJavaPackage(String packageName) {
		if (packageName == null || packageName.isEmpty()) {
			return "BeautySalon";
		}
		int lastDot = packageName.lastIndexOf('.');
		if (lastDot > 0) {
			return packageName.substring(0, lastDot);
		}
		return packageName;
	}

	private String sampleValueForType(String type, Map<String, String> enumDefaults) {
		if (type == null) {
			return "null";
		}

		if (enumDefaults.containsKey(type)) return enumDefaults.get(type);
		if ("String".equals(type)) return "\"sample-value\"";
		if ("Integer".equals(type) || "int".equals(type)) return "1";
		if ("Long".equals(type) || "long".equals(type)) return "1L";
		if ("Double".equals(type) || "double".equals(type)) return "10.5d";
		if ("Float".equals(type) || "float".equals(type)) return "10.5f";
		if ("Boolean".equals(type) || "boolean".equals(type)) return "true";
		if ("BigDecimal".equals(type)) return "new BigDecimal(\"10.50\")";
		if ("LocalDate".equals(type)) return "LocalDate.of(2024, 1, 1)";
		if ("LocalDateTime".equals(type)) return "LocalDateTime.of(2024, 1, 1, 10, 30)";
		if ("Date".equalsIgnoreCase(type)) return "new Date(1704067200000L)";
		if ("UUID".equals(type)) return "UUID.fromString(\"00000000-0000-0000-0000-000000000001\")";
		if ("Short".equals(type) || "short".equals(type)) return "(short) 1";
		if ("Byte".equals(type) || "byte".equals(type)) return "(byte) 1";
		if ("Character".equals(type) || "char".equals(type)) return "'a'";

		return "null";
	}

	@Override
	public Writer getWriter(String fileNamePart, String packageName) throws IOException {

		String generatedFileName = "";

		if (templateName.startsWith("ServiceTest")) {
			generatedFileName = fileNamePart + "ServiceTest";
		} else if (templateName.startsWith("ControllerTest")) {
			generatedFileName = fileNamePart + "ControllerTest";
		} else if (templateName.startsWith("RepositoryTest")) {
			generatedFileName = fileNamePart + "RepositoryTest";
		}

		if (packageName != filePackage) {
			packageName.replace(".", File.separator);
			filePackage = packageName;
		}

		String fullPath = outputPath
				+ File.separator
				+ (filePackage.isEmpty() ? "" : packageToPath(filePackage) + File.separator)
				+ outputFileName.replace("{0}", generatedFileName);

		File of = new File(fullPath);
		if (!of.getParentFile().exists()) {
			if (!of.getParentFile().mkdirs()) {
				throw new IOException("An error occurred during output folder creation " + outputPath);
			}
		}

		if (!isOverwrite() && of.exists()) {
			return null;
		}

		return new OutputStreamWriter(new FileOutputStream(of));
	}
}
