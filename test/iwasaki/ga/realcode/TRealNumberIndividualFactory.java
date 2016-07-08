package ga.realcode;

import ga.core.IIndividual;
import ga.core.IIndividualFactory;

/**
 * 個体ファクトリ
 * @since 2
 * @author yamhan, isao
 * @author hmkz
 */
public class TRealNumberIndividualFactory implements IIndividualFactory {
	
	private int fDimension = -1;
	
	/**
	 * コンストラクタ
	 * @since 2
	 * @author yamhan, isao
	 */
	public TRealNumberIndividualFactory() {
	}

	/**
	 * コンストラクタ
	 * @param dim 次元数
	 * @since 2
	 * @author yamhan, isao
	 */	
	public TRealNumberIndividualFactory(int dim) {
		fDimension = dim;
	}

	/**
	 * 個体を生成する．
	 * @return 生成された個体
	 * @since 2
	 * @author yamhan, isao
	 */
	public IIndividual create() {
		if ( fDimension <= 0 )
			return new TRealNumberIndividual();
		else
			return new TRealNumberIndividual(fDimension);	
	}

	/**
	 * 同じ次元数のTRealNumberIndividualを生成するならtrue，そうでなければfalse．
	 * @since 55
	 * @author hmkz
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TRealNumberIndividualFactory)) {
			return false;
		}
		TRealNumberIndividualFactory f = (TRealNumberIndividualFactory) o;
		if (fDimension <= 0 && f.fDimension <= 0) {
			return true;
		} else {
			return fDimension == f.fDimension;
		}
	}
}
