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
	 * 個体を評価する．
	 * @param ind 個体
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
	 * 集団中の全ての個体を評価する．
	 * @param pop 集団
	 */
	public static void evaluatePopulation(List<TBitStringIndividual> pop) {
		for (int i = 0; i < pop.size(); ++i) {
			evaluateIndividual( pop.get( i));
		}
	}

	/**
	 * 個体を画面に出力する．
	 * @param ind 個体
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
