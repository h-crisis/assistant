package iwasaki.ga.mgg;

import iwasaki.ga.core.IIndividual;
import iwasaki.ga.core.TPopulation;

/**
 * �����I�����`���邽�߂̃C���^�[�t�F�[�X
 * @since 2
 * @author isao
 */
public interface ISelectionForSurvival {
	
	/**
	 * �����I�������s����
	 * @param pop ���݂̏W�c
	 * @param parentIndices �W�c���ɂ�����e�̂̈ʒu
	 * @param parents �e��
	 * @param kids �q��
	 * @since 2
	 * @author isao
	 */
	public void doIt(TPopulation pop,  int[] parentIndices, IIndividual[] parents, IIndividual[] kids);
	
	/**
	 * �ŏ������H
	 * @return true:�ŏ����Cfalse�F�ő剻
	 */
	public boolean isMinimization();

}
