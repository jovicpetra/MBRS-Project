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

import freemarker.template.Configuration;

import myplugin.analyzer.AnalyzeException;
import myplugin.analyzer.ModelAnalyzer;
import myplugin.generator.AngularGenerator;
import myplugin.generator.AngularTestGenerator;
import myplugin.generator.BEGenerator;
import myplugin.generator.BETestGenerator;
import myplugin.generator.EnumGenerator;
import myplugin.generator.fmmodel.FMClass;
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
			Configuration freeMarkerConfiguration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

			GeneratorOptions go = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ModelGenerator");
			BEGenerator generator = new BEGenerator(go, freeMarkerConfiguration);
			generator.generate();

			GeneratorOptions go2 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("EnumGenerator");
			EnumGenerator enumGenerator = new EnumGenerator(go2, freeMarkerConfiguration);
			enumGenerator.generate();

			GeneratorOptions go3 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("RepositoryGenerator");
			BEGenerator repoGenerator = new BEGenerator(go3, freeMarkerConfiguration);
			repoGenerator.generate();

			GeneratorOptions go4 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ApplicationFileGenerator");
			BEGenerator springApplicationFileGenerator = new BEGenerator(go4, freeMarkerConfiguration);
			springApplicationFileGenerator.generateApplicationFile();

			GeneratorOptions go5 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ControllerGenerator");
			BEGenerator controllerGenerator = new BEGenerator(go5, freeMarkerConfiguration);
			controllerGenerator.generate();

			GeneratorOptions go6 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ServiceGenerator");
			BEGenerator serviceGenerator = new BEGenerator(go6, freeMarkerConfiguration);
			serviceGenerator.generate();

			GeneratorOptions go7 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("CustomServiceGenerator");
			BEGenerator customServiceGenerator = new BEGenerator(go7, freeMarkerConfiguration);
			customServiceGenerator.generate();

			GeneratorOptions go8 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ModelDTOGenerator");
			BEGenerator modelDTOGenerator = new BEGenerator(go8, freeMarkerConfiguration);
			modelDTOGenerator.generate();

			GeneratorOptions go9 = ProjectOptions.getProjectOptions().getGeneratorOptions().get("ModelMapperGenerator");
			BEGenerator modelMapperGenerator = new BEGenerator(go9, freeMarkerConfiguration);
			modelMapperGenerator.generate();

			// Frontend generation

			GeneratorOptions goAngularTable = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularEntityTableGenerator");
			AngularGenerator angularTableGenerator = new AngularGenerator(goAngularTable, freeMarkerConfiguration);
			angularTableGenerator.generate();

			GeneratorOptions goAngularAdd = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularAddEntityGenerator");
			AngularGenerator angularAddGenerator = new AngularGenerator(goAngularAdd, freeMarkerConfiguration);
			angularAddGenerator.generate();

			GeneratorOptions goAngularView = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularViewEntityGenerator");
			AngularGenerator angularViewGenerator = new AngularGenerator(goAngularView, freeMarkerConfiguration);
			angularViewGenerator.generate();

			// index page
			GeneratorOptions goAngularIndex = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularIndexPageGenerator");
			AngularGenerator angularIndexGenerator = new AngularGenerator(goAngularIndex, freeMarkerConfiguration);
			angularIndexGenerator.generateIndexHtml();

			// app module
			GeneratorOptions goAngularApp = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularAppGenerator");
			AngularGenerator angularAppGenerator = new AngularGenerator(goAngularApp, freeMarkerConfiguration);
			angularAppGenerator.generateJSScript();

			// controllers
			GeneratorOptions goAngularCtrl = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularControllersGenerator");
			AngularGenerator angularCtrlGenerator = new AngularGenerator(goAngularCtrl, freeMarkerConfiguration);
			angularCtrlGenerator.generateJSScript();

			// services
			GeneratorOptions goAngularSvc = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularServicesGenerator");
			AngularGenerator angularSvcGenerator = new AngularGenerator(goAngularSvc, freeMarkerConfiguration);
			angularSvcGenerator.generateJSScript();

			// routes
			GeneratorOptions goAngularRoutes = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularRoutesGenerator");
			AngularGenerator angularRoutesGenerator = new AngularGenerator(goAngularRoutes, freeMarkerConfiguration);
			angularRoutesGenerator.generateJSScript();

			// landing page
			GeneratorOptions goAngularLanding = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularLandingPageGenerator");
			AngularGenerator angularLandingGenerator = new AngularGenerator(goAngularLanding, freeMarkerConfiguration);
			angularLandingGenerator.generateIndexHtml();

			// client my-appointments page
			GeneratorOptions goAngularMyAppointments = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularMyAppointmentsGenerator");
			AngularGenerator angularMyAppointmentsGenerator = new AngularGenerator(goAngularMyAppointments, freeMarkerConfiguration);
			angularMyAppointmentsGenerator.generateIndexHtml();

			// admin treatment management table
			GeneratorOptions goAngularTreatmentAdmin = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularTreatmentAdminGenerator");
			AngularGenerator angularTreatmentAdminGenerator = new AngularGenerator(goAngularTreatmentAdmin, freeMarkerConfiguration);
			angularTreatmentAdminGenerator.generateIndexHtml();

			// client treatment detail page
			GeneratorOptions goAngularTreatmentDetail = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularTreatmentDetailGenerator");
			AngularGenerator angularTreatmentDetailGenerator = new AngularGenerator(goAngularTreatmentDetail, freeMarkerConfiguration);
			angularTreatmentDetailGenerator.generateIndexHtml();

			// Backend and frontend test generation
			java.util.List<FMClass> entities = FMModel.getInstance().getClasses();
			GeneratorOptions goBackendTests = ProjectOptions.getProjectOptions().getGeneratorOptions().get("BETestGenerator");
			BETestGenerator beTestGenerator = new BETestGenerator(goBackendTests, freeMarkerConfiguration);
			beTestGenerator.generateAllTests(entities, go.getOutputPath() + "/src/test/java");

			java.util.List<FMClass> uiClasses = FMModel.getInstance().getClasses();
			GeneratorOptions goAngularTests = ProjectOptions.getProjectOptions().getGeneratorOptions().get("AngularTestGenerator");
			AngularTestGenerator angularTestGenerator = new AngularTestGenerator(goAngularTests, freeMarkerConfiguration);
			angularTestGenerator.generateAllTests(uiClasses, goAngularApp.getOutputPath() + "/src/test/js");

			// ──────────────────────────────────────────────────────────────────

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