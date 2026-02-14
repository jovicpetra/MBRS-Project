package myplugin.generator.frontend;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import myplugin.generator.BasicGenerator;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generator for JavaScript file.
 * Generates a single JS file with client-side functionality for JSP pages.
 */
public class JsGenerator extends BasicGenerator {

    public JsGenerator(GeneratorOptions generatorOptions) {
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
            out = getWriter("app", getFilePackage());
            if (out != null) {
                context.clear();
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

    @Override
    public Writer getWriter(String fileNamePart, String packageName) throws IOException {
        String fullPath = outputPath
                + File.separator
                + filePackage
                + File.separator
                + "resources"
                + File.separator
                + "js"
                + File.separator
                + fileNamePart + ".js";

        File of = new File(fullPath);
        if (!of.getParentFile().exists()) {
            if (!of.getParentFile().mkdirs()) {
                throw new IOException("An error occurred during output folder creation "
                        + outputPath);
            }
        }

        System.out.println("Generating JS: " + of.getPath());

        if (!isOverwrite() && of.exists()) {
            return null;
        }

        return new OutputStreamWriter(new FileOutputStream(of), "UTF-8");
    }
}
