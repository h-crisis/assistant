package ga.mgg;

import ga.core.IIndividual;
import ga.core.TPopulation;
import ga.util.TRoulette;

/**
 * 最良個体選択とランクに基づくルーレット選択を組み合わせた生存選択
 * @since 2
 * @author isao
 */
public class TBestAndRankBasedRouletteSelection implements ISelectionForSurvival {

	/** 家族 */
	private IIndividual[] fFamily;

	/** 最良個体の添え字 */
	private final static int BEST_KIDS_INDEX = 0; 

	/** ルーレット */
	private TRoulette fRoulette;
	
	/** 最小化か？ */
	private boolean fMinimization;

	public TBestAndRankBasedRouletteSelection(boolean isMinimization) {
		fFamily = new IIndividual [1];
		fRoulette = new TRoulette(1);
		fMinimization = isMinimization;
	}

	/**
	 * @see TBestAndRankBasedRouletteSelection#doIt(IProblem, TPopulation, int[], IIndividual[], IIndividual[])
	 * @since 2
	 * @author isao
	 */
	public void doIt(TPopulation pop, int[] parentIndices, 
	                   IIndividual[] parents, IIndividual[] kids) {
		setFamilySize(2, kids.length);
		registerToFamily(parents, kids);
		sort(fFamily);
		IIndividual bestKid = chooseBestKid(fFamily);
		IIndividual selectedKid = chooseKidByRouletteWheelSelection(fFamily);
		pop.getIndividual(parentIndices[0]).copyFrom(bestKid);
		pop.getIndividual(parentIndices[1]).copyFrom(selectedKid);		
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
		fRoulette.setNoOfSlots(fFamily.length);
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

	/**
	 * 子集団中の最良個体を返す
	 * @param kids 子集団
	 * @return 最良個体
	 * @since 2
	 * @author isao
	 */
	private IIndividual chooseBestKid(IIndividual[] kids) {
		return kids[BEST_KIDS_INDEX];
	}
	
	/**
	 * ルーレット選択により，最良個体を除いた子集団の中からランダムに個体を一つ選ぶ
	 * @param kids 子集団
	 * @return 選択された個体
	 * @since 2
	 * @author isao
	 */
	private IIndividual chooseKidByRouletteWheelSelection(IIndividual[] kids) {
		fRoulette.resetCurrentSlotIndex();
		int noOfSurvivalKids = getNoOfAliveKids(kids);
		for(int i = 1; i < noOfSurvivalKids; i++) {
			fRoulette.setValueToSlot((double)(noOfSurvivalKids + 1 - i));
		}
		int selectedKid = fRoulette.doIt() + 1;
		return kids[selectedKid];
	}
	
	/**
	 * 子集団の生存している個体数を返す
	 * @param kids 子集団
	 * @return 生存している個体の数
	 * @since 2
	 * @author isao
	 */
	private int getNoOfAliveKids(IIndividual[] kids) {
		int survivalKids = 0;
		for(survivalKids = 0; survivalKids < kids.length; survivalKids++) {
			if(kids[survivalKids].getStatus() == IIndividual.INVALID) {
				break;
			}
		}
		return survivalKids;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ga.ISelectionForSurvival#isMinimization()
	 */
	public boolean isMinimization() {
		return fMinimization;
	}
	
}
