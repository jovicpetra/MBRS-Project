package myplugin;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import myplugin.analyzer.AnalyzeException;
import myplugin.analyzer.ModelAnalyzer;
import myplugin.generator.BEGenerator;
import myplugin.generator.EnumGenerator;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;

/** Action that activate code generation */
@SuppressWarnings("serial")
class GenerateAction extends MDAction{
	
	
	public GenerateAction(String name) {			
		super("", name, null, null);		
	}

	public void actionPerformed(ActionEvent evt) {
		
		if (Application.getInstance().getProject() == null) return;
		Package root = Application.getInstance().getProject().getModel();
		
		if (root == null) return;
	
		ModelAnalyzer analyzer = new ModelAnalyzer(root, "src/main/java/BeautySalon");
		
		try {
			analyzer.prepareModel();	
			GeneratorOptions go = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ModelGenerator");
			BEGenerator generator = new BEGenerator(go);
			generator.generate();

			GeneratorOptions go2 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("EnumGenerator");
			EnumGenerator enumGenerator = new EnumGenerator(go2);
			enumGenerator.generate();

			GeneratorOptions go3 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("RepositoryGenerator");
			BEGenerator repoGenerator = new BEGenerator(go3);
			repoGenerator.generate();

			GeneratorOptions go4 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ApplicationFileGenerator");
			BEGenerator springApplicationFileGenerator = new BEGenerator(go4);
			springApplicationFileGenerator.generateApplicationFile();

			GeneratorOptions go5 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ControllerGenerator");
			BEGenerator controllerGenerator = new BEGenerator(go5);
			controllerGenerator.generate();

			GeneratorOptions go6 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ServiceGenerator");
			BEGenerator serviceGenerator = new BEGenerator(go6);
			serviceGenerator.generate();

			GeneratorOptions go7 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("CustomServiceGenerator");
			BEGenerator customServiceGenerator = new BEGenerator(go7);
			customServiceGenerator.generate();

			GeneratorOptions go8 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ModelDTOGenerator");
			BEGenerator modelDTOGenerator = new BEGenerator(go8);
			modelDTOGenerator.generate();

			/**  @ToDo: Also call other generators */ 
			JOptionPane.showMessageDialog(null, "Code is successfully generated! Generated code is in folder: " + go.getOutputPath() +
					                         ", package: " + go.getFilePackage());
			exportToXml();
		} catch (AnalyzeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} 			
	}
	
	private void exportToXml() {
		if (JOptionPane.showConfirmDialog(null, "Do you want to save FM Model?") == 
			JOptionPane.OK_OPTION)
		{	
			JFileChooser jfc = new JFileChooser();
			if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				String fileName = jfc.getSelectedFile().getAbsolutePath();
			
				XStream xstream = new XStream(new DomDriver());
				BufferedWriter out;		
				try {
					out = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(fileName), "UTF8"));					
					xstream.toXML(FMModel.getInstance().getClasses(), out);
					xstream.toXML(FMModel.getInstance().getEnumerations(), out);
					
				} catch (UnsupportedEncodingException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());				
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());				
				}		             
			}
		}	
	}	  

}