package myplugin.generator.fmmodel;


import java.util.ArrayList;
import java.util.Iterator;



public class FMEnumeration extends FMType {
	private ArrayList <String> Values = new ArrayList<String>();

	public FMEnumeration(String name, String typePackage) {
		super(name, typePackage);
	}

	public ArrayList<String> getValues() {
		return Values;
	}

	public void setValues(ArrayList<String> values) {
		Values = values;
	}

	public Iterator<String> getValueIterator(){
		return Values.iterator();
	}
	
	public void addValue(String value){
		Values.add(value);		
	}
	
	public int getValuesCount(){
		return Values.size();
	}

	public String getValueAt(int i){
		return Values.get(i);
	}
	
}
