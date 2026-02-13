package myplugin.generator.frontend;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import myplugin.generator.BasicGenerator;
import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.fmmodel.FMProperty;
import myplugin.generator.fmmodel.ReferencedProperty;
import myplugin.generator.options.GeneratorOptions;

/**
 * Generator for JSP form pages (create/edit forms).
 * Generates JSP pages with Spring MVC form tags and JSTL support.
 * The generated forms dynamically handle different property types (boolean, date, enum, entity references).
 */
public class JspFormGenerator extends BasicGenerator {

    public JspFormGenerator(GeneratorOptions generatorOptions) {
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
                    context.put("pageTitle", cl.getName() + " Form");
                    context.put("properties", cl.getProperties());
                    context.put("persistentProperties", cl.getPersistentProperties());
                    context.put("referencedProperties", cl.getReferencedProperties());
                    context.put("enums", extractEnumProperties(cl));
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

    /**
     * Extract enum properties from the class to provide enum values in the form.
     */
    private List<Map<String, Object>> extractEnumProperties(FMClass cl) {
        List<Map<String, Object>> enums = new ArrayList<>();
        
        List<?> enumerationsList = FMModel.getInstance().getEnumerations();
        if (enumerationsList == null || enumerationsList.isEmpty()) {
            return enums;
        }
        
        // Build a map of enumeration names for faster lookup
        Map<String, FMEnumeration> enumMap = new HashMap<>();
        for (Object enumObj : enumerationsList) {
            if (enumObj instanceof FMEnumeration) {
                FMEnumeration enumeration = (FMEnumeration) enumObj;
                enumMap.put(enumeration.getName(), enumeration);
            }
        }
        
        // Check properties against enumerations
        for (FMProperty prop : cl.getProperties()) {
            String typeString = prop.getType();
            if (typeString != null && enumMap.containsKey(typeString)) {
                FMEnumeration enumeration = enumMap.get(typeString);
                Map<String, Object> enumData = new HashMap<>();
                enumData.put("name", prop.getName());
                enumData.put("type", typeString);
                enumData.put("literals", enumeration.getValues());
                enums.add(enumData);
            }
        }
        return enums;
    }

    @Override
    public Writer getWriter(String fileNamePart, String packageName) throws IOException {
        String generatedFileName = fileNamePart.toLowerCase() + "Form";
        
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

        System.out.println("Generating JSP form: " + of.getPath());

        if (!isOverwrite() && of.exists()) {
            return null;
        }

        return new OutputStreamWriter(new FileOutputStream(of), "UTF-8");
    }
}
