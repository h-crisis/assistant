/**
 * 
 */
package iwasaki.evaluator;

import iwasaki.ga.core.IIndividual;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TVector;
import java.util.Random;

import java.util.List;

/**
 * @author kurata
 *
 */
public class DMATEvaluator {

	public static final double MIN = 0;
	public static int totalnumber = 10;
	public static  double sum = 0;
	public static int capacity[] = new int[5];
	public static final double MAX = 1.0;
	public static double time[] = new double[5];


	//配置先の候補の確率が合計１ににるように設定する。
	public static TRealNumberIndividual reformat(TRealNumberIndividual ind) {
		TVector v = ind.getVector();

		TRealNumberIndividual ind_new = new TRealNumberIndividual(v.getDimension());

		TVector v_new = ind_new.getVector();

		sum = 0;
		for (int i = 0; i < v.getDimension(); ++i)
		{
				//System.out.println(v.getData(i));
				sum += v.getData(i);
				System.out.println(sum);
		}



		for (int i = 0; i < v.getDimension(); ++i) {
			v_new.setData(i, (v.getData(i)/ sum));
			//System.out.println(v_new.getData(i));


		}

		return ind_new;

	}


	/**
	 * �ϐ����`��֎ʑ�����D
	 * �����ŁC�̂��������x�N�g���́C�K�����[0.0, 1.0]�̒l�������Ƃɒ��ӂ��ꂽ���D
	 *
	 * @param x �ϐ�
	 * @return
	 */
	private static double map(double x) {
		return (MAX - MIN) * x + MIN;
	}

	/**
	 * �̂�]������D
	 *
	 * @param ind ��
	 */
	private static void evaluateIndividualtime(TRealNumberIndividual ind) {
		TVector v = reformat(ind).getVector();
		capacity[0] = 2;
		capacity[1] = 4;
		capacity[2] = 5;
		capacity[3] = 3;
		capacity[4] = 10;

		time[0] = 13;
		time[1] = 40;
		time[2] = 20;
		time[3] = 19;
		time[4] = 21;
		double evaluatonValuetime = 0.0;
		for (int i = 0; i < v.getDimension(); ++i) {
			double x = map(v.getData(i));
			if (x < MIN || x > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}

			evaluatonValuetime = Math.abs(x * time[i] * totalnumber);
		}
		ind.setEvaluationValue(evaluatonValuetime);
		ind.setStatus(IIndividual.VALID);
	}

	private static void evaluateIndividualcapacity(TRealNumberIndividual ind) {
		TVector v = reformat(ind).getVector();
		capacity[0] = 2;
		capacity[1] = 4;
		capacity[2] = 5;
		capacity[3] = 3;
		capacity[4] = 10;

		time[0] = 13;
		time[1] = 40;
		time[2] = 20;
		time[3] = 19;
		time[4] = 21;
		double evaluatonValuedcapacity = 0.0;
		for (int i = 0; i < v.getDimension(); ++i) {
			double x = map(v.getData(i));
			if (x < MIN || x > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}

			evaluatonValuedcapacity += Math.abs(capacity[i] - x*totalnumber);
		}


		ind.setEvaluationValue(evaluatonValuedcapacity);
		ind.setStatus(IIndividual.VALID);
	}

	private static void evaluateIndividual(TRealNumberIndividual ind) {
		TVector v = reformat(ind).getVector();
		capacity[0] = 2;
		capacity[1] = 4;
		capacity[2] = 5;
		capacity[3] = 3;
		capacity[4] = 10;

		time[0] = 13;
		time[1] = 40;
		time[2] = 20;
		time[3] = 19;
		time[4] = 21;
		double evaluatonValued = 0.0;
		for (int i = 0; i < v.getDimension(); ++i) {
			double x = map(v.getData(i));
			if (x < MIN || x > MAX) {
				ind.setStatus(IIndividual.INVALID);
				return;
			}
			;
			evaluatonValued += Math.abs(capacity[i] - (Math.round(x)*totalnumber)) + (Math.round(x)  * time[i] * totalnumber);
		}


		ind.setEvaluationValue(evaluatonValued);
		ind.setStatus(IIndividual.VALID);
	}



	/**
	 * �W�c���̑S�Ă̌̂�]������D
	 *
	 * @param pop �W�c
	 */
	public static void evaluatePopulationtime(List<TRealNumberIndividual> pop) {
		for (int i = 0; i < pop.size(); ++i) {
			evaluateIndividualtime(pop.get(i));
		}
	}

	public static void evaluatePopulationcapacity(List<TRealNumberIndividual> pop) {
		for (int i = 0; i < pop.size(); ++i) {
			evaluateIndividualcapacity(pop.get(i));
		}
	}

	public static void evaluatePopulation(List<TRealNumberIndividual> pop) {
		for (int i = 0; i < pop.size(); ++i) {
			evaluateIndividual(pop.get(i));
		}
	}





	/**
	 * �̂���ʂɏo�͂���D
	 *
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

