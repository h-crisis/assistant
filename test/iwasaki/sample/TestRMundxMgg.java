package iwasaki.sample;

import iwasaki.evaluator.CspEvaluator;
import iwasaki.ga.realcode.IRealNumberCoding;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TUndxMgg;
import iwasaki.ga.realcode.TVector;
import iwasaki.ga.util.TMyRandom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import iwasaki.agent.*;


//Developed by Shuang Chang to optimize resource allocation at post-disaster era while satisfying a set of constrains. 


//test resource mapping
public class TestRMundxMgg {

	public static int NO_OF_PARAMETERS = 0;

	public static final int POPULATION_SIZE = 500;

	public static final int NO_OF_CROSSOVERS = 100;
	
	public static Hospital[] hospitalList;
	public static Patient[] patientList;
	
	public static HashSet<Integer>[] abilityList = new HashSet[5]; //5: ability number; hospital set of each ability
	public static HashMap<String, Integer>[] situationList = new HashMap[10]; //10: number of locations; each location captures key:each situation; value:the number of patients 
	public static TRealNumberIndividual expected; 
	public static int[] patientNum; //total patient number of each location;
	public static LinkedList<String> coordinate = new LinkedList<String>(); //start latitude/longitude; end latitude/longitude
	public static double[] distance; //distance between patient location to hospital location
	public static HashMap<String, Integer> abilitymapping = new HashMap<String, Integer>(){{put("a",0); put("b",1); put("c",2); put("d",3); put("e",4);}};
	//fn1 for hospital specification; fn2 for patient specification
	
	public static int[][] record_location_ability; //record of patient location and their ability - length is the NO_OF_PARAMETERS
	public static int[] hospital;
	public static int[] patient;
	public static int[] totalnumber; //total number of patient of each situation
	public static int[] ability; //repeat of each situation of each location
	
