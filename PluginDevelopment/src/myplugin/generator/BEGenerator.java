package myplugin.generator;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMModel;
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

	public void generate() {

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
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
					context.put("properties", cl.getProperties());
					context.put("package", "BeautySalon");
					context.put("persistentProperties", cl.getPersistentProperties());
					context.put("referencedProperties", cl.getReferencedProperties());
					context.put("entity", cl.getEntity());
					context.put("app_name", "BeautySalon");
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
		else if(templateName.startsWith("modelMapperConfig")) {
			generatedFileName = fileNamePart;
		}
		else if(templateName.startsWith("frontendViewController")) {
			generatedFileName = fileNamePart;
		}
		else if(templateName.startsWith("viewController")) {
			generatedFileName = fileNamePart;
		}
		else if(templateName.startsWith("webconfig")) {
			generatedFileName = fileNamePart;
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

	public void generateConfigFile() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("ModelMapperConfig", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("package", "BeautySalon");
				if (getTemplate() != null) {
					getTemplate().process(context, out);
					out.flush();
				} else {
					throw new IOException("Template not loaded for ModelMapperConfig");
				}
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void generateFrontendViewController() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("FrontendViewController", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("package", "BeautySalon");
				context.put("app_name", "BeautySalon");
				context.put("classes", FMModel.getInstance().getClasses());
				if (getTemplate() != null) {
					getTemplate().process(context, out);
					out.flush();
				} else {
					throw new IOException("Template not loaded for FrontendViewController");
				}
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void generateWebConfig() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("WebConfig", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("package", "BeautySalon");
				if (getTemplate() != null) {
					getTemplate().process(context, out);
					out.flush();
				} else {
					throw new IOException("Template not loaded for WebConfig");
				}
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void generateViewController() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("ViewController", getFilePackage());
			if (out != null) {
				context.clear();
				context.put("package", "BeautySalon");
				context.put("app_name", "BeautySalon");
				context.put("classes", FMModel.getInstance().getClasses());
				if (getTemplate() != null) {
					getTemplate().process(context, out);
					out.flush();
				} else {
					throw new IOException("Template not loaded for ViewController");
				}
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void generateApplicationProperties() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			// Output path should be src/main/resources/application.properties
			String fullPath = outputPath + File.separator + "application.properties";
			
			File of = new File(fullPath);
			if (!of.getParentFile().exists())
				if (!of.getParentFile().mkdirs()) {
					throw new IOException("An error occurred during output folder creation: " + fullPath);
				}

			out = new OutputStreamWriter(new FileOutputStream(of));
			if (out != null) {
				context.clear();
				if (getTemplate() != null) {
					getTemplate().process(context, out);
					out.flush();
				} else {
					throw new IOException("Template not loaded for application.properties");
				}
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
