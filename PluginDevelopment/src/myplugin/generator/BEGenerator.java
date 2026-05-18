package myplugin.generator;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.fmmodel.PersistentProperty;
import myplugin.generator.options.GeneratorOptions;

/**
 * EJB generator that now generates incomplete ejb classes based on MagicDraw
 * class model
 * 
 * @ToDo: enhance resources/templates/ejbclass.ftl template and intermediate
 *        data structure (@see myplugin.generator.fmmodel) in order to generate
 *        complete ejb classes
 */

public class BEGenerator extends BasicGenerator {

	public BEGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public BEGenerator(GeneratorOptions generatorOptions, Configuration configuration) {
		super(generatorOptions, configuration);
	}

	public void generate() {

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		List<FMClass> classes = FMModel.getInstance().getClasses();
		for (int i = 0; i < classes.size(); i++) {
			FMClass cl = classes.get(i);
			PersistentProperty idProperty = getIdProperty(cl);
			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();
			try {
				out = getWriter(cl.getName(), getFilePackage());
				if (out != null) {
					context.clear();
					context.put("class", cl);
					context.put("properties", cl.getProperties());
					context.put("package", "BeautySalon");
					context.put("persistentProperties", cl.getPersistentProperties());
					context.put("referencedProperties", cl.getReferencedProperties());
					context.put("entity", cl.getEntity());
					context.put("app_name", "BeautySalon");
					context.put("idFieldName", idProperty == null ? "id" : idProperty.getName());
					context.put("idFieldType", idProperty == null ? "Integer" : idProperty.getType());
					context.put("idFieldAccessor", capitalize(idProperty == null ? "id" : idProperty.getName()));
					// Ensure the template is available
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

	private PersistentProperty getIdProperty(FMClass cl) {
		if (cl == null || cl.getPersistentProperties() == null) {
			return null;
		}
		for (int i = 0; i < cl.getPersistentProperties().size(); i++) {
			PersistentProperty property = cl.getPersistentProperties().get(i);
			if (property.getIsId() || "id".equalsIgnoreCase(property.getName())) {
				return property;
			}
		}
		return cl.getPersistentProperties().isEmpty() ? null : cl.getPersistentProperties().get(0);
	}

	private String capitalize(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}

	@Override
	public Writer getWriter(String fileNamePart, String packageName) throws IOException {
		if (packageName != filePackage) {
			packageName.replace(".", File.separator);
			filePackage = packageName;
		}

		String generatedFileName = "";

		if(templateName.startsWith("repository")) {
			generatedFileName = fileNamePart + "Repository";
		}
		else if(templateName.startsWith("service")) {
			generatedFileName = fileNamePart + "Service";
		}
		else if(templateName.startsWith("controller")) {
			generatedFileName = fileNamePart + "Controller";
		}
		else if(templateName.startsWith("model")) {
			generatedFileName = fileNamePart;
		}
		else if(templateName.startsWith("application")) {
			generatedFileName = fileNamePart;
		}
		else if(templateName.startsWith("customservice")) {
			generatedFileName = "CustomGenericService";
		}

		String fullPath = outputPath
				+ File.separator
				+ (filePackage.isEmpty() ? "" : packageToPath(filePackage)
				+ File.separator)
				+ outputFileName.replace("{0}", generatedFileName);

		File of = new File(fullPath);
		if (!of.getParentFile().exists())
			if (!of.getParentFile().mkdirs()) {
				throw new IOException("An error occurred during output folder creation "
						+ outputPath);
			}

		if (!isOverwrite() && of.exists())
			return null;

		return new OutputStreamWriter(new FileOutputStream(of));

	}

	public void generateApplicationFile() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("BeautySalonApplication", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("package", "BeautySalon");
				getTemplate().process(context, out);
				out.flush();
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