	public static void initialize(){
		hospitalList = new Hospital[10];
		patientList = new Patient[500];
		patientNum = new int[10];
		
		for (int i = 0;i<5;i++)
		{
			abilityList[i] = new HashSet();
		}
		for (int i = 0;i<10;i++)
		{
			situationList[i] = new HashMap();
		}
	}
	public static void readInput(String fn1, String fn2, String fn3) throws Exception{
		
		FileInputStream fstream1 = new FileInputStream(fn1);
		DataInputStream in1 = null;
		in1 = new DataInputStream(fstream1);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
		
		FileInputStream fstream2 = new FileInputStream(fn2);
		DataInputStream in2 = null;
		in2 = new DataInputStream(fstream2);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
		
		FileInputStream fstream3 = new FileInputStream(fn3);
		DataInputStream in3 = null;
		in3 = new DataInputStream(fstream3);
		BufferedReader br3 = new BufferedReader(new InputStreamReader(in3));
		
		String strLine1 = null;
		String strLine2 = null;
		String strLine3 = null;
		double[][] distancematrix = new double[5][4];
		String[] str1 = new String[4];
		String[] str2 = new String[3];
		String[] str3 = new String[4];
		String[] HosAbility = new String[5];// 5: ability
		String[] HosAbilityCap = new String[5];
		String Location = null;
		String LocationTemp = null;
		//each hashset element contains the ability
		
//////////////////////////////////////////////////Read Location/Distance Information//////////////////////////////////////
		int j = 0;
		while((strLine3=br3.readLine())!=null){
			str3 = strLine3.split(",");

			//j:location of patient; i:location of hospital
			for(int i = 0; i<str3.length;i++){
				distancematrix[j][i] = Double.parseDouble(str3[i]);
				System.out.println(distancematrix[j][i]);
			}
			j++;
		}


		//////////////////////////////////////////////////Read Hospital Information//////////////////////////////////////
		int countHospital = 0;
		int countPatient =0;
		
		int temp = 0;
		int index;
		HashMap<String,Double> hosabilityset = new HashMap<String,Double>();
		
		while((strLine1=br1.readLine())!=null){
			str1 = strLine1.split(",");
			
			temp = str1[1].split(";").length; //number of ability of each hospital
			HosAbility = str1[1].split(";");
			HosAbilityCap = str1[3].split(";");
			
			
			for(int i = 0;i<temp;i++)
			{
				
				//ability;corresponding capacity
				hosabilityset.put(HosAbility[i], Double.parseDouble(HosAbilityCap[i]));
				
				//System.out.println(HosAbility[i] + "," +Double.parseDouble(HosAbilityCap[i]));
				index = abilitymapping.get(HosAbility[i]);
				abilityList[index].add(countHospital);//abilityList[0] for ability 1, and so on
				
			}
			hospitalList[countHospital] = new Hospital(countHospital, str1[0], str1[2],hosabilityset); //ID;Name;Coordination;AbilityCap
			hosabilityset.clear();
			
			//System.out.println(countHospital + " " + hospitalList[countHospital].getName());
			countHospital++;
		}
		

//////////////////////////////////////////////////Read Patient Information//////////////////////////////////////
		int t = 0;
		int temp2 = 0;
		int cc = 0;
		int cp = 0; //count number of patients of each location
		LocationTemp = "test";
		int test = 0;
		
		
		FileOutputStream fout_patient;
		fout_patient = new FileOutputStream("Record of patients"+".csv");
		PrintStream Outstream = new PrintStream(fout_patient); 
		
		
		while((strLine2=br2.readLine())!=null){
			str2 = strLine2.split(",");
			
			patientList[countPatient] = new Patient(countPatient, str2[0], str2[2], str2[1]);//ID;Name;Coordination;Situation
			index = abilitymapping.get(str2[1]);
			
			Location = str2[2];
			
			patientList[countPatient].setHospitalList(abilityList[index]);

			//count at location 0, the number of patients of each situation
			if((!Location.contains(LocationTemp) && cp!=0)){
				patientNum[cc] = cp;
				System.out.println("cc"+cc + "cp" + cp);
				Outstream.println("cc"+cc + "cp" + cp);
				cp = 0;
				
				cc++;
			}
				
			if(cc == 4 && countPatient == 499)
			{
				cp++;
				System.out.println("cc"+cc + "cp" + cp);
				Outstream.println("cc"+cc + "cp" + cp);
			}
			
			if(situationList[cc].containsKey(str2[1])){
				temp2 = situationList[cc].get(str2[1]) + 1;
				situationList[cc].put(str2[1], temp2);
				
			}
			else{
				situationList[cc].put(str2[1], 1);
				
			}

			countPatient++;
			LocationTemp = str2[2];
			cp++;
		}
		//handle the 0 patient of any situation case
		for(int i =0; i<cc; i++){
			if(!situationList[cc].containsKey("a"))
				situationList[cc].put("a", 0);
			if(!situationList[cc].containsKey("b"))
				situationList[cc].put("b", 0);
			if(!situationList[cc].containsKey("c"))
				situationList[cc].put("c", 0);
			if(!situationList[cc].containsKey("d"))
				situationList[cc].put("d", 0);
			if(!situationList[cc].containsKey("e"))
				situationList[cc].put("e", 0);
				
		}
		int c = 0;
		
		//5 locations of patients
		for(int i = 0; i<5;i++){
			Iterator it = situationList[i].keySet().iterator();
			
			while(it.hasNext()){
				String key = (String)it.next();
				index = abilitymapping.get(key);
				t += abilityList[index].size();
	
			}
			NO_OF_PARAMETERS = t;
		}
		
		totalnumber = new int[NO_OF_PARAMETERS];
		distance = new double[NO_OF_PARAMETERS];
		ability = new int[NO_OF_PARAMETERS];
		record_location_ability = new int[NO_OF_PARAMETERS][2];
		//5 locations
		
		for(int i = 0; i<5; i++){
			Iterator it2 = situationList[i].keySet().iterator();
		
			System.out.println("position" + i);
			Outstream.println("position" + i);
			while(it2.hasNext()){
				String key = (String)it2.next();
				index = abilitymapping.get(key);
				Iterator it3 = abilityList[index].iterator();
				for(j = 0; j<abilityList[index].size();j++)
				{
					totalnumber[c] = situationList[i].get(key);
					ability[c] = abilitymapping.get(key);
					System.out.println("ability" + ability[c]);
					System.out.println(key + situationList[i].get(key));
					Outstream.println(key + situationList[i].get(key));
					
					//make distance vector
					temp = (Integer) it3.next();
					distance[c] = distancematrix[i][temp];	
					record_location_ability[c][0] = i; //location
					record_location_ability[c][1] = index; //ability
					c++;
				}
				
	
			}
		}
		
		fout_patient.close();
		//System.out.println(t);
	}

