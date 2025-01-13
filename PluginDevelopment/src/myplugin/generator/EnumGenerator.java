package myplugin.generator;

import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;

import javax.swing.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumGenerator extends BasicGenerator {

    public EnumGenerator(GeneratorOptions generatorOptions) {
        super(generatorOptions);
    }

    public void generate() {
        try {
            super.generate();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        List<FMEnumeration> enums = FMModel.getInstance().getEnumerations();
        for (int i = 0; i < enums.size(); i++) {
            FMEnumeration e1 = enums.get(i);
            Writer out;
            Map<String, Object> context = new HashMap<String, Object>();
            try {
                out = getWriter(e1.getName(), getFilePackage());
                if (out != null) {
                    context.clear();
                    context.put("enum", e1);
                    context.put("app_name", "BeautySalon");
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
}
