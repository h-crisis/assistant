package ga.mgg;

import ga.core.IIndividual;
import ga.core.TPopulation;
import ga.util.TRandomCombination;

/**
 * 集団から親個体をランダムに選択する複製選択器
 * @since 2
 * @author isao
 */
public class TRandomSelectionForReproduction implements ISelectionForReproduction {

	/** ランダムな組み合わせ生成器．複製選択で利用される */
	private TRandomCombination fRandomCombination;

	/**
	 * @since 2
	 * @author isao
	 */
	TRandomSelectionForReproduction() {
		fRandomCombination = new TRandomCombination();		
	}

	/**
	 * @see ISelectionForReproduction#doIt(TPopulation, int[], IIndividual[])
	 * @since 2
	 * @author isao
	 */
	public void doIt(TPopulation pop, int[] parentIndices, IIndividual[] parents) {
		fRandomCombination.doIt(pop.getSize(), parentIndices);
		for (int i = 0; i < parentIndices.length; ++i) {
			parents[i].copyFrom(pop.getIndividual(parentIndices[i]));
		}
	}

}
