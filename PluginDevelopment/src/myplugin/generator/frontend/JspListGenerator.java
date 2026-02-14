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
 * Generator for JSP list/table pages.
 * Generates JSP pages that display entity lists with search and CRUD action links.
 * Uses JSTL for iteration and conditional rendering.
 */
public class JspListGenerator extends BasicGenerator {

    public JspListGenerator(GeneratorOptions generatorOptions) {
        super(generatorOptions);
    }

    public void generate() {
        try {
            super.generate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        List<FMClass> classes = FMModel.getInstance().getClasses();
        for (FMClass cl : classes) {
            Writer out;
            Map<String, Object> context = new HashMap<String, Object>();
            try {
                out = getWriter(cl.getName(), getFilePackage());
                if (out != null) {
                    context.clear();
                    context.put("class", cl);
                    context.put("entityName", cl.getName());
                    context.put("entityNameLower", cl.getName().toLowerCase());
                    context.put("properties", cl.getProperties());
                    context.put("persistentProperties", cl.getPersistentProperties());
                    context.put("referencedProperties", cl.getReferencedProperties());
                    context.put("app_name", generatorOptions.getProjectOptions().getProjectName());
                    
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
    }

    @Override
    public Writer getWriter(String fileNamePart, String packageName) throws IOException {
        String generatedFileName = fileNamePart.toLowerCase() + "List";
        
        String fullPath = outputPath
                + File.separator
                + filePackage
                + File.separator
                + generatedFileName + ".jsp";

        File of = new File(fullPath);
        if (!of.getParentFile().exists()) {
            if (!of.getParentFile().mkdirs()) {
                throw new IOException("An error occurred during output folder creation "
                        + outputPath);
            }
        }

        System.out.println("Generating JSP list: " + of.getPath());

        if (!isOverwrite() && of.exists()) {
            return null;
        }

        return new OutputStreamWriter(new FileOutputStream(of), "UTF-8");
    }
}
