package iwasaki.evaluator;

import iwasaki.ga.core.IIndividual;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TVector;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;



//Evaluator for CSP (constraint satisfactory problems)

public class CspEvaluator {

	
	public static double MIN = 0;

	public static double MAX = 1.0;

	public static double TOTAL = 0;
	public static int[] TOTALVECTOR;
	
	public static double PENALTY = 500;

	private static double map(double x) {
		return (MAX - MIN) * x + MIN;
	}
	
	private static double mapValue(double x, int i){
		return 0;
	}

	//to make sure the allocated total number of person will not be lower/higher than the actual number: percentage is scaled to guarantee the sum is 1 for each situation 
	public static TRealNumberIndividual reformat(TRealNumberIndividual ind, int[] ability){
		
		
		TVector v = ind.getVector();
		TRealNumberIndividual ind_new = new TRealNumberIndividual(v.getDimension());
		
		TVector v_new = ind_new.getVector();
		
		int temp1 = 0;
		double sum = 0;
		double x = 0;
		int counter = 0;
		temp1 = ability[0];
		for (int i = 0; i < v.getDimension(); i++) {
			
			x = v.getData(i);

			if(i != 0){
				temp1 = ability[i-1];
			}
			
			
			//scale the percetage to guarantee the sum of each situation is 1
			if(temp1 != ability[i]){
				for(int j = counter; j >= 0;j--){
					v_new.setData(i-j, v.getData(i-j)/sum);
				}
				sum = 0;
				counter = 0;
			}
			
			if( i == v.getDimension()-1){
				sum += x;
				for(int j = counter; j >= 0;j--){
					v_new.setData(i-j, v.getData(i-j)/sum);
				}
				sum = 0;
				counter = 0;
			}
			sum += x;
			counter++;
		}
		
	
		return ind_new;
	}
	//evaluate individuals to minimize the difference between actual allocation and expected value
	private static void evaluateIndividual(TRealNumberIndividual ind, TRealNumberIndividual expected) {
		
		TVector v = ind.getVector();
		double temp = 0;
		double evaluatonValue = 0.0;
		double sum = 0.0;
		
		double x = 0;
		double y = 0;
		for (int i = 0; i < v.getDimension(); ++i) {
			x = map(v.getData(i));
			y = expected.getVector().getData(i);
			if (x < MIN || x > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}
			evaluatonValue += Math.abs(y-x);
			sum += x;
		}
		if (sum > TOTAL){
			temp = ind.getEvaluationValue() + PENALTY;
			ind.setEvaluationValue(temp);
			ind.setStatus(IIndividual.VALID);
			return;
		}
		ind.setEvaluationValue(evaluatonValue);
		ind.setStatus(IIndividual.VALID);
	}
	
	//evaluate individuals to minimize the difference between actual allocation and expected value, as well as the time cost
	//private static void evaluateIndividualDistance(TRealNumberIndividual ind, TRealNumberIndividual expected, LinkedList<String> coordinate, int[] totalnumber) throws NumberFormatException, Exception {
	private static void evaluateIndividualDistance(TRealNumberIndividual ind, TRealNumberIndividual expected, double[] dis, int[] totalnumber, int[] ability) throws NumberFormatException, Exception {			
		TVector v = reformat(ind, ability).getVector();
	//	TVector v = ind.getVector();
		//double temp = 0;
		double evaluatonValue = 0.0;
		double distance = 0;
		String position = null;
		String[] pos = new String[4];
		
		double[] normalizeEva = new double[v.getDimension()];
		double[] normalizeDis = new double[v.getDimension()];
		
		double x = 0;
		double y = 0;
		
		int temp1 = totalnumber[0];
		int counter = 0;
		Random randomGenerator = new Random();
		for (int i = 0; i < v.getDimension(); ++i) {
			
			x = v.getData(i)*totalnumber[i];
			y = expected.getVector().getData(i)/5; //expected number
		/*	double temp2 = 0;
	
			if(i != 0){
				temp1 = totalnumber[i-1];
			}
			
			
			if(temp1 != totalnumber[i]){
				temp2 = sum;
				sum = 0;
				if(temp2 < temp1){
					normalizeEva[i-1] += PENALTY;
				}
			}
			*/
			
			//get coordinate of end and start point
		//	position = coordinate.get(i);
			//start_x;start_y;end_x;end_y
		//	pos = position.split(";");
			
			if (v.getData(i) < MIN || v.getData(i) > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}
			
			
			//normalizeDis[i] = Math.sqrt((Double.parseDouble(pos[2])-Double.parseDouble(pos[0]))*(Double.parseDouble(pos[2])-Double.parseDouble(pos[0])) + (Double.parseDouble(pos[3])-Double.parseDouble(pos[1]))*(Double.parseDouble(pos[3])-Double.parseDouble(pos[1])));
	//		normalizeDis[i] = CalculateDistance(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]), Double.parseDouble(pos[3]),"osm_2po_Japan.gph");
		    normalizeDis[i] = v.getData(i)*dis[i];
			if(Math.round(x) < y)
				//normalizeEva[i] = Math.abs(x-Math.round(x));
				normalizeEva[i] += PENALTY;
				//evaluatonValue += PENALTY;
			else{
				normalizeEva[i] = Math.abs(y-x);
			}
				
				//evaluatonValue += Math.abs(y-x);
			//Math.sqrt((xDest-xOrig)*(xDest-xOrig) + (yDest-yOrig)*(yDest-yOrig)); 
		//	distance += Math.sqrt((Double.parseDouble(pos[2])-Double.parseDouble(pos[0]))*(Double.parseDouble(pos[2])-Double.parseDouble(pos[0])) + (Double.parseDouble(pos[3])-Double.parseDouble(pos[1]))*(Double.parseDouble(pos[3])-Double.parseDouble(pos[1])));
		//	distance +=  v.getData(i)*CalculateDistance(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]), Double.parseDouble(pos[3]),"osm_2po_Japan.gph");
		//	evaluatonValue += Math.abs(y-x);
		//	distance+=dis[i]*v.getData(i);
	
		}
		
		evaluatonValue = normalize(normalizeEva);
		distance = normalize(normalizeDis);
		
	//	ind.setEvaluationValue(evaluatonValue);
	//	ind.setEvaluationValueAdd(evaluatonValue, distance);
	//	ind.setEvaluationValueDiff(evaluatonValue);
	//	ind.setEvaluationValueDis(distance);
	// 	ind.setEvaluationValue(distance);
		double w = 0;
		
		w = randomGenerator.nextDouble();
		//w = 0;
		ind.setEvaluationValue(distance*w + evaluatonValue*(1-w));
		ind.setStatus(IIndividual.VALID);
	}
	
