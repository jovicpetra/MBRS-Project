package myplugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JOptionPane;

import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;


import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;

/** MagicDraw plugin that performes code generation */
public class MyPlugin extends com.nomagic.magicdraw.plugins.Plugin {
	
	String pluginDir = null; 
	
	public void init() {

		pluginDir = getDescriptor().getPluginDirectory().getPath();
		
		// Creating submenu in the MagicDraw main menu 	
		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();		
		manager.addMainMenuConfigurator(new MainMenuConfigurator(getSubmenuActions()));
		
		/** @Todo: load project options (@see myplugin.generator.options.ProjectOptions) from 
		 * ProjectOptions.xml and take ejb generator options */
		
		//for test purpose only:
		String outputPath = getOutputPath();

		// Model
		GeneratorOptions modelOptions = new GeneratorOptions(outputPath, "model", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/models");
		modelOptions.setTemplateDir(pluginDir + File.separator + modelOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelGenerator", modelOptions);

		// ModelDTO
		GeneratorOptions modelDTOOptions = new GeneratorOptions(outputPath, "modelDTO", "resources/templates", "{0}DTO.java", true, "src/main/java/BeautySalon/models/dto");
		modelDTOOptions.setTemplateDir(pluginDir + File.separator + modelDTOOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelDTOGenerator", modelDTOOptions);

		// Enums
		GeneratorOptions enumOptions = new GeneratorOptions(outputPath, "enum", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/enums");
		enumOptions.setTemplateDir(pluginDir + File.separator + enumOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EnumGenerator", enumOptions);

		// Spring Repositories
		GeneratorOptions repositoryOptions = new GeneratorOptions(outputPath, "repository", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/repositories");
		repositoryOptions.setTemplateDir(pluginDir + File.separator + repositoryOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("RepositoryGenerator", repositoryOptions);

		// Application file
		GeneratorOptions ApplicationFileOptions = new GeneratorOptions(outputPath, "application", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon");
		ApplicationFileOptions.setTemplateDir(pluginDir + File.separator + ApplicationFileOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ApplicationFileGenerator", ApplicationFileOptions);

		//Controller
		GeneratorOptions controllerOptions = new GeneratorOptions(outputPath, "controller", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/controllers");
		controllerOptions.setTemplateDir(pluginDir + File.separator + controllerOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ControllerGenerator", controllerOptions);

		//Service
		GeneratorOptions serviceOptions = new GeneratorOptions(outputPath, "service", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/services");
		serviceOptions.setTemplateDir(pluginDir + File.separator + serviceOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ServiceGenerator", serviceOptions);

		GeneratorOptions customServiceOptions = new GeneratorOptions(outputPath, "customservice", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/services");
		customServiceOptions.setTemplateDir(pluginDir + File.separator + customServiceOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("CustomServiceGenerator", customServiceOptions);

		// ModelMapper Config
		GeneratorOptions modelMapperConfigOptions = new GeneratorOptions(outputPath, "modelMapperConfig", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/config");
		modelMapperConfigOptions.setTemplateDir(pluginDir + File.separator + modelMapperConfigOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelMapperConfigGenerator", modelMapperConfigOptions);

		// Frontend generators
		// JSP Form
		GeneratorOptions jspFormOptions = new GeneratorOptions(outputPath, "jspForm", "resources/templates/frontend", "{0}.jsp", true, "src/main/webapp");
		jspFormOptions.setTemplateDir(pluginDir + File.separator + jspFormOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("JspFormGenerator", jspFormOptions);

		// JSP List
		GeneratorOptions jspListOptions = new GeneratorOptions(outputPath, "jspList", "resources/templates/frontend", "{0}.jsp", true, "src/main/webapp");
		jspListOptions.setTemplateDir(pluginDir + File.separator + jspListOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("JspListGenerator", jspListOptions);

		// JSP Home
		GeneratorOptions jspHomeOptions = new GeneratorOptions(outputPath, "jspHome", "resources/templates/frontend", "home.jsp", true, "src/main/webapp");
		jspHomeOptions.setTemplateDir(pluginDir + File.separator + jspHomeOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("JspHomeGenerator", jspHomeOptions);

		// CSS
		GeneratorOptions cssOptions = new GeneratorOptions(outputPath, "jspCss", "resources/templates/frontend", "style.css", true, "src/main/webapp");
		cssOptions.setTemplateDir(pluginDir + File.separator + cssOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("CssGenerator", cssOptions);

		// JavaScript
		GeneratorOptions jsOptions = new GeneratorOptions(outputPath, "jspJs", "resources/templates/frontend", "app.js", true, "src/main/webapp");
		jsOptions.setTemplateDir(pluginDir + File.separator + jsOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("JsGenerator", jsOptions);

		// Frontend View Controller (for JSP pages)
		GeneratorOptions viewControllerOptions = new GeneratorOptions(outputPath, "frontendViewController", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/controllers");
		viewControllerOptions.setTemplateDir(pluginDir + File.separator + viewControllerOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("FrontendViewControllerGenerator", viewControllerOptions);

		// WebConfig
		GeneratorOptions webConfigOptions = new GeneratorOptions(outputPath, "webconfig", "resources/templates", "{0}.java", true, "src/main/java/BeautySalon/config");
		webConfigOptions.setTemplateDir(pluginDir + File.separator + webConfigOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("WebConfigGenerator", webConfigOptions);

		// Application Properties
		GeneratorOptions applicationPropertiesOptions = new GeneratorOptions(outputPath, "applicationProperties", "resources/templates", "application.properties", true, "src/main/resources");
		applicationPropertiesOptions.setTemplateDir(pluginDir + File.separator + applicationPropertiesOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ApplicationPropertiesGenerator", applicationPropertiesOptions);

	}

	private NMAction[] getSubmenuActions()
	{
	   return new NMAction[]{
			new GenerateAction("Generate"),
	   };
	}
	
	public boolean close() {
		return true;
	}
	
	public boolean isSupported() {				
		return true;
	}

	private String getOutputPath() {
		String returnVal = "";
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = Files.newInputStream(Paths.get("resources/ProjectOptions.xml"));
			// load a properties file
			prop.load(input);
			// get the property value
			returnVal = prop.getProperty("OUTPUT_PATH");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return returnVal;
	}
}


