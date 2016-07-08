package iwasaki.sample;

import evaluator.*;
import ga.realcode.IRealNumberCoding;
import ga.realcode.TRealNumberIndividual;
import ga.realcode.TUndxMgg;
import ga.realcode.TVector;
import ga.util.TMyRandom;

import java.util.LinkedList;
import java.util.List;

public class TestCSPUndxMgg {

	public static final int NO_OF_PARAMETERS = 50;

	public static final int POPULATION_SIZE = 50;

	public static final int NO_OF_CROSSOVERS = 100;

	public static void main(String[] args) throws Exception{
		
		TUndxMgg ga = new TUndxMgg( true, NO_OF_PARAMETERS, POPULATION_SIZE, NO_OF_CROSSOVERS);
		
		List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();
		
		TRealNumberIndividual expected = new TRealNumberIndividual(NO_OF_PARAMETERS);
		LinkedList<String> coordinate = new LinkedList<String>();
		
		double max = 20;
		double total = 450;
		
		//Set expected vetor as the resources expected at each spot; Random number [0,20] 
		TMyRandom rand = TMyRandom.getInstance();
		TVector v = ((IRealNumberCoding)expected).getVector();
		for(int i = 0; i < NO_OF_PARAMETERS;i++){
			v.setData(i, rand.getDouble(0, 20));
		}
		
		CspEvaluator.setMax(max);
		CspEvaluator.setTotal(total);
		int[] dump = new int[NO_OF_PARAMETERS];
	//	CspEvaluator.evaluatePopulation( initialPopulation, expected, coordinate, dump, 0); //11.10 Cora
		
		for (int i = 0; i < 1000; ++i) {
			List<TRealNumberIndividual> family = ga.selectParentsAndMakeKids();
	//		CspEvaluator.evaluatePopulation( family, expected, coordinate, dump, 0 );//11.10 Cora
			List<TRealNumberIndividual> nextPop = ga.doSelectionForSurvival();
			System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
		}
		System.out.println();
		System.out.println( "*** Best individual ***");
		CspEvaluator.printIndividual(ga.getBestIndividual());
		
		double needed = 0;
		for(int i = 0;i<NO_OF_PARAMETERS;i++)
		{
			System.out.print(expected.getVector().getData(i) + " ");
			needed += expected.getVector().getData(i);
		}
		
		System.out.println("\n" + needed);
	}
	
}
