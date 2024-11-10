package myplugin.analyzer;

import java.util.Iterator;
import java.util.List;

import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import myplugin.generator.fmmodel.*;

import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;


/** Model Analyzer takes necessary metadata from the MagicDraw model and puts it in
 * the intermediate data structure (@see myplugin.generator.fmmodel.FMModel) optimized
 * for code generation using freemarker. Model Analyzer now takes metadata only for ejb code
 * generation

 * @ToDo: Enhance (or completely rewrite) myplugin.generator.fmmodel classes and
 * Model Analyzer methods in order to support GUI generation. */


public class ModelAnalyzer {
	//root model package
	private Package root;

	//java root package for generated code
	private String filePackage;

	public ModelAnalyzer(Package root, String filePackage) {
		super();
		this.root = root;
		this.filePackage = filePackage;
	}

	public Package getRoot() {
		return root;
	}

	public void prepareModel() throws AnalyzeException {
		FMModel.getInstance().getClasses().clear();
		FMModel.getInstance().getEnumerations().clear();
		processPackage(root, filePackage);
	}

	private void processPackage(Package pack, String packageOwner) throws AnalyzeException {
		//Recursive procedure that extracts data from package elements and stores it in the
		// intermediate data structure

		if (pack.getName() == null) throw
			new AnalyzeException("Packages must have names!");

		String packageName = packageOwner;
		if (pack != root) {
			packageName += "." + pack.getName();
		}

		if (pack.hasOwnedElement()) {

			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Class) {
					Class cl = (Class)ownedElement;
					FMClass fmClass = getClassData(cl, packageName);
					FMModel.getInstance().getClasses().add(fmClass);
				}

				if (ownedElement instanceof Enumeration) {
					Enumeration en = (Enumeration)ownedElement;
					FMEnumeration fmEnumeration = getEnumerationData(en, packageName);
					FMModel.getInstance().getEnumerations().add(fmEnumeration);
				}
			}

			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Package) {
					Package ownedPackage = (Package)ownedElement;
					if (StereotypesHelper.getAppliedStereotypeByString(ownedPackage, "BusinessApp") != null)
						//only packages with stereotype BusinessApp are candidates for metadata extraction and code generation:
						processPackage(ownedPackage, packageName);
				}
			}

			/** @ToDo:
			  * Process other package elements, as needed */
		}
	}

	private FMClass getClassData(Class cl, String packageName) throws AnalyzeException {
		if (cl.getName() == null)
			throw new AnalyzeException("Classes must have names!");

		FMClass fmClass = new FMClass(cl.getName(), packageName, cl.getVisibility().toString());

		// Process properties
		Iterator<Property> it = ModelHelper.attributes(cl);
		while (it.hasNext()) {
			Property p = it.next();
			FMProperty prop = getPropertyData(p, cl);
			fmClass.addProperty(prop);

			// Additional processing for referenced and persistent properties
	//		ReferencedProperty referencedProperty = prop.getReferencedProperty();
	//		if (referencedProperty != null) {
	//			fmClass.addReferencedProperty(referencedProperty);
	//		}
			PersistentProperty persistentProperty = prop.getPersistentProperty();
			if (persistentProperty != null) {
				persistentProperty.setName(prop.getName());
				persistentProperty.setVisibility(prop.getVisibility());
				persistentProperty.setType(prop.getType());
				persistentProperty.setLower(prop.getLower());
				persistentProperty.setUpper(prop.getUpper());
				fmClass.addPersistentProperty(persistentProperty);
			}
		}

		// Process stereotypes (Entity)
		processClassStereotypes(cl, fmClass);

		return fmClass;
	}

	private void processClassStereotypes(Class cl, FMClass fmClass) throws AnalyzeException {

		// Handle Entity stereotype
		Stereotype entityStereotype = StereotypesHelper.getAppliedStereotypeByString(cl, "Entity");
		if (entityStereotype != null) {
			String tableName = extractStereotypeProperty(cl, entityStereotype, "tableName");
			Entity entity = new Entity(tableName);
			fmClass.setEntity(entity);  // Set entity details (table name)
		}
	}

	private String extractStereotypeProperty(Class cl, Stereotype stereotype, String propertyName) {
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals(propertyName)) {
				List value = StereotypesHelper.getStereotypePropertyValue(cl, stereotype, propertyName);
				if (value.size() > 0) {
					return (String) value.get(0);
				}
			}
		}
		return null;  // Return null if no value found
	}

	private FMProperty getPropertyData(Property p, Class cl) throws AnalyzeException {
		String attName = p.getName();
		if (attName == null)
			throw new AnalyzeException("Properties of the class: " + cl.getName() + " must have names!");
		Type attType = p.getType();
		if (attType == null)
			throw new AnalyzeException("Property " + cl.getName() + "." + p.getName() + " must have type!");

		String typeName = attType.getName();
		if (typeName == null)
			throw new AnalyzeException("Type of the property " + cl.getName() + "." + p.getName() + " must have name!");

		int lower = p.getLower();
		int upper = p.getUpper();

		FMProperty prop = new FMProperty(attName, typeName, p.getVisibility().toString(), lower, upper);

		// Process referenced properties
	//	processReferencedPropertyStereotypes(p, prop);

		// Process persistent properties (same as before)
		processPersistentPropertyStereotypes(p, prop);


		return prop;
	}

	private void processPersistentPropertyStereotypes(Property p, FMProperty prop) throws AnalyzeException {

		// Handle PersistentProperty stereotype
		Stereotype persistentPropStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "PersistentProperty");
		if (persistentPropStereotype != null) {
			String columnName = extractStereotypeProperty(p, persistentPropStereotype, "columnName");
			Integer length = extractIntegerStereotypeProperty(p, persistentPropStereotype, "length");
			Integer precision = extractIntegerStereotypeProperty(p, persistentPropStereotype, "precision");
			boolean  isId = extractBooleanStereotypeProperty(p, persistentPropStereotype, "isId");
			boolean unique = extractBooleanStereotypeProperty(p, persistentPropStereotype, "unique");
			boolean nullable = extractBooleanStereotypeProperty(p, persistentPropStereotype, "nullable");

			PersistentProperty persistentProperty = new PersistentProperty(columnName, length, precision, isId, unique, nullable);
			prop.setPersistentProperty(persistentProperty);  // Set persistent property
		}
	}

	private void processReferencedPropertyStereotypes(Property p, FMProperty prop) throws AnalyzeException {
		// Handle ReferencedProperty stereotype
		Stereotype referencedPropStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "ReferencedProperty");
		if (referencedPropStereotype != null) {
			FetchType fetch = extractFetchType(p, referencedPropStereotype);
			CascadeType cascade = extractCascadeType(p, referencedPropStereotype);
			String columnName = extractStereotypeProperty(p, referencedPropStereotype, "columnName");
			String joinTable = extractStereotypeProperty(p, referencedPropStereotype, "joinTable");

			ReferencedProperty referencedProperty = new ReferencedProperty(fetch, cascade, columnName, joinTable);
			prop.setReferencedProperty(referencedProperty);  // Set referenced property
		}
	}

	// Helper method to extract FetchType from stereotype (example, adjust as needed)
	private FetchType extractFetchType(Property p, Stereotype stereotype) {
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals("fetch")) {
				List value = StereotypesHelper.getStereotypePropertyValue(p, stereotype, "fetch");
				if (value.size() > 0) {
					String fetchStr = (String) value.get(0);
					return FetchType.valueOf(fetchStr);  // Assuming FetchType is an enum
				}
			}
		}
		return FetchType.LAZY;  // Default fetch strategy, adjust if needed
	}

	// Helper method to extract CascadeType from stereotype (example, adjust as needed)
	private CascadeType extractCascadeType(Property p, Stereotype stereotype) {
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals("cascade")) {
				List value = StereotypesHelper.getStereotypePropertyValue(p, stereotype, "cascade");
				if (value.size() > 0) {
					String cascadeStr = (String) value.get(0);
					return CascadeType.valueOf(cascadeStr);  // Assuming CascadeType is an enum
				}
			}
		}
		return CascadeType.ALL;  // Default cascade type, adjust as needed
	}

	// Helper method to extract String property (columnName, joinTable, etc.)
	private String extractStereotypeProperty(Property p, Stereotype stereotype, String propertyName) {
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals(propertyName)) {
				List value = StereotypesHelper.getStereotypePropertyValue(p, stereotype, propertyName);
				if (value.size() > 0) {
					return (String) value.get(0);
				}
			}
		}
		return null;  // Return null if no value found (could also be a default value)
	}


	// Helper method to extract Integer property (length or precision)
	private Integer extractIntegerStereotypeProperty(Property p, Stereotype stereotype, String propertyName) {
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals(propertyName)) {
				List value = StereotypesHelper.getStereotypePropertyValue(p, stereotype, propertyName);
				if (value.size() > 0) {
					return (Integer) value.get(0);
				}
			}
		}
		return null;  // Return null if no value found (could also be a default value)
	}

	private boolean extractBooleanStereotypeProperty(Property p, Stereotype stereotype, String propertyName) {
		// Get the list of attributes from the stereotype
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Property tagDef : tags) {
			if (tagDef.getName().equals(propertyName)) {  // If the property matches the given name
				// Get the list of values for the property in the stereotype
				List<?> value = StereotypesHelper.getStereotypePropertyValue(p, stereotype, propertyName);
				if (value != null && !value.isEmpty()) {
					// Check if the first value is a Boolean
					Object firstValue = value.get(0);
					if (firstValue instanceof Boolean) {
						return (Boolean) firstValue;  // Safely cast the value to Boolean
					}
					// If it's not a Boolean, you can either throw an exception or log a warning
					// For now, we return false as a fallback
					// You can replace this with logging or a custom exception
					System.out.println("Warning: Expected Boolean for property '" + propertyName + "' but got: " + firstValue);
				}
			}
		}
		return false;  // Return false if no value found or if the value is not a Boolean
	}


	private FMEnumeration getEnumerationData(Enumeration enumeration, String packageName) throws AnalyzeException {
		FMEnumeration fmEnum = new FMEnumeration(enumeration.getName(), packageName);
		List<EnumerationLiteral> list = enumeration.getOwnedLiteral();
		for (int i = 0; i < list.size() - 1; i++) {
			EnumerationLiteral literal = list.get(i);
			if (literal.getName() == null)  
				throw new AnalyzeException("Items of the enumeration " + enumeration.getName() +
				" must have names!");
			fmEnum.addValue(literal.getName());
		}
		return fmEnum;
	}	
	
	
}
