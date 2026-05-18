package myplugin.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generates AngularJS unit test specs for each UI class:
 * - ControllerSpec (Jasmine)
 * - ServiceSpec (Jasmine + $httpBackend)
 * Also generates one-time Jasmine CLI configuration for Node.js execution.
 */
public class AngularTestGenerator extends BasicGenerator {

	public AngularTestGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public AngularTestGenerator(GeneratorOptions generatorOptions, Configuration configuration) {
		super(generatorOptions, configuration);
	}

	public void generateAllTests(List<FMClass> uiClasses, String testOutputPath) {
		if (uiClasses == null || uiClasses.isEmpty()) {
			return;
		}

		String previousOutputPath = getOutputPath();
		String previousTemplateName = getTemplateName();
		String previousFilePackage = getFilePackage();

		try {
			setOutputPath(testOutputPath);
			generateByTemplate("ControllerSpec", uiClasses, "");
			generateByTemplate("ServiceSpec", uiClasses, "");
			generateJasmineCliConfig(previousOutputPath);
		} finally {
			setOutputPath(previousOutputPath);
			setTemplateName(previousTemplateName);
			setFilePackage(previousFilePackage);
		}
	}

	private void generateByTemplate(String templateName, List<FMClass> uiClasses, String filePackagePath) {
		setTemplateName(templateName);
		setFilePackage(filePackagePath);

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}

		for (int i = 0; i < uiClasses.size(); i++) {
			FMClass uiClass = uiClasses.get(i);
			if (uiClass.getUiClass() == null) {
				continue;
			}

			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();

			try {
				out = getWriter(uiClass.getName(), getFilePackage());
				if (out != null) {
					context.clear();
					context.put("class", uiClass);
					context.put("uiClass", uiClass.getUiClass());
					context.put("entityName", uiClass.getName());
					context.put("entityNameLower", lowerCaseFirst(uiClass.getName()));
					context.put("persistentProperties", uiClass.getPersistentProperties());
					context.put("referencedProperties", uiClass.getReferencedProperties());
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

	private void generateJasmineCliConfig(String projectOutputPath) {
		try {
			writePackageJson(projectOutputPath);
			writeJasmineJson(projectOutputPath);
			writeJasmineSetupHelper(projectOutputPath);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void writePackageJson(String projectOutputPath) throws IOException {
		File file = new File(projectOutputPath + File.separator + "package.json");
		if (file.exists() && !isOverwrite()) {
			return;
		}
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new IOException("An error occurred during output folder creation " + file.getParent());
		}
		String content = "{\n"
				+ "  \"name\": \"generated-app-frontend-tests\",\n"
				+ "  \"version\": \"1.0.0\",\n"
				+ "  \"scripts\": {\n"
				+ "    \"test\": \"jasmine\"\n"
				+ "  },\n"
				+ "  \"devDependencies\": {\n"
				+ "    \"jasmine\": \"^5.1.0\",\n"
				+ "    \"jsdom\": \"^24.0.0\",\n"
				+ "    \"angular\": \"^1.8.3\",\n"
				+ "    \"angular-mocks\": \"^1.8.3\",\n"
				+ "    \"angular-route\": \"^1.8.3\"\n"
				+ "  }\n"
				+ "}\n";
		writeFile(file, content);
	}

	private void writeJasmineJson(String projectOutputPath) throws IOException {
		File dir = new File(projectOutputPath + File.separator + "spec" + File.separator + "support");
		if (!dir.exists() && !dir.mkdirs()) {
			throw new IOException("An error occurred during output folder creation " + dir.getPath());
		}
		File file = new File(dir, "jasmine.json");
		if (file.exists() && !isOverwrite()) {
			return;
		}
		String content = "{\n"
				+ "  \"spec_dir\": \"src/test/js\",\n"
				+ "  \"spec_files\": [\"**/*Spec.js\"],\n"
				+ "  \"helpers\": [\"../../../spec/helpers/setup.js\"],\n"
				+ "  \"stopSpecOnExpectationFailure\": false,\n"
				+ "  \"random\": false\n"
				+ "}\n";
		writeFile(file, content);
	}

	private void writeJasmineSetupHelper(String projectOutputPath) throws IOException {
		File dir = new File(projectOutputPath + File.separator + "spec" + File.separator + "helpers");
		if (!dir.exists() && !dir.mkdirs()) {
			throw new IOException("An error occurred during output folder creation " + dir.getPath());
		}
		File file = new File(dir, "setup.js");
		if (file.exists() && !isOverwrite()) {
			return;
		}
		String content = "// Set up a browser-like DOM environment for AngularJS\n"
				+ "const { JSDOM } = require('jsdom');\n\n"
				+ "const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>', {\n"
				+ "  url: 'http://localhost'\n"
				+ "});\n\n"
				+ "global.window    = dom.window;\n"
				+ "global.document  = dom.window.document;\n"
				+ "global.navigator = dom.window.navigator;\n"
				+ "global.Element   = dom.window.Element;\n\n"
				+ "// Load the UMD bundle directly; it initializes window.angular\n"
				+ "require('angular/angular');\n"
				+ "global.angular = window.angular;\n"
				+ "require('angular-route/angular-route');\n"
				+ "// angular-mocks initializes helpers only when window.jasmine exists\n"
				+ "window.jasmine = jasmine;\n"
				+ "window.beforeEach = beforeEach;\n"
				+ "window.afterEach = afterEach;\n"
				+ "require('angular-mocks/angular-mocks');\n"
				+ "global.inject = window.inject;\n\n"
				+ "// Stub ui.bootstrap — the module declaration must exist but its\n"
				+ "// directives/services are fully mocked in tests, so an empty module is enough\n"
				+ "angular.module('ui.bootstrap', []);\n\n"
				+ "// Load all application modules so specs can call module('exampleApp.controllers') etc.\n"
				+ "require('../../frontend/js/app.js');\n"
				+ "require('../../frontend/js/app.controllers.js');\n"
				+ "require('../../frontend/js/app.services.js');\n"
				+ "require('../../frontend/js/app.routes.js');\n"
				+ "global.inject = angular.mock.inject;\n";
		writeFile(file, content);
	}

	private void writeFile(File file, String content) throws IOException {
		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		try {
			writer.write(content);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	private String lowerCaseFirst(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		return Character.toLowerCase(value.charAt(0)) + value.substring(1);
	}

	@Override
	public Writer getWriter(String fileNamePart, String packageName) throws IOException {
		String generatedFileName = "";
		if (templateName.startsWith("ControllerSpec")) {
			generatedFileName = fileNamePart + "ControllerSpec";
		} else if (templateName.startsWith("ServiceSpec")) {
			generatedFileName = fileNamePart + "ServiceSpec";
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
