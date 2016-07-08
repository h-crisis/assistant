package ga.core;

import ga.util.TNoSuchValueException;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 集団の統計をとる
 * @since 38
 * @author hmkz
 */
public class TPopulationStatistics {

	/** 統計の母集団 */
	private TPopulation fPopulation;

	/** 最小化か？ */
	private boolean fMinimization;
	
	/** 十分に０に近い数 */
	public static final double EPS = 1e-30;
	
	/**
	 * コンストラクタ
	 * @param population 統計の母集団
	 * @param isMinimization 最小化：true, 最大化：true
	 * @since 38
	 */
	public TPopulationStatistics(TPopulation population, boolean isMinimization) {
		fPopulation = population;
		fMinimization = isMinimization;
	}

	/**
	 * 集団を設定する．
	 * 集団中の個体にはnullが含まれてはいけない．
	 * @param population 集団
	 * @since 38
	 */
	public void	setPopulation(TPopulation population) {
		fPopulation = population;
	}

	/**
	 * 集団中のVALIDな個体の平均評価値を返す．
	 * @return 平均評価値
	 * @throws TNoSuchValueException 集団中にVALIDな個体が一つもないとき．
	 * @since 38
	 */
	public double	getAverageOfEvaluationValues() {
		int validPopulationSize = 0;
		double averageOfEvaluationValues = 0.0;
		int populationSize = fPopulation.getSize();
		for(int i = 0; i < populationSize; i++) {
			if (fPopulation.getIndividual(i).getStatus() == IIndividual.VALID) {
				averageOfEvaluationValues += fPopulation.getIndividual(i).getEvaluationValue();
				validPopulationSize++;
			}
		}
		if (validPopulationSize == 0) {
			throw new TNoSuchValueException("No valid individual exists.");
		}
		return averageOfEvaluationValues / validPopulationSize;
	}

	/**
	 * 集団中の最良個体を返す．
	 * 集団サイズが0であるか，実行可能解が1つもない場合，最良個体は存在しない．
	 * そのときこのメソッドは例外を投げる．
	 * @return 最良個体
	 * @throws TNoSuchValueException 集団中にVALIDな個体が一つもないとき．
	 * @since 38
	 */
	public IIndividual getBestIndividual() {
		return fPopulation.getIndividual(getBestIndex());
	}

	/**
	 * 集団中の最良個体の添え字を返す．
	 * 集団中に最良個体が存在しない場合，このメソッドは例外を投げる．
	 * @return 最良個体の添え字
	 * @throws TNoSuchValueException 集団中にVALIDな個体が一つもないとき
	 * @since 38
	 */
	public int getBestIndex() {
		int populationSize = fPopulation.getSize();
		IIndividual best = null;
		int bestIndex = -1;
		for(int i = 0; i < populationSize; i++) {
			IIndividual current = fPopulation.getIndividual(i);
			if (current.getStatus() == IIndividual.INVALID) {
				continue;
			}
			if(best == null || isABetterThanB(current, best)) {
				best = current;
				bestIndex = i;
			}
		}
		if (bestIndex == -1) {
			throw new TNoSuchValueException("Best individual does not exist.");
		}
		return bestIndex;
	}

	/**
	 * 最良個体の評価値を返す．
	 * 集団中に最良個体が存在しない場合，このメソッドは例外を投げる．
	 * @return 最良個体の評価値
	 * @throws TNoSuchValueException 集団中にVALIDな個体が一つもないとき
	 * @since 38
	 */
	public double getBestEvaluationValue() {
		return getBestIndividual().getEvaluationValue();
	}

	/**
	 * 集団中の個体を評価値にしたがって並べ替える．
	 * 評価値が良い個体ほど先頭に並ぶ．
	 * INVALIDな個体はVALIDな個体よりも後ろにくる．
	 * @since 75
	 */
	public void sort() {
		sort(new Comparator() {
			public int compare(Object o1, Object o2) {
				IIndividual i1 = (IIndividual) o1;
				IIndividual i2 = (IIndividual) o2;
				if (i1.getStatus() == IIndividual.INVALID && i2.getStatus() == IIndividual.VALID) {
					return 1;
				}
				if (i1.getStatus() == IIndividual.VALID && i2.getStatus() == IIndividual.INVALID) {
					return -1;
				}
				return isABetterThanB(i1, i2) ? -1 : isAEqualToB(i1, i2) ? 0 : 1;
			}
		});
	}

