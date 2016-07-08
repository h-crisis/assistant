package ga.mgg;

import ga.core.IIndividual;
import ga.core.TPopulation;

/**
 * 上位２個体を選択する生存選択器．
 * @since 2
 * @author isao
 */
public class TBest2Selection implements ISelectionForSurvival {

	/** 家族 */
	private IIndividual[] fFamily;

	/** 最小化問題か？ */
	private boolean fMinimization;
	
	/**
	 * コンストラクタ
	 * @since 2
	 * @author isao
	 */
	public TBest2Selection(boolean isMinimization) {
		fFamily = new IIndividual [1];
		fMinimization = isMinimization;
	}

	/**
	 * @see TBestAndRankBasedRouletteSelection#doIt(TPopulation, int[], IIndividual[], IIndividual[])
	 * @since 2
	 * @author isao
	 */
	public void doIt(TPopulation pop, int[] parentIndices, 
					           IIndividual[] parents, IIndividual[] kids) {
		setFamilySize(2, kids.length);
		registerToFamily(parents, kids);
		sort(fFamily);
		IIndividual bestKid = fFamily[0];
		IIndividual secondBestKid = fFamily[1];
		pop.getIndividual(parentIndices[0]).copyFrom(bestKid);
		pop.getIndividual(parentIndices[1]).copyFrom(secondBestKid);		
	}

	/**
	 * 家族のサイズを設定する．
	 * @param noOfParents 親個体の数
	 * @param noOfKids 子個体の数
	 * @since 2
	 * @author isao
	 */
	private void setFamilySize(int noOfParents, int noOfKids) {
		if (fFamily.length == noOfParents + noOfKids)
			return;
		fFamily = new IIndividual [noOfParents + noOfKids];
	}
	
	/**
	 * 親個体と子個体を家族に登録する．
	 * @param parents 親個体の配列
	 * @param kids 子個体の配列
	 * @since 2
	 * @author isao
	 */
	private void registerToFamily(IIndividual[] parents, IIndividual[] kids) {
		int index = 0;
		for (int i = 0; i < 2; ++i) {
			fFamily[index++] = parents[i];
		}
		for (int i = 0; i < kids.length; ++i) {
			fFamily[index++] = kids[i];
		}
	}

	/**
	 * 個体配列をソートする
	 * @param array ソートする個体配列
	 * @since 2
	 * @author isao
	 */
	private void sort(IIndividual[] array) {
		for(int i = 0; i < array.length - 1; i++) {
			for(int j = i + 1; j < array.length; j++) {
				if(isABetterThanB(array[j], array[i])) {
					IIndividual tmp = array[i];
					array[i] = array[j];
					array[j] = tmp;					
				}
			}
		}		
	}
	
	/**
	 * 個体aと個体bを比較する．
	 * @param a 個体A
	 * @param b 個体B
	 * @param problem 問題
	 * @return aよりもbが優れている場合はtrue, そうでないときはfalse
	 * @since 2
	 * @author isao
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
	
	/*
	 * (non-Javadoc)
	 * @see ga.ISelectionForSurvival#isMinimization()
	 */
	public boolean isMinimization() {
		return fMinimization;
	}

}
