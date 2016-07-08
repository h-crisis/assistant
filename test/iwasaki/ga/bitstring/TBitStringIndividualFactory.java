package ga.bitstring;

import ga.core.IIndividual;
import ga.core.IIndividualFactory;

/**
 * ビットストリング個体のファクトリ
 * @since 2
 * @author isao
 */
public class TBitStringIndividualFactory implements IIndividualFactory {
	
	/** ビット数 */
	private int fNoOfBits;

	/**
	 * コンストラクタ
	 * @since 2
	 */
	public TBitStringIndividualFactory() {
		fNoOfBits = -1;
	}
	
	/**
	 * コンストラクタ
	 * @param noOfBits ビット数
	 * @since 2
	 */
	public TBitStringIndividualFactory(int noOfBits) {
		fNoOfBits = noOfBits;
	}

	/*
	 * (non-Javadoc)
	 * @see ga.core.IIndividualFactory#create()
	 */
	public IIndividual create() {
		if (fNoOfBits == -1)
			return new TBitStringIndividual();
		else
			return new TBitStringIndividual(fNoOfBits);
	}

}
