package ga.bitstring;

import java.util.ArrayList;
import java.util.List;

import ga.core.IIndividual;
import ga.core.IKidMakerFactory;
import ga.core.TPopulation;
import ga.mgg.ISelectionForReproductionFactory;
import ga.mgg.ISelectionForSurvivalFactory;
import ga.mgg.TBest2SelectionFactory;
import ga.mgg.TBestAndRankBasedRouletteSelectionFactory;
import ga.mgg.TMgg;
import ga.mgg.TRandomSelectionForReproductionFactory;

/**
 * UX+MGGクラス
 * @author isao
 *
 */
public class TUxMgg {
	
	/** 集団 */
	private TPopulation fPopulation;
	
	/** MGG */
	private TMgg fMgg;
	
	/**
	 * コンストラクタ
	 * @param isMinimization 最小化問題か？
	 * @param noOfBits ビット数
	 * @param populationSize 集団サイズ
	 * @param noOfCrossovers 交叉回数
	 * @param useRouletteSelection 生存選択において，ルーレット選択を使うか？　falseにすると，Best2が選択されるようになる．
	 */
	public TUxMgg(boolean isMinimization, int noOfBits, int populationSize, int noOfCrossovers, boolean useRouletteSelection) {
		ISelectionForReproductionFactory selectionForReproductionFactory = new TRandomSelectionForReproductionFactory();
		IKidMakerFactory kidMakerFactory = new TUxKidMakerFactory();
		ISelectionForSurvivalFactory selectionForSuvivalFactory = null;
		if (useRouletteSelection) {
			selectionForSuvivalFactory = new TBestAndRankBasedRouletteSelectionFactory(isMinimization);
		} else {
			selectionForSuvivalFactory = new TBest2SelectionFactory(isMinimization);
		}
		TBitStringPopulationMaker popMaker = new TBitStringPopulationMaker(populationSize, noOfBits);
		fPopulation = popMaker.createInitialPopulation();
		fMgg = new TMgg(selectionForReproductionFactory, kidMakerFactory, noOfCrossovers, 
				             selectionForSuvivalFactory, popMaker.getIndividualFactory());
		fMgg.setPopulationAndIteration(fPopulation, 0);
	}
	
	/**
	 * 未評価の個体から構成される初期集団を返す．
	 * @return 初期集団
	 */
	public List<TBitStringIndividual> getInitialPopulation() {
		List<TBitStringIndividual> initPop = new ArrayList<TBitStringIndividual>();
		for ( int i = 0; i < fPopulation.getSize(); ++i) {
			initPop.add( ( TBitStringIndividual)fPopulation.getIndividual( i));
		}
		return initPop;
	}
//	public TBitStringIndividual[] getInitialPopulation() {
//		TBitStringIndividual[] initPop = new TBitStringIndividual [fPopulation.getSize()];
//		for (int i = 0; i < fPopulation.getSize(); ++i) {
//			initPop[i] = (TBitStringIndividual)fPopulation.getIndividual(i);
//		}
//		return initPop;
//	}

	/**
	 * 集団から選択された親個体と，親個体から生成された未評価の子個体から構成される家族集団を返す．
	 * @return 家族集団
	 */
	public List<TBitStringIndividual> selectParentsAndMakeKids() {
		IIndividual[] tmp = fMgg.selectParentsAndMakeKids();
		List<TBitStringIndividual> family = new ArrayList<TBitStringIndividual>();
		for ( int i = 0; i < tmp.length; ++i) {
			family.add( ( TBitStringIndividual)tmp[ i]);
		}
		return family;
	}
//	public TBitStringIndividual[] selectParentsAndMakeKids() {
//		IIndividual[] tmp = fMgg.selectParentsAndMakeKids();
//		TBitStringIndividual[] family = new TBitStringIndividual [tmp.length];
//		for (int i = 0; i < tmp.length; ++i) {
//			family[i] = (TBitStringIndividual)tmp[i];
//		}
//		return family;
//	}

	/**
	 * 生存選択を行った後の集団を返す．
	 * @return 集団
	 */
	public List<TBitStringIndividual> doSelectionForSurvival() {
		fMgg.doSelectionForSurvival();
		List<TBitStringIndividual> population = new ArrayList<TBitStringIndividual>();
		for ( int i = 0; i < fPopulation.getSize(); ++i) {
			population.add( ( TBitStringIndividual)fPopulation.getIndividual( i));
		}
		return population;
	}
//	public TBitStringIndividual[] doSelectionForSurvival() {
//		fMgg.doSelectionForSurvival();
//		TBitStringIndividual[] population = new TBitStringIndividual [fPopulation.getSize()];
//		for (int i = 0; i < fPopulation.getSize(); ++i) {
//			population[i] = (TBitStringIndividual)fPopulation.getIndividual(i);
//		}
//		return population;
//	}

	/**
	 * 集団内の最良個体を返す．
	 * @return 集団内の最良個体
	 */
	public TBitStringIndividual getBestIndividual() {
		return (TBitStringIndividual)fMgg.getBestIndividual();
	}
	
	/**
	 * 集団内の最良個体の評価値を返す．
	 * @return 集団内の最良個体の評価値
	 */
	public double getBestEvaluationValue() {
		return fMgg.getBestEvaluationValue();
	}
	
	/**
	 * 集団内の個体の評価値の平均値を返す．
	 * @return 集団内の個体の評価値の平均値
	 */
	public double getAverageOfEvaluationValues() {
		return fMgg.getAverageOfEvaluationValues();
	}
	
	/**
	 * 現在の反復回数(世代数)を返す．
	 * @return 現在の反復回数(世代数)
	 */
	public long getIteration() {
		return fMgg.getIteration();
	}

}
