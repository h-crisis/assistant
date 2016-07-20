package iwasaki.sample;

import iwasaki.evaluator.SphereEvaluator;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TUndxMgg;

import java.util.List;

/**
 * UNDX+MGG��20����Sphere�֐��ŏ������(y=��xi^2)�ւ̓K�p��D
 * @author isao
 *
 */
public class TTestUndxMgg {

//	/** Sphere�֐��̒�`��̍ŏ��l */
	public static final double MIN = 0;
//
//	/** Sphere�֐��̒�`��̍ő�l */
	public static final double MAX = 1;

	/** ������ */
	public static final int NO_OF_PARAMETERS = 20;

	/** �̐� */
	public static final int POPULATION_SIZE = 50;

	/** ������ */
	public static final int NO_OF_CROSSOVERS = 100;

	/**
	 * ���C�����\�b�h
	 * @param args �Ȃ�
	 */
	public static void main(String[] args) {
		TUndxMgg ga = new TUndxMgg( true, NO_OF_PARAMETERS, POPULATION_SIZE, NO_OF_CROSSOVERS);
		List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();
		SphereEvaluator.evaluatePopulation( initialPopulation);
		for (int i = 0; i < 1000; ++i) {
			List<TRealNumberIndividual> family = ga.selectParentsAndMakeKids();
			SphereEvaluator.evaluatePopulation( family);
			List<TRealNumberIndividual> nextPop = ga.doSelectionForSurvival();
			System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
		}
		System.out.println();
		System.out.println( "*** Best individual ***");
		SphereEvaluator.printIndividual( ga.getBestIndividual());
	}
}

//	/**
//	 * �ϐ����`��֎ʑ�����D
//	 * �����ŁC�̂��������x�N�g���́C�K�����[0.0, 1.0]�̒l�������Ƃɒ��ӂ��ꂽ���D
//	 * @param x �ϐ�
//	 * @return
//	 */
//	private static double map(double x) {
//		return (MAX - MIN) * x + MIN;
//	}
//
//	/**
//	 * �̂�]������D
//	 * @param ind ��
//	 */
//	private static void evaluateIndividual(TRealNumberIndividual ind) {
//		TVector v = ind.getVector();
//		double evaluatonValue = 0.0;
//		for (int i = 0; i < v.getDimension(); ++i) {
//			double x = map(v.getData(i));
//			if (x < MIN || x > MAX) {
//				ind.setStatus(IIndividual.INVALID);
//				return;
//			}
//			evaluatonValue += x * x;
//		}
//		ind.setEvaluationValue(evaluatonValue);
//		ind.setStatus(IIndividual.VALID);
//	}
//
//	/**
//	 * �W�c���̑S�Ă̌̂�]������D
//	 * @param pop �W�c
//	 */
//	private static void evaluatePopulation(TRealNumberIndividual[] pop) {
//		for (int i = 0; i < pop.length; ++i) {
//			evaluateIndividual(pop[i]);
//		}
//	}
//
//	/**
//	 * �̂���ʂɏo�͂���D
//	 * @param ind ��
//	 */
//	private static void printIndividual(TRealNumberIndividual ind) {
//		System.out.println("Evaluation value: " + ind.getEvaluationValue());
//		TVector v = ind.getVector();
//		for (int i = 0; i < v.getDimension(); ++i) {
//			System.out.print(map(v.getData(i)) + " ");
//		}
//		System.out.println();
//	}
//
//	public static void main(String[] args) {
//		TUndxMgg ga = new TUndxMgg(true, NO_OF_PARAMETERS, POPULATION_SIZE, NO_OF_CROSSOVERS);
//		TRealNumberIndividual[] initialPopulation = ga.getInitialPopulation();
//		evaluatePopulation(initialPopulation);
//		for (int i = 0; i < 1000; ++i) {
//			TRealNumberIndividual[] family = ga.selectParentsAndMakeKids();
//			evaluatePopulation(family);
//			TRealNumberIndividual[] nextPop = ga.doSelectionForSurvival();
//			System.out.println(ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
//		}
//		System.out.println();
//		System.out.println("*** Best individual ***");
//		printIndividual(ga.getBestIndividual());
//	}
