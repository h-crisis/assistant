package ga.mgg;

import ga.core.IIndividual;
import ga.core.TPopulation;

/**
 * 複製選択インターフェース
 * @since 2
 * @author isao
 */
public interface ISelectionForReproduction {
	
	/**
	 * 生存選択を行なう
	 * @param pop 集団
	 * @param parentIndices 選択された親個体の集団中における位置
	 * @param parents 選択された親個体のコピー
	 * @since 2
	 */
	public void doIt(TPopulation pop, int[] parentIndices, IIndividual[] parents);

}