	//make capacity(expected) vector
	public static void makeInputExpect() throws Exception{
		
		hospital = new int[NO_OF_PARAMETERS];
		//set expected vector
		
		int index = 0;
		int counter = 0;
	
		double hospitalcap = 0;
		int temp = 0;
		expected = new TRealNumberIndividual(NO_OF_PARAMETERS);
		TVector v = ((IRealNumberCoding)expected).getVector();
		String[] str = new String[2]; 
		
		//5 locations
		for(int i = 0;i<5;i++){
			Iterator it = situationList[i].keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				index = abilitymapping.get(key);
			
				Iterator it2 = abilityList[index].iterator();
				while(it2.hasNext())
				{
					temp = (Integer)it2.next();
					hospital[counter] = temp; 
				
					hospitalcap = hospitalList[temp].getCapacity(key);
					v.setData(counter, hospitalcap);
				
			//	System.out.println(temp + "," + key+ ","+ hospitalcap);
					counter++;
				//str = hospitalList[temp].getAbility().;
				}
	
			}
		}
	}
	
	//make coordination vector
	public static void makeInputCoordinate() throws Exception{

		int t = 0;
		int count = 0;
		int counttotal = 0;
		int j = 0;
		
		
		String s = patientList[0].getCoordination();
		
		for(int i = 0; i<hospital.length; i++)
		{
			t = hospital[i];
			coordinate.add(i, s + ";" + hospitalList[t].getCoordination());
			
			if(count == patientNum[j]){
				s = patientList[counttotal].getCoordination();
				count = 0;
			}
			count++;
			counttotal++;
		}

	}
		
	public static double evaluate(int iteration, String mode) throws Exception{
			
		TUndxMgg ga = new TUndxMgg(true, NO_OF_PARAMETERS, POPULATION_SIZE, NO_OF_CROSSOVERS);

		List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();

		double total = 450;
		
		double evaluation = 0; //evaluation value of the best individual
		FileOutputStream fout_evaluation;
		FileOutputStream fout_allpopulation;
		
		
		fout_evaluation = new FileOutputStream("Record of evaluation value_" + iteration + "_"+ mode+".csv");
		fout_allpopulation = new FileOutputStream("Record of evaluation of all population last iteration_" + iteration + "_" + mode+ ".csv");
		PrintStream Outstream2 = new PrintStream(fout_evaluation);
		PrintStream Outstream3 = new PrintStream(fout_allpopulation);
		
		CspEvaluator.setTotal(total);
		String value = null;
		//CspEvaluator.evaluatePopulation( initialPopulation, expected, coordinate, totalnumber, 1);
		CspEvaluator.evaluatePopulation( initialPopulation, expected, distance, totalnumber, 1, ability);
		for (int i = 0; i < 1; ++i) {
			List<TRealNumberIndividual> family = ga.selectParentsAndMakeKids();
			//CspEvaluator.evaluatePopulation( family, expected, coordinate, totalnumber, 1);
			CspEvaluator.evaluatePopulation( family, expected, distance, totalnumber, 1, ability);
			List<TRealNumberIndividual> nextPop = ga.doSelectionForSurvival();
			System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
			Outstream2.print(ga.getIteration() + "," + ga.getBestEvaluationValue() + "," +  ga.getAverageOfEvaluationValues() + "\n" );
			/*if(i >= 100){
				for(int j = 400;j<POPULATION_SIZE;j++)
					Outstream3.print(ga.getEvaluationValue(j) + "," + ga.getEvaluationValueTwo(j) + "\n");
				//value = ga.getIteration() + "," + ga.getBestEvaluationValue() + "," + ga.getBestEvaluationValueTwo() + "," + ga.getAverageOfEvaluationValues();
				//for(int j = 0;j<POPULATION_SIZE;j++)
				//	System.out.println(ga.getEvaluationValue(j));
			}*/
			}
		evaluation = ga.getBestEvaluationValue();
		System.out.println();
		System.out.println( "*** Best individual ***");
		//CspEvaluator.printIndividualDistance(ga.getBestIndividual(), totalnumber);

		double[] result = new double[NO_OF_PARAMETERS];
		result = CspEvaluator.IndividualDistanceOutput(ga.getBestIndividual(), ability);
		
		double needed = 0;
		
		for(int i = 0;i<NO_OF_PARAMETERS;i++)
		{
			System.out.print(expected.getVector().getData(i) + " ");
			needed += expected.getVector().getData(i);
		}

		System.out.println("Hospital ID");
		for(int i = 0;i<NO_OF_PARAMETERS;i++)
		{
			System.out.print(hospital[i] + " ");
		}

		System.out.println("Actual hospital capacity" + "\n" + needed);
		printfile(result, hospital,record_location_ability, distance, iteration, value, mode);

		fout_evaluation.close();
		fout_allpopulation.close();
		return evaluation;
	}
	
	public static void printfile(double[] result, int[] hospital, int[][] record, double[] distance, int iteration, String evaluation, String mode) throws Exception
	{
		
		FileOutputStream fout_destination;
		
	
		fout_destination = new FileOutputStream("Record of destination_" + iteration + "_"+ mode+".csv");
		PrintStream Outstream2 = new PrintStream(fout_destination);
		
		Outstream2.println(evaluation);
		Outstream2.print("Location" + "," +"Ability"+","+"Number of patients" + "," + "Hospital"+ "," + "distance-in-between" +"\n");
		
		for(int i=0;i<NO_OF_PARAMETERS;i++){
		
			Outstream2.print((record[i][0]+1) + "," +(record[i][1]+1)+","+result[i] + "," + (hospital[i]+1)+ "," + distance[i]+ "\n");
		}
	
		fout_destination.close();
	}
	
	//calculate the average value
	public static void DataAnalysis(int count, String mode) throws Exception{
		
		int counter = count;
		String name_num = "";
	
		FileOutputStream fout_avg_Num; 
		
		String str_final = "";   //record the number
		
		String[] str;
	
		
		str = new String[5];

		int[] patient_num = new int[65];
		
		DataInputStream in = null;
		
		fout_avg_Num = new FileOutputStream("Average value_" + mode + ".csv");
	
		for (int i = 0; i < counter; i++)
		{
			
		    name_num = "Record of destination_" + i + "_"+ mode+".csv";
		    int j= 0;
		    
			FileInputStream fstream = new FileInputStream(name_num);
			
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			
			strLine = br.readLine();
			str_final += strLine;
			strLine = br.readLine();
			str_final = strLine + "\n";
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {

					str = strLine.split(",");
					
					patient_num[j] += Double.parseDouble(str[2]);
					
					if(i == counter-1)
					{
						
						patient_num[j] = (int)(patient_num[j]/counter);

						str_final = str_final + str[0] + "," + str[1] + "," + patient_num[j] + "," + str[3] + "," + str[4]+ "\n";

					}
					
					j++;
					
				//}//if
			
		  }//while
			//System.out.println(i);
			
			
		}//for
		  //Close the input stream
				
		   new PrintStream(fout_avg_Num).println(str_final);

		   System.out.println("test");
		 	
           in.close();
         
		 	//	System.out.println(min);
		
	}
	
	//make the input table tableProjects
	public static void makeScheduleTable(String mode, int index) throws Exception{
		
		String fn = "Record of destination_" + index + "_" + mode + ".csv";
		
		FileInputStream fstream = new FileInputStream(fn);
		DataInputStream in = null;
		in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String[] str = new String[5];
		String strLine = null;
		strLine = br.readLine();
		strLine = br.readLine();
		int counter = 1;
		
		String strOutput = "#DtalgebraTable" + "\n" + "ProjectName" + "," +	"TaskPathName" + "," + "AllowedStartTime" + "\n" + "string" + "," +"string" + "," + "decimal" + "\n" + "#" + ","+"#" + ","+"#" + "\n"+ "#" + ","+"#" + ","+"#";
		while((strLine=br.readLine())!=null){
			str = strLine.split(",");

			for(int i = 0; i<Double.parseDouble(str[2]);i++){
				strOutput += "\n" + "p"+counter+"," + "path" + str[0] + str[3]+str[1]; 
				counter++;
			}
			
		}
		
		FileOutputStream fout_destination;
		
		
		fout_destination = new FileOutputStream("tableProjects.csv");
		PrintStream Outstream = new PrintStream(fout_destination);	
		Outstream.println(strOutput);
		fout_destination.close();
		
		
		
	}
	
	//Dispatch transportation utilities to disaster-hit spots
	public static void DisasterHitSpotDispatch() throws Exception{
		
	}
	//decide which hospital to go for each patient at each disaster-hit location
	public static void HospitalDisptach() throws Exception{
		
		double value= 0;
		double min = 0;
		int index = 0;
		///////////////////////////////////Resource allocation to hospital//////////////////////////////////////////
		
		initialize();
		readInput("test/iwasaki/Hospitalspec.csv", "test/iwasaki/PatientSpec.csv", "test/iwasaki/DistanceMatrix.csv");
		makeInputExpect();
		//makeInputCoordinate();
		String mode = "both"; //mode: evaluation mode: both (difference+distance); diff (different); dis (distance)
		for(int i = 0;i<1;i++){
			if(i == 0)
				min = value = evaluate(i,mode);
			
			else
			{
				value = evaluate(i, mode);
				if(value <= min){
					min = value;
					index = i;
				}
			}
		}
	
		System.out.println(index);
		makeScheduleTable(mode, index);
	}
	
	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		HospitalDisptach(); //Decide hospital;

		System.out.println(NO_OF_PARAMETERS);
	//	DataAnalysis(10, mode); 

		
		////////////////////////////////////Dynamic scheduling///////////////////////////////////////
		//        for this part, make the input tables for dynamic scheduling, and then get the output
		//        The the problem is how to make the dynamic scheduling
		////////////////////////////////////////////////////////////////////////////////////////////
	//	DevDynamicScheduling3 ds = new DevDynamicScheduling3();
	//	ds.calladdlFun(args, ds);
		

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime)/1000 + " seconds");
		
	}

}