	/**
	 * 集団中の個体を並べ替える
	 * @param c
	 * @since 75
	 */
	public void sort(Comparator c) {
		TPopulation aux = new TPopulation(fPopulation);
		mergeSort(aux, fPopulation, 0, fPopulation.getSize(), 0, c);
	}

	/**
	 * xのi番目とj番目の要素を交換する
	 * @param x
	 * @param i
	 * @param j
	 * @since 75
	 */
	private static void swap(TPopulation x, int i, int j) {
		IIndividual indI = x.getIndividual(i);
		IIndividual indJ = x.getIndividual(j);
		x.setIndividual(i, indJ);
		x.setIndividual(j, indI);
	}

	/**
	 * 要素数が少ない場合にはマージソートは適さない．この値より少なければ挿入ソートを用いる．
	 */
	private static final int INSERTIONSORT_THRESHOLD = 7;

	/**
	 * @param src 元の集団
	 * @param dest ソート済みの値が入る
	 * @param low ソート範囲の先頭のインデックス
	 * @param high ソート範囲の末尾のインデックス
	 * @param off srcにおいてlowとhighを対応させるためのオフセット
	 * @since 75
	 */
	private static void mergeSort(TPopulation src, TPopulation dest, int low, int high, int off, Comparator c) {
		int length = high - low;
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++) {
				for (int j = i; j > low && c.compare(dest.getIndividual(j-1), dest.getIndividual(j)) > 0; j--) {
					swap(dest, j, j-1);
				}
			}
			return;
		}

		int destLow  = low;
		int destHigh = high;
		low  += off;
		high += off;
		int mid = (low + high) / 2;
		mergeSort(dest, src, low, mid, -off, c);
		mergeSort(dest, src, mid, high, -off, c);

		if (c.compare(src.getIndividual(mid-1), src.getIndividual(mid)) <= 0) {
			for (int i = 0; i < length; i++) {
				dest.setIndividual(i + destLow, src.getIndividual(i + low));
			}
			return;
		}

		for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && c.compare(src.getIndividual(p), src.getIndividual(q)) <= 0)
				dest.setIndividual(i, src.getIndividual(p++));
			else
				dest.setIndividual(i, src.getIndividual(q++));
		}
	}

	/**
	 * フィルタfを通過する個体の数をカウントし，その値を返す．
	 * @param f 集団の中から特定の条件を満たす要素だけを選び出すフィルタ
	 * @return フィルタfがtrueを返す要素の数
	 * @since 98
	 */
	public int frequency(IPopulationFilter f) {
		int freq = 0;
		for (int i = 0; i < fPopulation.getSize(); i++) {
			IIndividual ind = fPopulation.getIndividual(i);
			if (f.accept(i, ind)) {
				freq++;
			}
		}
		return freq;
	}

	/**
	 * 集団中の個体を特定の条件でフィルタリングし，条件を満たした個体のみから成る新たな集団を返す．
	 * @param f フィルタ
	 * @return フィルタリングされ，条件を満たす個体のみで構成された集団
	 * @since 98
	 */
	public TPopulation subset(IPopulationFilter f) {
		ArrayList tmp = new ArrayList(fPopulation.getSize() / 2);
		for (int i = 0; i < fPopulation.getSize(); i++) {
			IIndividual ind = fPopulation.getIndividual(i);
			if (f.accept(i, ind)) {
				tmp.add(ind);
			}
		}
		TPopulation p = new TPopulation(fPopulation.getIndividualFactory(), tmp.size());
		for (int i = 0; i < tmp.size(); i++) {
			p.setIndividual(i, (IIndividual) tmp.get(i));
		}
		return p;
	}
	
	/**
	 * 個体aと個体bを比較する．
	 * @param a 個体A
	 * @param b 個体B
	 * @param problem 問題
	 * @return aよりもbが優れている場合はtrue, そうでないときはfalse
	 * @since 2
	 */
	private boolean isABetterThanB(IIndividual a, IIndividual b) {
		if (a.getStatus() == IIndividual.INVALID)
			return false;
		if (b.getStatus() == IIndividual.INVALID)
			return true;
		if (fMinimization) {
			return a.getEvaluationValue() < b.getEvaluationValue();
		} else {
			return a.getEvaluationValue() > b.getEvaluationValue();
		}
	}
	
	/**
	 * 個体aと個体bの評価値が同じかどうか調べる
	 * @return 同じ：true, 異なる：false
	 * @since 2
	 */
	public boolean isAEqualToB(IIndividual a, IIndividual b) {
		return Math.abs(a.getEvaluationValue() - b.getEvaluationValue()) < EPS;
	}
	
}
