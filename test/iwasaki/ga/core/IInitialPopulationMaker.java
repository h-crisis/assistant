package ga.core;


/**
 * 初期集団生成器インターフェース．
 * @since 2
 * @author isao
 */
public interface IInitialPopulationMaker {
	
	/**
	 * 初期集団を生成して返す．
	 * @return 初期集団
	 * @since 2
	 */
	TPopulation createInitialPopulation();
	
	/**
	 * 個体ファクトリを返す．
	 * @return 個体ファクトリ
	 */
	IIndividualFactory getIndividualFactory();

}
