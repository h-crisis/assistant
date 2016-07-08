package iwasaki.sample;
import evaluator.OneMaxEvaluator;
import ga.bitstring.TBitString;
import ga.bitstring.TBitStringIndividual;
import ga.bitstring.TUxMgg;
import ga.core.IIndividual;

import java.util.List;

/**
 * UX+MGG��OneMax���ւ̓K�p��D
 * OneMax���́C�r�b�g�X�g�����O����"1"�̐����ő剻������ł���D
 * @author isao
 *
 */
public class TTestUxMgg {

	/** �r�b�g�� */
	public static final int NO_OF_BITS = 500;

	/** �W�c�T�C�Y */
	public static final int POPULATION_SIZE = 50;

	/** ������ */
	public static final int NO_OF_CROSSOVERS = 100;

	/**
	 * ���C�����\�b�h
	 * @param args �Ȃ�
	 */
/*	public static void main(String[] args) {
		TUxMgg ga = new TUxMgg( false, NO_OF_BITS, POPULATION_SIZE, NO_OF_CROSSOVERS, true);
		List<TBitStringIndividual> initialPopulation = ga.getInitialPopulation();
		OneMaxEvaluator.evaluatePopulation( initialPopulation);
		for ( int i = 0; i < 1000; ++i) {
			List<TBitStringIndividual> family = ga.selectParentsAndMakeKids();
			OneMaxEvaluator.evaluatePopulation( family);
			List<TBitStringIndividual> nextPop = ga.doSelectionForSurvival();
			System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
		}
		System.out.println();
		System.out.println( "*** Best individual ***");
		OneMaxEvaluator.printIndividual( ga.getBestIndividual());
	}*/
//}

//	/**
//	 * �̂�]������D
//	 * @param ind ��
//	 */
	private static void evaluateIndividual(TBitStringIndividual ind) {
		TBitString str = ind.getBitString();
		double evaluatonValue = 0.0;
		for (int i = 0; i < str.getLength(); ++i) {
			evaluatonValue += str.getData(i);
		}
		ind.setEvaluationValue(evaluatonValue);
		ind.setStatus(IIndividual.VALID);
	}
//
//	/**
//	 * �W�c���̑S�Ă̌̂�]������D
//	 * @param pop �W�c
//	 */
	private static void evaluatePopulation(TBitStringIndividual[] pop) {
		for (int i = 0; i < pop.length; ++i) {
			evaluateIndividual(pop[i]);
		}
	}
//
//	/**
//	 * �̂���ʂɏo�͂���D
//	 * @param ind ��
//	 */
	private static void printIndividual(TBitStringIndividual ind) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		TBitString str = ind.getBitString();
		for (int i = 0; i < str.getLength(); ++i) {
			System.out.print(str.getData(i));
		}
		System.out.println();
	}
//
/*	public static void main(String[] args) {
		TUxMgg ga = new TUxMgg(false, NO_OF_BITS, POPULATION_SIZE, NO_OF_CROSSOVERS, true);
		TBitStringIndividual[] initialPopulation = ga.getInitialPopulation();
		evaluatePopulation(initialPopulation);
		for (int i = 0; i < 1000; ++i) {
			TBitStringIndividual[] family = ga.selectParentsAndMakeKids();
			evaluatePopulation(family);
			TBitStringIndividual[] nextPop = ga.doSelectionForSurvival();
			System.out.println(ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
		}
		System.out.println();
		System.out.println("*** Best individual ***");
		printIndividual(ga.getBestIndividual());
	}*/
}