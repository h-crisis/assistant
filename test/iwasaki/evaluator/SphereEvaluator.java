/**
 * 
 */
package evaluator;

import ga.core.IIndividual;
import ga.realcode.TRealNumberIndividual;
import ga.realcode.TVector;

import java.util.List;

/**
 * @author kurata
 *
 */
public class SphereEvaluator {

	/** Sphere�֐��̒�`��̍ŏ��l */
	public static final double MIN = -5.12;

	/** Sphere�֐��̒�`��̍ő�l */
	public static final double MAX = 5.12;

	/**
	 * �ϐ����`��֎ʑ�����D
	 * �����ŁC�̂��������x�N�g���́C�K�����[0.0, 1.0]�̒l�������Ƃɒ��ӂ��ꂽ���D
	 * @param x �ϐ�
	 * @return
	 */
	private static double map(double x) {
		return (MAX - MIN) * x + MIN;
	}

	/**
	 * �̂�]������D
	 * @param ind ��
	 */
	private static void evaluateIndividual(TRealNumberIndividual ind) {
		TVector v = ind.getVector();
		double evaluatonValue = 0.0;
		for (int i = 0; i < v.getDimension(); ++i) {
			double x = map(v.getData(i));
			if (x < MIN || x > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}
			evaluatonValue += x * x;
		}
		ind.setEvaluationValue(evaluatonValue);
		ind.setStatus(IIndividual.VALID);
	}

	/**
	 * �W�c���̑S�Ă̌̂�]������D
	 * @param pop �W�c
	 */
	public static void evaluatePopulation(List<TRealNumberIndividual> pop) {
		for ( int i = 0; i < pop.size(); ++i) {
			evaluateIndividual( pop.get( i));
		}
	}

	/**
	 * �̂���ʂɏo�͂���D
	 * @param ind ��
	 */
	public static void printIndividual(TRealNumberIndividual ind) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		TVector v = ind.getVector();
		for (int i = 0; i < v.getDimension(); ++i) {
			System.out.print(map(v.getData(i)) + " ");
		}
		System.out.println();
	}
}
