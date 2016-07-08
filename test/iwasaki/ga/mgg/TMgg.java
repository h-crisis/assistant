package ga.mgg;

import ga.core.IIndividual;
import ga.core.IIndividualFactory;
import ga.core.IKidMaker;
import ga.core.IKidMakerFactory;
import ga.core.TPopulation;
import ga.core.TPopulationStatistics;

/**
 * SOARS用世代交代モデルMinimal Generation Gap (MGG)
 * @since 2
 * @author yamhan, isao
 */
public class TMgg {
	
	/** 個体ファクトリ */
	private IIndividualFactory fIndividualFactory;
	
	/** 子個体生成器ファクトリ */
	private IKidMakerFactory fKidMakerFactory;
	
	/** 複製選択器ファクトリ */
	private ISelectionForReproductionFactory fSelectionForReproductionFactory;
	
	/** 生存選択器ファクトリ */
	private ISelectionForSurvivalFactory fSelectionForSuvivalFactory;
		
	/** 現在の反復回数 */
	private long fIteration;

	/** 交叉回数 */
	private int fNoOfCrossovers;
	
	/** 集団 */
	private TPopulation fPopulation;
	
	/** 子個体生成器 */
	private IKidMaker fKidMaker;
	
	/** 親個体の添え字 */		
	private int[] fParentIndices;

	/** 親個体の配列 */
	private IIndividual[] fParents;

	/** 子個体の配列 */	
	private IIndividual[] fKids;
	
	/** 複製選択器 */
	private ISelectionForReproduction fSelectionForReproduction;
	
	/** 生存選択器 */
	private ISelectionForSurvival fSelectionForSurvival;
	
	/** 集団の統計 */
	private TPopulationStatistics fPopulationStatistics;
	
	/**
	 * コンストラクタ
	 * @param selectionForReproductionFactory 複製選択器ファクトリ
	 * @param kidMakerFactory 個体生成器ファクトリ
	 * @param noOfCrossovers 交叉回数
	 * @param selectionForSurvivalFactory 生存選択器ファクトリ 
	 * @param individualFactory 個体ファクトリ
	 * @since 2
	 * @author yamhan, isao
	 */
	public TMgg(ISelectionForReproductionFactory selectionForReproductionFactory,
							 IKidMakerFactory kidMakerFactory, int noOfCrossovers,
							 ISelectionForSurvivalFactory selectionForSurvivalFactory,
			         IIndividualFactory individualFactory) {
		fNoOfCrossovers = noOfCrossovers;
		fIndividualFactory = individualFactory;
		fKidMakerFactory = kidMakerFactory;
		fSelectionForReproductionFactory = selectionForReproductionFactory;
		fSelectionForSuvivalFactory = selectionForSurvivalFactory;
		fKidMaker = fKidMakerFactory.create();
		fSelectionForReproduction = fSelectionForReproductionFactory.create();
		fSelectionForSurvival = fSelectionForSuvivalFactory.create();
		fIteration = 0;
		fParentIndices = new int [fKidMaker.getNoOfParents()];
		fParents = new IIndividual [fKidMaker.getNoOfParents()];
		for (int i = 0; i < fKidMaker.getNoOfParents(); ++i) {
			fParents[i] = fIndividualFactory.create();
		}
		fKids = new IIndividual[2 * fNoOfCrossovers];
		for(int i = 0; i < fKids.length; i++) {
			fKids[i] = fIndividualFactory.create();
		}
	}
	
	/**
	 * 現在の反復回数を返す．
	 * @return 反復回数
	 * @since 2
	 * @author yamhan, isao
	 */
	public long getIteration() {
		return fIteration;
	}

	/**
	 * 現在の反復回数を設定する．
	 * @param itr 反復回数
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setIteration(long itr) {
		fIteration = itr;
	}

	/**
	 * 集団中の最良個体を返す．
	 * @return 最良個体
	 * @since 2
	 * @author yamhan, isao
	 */
	public IIndividual getBestIndividual() {
		return fPopulationStatistics.getBestIndividual();
	}

	/**
	 * 集団中の最良個体の添え字を返す．
	 * @return 最良個体の添え字
	 * @since 2
	 * @author yamhan, isao
	 */
	public int getBestIndex() {
		return fPopulationStatistics.getBestIndex();
	}


	/**
	 * 最良個体の評価値を返す．
	 * @return 最良個体の評価値
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getBestEvaluationValue() {
		return fPopulationStatistics.getBestEvaluationValue();
	}

	/**
	 * 集団中のVALIDな個体の平均評価値を返す．
	 * 集団中にVALIDな個体が一つもなければ平均評価値は0とする．
	 * @return 平均評価値
	 * @since 2
	 * @author yamhan, isao
	 */
	public double getAverageOfEvaluationValues() {
		return fPopulationStatistics.getAverageOfEvaluationValues();
	}
	
	/**
	 * 子を生成するための親を選択した後，交叉を交叉回数だけ実行して子を複数生成する．
	 * @return 親と生成された子
	 */
	public IIndividual[] selectParentsAndMakeKids() {
		fSelectionForReproduction.doIt(fPopulation, fParentIndices, fParents);
		makeKids(fParents, fKids);
		IIndividual[] family = new IIndividual [fParents.length + fKids.length];
		int index = 0;
		for (int i = 0; i < fParents.length; ++i) {
			family[index] = fParents[i];
			++index;
		}
		for (int i= 0; i < fKids.length; ++i) {
			family[index] = fKids[i];
			++index;
		}
		return family;
	}
	
	/**
	 * 生存選択を行い，世代を1進める．
	 */
	public void doSelectionForSurvival() {
		fSelectionForSurvival.doIt(fPopulation, fParentIndices, fParents, fKids);
		fIteration++;
	}

	/**
	 * 集団を返す．
	 * @return 現在の集団
	 * @since 2
	 * @author yamhan, isao
	 */
	public TPopulation getPopulation() {
		return fPopulation;
	}
	
	/**
	 * 集団を設定する．
	 * このとき，内部的には以下の処理がなされる：
	 * - 反復回数を０に設定する．
	 * - 初期状態のログを出力する．
	 * - 子個体集団が集団中のある個体と同じ設定に初期化される．
	 * @param pop 集団
	 * @since 2
	 * @author yamhan, isao
	 */
	public void setPopulationAndIteration(TPopulation pop, long iteration) throws IllegalArgumentException {
		fPopulation = pop;
		fPopulationStatistics = new TPopulationStatistics(fPopulation, fSelectionForSurvival.isMinimization());
		fIteration = iteration;
		for (int i = 0; i < fParents.length; ++i) {
			fParents[i].copyFrom(fPopulation.getIndividual(0));
		}
		for (int i = 0; i < fKids.length; ++i) {
			fKids[i].copyFrom(fPopulation.getIndividual(0));
		}			
	}
	
	/**
	 * 子集団を生成し，生成した子集団を返す．
	 * @param parents 親集団
	 * @since 2
	 * @author yamhan, isao
	 */	
	private void makeKids(IIndividual[] parents, IIndividual[] kids) {
		fKidMaker.setParents(parents);
		fKidMaker.doIt(2 * fNoOfCrossovers, kids);
	}
	
}
