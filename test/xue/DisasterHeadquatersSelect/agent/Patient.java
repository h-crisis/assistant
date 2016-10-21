package DisasterHeadquatersSelect.agent;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author chang
 *
 */

public class Patient {

	private int ID; //start from 0
	private String name;
	private String coordination;
	private String situation; //conversion might be needed. Start from 1
	private HashSet<Integer> hospitallist; //hospital ID can go
	
	public Patient(int i, String n, String co, String s)
	{
		ID = i;
		name = n;
		coordination = co;
		situation = s;
		hospitallist = new HashSet();
				
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
	
	public void setSituation(String s)
	{
		situation = s;
	}
	public String getSituation(){
		return situation;
	}
	public void setHospitalList(HashSet<Integer> a){
		
		Iterator<Integer> it = a.iterator();
		while(it.hasNext())
			hospitallist.add(it.next());
	}
	public HashSet<Integer> getHospitalList()
	{
		return hospitallist;
	}

}
