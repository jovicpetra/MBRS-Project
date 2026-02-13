package myplugin.generator.frontend;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import myplugin.generator.BasicGenerator;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generator for the home page JSP.
 * Generates a single home page that lists all available entities with navigation links.
 * This page serves as the entry point for the web application.
 */
public class JspHomePageGenerator extends BasicGenerator {

    public JspHomePageGenerator(GeneratorOptions generatorOptions) {
        super(generatorOptions);
    }

    public void generate() {
        try {
            super.generate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        Writer out;
        Map<String, Object> context = new HashMap<String, Object>();
        try {
            out = getWriter("index", getFilePackage());
            if (out != null) {
                List<FMClass> classes = FMModel.getInstance().getClasses();
                
                context.clear();
                context.put("classes", classes);
                context.put("app_name", generatorOptions.getProjectOptions().getProjectName());
                context.put("currentYear", java.time.Year.now().getValue());
                
                getTemplate().process(context, out);
                out.flush();
                out.close();
            }
        } catch (TemplateException e) {
            JOptionPane.showMessageDialog(null, "Template error: " + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IO error: " + e.getMessage());
        }
    }

    @Override
    public Writer getWriter(String fileNamePart, String packageName) throws IOException {
        String fullPath = outputPath
                + File.separator
                + filePackage
                + File.separator
                + "home.jsp";

        File of = new File(fullPath);
        if (!of.getParentFile().exists()) {
            if (!of.getParentFile().mkdirs()) {
                throw new IOException("An error occurred during output folder creation "
                        + outputPath);
            }
        }

        System.out.println("Generating home page: " + of.getPath());

        if (!isOverwrite() && of.exists()) {
            return null;
        }

        return new OutputStreamWriter(new FileOutputStream(of), "UTF-8");
    }
}
