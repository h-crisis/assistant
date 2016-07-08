package ga.mgg;

import ga.core.IIndividual;
import ga.core.TPopulation;

/**
 * 生存選択を定義するためのインターフェース
 * @since 2
 * @author isao
 */
public interface ISelectionForSurvival {
	
	/**
	 * 生存選択を実行する
	 * @param pop 現在の集団
	 * @param parentIndices 集団内における親個体の位置
	 * @param parents 親個体
	 * @param kids 子個体
	 * @since 2
	 * @author isao
	 */
	public void doIt(TPopulation pop,  int[] parentIndices, IIndividual[] parents, IIndividual[] kids);
	
	/**
	 * 最小化か？
	 * @return true:最小化，false：最大化
	 */
	public boolean isMinimization();

}
