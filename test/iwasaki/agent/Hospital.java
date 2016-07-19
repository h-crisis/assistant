package iwasaki.agent;

import java.util.HashMap;
import java.util.HashSet;

public class Hospital {

	private int ID; //start from 0
	private String name;
	private String coordination;
	private HashMap<String, Double> abilityset = new HashMap<String, Double>(); //key: ability;Value: corresponding capacity; conversion might be needed, start from 1
	
	public Hospital(int i, String n, String co, HashMap<String, Double> s){
		ID = i;
		name = n;
		coordination = co;
		
		abilityset.putAll(s);;
	}
	public void setID(int i){
		ID = i;
	}
	public int getID()
	{
		return ID;
	}
	
	public void setName(String n){
		name = n;
	}
	public String getName()
	{
		return name;
	}
	public void setCoordination(String n){
		coordination = n;
	}
	public String getCoordination()
	{
		return coordination;
	}
	public void setAbility(HashMap<String, Double> a){
		abilityset.putAll(a);
	}
	public HashMap<String, Double> getAbility()
	{
		return abilityset;
	}
	public double getCapacity(String a)
	{
		double c = 0;
		if(abilityset.containsKey(a)){
			c = abilityset.get(a);
			return c;
		}
		else
			return 0;
	}
}
