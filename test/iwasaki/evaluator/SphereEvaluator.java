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

	/** Sphere関数の定義域の最小値 */
	public static final double MIN = -5.12;

	/** Sphere関数の定義域の最大値 */
	public static final double MAX = 5.12;

	/**
	 * 変数を定義域へ写像する．
	 * ここで，個体がもつ実数ベクトルは，必ず区間[0.0, 1.0]の値を持つことに注意されたい．
	 * @param x 変数
	 * @return
	 */
	private static double map(double x) {
		return (MAX - MIN) * x + MIN;
	}

	/**
	 * 個体を評価する．
	 * @param ind 個体
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
	 * 集団中の全ての個体を評価する．
	 * @param pop 集団
	 */
	public static void evaluatePopulation(List<TRealNumberIndividual> pop) {
		for ( int i = 0; i < pop.size(); ++i) {
			evaluateIndividual( pop.get( i));
		}
	}

	/**
	 * 個体を画面に出力する．
	 * @param ind 個体
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
