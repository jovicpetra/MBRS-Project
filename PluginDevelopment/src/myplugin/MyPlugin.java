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
		GeneratorOptions modelOptions = new GeneratorOptions(outputPath, "model", "templates", "{0}.java", true, "src/main/java/BeautySalon/models");
		modelOptions.setTemplateDir(pluginDir + File.separator + modelOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelGenerator", modelOptions);

		// ModelDTO
		GeneratorOptions modelDTOOptions = new GeneratorOptions(outputPath, "modelDTO", "templates", "{0}DTO.java", true, "src/main/java/BeautySalon/models/dto");
		modelDTOOptions.setTemplateDir(pluginDir + File.separator + modelDTOOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelDTOGenerator", modelDTOOptions);

		// Enums
		GeneratorOptions enumOptions = new GeneratorOptions(outputPath, "enum", "templates", "{0}.java", true, "src/main/java/BeautySalon/enums");
		enumOptions.setTemplateDir(pluginDir + File.separator + enumOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EnumGenerator", enumOptions);

		// Spring Repositories
		GeneratorOptions repositoryOptions = new GeneratorOptions(outputPath, "repository", "templates", "{0}.java", true, "src/main/java/BeautySalon/repositories");
		repositoryOptions.setTemplateDir(pluginDir + File.separator + repositoryOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("RepositoryGenerator", repositoryOptions);

		// Application file
		GeneratorOptions ApplicationFileOptions = new GeneratorOptions(outputPath, "application", "templates", "{0}.java", true, "src/main/java/BeautySalon");
		ApplicationFileOptions.setTemplateDir(pluginDir + File.separator + ApplicationFileOptions.getTemplateDir());
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ApplicationFileGenerator", ApplicationFileOptions);

		//Controller
		GeneratorOptions controllerOptions = new GeneratorOptions(outputPath, "controller", "templates", "{0}.java", true, "src/main/java/BeautySalon/controllers");
		controllerOptions.setTemplateDir(pluginDir + File.separator + controllerOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ControllerGenerator", controllerOptions);

		//Service
		GeneratorOptions serviceOptions = new GeneratorOptions(outputPath, "service", "templates", "{0}.java", true, "src/main/java/BeautySalon/services");
		serviceOptions.setTemplateDir(pluginDir + File.separator + serviceOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ServiceGenerator", serviceOptions);

		GeneratorOptions customServiceOptions = new GeneratorOptions(outputPath, "customservice", "templates", "{0}.java", true, "src/main/java/BeautySalon/services");
		customServiceOptions.setTemplateDir(pluginDir + File.separator + customServiceOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("CustomServiceGenerator", customServiceOptions);

		// ModelMapper
		GeneratorOptions modelMapperOptions = new GeneratorOptions(outputPath, "model_mapper", "templates", "ModelMapperConfig.java", true, "src/main/java/BeautySalon/config");
		modelMapperOptions.setTemplateDir(pluginDir + File.separator + modelMapperOptions.getTemplateDir()); //apsolutna putanja
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ModelMapperGenerator", modelMapperOptions);
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


