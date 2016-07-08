/**
 * 
 */
package evaluator;

import ga.bitstring.TBitString;
import ga.bitstring.TBitStringIndividual;
import ga.core.IIndividual;

import java.util.List;

/**
 * @author kurata
 *
 */
public class OneMaxEvaluator {

	/**
	 * �̂�]������D
	 * @param ind ��
	 */
	private static void evaluateIndividual(TBitStringIndividual ind) {
		TBitString str = ind.getBitString();
		double evaluatonValue = 0.0;
		for (int i = 0; i < str.getLength(); ++i) {
			evaluatonValue += str.getData(i);
		}
		ind.setEvaluationValue(evaluatonValue);
		ind.setStatus(IIndividual.VALID);
	}

	/**
	 * �W�c���̑S�Ă̌̂�]������D
	 * @param pop �W�c
	 */
	public static void evaluatePopulation(List<TBitStringIndividual> pop) {
		for (int i = 0; i < pop.size(); ++i) {
			evaluateIndividual( pop.get( i));
		}
	}

	/**
	 * �̂���ʂɏo�͂���D
	 * @param ind ��
	 */
	public static void printIndividual(TBitStringIndividual ind) {
		System.out.println("Evaluation value: " + ind.getEvaluationValue());
		TBitString str = ind.getBitString();
		for (int i = 0; i < str.getLength(); ++i) {
			System.out.print(str.getData(i));
		}
		System.out.println();
	}
}
