package iwasaki.ga.mgg;

import iwasaki.ga.core.IIndividual;
import iwasaki.ga.core.TPopulation;
import iwasaki.ga.util.TRandomCombination;

/**
 * �W�c����e�̂������_���ɑI�����镡���I����
 * @since 2
 * @author isao
 */
public class TRandomSelectionForReproduction implements ISelectionForReproduction {

	/** �����_���ȑg�ݍ��킹������D�����I���ŗ��p����� */
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
