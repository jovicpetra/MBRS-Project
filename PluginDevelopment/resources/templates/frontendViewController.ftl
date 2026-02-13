package ${package}.controllers;

import ${package}.models.*;
import ${package}.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class FrontendViewController {

<#list classes as class>
    @Autowired
    private ${class.name}Service ${class.name?uncap_first}Service;

</#list>

    // Home Page
    @GetMapping({"", "/", "home.jsp"})
    public String home(Model model) {
        return "home";
    }

<#list classes as class>
    // =============== ${class.name?upper_case} VIEWS ===============
    @GetMapping("${class.name?uncap_first}List.jsp")
    public String ${class.name?uncap_first}List(Model model) {
        try {
            List<${class.name}> ${class.name?uncap_first}List = ${class.name?uncap_first}Service.findAll();
            model.addAttribute("${class.name?uncap_first}List", ${class.name?uncap_first}List != null ? ${class.name?uncap_first}List : new ArrayList<>());
        } catch (Exception e) {
            model.addAttribute("${class.name?uncap_first}List", new ArrayList<>());
            model.addAttribute("error", "Error loading ${class.name?uncap_first} list: " + e.getMessage());
        }
        return "${class.name?uncap_first}List";
    }

    @GetMapping("${class.name?uncap_first}Form.jsp")
    public String ${class.name?uncap_first}Form(@RequestParam(required = false) Integer id, Model model) {
        try {
            if (id != null) {
                ${class.name} ${class.name?uncap_first} = ${class.name?uncap_first}Service.findById(id);
                if (${class.name?uncap_first} != null) {
                    model.addAttribute("${class.name?uncap_first}", ${class.name?uncap_first});
                } else {
                    model.addAttribute("${class.name?uncap_first}", new ${class.name}());
                    model.addAttribute("error", "${class.name} not found");
                }
            } else {
                model.addAttribute("${class.name?uncap_first}", new ${class.name}());
            }
<#if class.referencedProperties?has_content>
            populate${class.name}References(model);
</#if>
        } catch (Exception e) {
            model.addAttribute("${class.name?uncap_first}", new ${class.name}());
            model.addAttribute("error", "Error loading ${class.name?uncap_first} form: " + e.getMessage());
<#if class.referencedProperties?has_content>
            populate${class.name}References(model);
</#if>
        }
        return "${class.name?uncap_first}Form";
    }

    @PostMapping("${class.name?uncap_first}Save")
    public String ${class.name?uncap_first}Save(@ModelAttribute ${class.name} ${class.name?uncap_first}, Model model) {
        try {
            ${class.name?uncap_first}Service.save(${class.name?uncap_first});
            return "redirect:/${class.name?uncap_first}List.jsp?success=true";
        } catch (Exception e) {
            model.addAttribute("${class.name?uncap_first}", ${class.name?uncap_first});
            model.addAttribute("error", "Error saving ${class.name?uncap_first}: " + e.getMessage());
<#if class.referencedProperties?has_content>
            populate${class.name}References(model);
</#if>
            return "${class.name?uncap_first}Form";
        }
    }

    @GetMapping("${class.name?uncap_first}Delete/{id}")
    public String ${class.name?uncap_first}Delete(@PathVariable Integer id, Model model) {
        try {
            ${class.name?uncap_first}Service.delete(id);
            return "redirect:/${class.name?uncap_first}List.jsp?deleted=true";
        } catch (Exception e) {
            return "redirect:/${class.name?uncap_first}List.jsp?error=" + e.getMessage();
        }
    }

<#if class.referencedProperties?has_content>
    private void populate${class.name}References(Model model) {
        try {
<#list class.referencedProperties as refProp>
            List<${refProp.type}> ${refProp.type?uncap_first}List = ${refProp.type?uncap_first}Service.findAll();
            model.addAttribute("${refProp.type?uncap_first}List", ${refProp.type?uncap_first}List != null ? ${refProp.type?uncap_first}List : new ArrayList<>());
</#list>
        } catch (Exception e) {
<#list class.referencedProperties as refProp>
            model.addAttribute("${refProp.type?uncap_first}List", new ArrayList<>());
</#list>
        }
    }

</#if>
</#list>
}