	public static double normalize(double[] d)
	{
		
		double max = 0;
		double min = 0;
	
		double temp_max = d[0];
		double temp_min = d[0];
		for(int i = 1; i<d.length; i++)
		{
			if(temp_max >= d[i])
				max = temp_max;
			else if(temp_max < d[i]){
				max = d[i];
				temp_max = max;
			}
			
			if(temp_min <= d[i])
				min = temp_min;
			else if (temp_min >d[i]){
				min = d[i];
				temp_min = min;
			}
		}
		
		for(int i = 0;i<d.length;i++){
			
			if((max-min) != 0)
				d[i] = (d[i] - min)/(max-min);
			else
				d[i] = 0;
		}
		
		double sum = 0;
		
		for(int i = 0;i<d.length;i++)
			sum += d[i];
		
		return sum;
	}

	//evaluate populations
//	public static void evaluatePopulation(List<TRealNumberIndividual> pop,TRealNumberIndividual exp, LinkedList<String> coordinate, int[] totalnumber, int mode) throws NumberFormatException, Exception {
	public static void evaluatePopulation(List<TRealNumberIndividual> pop,TRealNumberIndividual exp, double[] dis, int[] totalnumber, int mode, int[] ability) throws NumberFormatException, Exception {
	 
		if (mode == 0){
			for ( int i = 0; i < pop.size(); ++i) {
				evaluateIndividual(pop.get(i), exp);
			}
		}
		else
		{
			for ( int i = 0; i < pop.size(); ++i) {
				evaluateIndividualDistance(pop.get(i), exp, dis, totalnumber, ability);
			}
		}
	}

		
	//print individuals
	public static void printIndividual(TRealNumberIndividual ind) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		TVector v = ind.getVector();
		double allocated = 0;
		for (int i = 0; i < v.getDimension(); ++i) {
			System.out.print(map(v.getData(i)) + " ");
			allocated += map(v.getData(i));
		}
		System.out.println("\n" + allocated);
	}
	
	public static void printIndividualDistance(TRealNumberIndividual ind, int[] total) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		TVector v = reformat(ind,total).getVector();
		double allocated = 0;
		double temp = 0;
		for (int i = 0; i < v.getDimension(); ++i) {
			
			if(Math.round(v.getData(i)*total[i]) == 0)
				temp = Math.round(v.getData(i)*total[i]) + 1;
			else
				temp = Math.round(v.getData(i)*total[i]);
			System.out.print(temp + " ");
			allocated += Math.round(temp);
		}
		
		System.out.println("\n" + allocated);
	}
	
	public static double[] IndividualDistanceOutput(TRealNumberIndividual ind, int[] total) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		double[] result;
		int size = total.length;
		result = new double[size];
		
		TVector v = reformat(ind,total).getVector();
		double allocated = 0;
		double temp = 0;
		for (int i = 0; i < v.getDimension(); ++i) {
			
			temp =  v.getData(i);
		/*	if(Math.round(v.getData(i)*total[i]) == 0)
				temp = Math.round(v.getData(i)*total[i]) + 1;
			else
				temp = Math.round(v.getData(i)*total[i]);*/
			System.out.print(temp + " ");
			result[i] = temp;
			allocated += Math.round(temp);
		}
		System.out.println("\n" + allocated);
		return result;
	}
	
	public static void setMin(double min){
		MIN = min;
	}
	
	public static void setMax(double max){
		MAX = max;
	}
	
	public static void setTotal(double total){
		TOTAL = total;
	}
	
	public static void setTotalVector(int[] total)
	{
		TOTALVECTOR = new int[total.length];
		for(int i = 0; i<total.length; i++)
			TOTALVECTOR[i] = total[i];
	}
	
	

}
