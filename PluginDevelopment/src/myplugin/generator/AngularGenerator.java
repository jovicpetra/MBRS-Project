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

import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generator for AngularJS frontend artifacts (HTML partials and JS scripts).
 * Per-class templates: entity_table, add_entity, view_entity.
 * Global templates: index, angular_controller, angular_service, angular_routes.
 */
public class AngularGenerator extends BasicGenerator {

	public AngularGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	/**
	 * Generate one file per entity class (table, creation, view pages).
	 */
	public void generate() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}

		List<FMClass> classes = FMModel.getInstance().getClasses();

		for (int i = 0; i < classes.size(); i++) {
			FMClass cl = classes.get(i);
			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();
			try {
				out = getWriter(cl.getName(), getFilePackage());
				if (out != null) {
					context.clear();
					context.put("class", cl);
					context.put("persistentProperties", cl.getPersistentProperties());
					context.put("referencedProperties", cl.getReferencedProperties());
					context.put("enumerations", FMModel.getInstance().getEnumerations());
					context.put("importedPackages", cl.getImportedPackages());
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

	/**
	 * Generate the single index.html page (lists all classes in navbar).
	 */
	public void generateIndexHtml() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		List<FMClass> classes = FMModel.getInstance().getClasses();

		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("index", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("classes", classes);
				getTemplate().process(context, out);
				out.flush();
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	/**
	 * Generate a single global JS file (controllers, services, or routes).
	 */
	public void generateJSScript() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		List<FMClass> classes = FMModel.getInstance().getClasses();

		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("js", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("classes", classes);
				getTemplate().process(context, out);
				out.flush();
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	public Writer getWriter(String fileNamePart, String packageName) throws IOException {

		String lowercaseFileName = Character.toLowerCase(fileNamePart.charAt(0)) + fileNamePart.substring(1);

		if (packageName != filePackage) {
			packageName.replace(".", File.separator);
			filePackage = packageName;
		}

		String generatedFileName = "";

		if (templateName.startsWith("entity_table")) {
			generatedFileName = lowercaseFileName + "Table";
		} else if (templateName.startsWith("view_entity")) {
			generatedFileName = lowercaseFileName + "View";
		} else if (templateName.startsWith("add_entity")) {
			generatedFileName = lowercaseFileName + "Creation";
		} else if (templateName.startsWith("index")) {
			generatedFileName = fileNamePart;
		} else if (templateName.startsWith("angular_service")) {
			generatedFileName = "app.services";
		} else if (templateName.startsWith("angular_controller")) {
			generatedFileName = "app.controllers";
		} else if (templateName.startsWith("angular_routes")) {
			generatedFileName = "app.routes";
		} else if (templateName.startsWith("app")) {
			generatedFileName = "app";
		} else if (templateName.startsWith("landing")) {
			generatedFileName = "landing";
		} else if (templateName.startsWith("client_my_appointments")) {
			generatedFileName = "myAppointments";
		} else if (templateName.startsWith("treatment_admin")) {
			generatedFileName = "treatmentAdmin";
		} else if (templateName.startsWith("client_treatment_detail")) {
			generatedFileName = "treatmentDetail";
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

		System.out.println(of.getPath());
		System.out.println(of.getName());

		if (!isOverwrite() && of.exists()) {
			return null;
		}

		return new OutputStreamWriter(new FileOutputStream(of));
	}
}
