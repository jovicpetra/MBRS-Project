package myplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FMClass extends FMType {	
	
	private String visibility;
	

	//Class properties
	private List<FMProperty> FMProperties = new ArrayList<FMProperty>();
	
	//list of packages (for import declarations) 
	private List<String> importedPackages = new ArrayList<String>();

	//Entity
	private Entity entity = null;

	private List<PersistentProperty> persistentProperties = new ArrayList<PersistentProperty>();
	private List<ReferencedProperty> referencedProperties = new ArrayList<ReferencedProperty>();

	private List<FMMethod> methods = new ArrayList<FMMethod>();
	
	
	public FMClass(String name, String classPackage, String visibility) {
		super(name, classPackage);		
		this.visibility = visibility;
	}

	public FMClass() {
		super();
	}

	public List<FMProperty> getProperties(){
		return FMProperties;
	}
	
	public Iterator<FMProperty> getPropertyIterator(){
		return FMProperties.iterator();
	}
	
	public void addProperty(FMProperty property){
		FMProperties.add(property);		
	}
	
	public int getPropertyCount(){
		return FMProperties.size();
	}
	
	public List<String> getImportedPackages(){
		return importedPackages;
	}

	public Iterator<String> getImportedIterator(){
		return importedPackages.iterator();
	}
	
	public void addImportedPackage(String importedPackage){
		importedPackages.add(importedPackage);		
	}
	
	public int getImportedCount(){
		return FMProperties.size();
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}


    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<PersistentProperty> getPersistentProperties() {
        return persistentProperties;
    }

    public void setPersistentProperties(List<PersistentProperty> persistentProperties) {
        this.persistentProperties = persistentProperties;
    }

	public void addPersistentProperty(PersistentProperty property){
		persistentProperties.add(property);
	}

	public List<ReferencedProperty> getReferencedProperties() {
        return referencedProperties;
    }

    public void setReferencedProperties(List<ReferencedProperty> referencedProperties) {
        this.referencedProperties = referencedProperties;
    }


	public List<FMMethod> getMethods() {
		return methods;
	}

	public void addReferencedProperty(ReferencedProperty property){
		referencedProperties.add(property);
	}

}
